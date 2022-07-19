package com.ltxdToZy.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltxdToZy.dao.*;
import com.ltxdToZy.entity.*;
import com.ltxdToZy.service.*;

@Service
public class EntityServiceImpl implements EntityService {

	@Autowired
	private EntityMapper entityDao;

	@Override
	public int add(List<Entity> entityList) {
		// TODO Auto-generated method stub
		int count=0;
		for (Entity entity : entityList) {
			if(entityDao.getCountById(entity.getId())==0)
				count+=entityDao.add(entity);
			else
				count+=entityDao.edit(entity);
		}
		return count;
	}

	@Override
	public List<Entity> querySelectData(String entityType) {
		// TODO Auto-generated method stub
		return entityDao.querySelectData(entityType);
	}
}
