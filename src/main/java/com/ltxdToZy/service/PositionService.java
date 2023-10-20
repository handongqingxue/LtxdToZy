package com.ltxdToZy.service;

import java.util.List;

import com.ltxdToZy.entity.*;

public interface PositionService {

	int add(Position position);

	/**
	 * 查询平台同步人员位置的信息列表
	 * @return
	 */
	List<Position> queryELList();

}
