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

package it.smartcommunitylab.aac.conf;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * @author raman
 *
 */
public class OAuthTokenFilter extends AbstractPreAuthenticatedProcessingFilter {

	/**
	 * 
	 */
	private static final String BEARER_TYPE = "bearer ";
	/**
	 * 
	 */
	private static final String AUTHORIZATION = "Authorization";

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		return extractToken(request);
	}

	private Object extractToken(HttpServletRequest request) {
		String completeToken = request.getHeader(AUTHORIZATION);
		if (completeToken == null) return null;
		if (completeToken.toLowerCase().startsWith(BEARER_TYPE)) {
			completeToken = completeToken.substring(BEARER_TYPE.length());
		}
		return completeToken;
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return extractToken(request);
	}

}
