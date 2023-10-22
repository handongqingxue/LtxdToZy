package com.ltxdToZy.dao.v3_1;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ltxdToZy.entity.v1_3.*;
import com.ltxdToZy.entity.v3_1.Position;

public interface PositionMapper {

	int add(Position position);

	int edit(Position position);

	int getCountByTagId(@Param("tagId")String tagId);

	List<Position> queryELList();
}
