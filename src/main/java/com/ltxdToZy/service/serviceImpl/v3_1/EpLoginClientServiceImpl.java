package com.ltxdToZy.service.serviceImpl.v3_1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltxdToZy.entity.v3_1.*;
import com.ltxdToZy.dao.v3_1.*;
import com.ltxdToZy.service.v3_1.*;

@Service
public class EpLoginClientServiceImpl implements EpLoginClientService {

	@Autowired
	private EpLoginClientMapper epLoginClientDao;

	@Override
	public int add(EpLoginClient elc) {
		// TODO Auto-generated method stub
		int count=epLoginClientDao.getCountByClientId(elc.getClient_id());
		if(count==0)
			count=epLoginClientDao.add(elc);
		else
			count=epLoginClientDao.edit(elc);
		return count;
	}

	@Override
	public String getTokenByClientId(String clientId) {
		// TODO Auto-generated method stub
		return epLoginClientDao.getTokenByClientId(clientId);
	}
}
