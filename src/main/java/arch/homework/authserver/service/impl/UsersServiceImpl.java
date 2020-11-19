package arch.homework.authserver.service.impl;

import arch.homework.authserver.entity.User;
import arch.homework.authserver.repository.UsersRepository;
import arch.homework.authserver.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    public User getUser(Long id) {
        return usersRepository.getUser(id);
    }

    @Override
    public User createUser(Long id, User user) {
        return usersRepository.createUser(user.setId(id));
    }

    @Override
    public void deleteUser(Long id) {
        usersRepository.deleteUser(id);
    }

    @Override
    public User updateUser(Long id, User user) {
        return usersRepository.updateUser(user.setId(id));
    }

    @Override
    public User getUserByCreds(String username, String password) {
        return usersRepository.getUserByCreds(username, password);
    }
}
