package it.smartcommunitylab.aac.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class AccountProfiles {

	@XmlElement(name = "AccountProfile")
	private List<AccountProfile> profiles;

	public List<AccountProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<AccountProfile> profiles) {
		this.profiles = profiles;
	}
	
	
	
}
