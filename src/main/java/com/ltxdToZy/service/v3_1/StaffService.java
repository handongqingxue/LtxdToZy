package com.ltxdToZy.service.v3_1;

import java.util.List;

import com.ltxdToZy.entity.v3_1.*;

public interface StaffService {

	int add(List<Staff> staffList);

	List<Staff> queryList();

	/**
	 * 查询省平台所需的员工数据
	 * @return
	 */
	List<Staff> queryEIList();

}
