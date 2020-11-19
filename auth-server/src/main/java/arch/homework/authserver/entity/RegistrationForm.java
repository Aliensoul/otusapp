package arch.homework.authserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class RegistrationForm {

    @JsonIgnore
    private Long id;

    @JsonUnwrapped
    private Credentials credentials;

    @NotNull(message = "Не передано обязательное поле email")
    private String email;
}
