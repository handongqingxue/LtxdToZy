package com.ltxdToZy.service.v3_1;

import com.ltxdToZy.entity.v3_1.*;

public interface EpLoginClientService {

	int add(EpLoginClient elc);
	
	String getTokenByClientId(String clientId);

}
