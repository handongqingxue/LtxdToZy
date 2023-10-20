package com.ltxdToZy.service.v1_3;

import java.util.List;

import com.ltxdToZy.entity.v1_3.*;

public interface EntityService {

	int add(List<Entity> entityList);

	List<Entity> querySelectData(String entityType);

}
