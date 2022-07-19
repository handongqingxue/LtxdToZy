package com.ltxdToZy.dao;

import org.apache.ibatis.annotations.Param;

public interface LoginUserMapper {

	String getCookieByUserId(@Param("userId")String userId);

}
