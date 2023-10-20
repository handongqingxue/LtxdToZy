package com.ltxdToZy.service.v3_1;

import java.util.List;

import com.ltxdToZy.entity.v1_3.*;
import com.ltxdToZy.entity.v3_1.Position;

public interface PositionService {

	int add(Position position);

	/**
	 * 查询平台同步人员位置的信息列表
	 * @return
	 */
	List<Position> queryELList();

}
