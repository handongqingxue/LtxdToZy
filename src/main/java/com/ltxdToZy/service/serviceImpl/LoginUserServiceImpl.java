package com.ltxdToZy.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltxdToZy.dao.*;
import com.ltxdToZy.service.*;

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
