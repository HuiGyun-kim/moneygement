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
 import org.springframework.security.core.userdetails.UserDetailsService;
 import org.springframework.security.core.userdetails.UsernameNotFoundException;
 import org.springframework.security.crypto.password.NoOpPasswordEncoder;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.web.SecurityFilterChain;
 import org.springframework.security.web.authentication.AuthenticationFailureHandler;
 import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
 import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
 import com.room7.moneygement.dto.UserDTO;
 import com.room7.moneygement.model.User;
 import com.room7.moneygement.service.CustomUserDetails;
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

			 UserDTO userDTO = UserDTO.builder()
				 .userId(user.getUserId())
				 .username(user.getUsername())
				 .password(user.getPassword())
				 .profileImg(user.getProfileImg())
				 .build(); // 프로필 이미지 등 추가 데이터 설정

			 return new CustomUserDetails(userDTO); // CustomUserDetails 객체 반환
		 };
	 }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/signup", "/signup-email","/users/**", "/users/send-id-verification-code","/users/verify-id-code","/users/verifyEmail", "/sendEmail", "/users/sendEmail", "/emailVerified","/find-id","/users/find-id","/find-password", "/css/**", "/js/**", "/img/**").permitAll()
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
                        .accessDeniedPage("/access-denied"))
                .csrf()
                    .ignoringRequestMatchers("/users/sendEmail");
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
