package arch.homework.notifications.repository;

import arch.homework.notifications.entity.User;

public interface MailRepository {

    void saveMail(Long userId, String mailText);

    long createUser(User user);

    boolean existsUsersAccount(User user);


}
