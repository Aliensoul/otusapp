package arch.homework.notifications.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {

    private Long id;

    private String username;

    private String email;
}