package arch.homework.authserver.service.impl;

import arch.homework.authserver.entity.*;
import arch.homework.authserver.exception.BusinessLogicException;
import arch.homework.authserver.mapper.UsersMapper;
import arch.homework.authserver.repository.UsersRepository;
import arch.homework.authserver.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static arch.homework.authserver.config.PgConfig.TX_MANAGER_BEAN;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService, UsersService {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final UsersRepository usersRepository;

    private static final String USERS_TOPIC = "users";
    private final KafkaTemplate<String, UserEvent> kafkaProducer;
    private final UsersMapper usersMapper;


    @Transactional(propagation = Propagation.REQUIRED, transactionManager = TX_MANAGER_BEAN)
    public Result registerUser(RegistrationForm form) {

        Boolean exists = usersRepository.checkUserExists(form.getEmail());

        if (exists) {
            throw new BusinessLogicException(-1, "Пользователь с таким username уже существует");
        }

        //encode password
        form.getCredentials().setPassword(encoder.encode(form.getCredentials().getPassword()));

        RegistrationForm response = usersRepository.registerUser(form);

        kafkaProducer.send(USERS_TOPIC, new UserEvent().setEventType(EventType.CREATE).setUser(usersMapper.mapToUser(response)))
                .completable().join().getRecordMetadata().hasOffset();

        return new Result(0, "ok");

    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(usersRepository.loadUserByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
    }
}
