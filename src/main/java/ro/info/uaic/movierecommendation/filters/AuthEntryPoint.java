package ro.info.uaic.movierecommendation.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import ro.info.uaic.movierecommendation.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
public class AuthEntryPoint implements AuthenticationEntryPoint {
    /**
     * Commences an authentication and returns an error message if the exception is invoked
     * @param request - the input {@link HttpServletRequest}
     * @param response - the input {@link HttpServletResponse}
     * @param authException - the input {@link AuthenticationException}
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
        log.error("Unauthorized error: {}", authException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), JsonUtil.objectToJsonString("Unauthorized error: Bad credentials"));
    }
}