package com.ltxdToZy.service.serviceImpl.v3_1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltxdToZy.dao.v3_1.*;
import com.ltxdToZy.entity.v3_1.*;
import com.ltxdToZy.service.v3_1.*;
import com.ltxdToZy.utils.Constant;

@Service
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptMapper deptDao;
	
	@Override
	public int add(List<Dept> deptList) {
		// TODO Auto-generated method stub
		int count=0;
		for (Dept dept : deptList) {
			if(deptDao.getCountByDeptId(dept.getDeptId())==0) {
				count+=deptDao.add(dept);
			}
			else
				count+=deptDao.edit(dept);
		}
		return count;
	}

}
