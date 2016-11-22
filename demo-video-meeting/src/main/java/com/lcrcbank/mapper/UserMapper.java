package com.lcrcbank.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lcrcbank.entity.User;

/**
 * @author LCS
 */
public interface UserMapper {
	
	@Select("select * from vm_user where name=#{name}")
	User findByUsername(@Param("name") String name);
		
	@Select("select * from vm_user")
	List<User> findAll();
}
