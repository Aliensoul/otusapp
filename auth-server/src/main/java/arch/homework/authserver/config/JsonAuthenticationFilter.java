package arch.homework.authserver.config;

import arch.homework.authserver.entity.Credentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper mapper;

    public JsonAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationSuccessHandler authenticationSuccessHandler) {
        super();
        mapper = new ObjectMapper();
        super.setAuthenticationManager(authenticationManager);
        super.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String username = null;
        String password = null;

        try {
            Credentials credentials = mapper.readValue(request.getInputStream(), Credentials.class);
            username = credentials.getUsername();
            password = credentials.getPassword();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

        // Allow subclasses to set the "details" property
        super.setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
