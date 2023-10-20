package com.ltxdToZy.service.serviceImpl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltxdToZy.entity.*;
import com.ltxdToZy.dao.*;
import com.ltxdToZy.service.*;

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
