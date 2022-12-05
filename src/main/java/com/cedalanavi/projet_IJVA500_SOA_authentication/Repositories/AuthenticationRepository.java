package com.cedalanavi.projet_IJVA500_SOA_authentication.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cedalanavi.projet_IJVA500_SOA_authentication.Entities.Authentication;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Integer> {
	
	Authentication findByUsername(String username);
}
