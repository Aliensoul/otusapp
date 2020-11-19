package arch.homework.authserver.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Credentials {

    private String username;

    private String password;
}
