package com.room7.moneygement.config;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.room7.moneygement.dto.UserDTO;
import com.room7.moneygement.model.User;
import com.room7.moneygement.service.CustomUserDetails;
import com.room7.moneygement.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ComponentScan(basePackages = {"com.room7.moneygement.service"})
public class SecurityConfig implements WebMvcConfigurer {

	private final UserService userService;

	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			User user = userService.findByUsername(username);
			if (user == null) {
				throw new UsernameNotFoundException("User not found");
			}
			UserDTO userDTO = UserDTO.builder()
				.userId(user.getUserId())
				.username(user.getUsername())
				.password(user.getPassword())
				.profileImg(user.getProfileImg())
				.role(user.getRole())
				.introduction(user.getIntroduction())
				.build(); // 프로필 이미지 등 추가 데이터 설정

			return new CustomUserDetails(user); // CustomUserDetails 객체 반환
		};
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/login", "/signup", "/signup-email", "/users/**", "/ledgerEntry/**",
								"/diary/**", "/level/**","/diary/saveDiary","/ledgers/edit/{ledgerId}","/admin/**",
								"/users/send-id-verification-code", "/users/verify-id-code", "/users/verifyEmail",
								"/sendEmail","/follow/unfollow/{userId}","/follow/followers/{userId}", "/attendance/**","/attendance/check",
								"/users/sendEmail", "/emailVerified", "/find-id", "/users/find-id", "/find-password",
								"/ledgers/**", "/css/**", "/js/**", "/img/**", "/api/auth/image").permitAll()
						.requestMatchers("/manager/**").hasAuthority("ADMIN")
						.requestMatchers("/admin/**").hasAuthority("ADMIN")
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
						.accessDeniedPage("/access-denied"))
				.csrf(csrf -> csrf
						.ignoringRequestMatchers("/ledgerEntry/**", "/users/sendEmail", "/diary/**", "/userChallenges/**", "/qna/**", "/follow/**", "/api/auth/image","/ledgers/**", "/level/**", "/attendance/**")
				);
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

	@Bean
	public PasswordEncoder passwordEncoder() {
		// 테스트 환경
		return NoOpPasswordEncoder.getInstance();

		// // 실제 운영 환경
		// return new BCryptPasswordEncoder();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:8080") // 허용할 도메인과 포트
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
			.allowCredentials(true);
	}

}