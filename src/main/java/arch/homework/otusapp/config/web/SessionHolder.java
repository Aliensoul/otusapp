package arch.homework.otusapp.config.web;

import arch.homework.otusapp.entity.User;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Data
public class SessionHolder {

    private Map<String, User> sessions = new ConcurrentHashMap<>();
}
