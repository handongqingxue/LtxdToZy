package com.ltxdToZy.service.v3_1;

import java.util.List;

import com.ltxdToZy.entity.v1_3.*;
import com.ltxdToZy.entity.v3_1.Position;

public interface PositionService {

	int add(Position position);

	/**
	 * ��ѯƽ̨ͬ����Աλ�õ���Ϣ�б�
	 * @return
	 */
	List<Position> queryELList();

}
