package com.ltxdToZy.service;

import java.util.List;

import com.ltxdToZy.entity.*;

public interface PositionService {

	int add(Position position);

	/**
	 * ��ѯƽ̨ͬ����Աλ�õ���Ϣ�б�
	 * @return
	 */
	List<Position> queryELList();

}
