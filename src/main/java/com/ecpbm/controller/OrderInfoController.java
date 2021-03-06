package com.ecpbm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecpbm.pojo.OrderDetail;
import com.ecpbm.pojo.OrderInfo;
import com.ecpbm.pojo.Pager;
import com.ecpbm.service.OrderInfoService;
import com.ecpbm.service.ProductInfoService;
import com.ecpbm.service.UserInfoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonParseException;

@Controller
@RequestMapping("/orderinfo")
public class OrderInfoController {
	@Autowired
	OrderInfoService orderInfoService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	ProductInfoService productInfoService;

	// 分页显示
	@RequestMapping(value = "/list")
	@ResponseBody
	public Map<String, Object> list(Integer page, Integer rows, OrderInfo orderInfo) {
		// 初始化一个分页类对象pager
		Pager pager = new Pager();
		pager.setCurPage(page);
		pager.setPerPageRows(rows);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderInfo", orderInfo);
		int totalCount = orderInfoService.count(params);
		List<OrderInfo> orderinfos = orderInfoService.findOrderInfo(orderInfo, pager);
		Map<String, Object> result = new HashMap<String, Object>(2);
		result.put("total", totalCount);
		result.put("rows", orderinfos);
		return result;
	}
	// 保存订单
	@ResponseBody
	@RequestMapping(value = "/commitOrder")
	public String commitOrder(String inserted, String orderinfo)
			throws JsonParseException, JsonMappingException, IOException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			OrderInfo oi = mapper.readValue(orderinfo, OrderInfo[].class)[0];
			// 保存订单信息
			orderInfoService.addOrderInfo(oi);
			List<OrderDetail> odList = mapper.readValue(inserted, new TypeReference<ArrayList<OrderDetail>>() {
			});
			// 给订单明细对象的其他属性赋值
			for (OrderDetail od : odList) {
				od.setOid(oi.getId());
				orderInfoService.addOrderDetail(od);
			}
			return "success";
		} catch (Exception e) {
			return "failure";
		}
	}

	// 根据订单id号获取要查看的订单对象, 再返回订单明细页
	@RequestMapping("/getOrderInfo")
	public String getOrderInfo(String oid, Model model) {
		OrderInfo oi = orderInfoService.getOrderInfoById(Integer.parseInt(oid));
		model.addAttribute("oi", oi);
		return "orderdetail";
	}

	// 根据订单id号获取订单明细列表
	@RequestMapping("/getOrderDetails")
	@ResponseBody
	public List<OrderDetail> getOrderDetails(String oid) {
		List<OrderDetail> ods = orderInfoService.getOrderDetailByOid(Integer.parseInt(oid));
		for (OrderDetail od : ods) {
			// od.setPid(od.getPi().getId());
			od.setPrice(od.getPi().getPrice());
			od.setTotalprice(od.getPi().getPrice() * od.getNum());
		}
		return ods;
	}

	// 删除订单
	@ResponseBody
	@RequestMapping(value = "/deleteOrder", produces = "text/html;charset=UTF-8")
	public String deleteOrder(String oids) {
		String str = "";
		try {
			oids = oids.substring(0, oids.length() - 1);
			String[] ids = oids.split(",");
			for (String id : ids) {
				orderInfoService.deleteOrder(Integer.parseInt(id));
			}
			str = "{\"success\":\"true\",\"message\":\"删除成功！\"}";
		} catch (Exception e) {
			str = "{\"success\":\"false\",\"message\":\"删除失败！\"}";
		}
		return str;
	}

}
