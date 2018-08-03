package it.smartcommunitylab.aac.test;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import it.smartcommunitylab.aac.AACAuthorizationService;
import it.smartcommunitylab.aac.AACProfileService;
import it.smartcommunitylab.aac.AACRoleService;
import it.smartcommunitylab.aac.AACService;
import it.smartcommunitylab.aac.authorization.beans.AuthorizationDTO;
import it.smartcommunitylab.aac.authorization.beans.RequestedAuthorizationDTO;
import it.smartcommunitylab.aac.model.AccountProfile;
import it.smartcommunitylab.aac.model.AccountProfiles;
import it.smartcommunitylab.aac.model.BasicProfile;
import it.smartcommunitylab.aac.model.BasicProfiles;
import it.smartcommunitylab.aac.model.Role;

@RunWith(SpringJUnit4ClassRunner.class)
public class AACTest {

	private static final String TEST = "cartellastudente"; 
	private static final String AUTHORIZATION_TEST = "authorization_" + TEST;
	private String aacURL = "http://localhost:8080/aac";
	private String clientId = "API_MGT_CLIENT_ID";
	private String clientSecret = "86ed3837-d55b-4ad5-8a7c-d3e591bd692b";	
	
	AACService aacService;
	AACProfileService profileService;
	AACRoleService roleService;
	AACAuthorizationService authService;
	
	private ObjectMapper mapper = new ObjectMapper();	
	
	@Before
	public void setup() {
		aacService = new AACService(aacURL, clientId, clientSecret);
		profileService = new AACProfileService(aacURL);
		roleService = new AACRoleService(aacURL);
		authService = new AACAuthorizationService(aacURL);
	}

//	@Test
//	public void test() throws Exception {
//		aacService.generateUserToken("admin", "admin", null);
//	}
	
//	@Test
	public void testProfileClient() throws Exception {
		String clientToken = aacService.generateClientToken("profile.basicprofile.all profile.accountprofile.all").getAccess_token();
		
		BasicProfiles basicProfiles = profileService.searchUsers(clientToken);
		Assert.assertNotEquals(0, basicProfiles.getProfiles().size());
		BasicProfile profile = basicProfiles.getProfiles().get(0);
		
		basicProfiles = profileService.findProfiles(clientToken, Lists.newArrayList(profile.getUserId()));
		Assert.assertEquals(1, basicProfiles.getProfiles().size());
		
		AccountProfiles accountProfiles = profileService.findAccountProfiles(clientToken, Lists.newArrayList(Lists.newArrayList(profile.getUserId())));
		Assert.assertEquals(1, accountProfiles.getProfiles().size());		
	}
	
//	@Test
	public void testProfileUser() throws Exception {
		String userToken = aacService.generateUserToken("admin", "admin", "profile.basicprofile.all profile.basicprofile.me profile.accountprofile.me").getAccess_token();
		String clientToken = aacService.generateClientToken("profile.basicprofile.all").getAccess_token();
		
		BasicProfiles basicProfiles = profileService.searchUsers(clientToken);		
		BasicProfile profile = basicProfiles.getProfiles().stream().filter(x -> "admin".equals(x.getName())).findFirst().get();
		
		BasicProfile basicProfile = profileService.getUser(userToken, profile.getUserId());
		Assert.assertNotNull(basicProfile);
		
		basicProfile = profileService.findProfile(userToken);
		Assert.assertNotNull(basicProfile);		
		
		AccountProfile accountProfile = profileService.findAccountProfile(userToken);
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountProfile));
		Assert.assertNotNull(accountProfile);				
	}	
	
	@Test
	public void testRoleUser() throws Exception {
		String clientToken = aacService.generateClientToken("profile.basicprofile.all profile.accountprofile.all user.roles.write user.roles.read.all user.roles.read client.roles.read.all").getAccess_token();
		
		BasicProfiles basicProfiles = profileService.searchUsers(clientToken);
		Assert.assertNotEquals(0, basicProfiles.getProfiles().size());
		BasicProfile profile = basicProfiles.getProfiles().get(0);		
		
		String userToken = aacService.generateUserToken("admin", "admin", "user.roles.me").getAccess_token();
		
		Set<Role> roles = roleService.getRoles(userToken);
		int rolesN = roles.size();
		
		roleService.addRoles(clientToken, profile.getUserId(), Lists.newArrayList("apimanager/carbon.super:testrole"));
		roles = roleService.getRoles(userToken);
		Assert.assertEquals(rolesN + 1, roles.size());		
		
		roleService.deleteRoles(clientToken, profile.getUserId(), Lists.newArrayList("apimanager/carbon.super:testrole"));
		roles = roleService.getRoles(userToken);
		Assert.assertEquals(rolesN, roles.size());		
		
		// should return one role less
		roles = roleService.getRolesByUserId(clientToken, profile.getUserId());
		Assert.assertEquals(rolesN - 1, roles.size());			
		
		roles = roleService.getClientRoles(clientToken);
		Assert.assertEquals(rolesN, roles.size());			
	}	
	
	@Test
	public void testAuthorization() throws Exception {
		String clientToken = aacService.generateClientToken("profile.basicprofile.all user.roles.write authorization.manage authorization.schema.manage").getAccess_token();
		
		BasicProfiles basicProfiles = profileService.searchUsers(clientToken);
		Assert.assertNotEquals(0, basicProfiles.getProfiles().size());
		BasicProfile profile = basicProfiles.getProfiles().get(0);			
		
		roleService.addRoles(clientToken, profile.getUserId(), Lists.newArrayList(AUTHORIZATION_TEST));
		
		String json = Resources.toString(Resources.getResource("load.json"), Charsets.UTF_8);
		authService.loadSchema(clientToken, TEST, json);
		
		json = Resources.toString(Resources.getResource("auth.json"), Charsets.UTF_8);
		AuthorizationDTO auth = mapper.readValue(json, AuthorizationDTO.class);
		authService.insertAuthorization(clientToken, TEST, auth);
		
		json = Resources.toString(Resources.getResource("validate.json"), Charsets.UTF_8);
		RequestedAuthorizationDTO reqAuth = mapper.readValue(json, RequestedAuthorizationDTO.class);		
		boolean res = authService.validateAuthorization(clientToken, TEST, reqAuth);
		System.err.println(res);
		
		roleService.deleteRoles(clientToken, profile.getUserId(), Lists.newArrayList(AUTHORIZATION_TEST));
	}
	
	

}
