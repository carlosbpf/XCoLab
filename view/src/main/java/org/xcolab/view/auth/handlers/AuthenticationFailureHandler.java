package org.xcolab.view.auth.handlers;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;

import org.xcolab.view.auth.login.AuthenticationError;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String refererHeader = request.getHeader("referer");
        String redirectBaseUrl;
        if (StringUtils.isNotBlank(refererHeader)) {
            redirectBaseUrl = refererHeader;
        } else {
            redirectBaseUrl = "/";
        }

        UriComponentsBuilder redirectUrlBuilder = UriComponentsBuilder.fromHttpUrl(redirectBaseUrl);
        redirectUrlBuilder.replaceQueryParam("isSigningIn", true);
        log.error("Login failed", exception);
        final AuthenticationError authenticationError = AuthenticationError.fromException(exception);
        redirectUrlBuilder.replaceQueryParam("signinRegError", authenticationError);
        getRedirectStrategy().sendRedirect(request, response, redirectUrlBuilder.toUriString());
    }
}
