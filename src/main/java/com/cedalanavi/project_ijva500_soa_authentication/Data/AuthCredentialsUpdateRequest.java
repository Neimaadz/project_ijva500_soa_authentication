package com.cedalanavi.project_ijva500_soa_authentication.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AuthCredentialsUpdateRequest {
	
	@JsonIgnore(value = false)
	public String password;
}
