package com.cedalanavi.projet_IJVA500_SOA_authentication.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AuthenticationRequest {

	@JsonIgnore(value = false)
	public String username;
	
	@JsonIgnore(value = false)
	public String password;
}
