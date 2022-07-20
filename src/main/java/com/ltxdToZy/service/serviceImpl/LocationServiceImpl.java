package com.ltxdToZy.service.serviceImpl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltxdToZy.dao.*;
import com.ltxdToZy.entity.*;
import com.ltxdToZy.service.*;

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
