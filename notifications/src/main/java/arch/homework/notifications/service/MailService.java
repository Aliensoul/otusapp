package arch.homework.notifications.service;

import arch.homework.notifications.entity.User;

public interface MailService {

    void createUser(User user);

    boolean existsUsersAccount(User user);
}
