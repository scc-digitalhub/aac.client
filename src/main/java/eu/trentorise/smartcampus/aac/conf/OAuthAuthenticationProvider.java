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

package eu.trentorise.smartcampus.aac.conf;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.aac.AACService;

/**
 * This {@link AuthenticationProvider} validates the {@link Authentication} instance containing the 
 * token as principal against (optionally) the token store and (optionally) the specified target scope.
 * @author raman
 *
 */
public class OAuthAuthenticationProvider implements AuthenticationProvider {

	private TokenStore tokenStore;
	private String scope;
	private String aacURL;
	
	public OAuthAuthenticationProvider() {
		super();
	}
	public OAuthAuthenticationProvider(TokenStore tokenStore) {
		super();
		this.tokenStore = tokenStore;
	}
	public OAuthAuthenticationProvider(TokenStore tokenStore, String scope,
			String aacURL) {
		super();
		this.tokenStore = tokenStore;
		this.scope = scope;
		this.aacURL = aacURL;
	}
	/**
	 * Check that the token is not empty, validate against the {@link TokenStore} if specified,
	 * and if it is valid for the given scope (if specified)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String token = (String)authentication.getPrincipal();
		if (token == null || token.trim().isEmpty()) {
			throw new BadCredentialsException("Authentication token is absent"); 
		}
		if (tokenStore != null && !tokenStore.validateToken(token)) {
			throw new BadCredentialsException("Authentication token is not valid"); 
		}
		try {
			if (scope != null && aacURL != null && !new AACService(aacURL, null, null).isTokenApplicable(token, scope)) {
				throw new BadCredentialsException("Authentication token is not valid for the required scope");
			}
		} catch (AACException e) {
			throw new BadCredentialsException("Failed to valdiate token scope: "+e.getMessage());
		}
		authentication.setAuthenticated(true);
		return authentication; 	
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication); 
	}
	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}
	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}
	/**
	 * @return the aacURL
	 */
	public String getAacURL() {
		return aacURL;
	}
	/**
	 * @param aacURL the aacURL to set
	 */
	public void setAacURL(String aacURL) {
		this.aacURL = aacURL;
	}
	/**
	 * @return the tokenStore
	 */
	public TokenStore getTokenStore() {
		return tokenStore;
	}
	/**
	 * @param tokenStore the tokenStore to set
	 */
	public void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}
}
