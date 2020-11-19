package arch.homework.billing.mapper;

import arch.homework.billing.entity.Account;
import arch.homework.billing.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "user.id", target = "userId")
    Account toAccount(User user, Long id);
}
