package arch.homework.notifications.service.impl;

import arch.homework.notifications.entity.User;
import arch.homework.notifications.repository.MailRepository;
import arch.homework.notifications.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private final MailRepository mailRepository;

    @Override
    @Transactional
    public void createUser(User user) {
        long mailboxId = mailRepository.createUser(user);
        mailRepository.saveMail(mailboxId, "Mail: Created user " + user.getUsername());
    }

    @Override
    public boolean existsUsersAccount(User user) {
        return mailRepository.existsUsersAccount(user);
    }
}
