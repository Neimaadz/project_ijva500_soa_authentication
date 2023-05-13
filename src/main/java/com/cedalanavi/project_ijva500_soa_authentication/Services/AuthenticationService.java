package com.cedalanavi.project_ijva500_soa_authentication.Services;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cedalanavi.project_ijva500_soa_authentication.Data.AuthCredentialsUpdateRequest;
import com.cedalanavi.project_ijva500_soa_authentication.Data.AuthenticationRequest;
import com.cedalanavi.project_ijva500_soa_authentication.Data.AuthenticationResource;
import com.cedalanavi.project_ijva500_soa_authentication.Data.UserCreateResource;
import com.cedalanavi.project_ijva500_soa_authentication.Data.UserDetailsResource;
import com.cedalanavi.project_ijva500_soa_authentication.Entities.Authentication;
import com.cedalanavi.project_ijva500_soa_authentication.Repositories.AuthenticationRepository;
import com.cedalanavi.project_ijva500_soa_authentication.Utils.JwtTokenUtil;

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

    	Authentication authentication = authenticationRepository.findByUsername(username);
    	UserDetails userDetails = loadUserByUsername(username);
    	UserDetailsResource userDetailsResource = new UserDetailsResource();
    	userDetailsResource.setIdUser(authentication.getIdUser());
    	userDetailsResource.setUsername(authentication.getUsername());
    	
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
			newUser.setIdUser(UUID.randomUUID().toString());
			newUser.setUsername(authRequest.username);
			newUser.setPassword(bCryptPasswordEncoder.encode(authRequest.password));
			
			Authentication authenticationCreated = authenticationRepository.save(newUser);
			final UserDetails userDetails = loadUserByUsername(authRequest.username);
			
			UserCreateResource userCreateResource = new UserCreateResource();
			userCreateResource.idUser = authenticationCreated.getIdUser();
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

	@Transactional
	public void deleteUser(String idUser) {
		authenticationRepository.deleteByIdUser(idUser);
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
        UserDetails userDetail = new org.springframework.security.core.userdetails.User(authentication.getUsername(), authentication.getPassword(), grantedAuthorities);
        
        return userDetail;
    }
}
