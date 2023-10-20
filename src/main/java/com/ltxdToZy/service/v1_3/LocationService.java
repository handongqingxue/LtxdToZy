package com.ltxdToZy.service.v1_3;

import java.util.List;

import com.ltxdToZy.entity.v1_3.*;

public interface LocationService {

	int add(Location location);

	List<Location> selectEntityLocation();

}
