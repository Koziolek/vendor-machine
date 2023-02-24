package pl.koziolekweb.vendormachine.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	@SneakyThrows
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) {

		authException.printStackTrace();

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
}