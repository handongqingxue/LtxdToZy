package com.ltxdToZy.service.serviceImpl.v1_3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltxdToZy.dao.v1_3.*;
import com.ltxdToZy.service.v1_3.*;

@Service
public class LoginUserServiceImpl implements LoginUserService {

	@Autowired
	private LoginUserMapper loginUserDao;

	@Override
	public String getCookieByUserId(String userId) {
		// TODO Auto-generated method stub
		return loginUserDao.getCookieByUserId(userId);
	}
}
