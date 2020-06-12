package com.ecpbm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.ecpbm.dao.provider.UserInfoDynaSqlProvider;
import com.ecpbm.pojo.UserInfo;

public interface UserInfoDao {
	// ��ȡϵͳ�Ϸ��ͻ��������ݱ�user_info��status�ֶ�Ϊ1���û��б�
	@Select("select * from user_info where status=1")
	public List<UserInfo> getValidUser();

	// �����û�id�Ż�ȡ�ͻ�����
	@Select("select * from user_info where id=#{id}")
	public UserInfo getUserInfoById(int id);

	// ��ҳ��ȡ�ͻ���Ϣ
	@SelectProvider(type = UserInfoDynaSqlProvider.class, method = "selectWithParam")
	List<UserInfo> selectByPage(Map<String, Object> params);

	// ����������ѯ�ͻ�����
	@SelectProvider(type = UserInfoDynaSqlProvider.class, method = "count")
	Integer count(Map<String, Object> params);

	// ���¿ͻ�״̬
	@Update("update user_info set status=#{flag} where id in (${ids})")
	void updateState(@Param("ids") String ids, @Param("flag") int flag);
}
