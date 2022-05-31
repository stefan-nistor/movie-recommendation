package ro.info.uaic.movierecommendation.handlers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import ro.info.uaic.movierecommendation.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Data
@AllArgsConstructor
public class GoogleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    private final RedirectStrategy redirectStrategy;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        userService.saveOauth2User((OAuth2User) authentication.getPrincipal());
    }
}
