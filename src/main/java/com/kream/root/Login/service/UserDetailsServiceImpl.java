package com.kream.root.Login.service;

import com.kream.root.Login.repository.UserListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final Logger logger=LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	private final UserListRepository userListRepository;
	
	public UserDetailsServiceImpl(UserListRepository userListRepository) {
		super();
		this.userListRepository = userListRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		logger.info("loadUserByUsername 동작 userId:", userId );
		return userListRepository.getByUserId(userId);     //UserListDTO is implementing UserDetails
	}
	

}
