package pl.koziolekweb.vendormachine.security;

import org.springframework.util.StringUtils;

public interface SecurityUtils {

	String HEADER_NAME = "Authorization";
	String HEADER_PREFIX = "Bearer ";

	static String removePrefix(String headerAuth) {
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(HEADER_PREFIX)) {
			return headerAuth.substring(7);
		}
		return headerAuth;
	}

	static String maybeRemovePrefix(String headerAuth, String defaultVal) {
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(HEADER_PREFIX)) {
			return headerAuth.substring(7);
		}
		return defaultVal;
	}
}
