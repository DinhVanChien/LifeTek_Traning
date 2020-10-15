package com.chiendv.login.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import com.chiendv.login.dto.UserDTO;
import com.chiendv.login.service.UserService;
import com.chiendv.login.service.impl.JwtService;
import com.chiendv.login.service.impl.UserDetailsServiceImpl;
import com.chiendv.login.util.Constant;

public class JwtAuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	private JwtService jwtService;
	private UserService userService;
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	public JwtAuthenticationTokenFilter(JwtService jwtService, UserService userService,
			UserDetailsServiceImpl userDetailsService) {
		this.jwtService = jwtService;
		this.userService = userService;
		this.userDetailsService = userDetailsService;
	}

	public JwtAuthenticationTokenFilter(ApplicationContext ctx) {
		this.jwtService = ctx.getBean(JwtService.class);
		this.userService = ctx.getBean(UserService.class);
		this.userDetailsService = ctx.getBean(UserDetailsServiceImpl.class);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		JwtService jwtService = new JwtService();
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String authToken = getJwtFromRequest(httpRequest);
		// lấy về path request
		String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
		// lần đầu vào login không cần token
		if (authToken == null && (Constant.LOGIN_ANNOTATION.equals(path)) || Constant.START_REQUEST.equals(path)) {
			chain.doFilter(request, response);
		} else if (jwtService.validateTokenLogin(authToken)) {
			String username = jwtService.getUsernameFromToken(authToken);
			UserDTO userDto = new UserDTO(userService.findByUsername(username));
			if (userDto != null) {
				UserDetails userDetail = userDetailsService.loadUserByUsername(username);
				// Tạo object Authentication
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail,
						null, userDetail.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
				// Xác thực thành công, lưu object Authentication vào SecurityContextHolder
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			chain.doFilter(request, response);
		}
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		// Kiểm tra xem header Authorization có chứa thông tin jwt không
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
