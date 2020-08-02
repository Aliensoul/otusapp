package arch.homework.otusapp.controller;

import arch.homework.otusapp.entity.User;
import arch.homework.otusapp.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UsersController {

    public static final String BASE_URL = "/api";
    public static final String USERS_URL = BASE_URL + "/users/{id}";

    private final UsersService usersService;

    @GetMapping(path = USERS_URL)
    public User getUser(@PathVariable Long id) {
        return usersService.getUser(id);
    }

    @PostMapping(path = USERS_URL)
    public User createUser(@PathVariable Long id, @RequestBody User user) {
        return usersService.createUser(id, user);
    }

    @DeleteMapping(path = USERS_URL)
    public void deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
    }

    @PutMapping(path = USERS_URL)
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return usersService.updateUser(id, user);
    }


}
