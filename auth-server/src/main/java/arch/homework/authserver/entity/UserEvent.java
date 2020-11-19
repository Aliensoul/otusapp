package arch.homework.authserver.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserEvent {

    private User user;

    private EventType eventType;
}
