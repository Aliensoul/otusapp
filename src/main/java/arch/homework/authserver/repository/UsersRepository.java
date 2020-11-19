package arch.homework.authserver.repository;

import arch.homework.authserver.entity.User;

public interface UsersRepository {

    User getUser(Long id);

    User createUser(User user);

    void deleteUser(Long id);

    User updateUser(User user);

    User getUserByCreds(String username, String password);
}