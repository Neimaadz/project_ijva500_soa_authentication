package com.cedalanavi.projet_IJVA500_SOA_authentication.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AuthCredentialsUpdateRequest {
	
	@JsonIgnore(value = false)
	public String password;
}
