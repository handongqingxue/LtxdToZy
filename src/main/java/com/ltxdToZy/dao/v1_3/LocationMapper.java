package com.ltxdToZy.dao.v1_3;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ltxdToZy.entity.v1_3.*;

public interface LocationMapper {

	int add(Location location);

	int getCountByUid(@Param("uid")String uid);

	int edit(Location location);

	List<Location> selectEntityLocation();

}
