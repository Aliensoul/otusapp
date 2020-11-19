package arch.homework.notifications.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AccountEvent {

    private Account account;

    private EventType eventType;
}
