package arch.homework.authserver.mapper;

import arch.homework.authserver.entity.RegistrationForm;
import arch.homework.authserver.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mapping(source = "form.credentials.username", target = "username")
    User mapToUser(RegistrationForm form);
}
