package com.cedalanavi.projet_IJVA500_SOA_authentication.Services;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cedalanavi.projet_IJVA500_SOA_authentication.Data.AuthCredentialsUpdateRequest;
import com.cedalanavi.projet_IJVA500_SOA_authentication.Data.AuthenticationRequest;
import com.cedalanavi.projet_IJVA500_SOA_authentication.Data.AuthenticationResource;
import com.cedalanavi.projet_IJVA500_SOA_authentication.Data.UserCreateResource;
import com.cedalanavi.projet_IJVA500_SOA_authentication.Data.UserDetailsResource;
import com.cedalanavi.projet_IJVA500_SOA_authentication.Entities.Authentication;
import com.cedalanavi.projet_IJVA500_SOA_authentication.Repositories.AuthenticationRepository;
import com.cedalanavi.projet_IJVA500_SOA_authentication.Utils.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Service
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private AuthenticationRepository authenticationRepository;
	
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
    
    public UserDetailsResource isAuthenticated(String jwtToken) {
    	String username = null;
    	try {
        	username = jwtTokenUtil.getUsernameFromToken(jwtToken);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (ExpiredJwtException e) {
			throw e;
		}
    	
    	UserDetails userDetails = loadUserByUsername(username);
    	UserDetailsResource userDetailsResource = new UserDetailsResource();
    	userDetailsResource.setUsername(userDetails.getUsername());
    	List<String> authorities = new ArrayList<String>();
    	userDetails.getAuthorities().forEach(authoritie -> {
    		authorities.add(authoritie.getAuthority());
    	});
    	userDetailsResource.setAuthorities(authorities);
    	
    	if (username != null && jwtTokenUtil.validateToken(jwtToken, userDetails)) {
        	return userDetailsResource;
    	}
    	else {
    		return null;
    	}
	}
	
	public UserCreateResource register(AuthenticationRequest authRequest) {
		Authentication userExist = authenticationRepository.findByUsername(authRequest.username);
		
		if(userExist == null && authRequest.password != "") {
			
			Authentication newUser = new Authentication();
			newUser.setUsername(authRequest.username);
			newUser.setPassword(bCryptPasswordEncoder.encode(authRequest.password));
			
			Authentication authenticationCreated = authenticationRepository.save(newUser);
			final UserDetails userDetails = loadUserByUsername(authRequest.username);
			
			UserCreateResource userCreateResource = new UserCreateResource();
			userCreateResource.username = authenticationCreated.getUsername();
			userCreateResource.token = jwtTokenUtil.generateToken(userDetails);
			
			return userCreateResource;
		} else {
			return null;
		}
	}
	
	public AuthenticationResource signin(AuthenticationRequest authenticationRequest) {
		final UserDetails userDetails = loadUserByUsername(authenticationRequest.username);
		AuthenticationResource authenticationResource = new AuthenticationResource();
		authenticationResource.token = jwtTokenUtil.generateToken(userDetails);
		
		return authenticationResource;
	}
	
	public void updateUserCredentials(AuthCredentialsUpdateRequest authCredentialsUpdateRequest, String jwtToken) {
    	String username = null;
    	try {
        	username = jwtTokenUtil.getUsernameFromToken(jwtToken);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (ExpiredJwtException e) {
			throw e;
		}
		Authentication updatedUser = authenticationRepository.findByUsername(username);
		updatedUser.setPassword(bCryptPasswordEncoder.encode(authCredentialsUpdateRequest.password));
		authenticationRepository.save(updatedUser);
	}
	
	public void deleteUser(int id) {
		authenticationRepository.deleteById(id);
	}
	
	
	
	
	

    @Override
    @Transactional(readOnly = true)
    // Override the UserDetailsService method using by Spring Security
    public UserDetails loadUserByUsername(String username) {
		Authentication authentication = authenticationRepository.findByUsername(username);
        
        if (authentication == null) {
            throw new UsernameNotFoundException(username);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("admin"));

        UserDetails userDetail = new org.springframework.security.core.userdetails.User(authentication.getUsername(), authentication.getPassword(), grantedAuthorities);
        
        return userDetail;
    }
}
