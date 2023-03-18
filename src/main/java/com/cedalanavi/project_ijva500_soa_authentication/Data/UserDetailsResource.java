package com.cedalanavi.project_ijva500_soa_authentication.Data;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsResource {

	String username;

	List<String> authorities = new ArrayList<String>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}
	
	
}
