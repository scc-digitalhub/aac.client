package it.smartcommunitylab.aac;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.smartcommunitylab.aac.model.Page;
import it.smartcommunitylab.aac.model.Service;
import it.smartcommunitylab.aac.model.UserClaimProfile;

public class AACUserClaimService {

	private String aacURL;
	
	private ObjectMapper mapper = new ObjectMapper();

    /**
     * @param aacURL URL of the AAC service
     */
	public AACUserClaimService(String aacURL) {
		super();
		this.aacURL = aacURL;
		if (!aacURL.endsWith("/")) this.aacURL += '/';
	}	
	
	@SuppressWarnings("unchecked")
	public Page<UserClaimProfile> getServiceClaims(String token, String serviceId) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/claims/" + serviceId;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                Page<UserClaimProfile> page = mapper.readValue(response, Page.class);
	                page.setContent(page.getContent().stream().map(c -> mapper.convertValue(c, UserClaimProfile.class)).collect(Collectors.toList()));
	                return page;
	            }
	            throw new AACException("Error in read service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		
	
	public UserClaimProfile getServiceClaimsForUserId(String token, String serviceId, String userId) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/claims/" + serviceId +"/" + userId;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return mapper.readValue(response, UserClaimProfile.class);
	            }
	            throw new AACException("Error in read service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		
	
	public UserClaimProfile getServiceClaimsForUsername(String token, String serviceId, String username) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/claims/" + serviceId +"?username=" + username;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return mapper.readValue(response, UserClaimProfile.class);
	            }
	            throw new AACException("Error in read service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
	public Service saveClaimsForUserId(String token, String serviceId, String userId, Map<String, Object> claims) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/claims/" + serviceId +"/" + userId;
	        final HttpPost post = new HttpPost(url);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-Type", "application/json");
	        post.setHeader("Authorization", "Bearer " + token);
	        UserClaimProfile profile = new UserClaimProfile(userId, null);
	        profile.setClaims(claims);
	        post.setEntity(new StringEntity(mapper.writeValueAsString(profile), "UTF-8"));

	        try {
	            resp = getHttpClient().execute(post);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return mapper.readValue(response, Service.class);
	            }
	            throw new AACException("Error in save service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		

	public Service saveClaimsForUsername(String token, String serviceId, String username, Map<String, Object> claims) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/claims/" + serviceId +"?username=" + username;
	        final HttpPost post = new HttpPost(url);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-Type", "application/json");
	        post.setHeader("Authorization", "Bearer " + token);
	        UserClaimProfile profile = new UserClaimProfile(null, username);
	        profile.setClaims(claims);
	        post.setEntity(new StringEntity(mapper.writeValueAsString(profile), "UTF-8"));

	        try {
	            resp = getHttpClient().execute(post);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return mapper.readValue(response, Service.class);
	            }
	            throw new AACException("Error in save service " + resp.getStatusLine());
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
