package arch.homework.otusapp.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

}
