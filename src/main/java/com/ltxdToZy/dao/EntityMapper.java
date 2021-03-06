package com.ltxdToZy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ltxdToZy.entity.*;

public interface EntityMapper {

	int add(Entity entity);

	int edit(Entity entity);

	List<Entity> querySelectData(@Param("entityType")String entityType);

	int getCountById(@Param("id")Integer id);

}
