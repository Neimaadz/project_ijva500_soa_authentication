package com.cedalanavi.project_ijva500_soa_authentication.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cedalanavi.project_ijva500_soa_authentication.Entities.Authentication;

public interface AuthenticationRepository extends JpaRepository<Authentication, Integer> {
	
	Authentication findByUsername(String username);
}
