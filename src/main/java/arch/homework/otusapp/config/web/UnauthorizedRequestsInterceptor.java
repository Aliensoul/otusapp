package arch.homework.otusapp.config.web;

import arch.homework.otusapp.exception.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UnauthorizedRequestsInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Matcher matcher = Pattern.compile(".*\\/users\\/([a-zA-Z0-9-]+)(\\/?).*").matcher(request.getRequestURI());

        if (matcher.matches()) {
            String id = Optional.ofNullable(request.getHeader("X-User-Id")).orElseThrow(UnauthorizedException::new);

            if (!id.equals(matcher.group(1))) {
                throw new UnauthorizedException();
            }
        }

        return super.preHandle(request, response, handler);
    }
}
