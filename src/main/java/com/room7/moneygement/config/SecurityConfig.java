 //
 // package com.room7.moneygement.config;
 //
 //
 // import lombok.RequiredArgsConstructor;
 // import org.springframework.context.annotation.Bean;
 // import org.springframework.context.annotation.Configuration;
 // import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 // import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
 // import org.springframework.security.core.authority.AuthorityUtils;
 // import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 // import org.springframework.security.web.SecurityFilterChain;
 // import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
 // import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
 //
 // @Configuration
 // @EnableWebSecurity
 // @RequiredArgsConstructor
 // public class SecurityConfig{
 //
 //     // 특정 HTTP 요청에 대한 웹 기반 보안 구성
	//  @Bean
	//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	// 	 http
	// 		 .authorizeHttpRequests((auth) -> auth
	// 			 .requestMatchers("/", "/login", "/css/**", "/js/**", "/img/**").permitAll()
	// 			 .requestMatchers("/manager/**").hasAuthority("ADMIN")
	// 			 .anyRequest().authenticated()
	// 		 )
	// 		 .formLogin((auth) -> auth
	// 			 .loginPage("/login")
	// 			 .successHandler(successHandler()) // 로그인 성공 핸들러
	// 			 .failureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error=true")) // 로그인 실패 핸들러
	// 			 .usernameParameter("email")
	// 			 .passwordParameter("password")
	// 			 .permitAll()
	// 		 )
	// 		 .logout(auth -> auth
	// 			 .logoutSuccessUrl("/login") // 로그아웃 후 리다이렉션 페이지
	// 			 .invalidateHttpSession(true) // 세션 무효화
	// 			 .deleteCookies("JSESSIONID") // 쿠키 삭제
	// 		 )
	// 		 .sessionManagement(session -> session
	// 			 .sessionFixation().migrateSession()
	// 			 .maximumSessions(1) // 동시 세션 수 제한
	// 			 .maxSessionsPreventsLogin(true) // 추가 로그인 방지
	// 		 )
	// 		 .exceptionHandling(e -> e
	// 			 .accessDeniedPage("/access-denied") // 접근 거부 시 페이지
	// 		 );
	// 	 return http.build();
	//  }
 //
	//  // 로그인 성공 핸들러
	//  @Bean
	//  public AuthenticationSuccessHandler successHandler() {
	// 	 return (request, response, authentication) -> {
	// 		 if (AuthorityUtils.authorityListToSet(authentication.getAuthorities()).contains("ADMIN")) {
	// 			 response.sendRedirect("/manager/main"); // 관리자 페이지로 리다이렉트
	// 		 } else {
	// 			 response.sendRedirect("/"); // 일반 사용자 페이지로 리다이렉트
	// 		 }
	// 	 };
	//  }
 //
	//  // 패스워드 인코더로 사용할 빈 등록
 // 	@Bean
 // 	public BCryptPasswordEncoder bCryptPasswordEncoder() {
 // 		return new BCryptPasswordEncoder();
 // 	}
 //
 // }
 //
