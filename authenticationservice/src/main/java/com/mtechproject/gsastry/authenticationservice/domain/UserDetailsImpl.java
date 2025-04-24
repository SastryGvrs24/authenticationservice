
package com.mtechproject.gsastry.authenticationservice.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

	AppUser appUser;

	public UserDetailsImpl(AppUser appUser) {
        this.appUser = appUser;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getPassword() {
		return appUser.getPassword();
	}

	@Override
	public String getUsername() {
		return appUser.getUsername();
	}

}