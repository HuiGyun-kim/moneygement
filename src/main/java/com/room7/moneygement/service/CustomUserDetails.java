package com.room7.moneygement.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.room7.moneygement.model.User;

public class CustomUserDetails implements UserDetails {
	private User user;
	public CustomUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 권한 처리
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		// 사용하지 않는 경우 null 반환
		return user.getPassword();
	}
	@Override
	public String getUsername() {
		return user.getUsername();
	}
	public Long getUserId() {
		return user.getUserId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // 계정 만료 여부를 처리, 여기선 항상 true
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // 계정 잠금 여부를 처리, 여기선 항상 true
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;  // 자격 증명 만료 여부를 처리, 여기선 항상 true
	}

	@Override
	public boolean isEnabled() {
		return true; // 계정 활성화 여부를 처리, 여기선 항상 true
	}

	public User getUser() {
		return this.user;
	}
	// UserDTO를 반환하는 추가적인 메소드
	public User getUserDTO() {
		return user;
	}

	// UserDTO를 반환하는 추가적인 메소드
	public User getUserDTO() {
		return user;
	}

}