package com.ecpbm.service;

import java.util.List;
import java.util.Map;

import com.ecpbm.pojo.Pager;
import com.ecpbm.pojo.UserInfo;

public interface UserInfoService {
	// ��ȡ�Ϸ��ͻ�
	public List<UserInfo> getValidUser();

	// �����û���Ų�ѯ�ͻ�
	public UserInfo getUserInfoById(int id);

	// ��ҳ��ʾ�ͻ�
	List<UserInfo> findUserInfo(UserInfo userInfo, Pager pager);

	// �ͻ�����
	Integer count(Map<String, Object> params);
	
	// �޸�ָ����ŵĿͻ�״̬
	void modifyStatus(String ids, int flag); 
}
