package com.ecpbm.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.ecpbm.pojo.AdminInfo;
import com.ecpbm.pojo.Functions;
import com.ecpbm.pojo.TreeNode;
import com.ecpbm.service.AdminInfoService;
import com.ecpbm.util.JsonFactory;

@SessionAttributes(value = { "admin" })
@Controller
@RequestMapping("/admininfo")
public class AdminInfoController {
	@Autowired
	private AdminInfoService adminInfoService;

	@RequestMapping(value = "/login", produces = "text/html;charset=UTF-8")//��ֹ����
	@ResponseBody
	public String login(AdminInfo ai, ModelMap model) {
		// ��̨��¼��֤
		AdminInfo admininfo = adminInfoService.login(ai);
		if (admininfo != null && admininfo.getName() != null) {
			// �ж�Ȩ��
			if (adminInfoService.getAdminInfoAndFunctions(admininfo.getId()).getFs().size() > 0) {
				// ��֤ͨ�����ѷ��书��Ȩ�ޣ���admininfo�������model��
				model.put("admin", admininfo);
				// ��JSON��ʽ����
				return "{\"success\":\"true\",\"message\":\"��¼�ɹ�\"}";
			} else {
				return "{\"success\":\"false\",\"message\":\"��û��Ȩ�ޣ�����ϵ��������Ա����Ȩ�ޣ�\"}";
			}
		} else
			return "{\"success\":\"false\",\"message\":\"��¼ʧ��\"}";
	}
//�Բ˵�ҳ�������������
@RequestMapping("getTree")
@ResponseBody
public List<TreeNode> getTree(@RequestParam(value = "adminid") String adminid) {
	// ���ݹ���Ա��ţ���ȡAdminInfo����
	AdminInfo admininfo = adminInfoService.getAdminInfoAndFunctions(Integer.parseInt(adminid));
	List<TreeNode> nodes = new ArrayList<TreeNode>();
	// ��ȡ������Functions���󼯺�
	List<Functions> functionsList = admininfo.getFs();
	// ��List<Functions>���͵�Functions���󼯺�����
	Collections.sort(functionsList);
	// ��������Functions���󼯺�ת����List<TreeNode>���͵��б�nodes
	for (Functions functions : functionsList) {
		TreeNode treeNode = new TreeNode();
		treeNode.setId(functions.getId());
		treeNode.setFid(functions.getParentid());
		treeNode.setText(functions.getName());
		nodes.add(treeNode);
	}
	List<TreeNode> treeNodes = JsonFactory.buildtree(nodes, 0);
	return treeNodes;
}
	// �˳�
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public String logout(SessionStatus status) {
		// @SessionAttributes���
		status.setComplete();
		return "{\"success\":\"true\",\"message\":\"ע���ɹ�\"}";
	}
}
