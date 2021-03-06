package arch.homework.otusapp.service.impl;

import arch.homework.otusapp.entity.User;
import arch.homework.otusapp.repository.UsersRepository;
import arch.homework.otusapp.service.UsersService;
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
}
