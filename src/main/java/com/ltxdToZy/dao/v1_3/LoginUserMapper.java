package com.ltxdToZy.dao.v1_3;

import org.apache.ibatis.annotations.Param;

public interface LoginUserMapper {

	String getCookieByUserId(@Param("userId")String userId);

}
