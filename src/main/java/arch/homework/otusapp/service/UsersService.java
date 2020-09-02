package arch.homework.otusapp.service;

import arch.homework.otusapp.entity.User;

public interface UsersService {

    User getUser(Long id);

    User createUser(Long id, User user);

    default User createUser(User user) {
        return createUser(null, user);
    }

    void deleteUser(Long id);

    User updateUser(Long id, User user);

    User getUserByCreds(String username, String password);
}
