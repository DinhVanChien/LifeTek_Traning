package com.chiendv.login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.chiendv.login.filter.JwtAuthenticationTokenFilter;
import com.chiendv.login.rest.CustomAccessDeniedHandler;
import com.chiendv.login.rest.RestAuthenticationEntryPoint;
import com.chiendv.login.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    UserDetailsServiceImpl userDetailsService;
    // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Cung cáp userservice cho spring security
        auth.userDetailsService(userDetailsService)
                // cung cấp password encoder
                .passwordEncoder(bCryptPasswordEncoder());
    }
    
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception {
        JwtAuthenticationTokenFilter jwtAuthenTokenFilter = new JwtAuthenticationTokenFilter(getApplicationContext());
        jwtAuthenTokenFilter.setAuthenticationManager(authenticationManager());
        return jwtAuthenTokenFilter;
    }
    
    // xác thực người dùng
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    
	//sẽ xử lý những request chưa được xác thực.
    @Bean
    public RestAuthenticationEntryPoint restServicesEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }
	/*
		trường hợp người dùng gửi request mà không có quyền sẽ do
	 	bean customAccessDeniedHandler xử lý (Ví dụ role USER nhưng gửi request xóa user)
	 */

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
   

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/", "/login","/logout").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll();

        http.antMatcher("/api/**").httpBasic().authenticationEntryPoint(restServicesEntryPoint()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/api/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
		        .and()
                .addFilterBefore(new JwtAuthenticationTokenFilter(getApplicationContext()), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler())
        		.and()
                .logout()
		        .logoutSuccessUrl("/login")
		        .logoutUrl("/logout")
		        .permitAll();
        
    }
}
