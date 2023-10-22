package com.ltxdToZy.dao.v3_1;

import org.apache.ibatis.annotations.Param;

import com.ltxdToZy.entity.v3_1.*;

public interface DeptMapper {

	int add(Dept dept);

	int edit(Dept dept);

	int getCountByDeptId(@Param("deptId")Integer deptId);
}
