package it.smartcommunitylab.aac.model;

import java.io.Serializable;

public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5989316043413301379L;

	/** Predefined system role USER */
	public static final String R_USER = "ROLE_USER";
	/** Predefined system role ADMIN */
	public static final String R_ADMIN = "ROLE_ADMIN";	
	

	private Long id;

	private ROLE_SCOPE scope;
	private String role;
	private String context;
	
	private String authority;

	public Role() {
	}
	
	public Role(ROLE_SCOPE scope, String role, String context) {
		super();
		this.scope = scope;
		this.role = role;
		this.context = context;
	}
	
	public static Role systemUser() {
		return new Role(ROLE_SCOPE.system, R_USER, null);
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ROLE_SCOPE getScope() {
		return scope;
	}

	public void setScope(ROLE_SCOPE scope) {
		this.scope = scope;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (!role.equals(other.role))
			return false;
		if (!scope.equals(other.scope))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return scope + " " + role + (context == null ? "" : " - " + context) + " [" + id + "]";
	}

	public enum ROLE_SCOPE {
		system ("SCOPE_SYSTEM"), 
		application ("SCOPE_APPLICATION"),
		tenant ("SCOPE_TENANT"),
		user ("SCOPE_USER");
		
		private final String scopeName;

		private ROLE_SCOPE(String scopeName) {
			this.scopeName = scopeName;
		}
		
		public String scopeName(){
			return scopeName;
		}
	};	
	
}
