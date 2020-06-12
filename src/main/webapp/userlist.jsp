<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
  if(session.getAttribute("admin")==null)
  	response.sendRedirect("/digital-um/admin_login.jsp");
 %>
<html>
<head>
<base href="<%=basePath%>">

<title>用户查询</title>
<link href="EasyUI/themes/default/easyui.css" rel="stylesheet"
	type="text/css" />
<link href="EasyUI/themes/icon.css" rel="stylesheet" type="text/css" />
<link href="EasyUI/demo.css" rel="stylesheet" type="text/css" />
<script src="EasyUI/jquery.min.js" type="text/javascript"></script>
<script src="EasyUI/jquery.easyui.min.js" type="text/javascript"></script>
<script src="EasyUI/easyui-lang-zh_CN.js" type="text/javascript"></script>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
</head>

<body>
    <!-- 创建一个table -->
	<table id="userListDg" class="easyui-datagrid"></table>
	
	<!-- 创建工具栏 -->
	<div id="userListTb" style="padding:2px 5px;"><a href="javascript:void(0)"
		class="easyui-linkbutton" iconCls="icon-edit" plain="true"
		onclick="SetIsEnableUser(1);">启用客户</a> <a href="javascript:void(0)"
		class="easyui-linkbutton" iconCls="icon-remove"
		onclick="SetIsEnableUser(0);" plain="true">禁用客户</a>
	</div>
	
	<!-- 创建搜索栏 -->
	<div id="searchUserListTb" style="padding:4px 3px;">
		<form id="searchUserListForm">
			<div style="padding:3px ">
				客户名称&nbsp;&nbsp;<input class="easyui-textbox" name="search_userName"
					id="search_userName" style="width:110px" /><a href="javascript:void(0)"
					class="easyui-linkbutton" iconCls="icon-search" plain="true"
					onclick="searchUserInfo();">查找</a>
			</div>			
		</form>
	</div>
<script type="text/javascript">
		$(function() {
			$('#userListDg').datagrid({
				singleSelect : false,
				url : 'userinfo/list',
				queryParams : {},
				pagination : true, 
				pageSize : 5,
				pageList : [ 5, 10, 15 ],
				rownumbers : true, 
				fit : true, 
				toolbar : '#userListTb',
				header : '#searchUserListTb',
				columns : [ [ { 
					title : '序号',
					field : 'id',
					align : 'center',
					checkbox : true
				}, {
					field : 'userName',
					title : '登录名',					
					width : 100
				}, {
					field : 'realName',
					title : '真实姓名',
					width : 80
				}, {
					field : 'sex',
					title : '性别',
					width : 100
				}, {
					field : 'address',
					title : '住址',
					width : 200
				} , {
					field : 'email',
					title : '邮箱',
					width : 150
				} , {
					field : 'regDate',
					title : '注册日期',
					width : 100
				} , {
					field : 'status',
					title : '客户状态',
					width : 100,
					formatter : function(value, row, index) {
						if (row.status==1) {
							return "启用";
						} else {
							return "禁用";
						}
					}
				} ] ]
			});
		});

		var urls;
		var data;
		
		function searchUserInfo() {
			var userName = $('#search_userName').textbox("getValue");
			$('#userListDg').datagrid('load', {
				userName : userName
			});
		}
		
		
		// 设置启用或禁用客户
		function SetIsEnableUser(flag) {
			var rows = $("#userListDg").datagrid('getSelections');
			if (rows.length > 0) {
				$.messager.confirm('Confirm', '确认要设置么?', function(r) {
					if (r) {
						var uids = "";
						for (var i = 0; i < rows.length; i++) {
							uids += rows[i].id + ",";
						}						
						$.post('userinfo/setIsEnableUser', {
							uids : uids,
							flag : flag
						}, function(result) {
							if (result.success == 'true') {
								$("#userListDg").datagrid('reload'); 
								$.messager.show({
									title : '提示信息',
									msg : result.message
								});
							} else {
								$.messager.show({
									title : '提示信息',
									msg : result.message
								});
							}
						}, 'json');

					}
				});
			} else {
				$.messager.alert('提示', '请选择要启用或禁用的客户', 'info');
			}
		}
		
	</script>
</body>
</html>
