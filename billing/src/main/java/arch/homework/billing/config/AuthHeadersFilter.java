package arch.homework.billing.config;

import arch.homework.billing.entity.CustomAuthentication;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthHeadersFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String role = request.getHeader("X-Role");

        CustomAuthentication auth = new CustomAuthentication();
        if (role == null) {
            auth.setAuthenticated(false);
        } else {
            Matcher matcher = Pattern.compile(".*\\/accounts\\/([a-zA-Z0-9-]+)(\\/?).*").matcher(request.getRequestURI());

            if (matcher.matches()) {
                String id = Optional.ofNullable(request.getHeader("X-User-Id")).orElseThrow(() -> new BadCredentialsException(""));

                if (!id.equals(matcher.group(1))) {
                    throw new BadCredentialsException("");
                }
            }

            Long id = Long.valueOf(request.getHeader("X-User-Id"));


            auth.setAuthenticated(true);
            auth.setUserId(id);
            auth.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(String.format("ROLE_%s", role))));
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}
