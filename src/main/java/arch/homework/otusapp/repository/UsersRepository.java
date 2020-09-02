package arch.homework.otusapp.repository;

import arch.homework.otusapp.entity.User;

public interface UsersRepository {

    User getUser(Long id);

    User createUser(User user);

    void deleteUser(Long id);

    User updateUser(User user);

    User getUserByCreds(String username, String password);
}