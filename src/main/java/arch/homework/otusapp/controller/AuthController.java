package arch.homework.otusapp.controller;

import arch.homework.otusapp.config.web.SessionHolder;
import arch.homework.otusapp.entity.Credentials;
import arch.homework.otusapp.entity.Result;
import arch.homework.otusapp.entity.User;
import arch.homework.otusapp.service.UsersService;
import lombok.AllArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

//@RestController
@AllArgsConstructor
public class AuthController {

    private final SessionHolder sessionHolder;
    private final UsersService usersService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User userRes = usersService.createUser(user);
        return ResponseEntity.ok().body(userRes);
    }

    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestBody Credentials credentials,
                                        HttpServletResponse response) {
        User user = usersService.getUserByCreds(credentials.getUsername(), credentials.getPassword());
        if (user != null) {
            String sessionId = UUID.randomUUID().toString();
            sessionHolder.getSessions().put(sessionId, user);

            Cookie cookie = new Cookie("SESSION", sessionId);
            cookie.setMaxAge(60 * 10);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return ResponseEntity.ok().body(new Result("Success"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED_401).body(new Result("Wrong credentials"));
        }

    }

    @GetMapping("/auth")
    public ResponseEntity<Object> auth(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED_401).build();
        }

        Optional<Cookie> sessionCookie = Arrays.stream(request.getCookies()).filter(x -> x.getName().equals("SESSION")).findAny();
        if (sessionCookie.isPresent()) {
            User user = sessionHolder.getSessions().get(sessionCookie.get().getValue());

            if (user != null) {
                return ResponseEntity.ok()
                        .header("X-User-Id", user.getId().toString())
                        .build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED_401).build();
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "/logout")
    public ResponseEntity<Result> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("SESSION", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().body(new Result("Success"));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/signin")
    public ResponseEntity<Result> signin(HttpServletResponse response) {
        return ResponseEntity.ok().body(new Result().setMessage("Please go to login and provide Login/Password"));
    }
}
