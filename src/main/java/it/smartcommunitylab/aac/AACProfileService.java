package it.smartcommunitylab.aac;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;

import it.smartcommunitylab.aac.model.AccountProfile;
import it.smartcommunitylab.aac.model.AccountProfiles;
import it.smartcommunitylab.aac.model.BasicProfile;
import it.smartcommunitylab.aac.model.BasicProfiles;

public class AACProfileService {

	private String aacURL;
	
	private ObjectMapper mapper = new ObjectMapper();

    /**
     * @param aacURL URL of the AAC service
     */
	public AACProfileService(String aacURL) {
		super();
		this.aacURL = aacURL;
		if (!aacURL.endsWith("/")) {
			this.aacURL += '/';
		}
	}	
	
	public BasicProfiles searchUsers(String token) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "basicprofile/all";
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	BasicProfiles data = mapper.readValue(response, BasicProfiles.class);
	                return data;
	            }
	            throw new AACException("Error in basicprofile/all " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
	
	
	public BasicProfiles findProfiles(String token, List<String> userIds) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "basicprofile/profiles?userIds=" + Joiner.on(",").join(userIds);
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	BasicProfiles data = mapper.readValue(response, BasicProfiles.class);
	                return data;
	            }
	            throw new AACException("Error in basicprofile/profiles " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		
	
	public BasicProfile findProfile(String token) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "basicprofile/me";
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	BasicProfile data = mapper.readValue(response, BasicProfile.class);
	                return data;
	            }
	            throw new AACException("Error in basicprofile/all " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
	
	public BasicProfile getUser(String token, String userId) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "basicprofile/all/" + userId;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	BasicProfile data = mapper.readValue(response, BasicProfile.class);
	                return data;
	            }
	            throw new AACException("Error in basicprofile/all " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
	
	public AccountProfiles findAccountProfiles(String token, List<String> userIds) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "accountprofile/profiles?userIds=" + Joiner.on(",").join(userIds);
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	AccountProfiles data = mapper.readValue(response, AccountProfiles.class);
	                return data;
	            }
	            throw new AACException("Error in accountprofile/profiles " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
	
	public AccountProfile findAccountProfile(String token)  throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "accountprofile/me";
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	AccountProfile data = mapper.readValue(response, AccountProfile.class);
	                return data;
	            }
	            throw new AACException("Error in accountprofile/me " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
			
	
	
	
    private HttpClient getHttpClient() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return httpClient;
    }
	
}
