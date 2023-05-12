package com.cedalanavi.project_ijva500_soa_authentication.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserCreateResource {
	
	@JsonIgnore(value = false)
	public String idUser;

	@JsonIgnore(value = false)
	public String username;
	
	@JsonIgnore(value = false)
	public String token;
}
