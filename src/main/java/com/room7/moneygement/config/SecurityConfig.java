
 package com.room7.moneygement.config;



 import java.net.URLEncoder;
 import java.nio.charset.StandardCharsets;
 import java.util.ArrayList;

 import jakarta.servlet.http.Cookie;
 import lombok.RequiredArgsConstructor;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.ComponentScan;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
 import org.springframework.security.core.authority.AuthorityUtils;
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 import org.springframework.security.crypto.password.NoOpPasswordEncoder;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.web.SecurityFilterChain;
 import org.springframework.security.web.authentication.AuthenticationFailureHandler;
 import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
 import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

 import com.room7.moneygement.model.User;
 import com.room7.moneygement.service.UserService;

 @Configuration
 @EnableWebSecurity
 @RequiredArgsConstructor
 @ComponentScan(basePackages = {"com.room7.moneygement.service"})
 public class SecurityConfig {

	 private final UserService userService;

	 @Bean
	 public UserDetailsService userDetailsService() {
		 return username -> {
			 User user = userService.findByUsername(username);
			 if (user == null) {
				 throw new UsernameNotFoundException("User not found");
			 }
			 return new org.springframework.security.core.userdetails.User(
				 user.getUsername(),
				 user.getPassword(),
				 new ArrayList<>());
		 };
	 }

	 @Bean
	 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http
			 .authorizeHttpRequests(auth -> auth
				 .requestMatchers("/", "/login", "/css/**", "/js/**", "/img/**").permitAll()
				 .requestMatchers("/manager/**").hasAuthority("ADMIN")
				 .anyRequest().authenticated())
			 .formLogin(form -> form
				 .loginPage("/login")
				 .loginProcessingUrl("/users/login")
				 .failureHandler(customFailureHandler())
				 .successHandler(customSuccessHandler())
				 .permitAll())
			 .logout(logout -> logout
				 .logoutUrl("/logout")
				 .logoutSuccessUrl("/login")
				 .invalidateHttpSession(true)
				 .deleteCookies("JSESSIONID"))
			 .exceptionHandling(e -> e
				 .accessDeniedPage("/access-denied"));
		 return http.build();
	 }

	 // 로그인 실패 핸들러
	 @Bean
	 public AuthenticationFailureHandler customFailureHandler() {
		 return (request, response, exception) -> {
			 String errorMessage = URLEncoder.encode("아이디나 비밀번호가 맞지 않습니다.", StandardCharsets.UTF_8.name());
			 Cookie errorCookie = new Cookie("loginError", errorMessage);
			 errorCookie.setMaxAge(10); // 10초
			 errorCookie.setPath("/");
			 response.addCookie(errorCookie);
			 response.sendRedirect("/login?error=true");
		 };
	 }

	 // 로그인 성공 핸들러
	 @Bean
	 public AuthenticationSuccessHandler customSuccessHandler() {
		 return (request, response, authentication) -> {
			 String rememberMe = request.getParameter("rememberMe");
			 if ("on".equals(rememberMe)) {
				 String username = authentication.getName();
				 String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.name());
				 Cookie rememberMeCookie = new Cookie("rememberUser", encodedUsername);
				 rememberMeCookie.setMaxAge(60 * 60 * 24 * 30); // 30일 동안 유효
				 rememberMeCookie.setPath("/");
				 response.addCookie(rememberMeCookie);
			 } else {
				 Cookie rememberMeCookie = new Cookie("rememberUser", "");
				 rememberMeCookie.setMaxAge(0); // 즉시 만료
				 rememberMeCookie.setPath("/");
				 response.addCookie(rememberMeCookie);
			 }
			 response.sendRedirect("/");
		 };
	 }
	 // @Bean
	 // public AuthenticationSuccessHandler customSuccessHandler() {
		//  return (request, response, authentication) -> {
		// 	 // 'rememberMe' 파라미터 확인
		// 	 if ("on".equals(request.getParameter("rememberMe"))) {
		// 		 String username = authentication.getName();
		// 		 String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.name());
		// 		 Cookie rememberMeCookie = new Cookie("rememberUser", encodedUsername);
		// 		 rememberMeCookie.setMaxAge(60 * 60 * 24 * 30); // 30일
		// 		 rememberMeCookie.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
		// 		 response.addCookie(rememberMeCookie);
		// 	 }
		// 	 response.sendRedirect("/");
		//  };
	 // }
	 @Bean
	 public PasswordEncoder passwordEncoder() {
		 return NoOpPasswordEncoder.getInstance();
	 }
 }

 //  // 패스워드 인코더로 사용할 빈 등록
 // 	@Bean
 // 	public BCryptPasswordEncoder bCryptPasswordEncoder() {
 // 		return new BCryptPasswordEncoder();
 // 	}
 //
 // }
 //
