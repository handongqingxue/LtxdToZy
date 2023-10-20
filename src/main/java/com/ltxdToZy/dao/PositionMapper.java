package com.ltxdToZy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ltxdToZy.entity.*;

public interface PositionMapper {

	int add(@Param("position")Position position);

	int edit(@Param("position")Position position);

	int getCountByTagId(@Param("tagId")String tagId);

	List<Position> queryELList();
}
