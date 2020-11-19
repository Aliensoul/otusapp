package arch.homework.authserver.config;

import arch.homework.authserver.entity.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final ObjectMapper objectMapper;

    public CustomLogoutSuccessHandler() {
        super();
        setRedirectStrategy(new NoRedirectStrategy());
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        super.onLogoutSuccess(request, response, authentication);
        response.getWriter().write(objectMapper.writeValueAsString(new Result().setMessage("success")));
    }


    protected class NoRedirectStrategy implements RedirectStrategy {

        @Override
        public void sendRedirect(HttpServletRequest request,
                                 HttpServletResponse response, String url) throws IOException {
            // no redirect

        }

    }

}