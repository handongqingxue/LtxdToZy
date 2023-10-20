package com.ltxdToZy.dao.v1_3;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ltxdToZy.entity.v1_3.*;

public interface EntityMapper {

	int add(Entity entity);

	int edit(Entity entity);

	List<Entity> querySelectData(@Param("entityType")String entityType);

	int getCountById(@Param("id")Integer id);

}
