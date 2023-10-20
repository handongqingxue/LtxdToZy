package com.ltxdToZy.service.serviceImpl.v1_3;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltxdToZy.dao.v1_3.*;
import com.ltxdToZy.entity.v1_3.*;
import com.ltxdToZy.service.v1_3.*;

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationMapper locationDao;

	@Override
	public int add(Location location) {
		// TODO Auto-generated method stub
		int count=locationDao.getCountByUid(location.getUid());
		if(count==0)
			locationDao.add(location);
		else
			count=locationDao.edit(location);
		return count;
	}

	@Override
	public List<Location> selectEntityLocation() {
		// TODO Auto-generated method stub
		return locationDao.selectEntityLocation();
	}
}
