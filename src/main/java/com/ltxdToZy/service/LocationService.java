package com.ltxdToZy.service;

import java.util.List;

import com.ltxdToZy.entity.*;

public interface LocationService {

	int add(Location location);

	List<Location> selectEntityLocation();

}
