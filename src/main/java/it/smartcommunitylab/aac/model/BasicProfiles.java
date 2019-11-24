package it.smartcommunitylab.aac.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class BasicProfiles {

	@XmlElement(name = "BasicProfile")
	private List<BasicProfile> profiles;

	public List<BasicProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<BasicProfile> profiles) {
		this.profiles = profiles;
	}
	
	
	
}
