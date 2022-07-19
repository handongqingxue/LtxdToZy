package com.ltxdToZy.service;

import java.util.List;

import com.ltxdToZy.entity.*;

public interface EntityService {

	int add(List<Entity> entityList);

	List<Entity> querySelectData(String entityType);

}
