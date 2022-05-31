package ro.info.uaic.movierecommendation.handlers;

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

@Data
@AllArgsConstructor
public class GoogleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    private final RedirectStrategy redirectStrategy;

    private final String REDIRECT_URI = "https://a6-movie-recommendation.netlify.app/";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        userService.saveOauth2User((OAuth2User) authentication.getPrincipal());
        redirectStrategy.sendRedirect(request, response, REDIRECT_URI);
    }
}
