/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/

package eu.trentorise.smartcampus.aac.conf.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import eu.trentorise.smartcampus.aac.conf.TokenStore;
import eu.trentorise.smartcampus.aac.model.TokenData;

/**
 * Simple, in-memory implementation of {@link TokenStore}.
 * 
 * @author raman
 *
 */
public class InMemoryTokenStore implements TokenStore {

	// margin of time to consider for the token expiration
	private static final long MARGIN = 1000*10;
	private static Map<String,TokenData> map = new ConcurrentHashMap<String, TokenData>();
	
	
	@Override
	public void StoreTokenData(TokenData data) {
		assert data != null;
		map.put(data.getAccess_token(), data);
		
	}

	@Override
	public void deleteTokenData(String accessToken) {
		map.remove(accessToken);
	}

	@Override
	public TokenData readTokenData(String accessToken) {
		return map.get(accessToken);
	}

	@Override
	public boolean validateToken(String accessToken) {
		TokenData td = readTokenData(accessToken);
		return td != null && td.getExpires_on() - MARGIN > System.currentTimeMillis();
	}

}
