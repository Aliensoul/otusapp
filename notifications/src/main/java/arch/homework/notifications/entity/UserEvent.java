package arch.homework.notifications.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserEvent {

    private User user;

    private EventType eventType;
}
