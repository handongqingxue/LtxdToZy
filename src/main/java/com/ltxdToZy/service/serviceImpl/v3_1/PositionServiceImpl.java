package com.ltxdToZy.service.serviceImpl.v3_1;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltxdToZy.dao.v3_1.*;
import com.ltxdToZy.entity.v3_1.*;
import com.ltxdToZy.service.v3_1.*;

@Service
public class PositionServiceImpl implements PositionService {

	@Autowired
	private PositionMapper positionDao;

	@Override
	public int add(Position position) {
		// TODO Auto-generated method stub
		int count=positionDao.getCountByTagId(position.getTagId());
		if(count==0)
			positionDao.add(position);
		else
			count=positionDao.edit(position);
		return count;
	}

	@Override
	public List<Position> queryELList() {
		// TODO Auto-generated method stub
		return positionDao.queryELList();
	}
}
