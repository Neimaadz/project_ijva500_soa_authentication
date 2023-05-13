package com.cedalanavi.project_ijva500_soa_authentication.Controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cedalanavi.project_ijva500_soa_authentication.Data.AuthCredentialsUpdateRequest;
import com.cedalanavi.project_ijva500_soa_authentication.Data.AuthenticationRequest;
import com.cedalanavi.project_ijva500_soa_authentication.Data.AuthenticationResource;
import com.cedalanavi.project_ijva500_soa_authentication.Data.UserCreateResource;
import com.cedalanavi.project_ijva500_soa_authentication.Data.UserDetailsResource;
import com.cedalanavi.project_ijva500_soa_authentication.Services.AuthenticationService;

@RestController
@RequestMapping("authentication")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthenticationService authenticationService;
	
	@GetMapping("/isAuthenticated")
	public UserDetailsResource isAuthenticated(HttpServletRequest request) {
		final String requestTokenHeader = request.getHeader("Authorization");
		String jwtToken = requestTokenHeader.substring(7);
		return authenticationService.isAuthenticated(jwtToken);
	}
	
	@PostMapping("/register")
	public UserCreateResource register(@RequestBody AuthenticationRequest authRequest, HttpServletResponse response) {
		UserCreateResource createUserResource = authenticationService.register(authRequest);
		if (createUserResource != null) {
			response.setStatus(HttpServletResponse.SC_OK);
			return createUserResource;
		}
		else {
			throw new IllegalArgumentException("Values already exists.");
		}
	}
	
	@DeleteMapping("/delete/{idUser}")
	public void deleteUser(@PathVariable String idUser) {
		authenticationService.deleteUser(idUser);
	}
	
	@PutMapping("/credentials/update")
	public void updateUserCredentials(@RequestBody AuthCredentialsUpdateRequest authCredentialsUpdateRequest, HttpServletRequest request) {
		final String requestTokenHeader = request.getHeader("Authorization");
		String jwtToken = requestTokenHeader.substring(7);
		authenticationService.updateUserCredentials(authCredentialsUpdateRequest, jwtToken);
	}

	@PostMapping("/signin")
	public AuthenticationResource createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.username, authenticationRequest.password);
		return authenticationService.signin(authenticationRequest);
	}

	private void authenticate(String username, String password) throws AuthenticationException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		}
		catch (BadCredentialsException e) {
			throw new AuthenticationCredentialsNotFoundException("Error, bad credentials", e);
		}
	}
}
