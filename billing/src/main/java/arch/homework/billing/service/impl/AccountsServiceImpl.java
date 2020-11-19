package arch.homework.billing.service.impl;

import arch.homework.billing.entity.GetBalanceResponse;
import arch.homework.billing.entity.MakeReplenishmentRequest;
import arch.homework.billing.entity.Result;
import arch.homework.billing.repository.AccountRepository;
import arch.homework.billing.service.AccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private AccountRepository accountRepository;

    @Override
    public GetBalanceResponse getBalance(Long userId) {
        BigDecimal balance = accountRepository.getUsersAccount(userId).getBalance();
        return new GetBalanceResponse(0, "ok").setBalance(balance);
    }

    @Override
    public Result makeReplenishment(Long userId, MakeReplenishmentRequest request) {
        accountRepository.makeReplenishment(request, userId);
        return new Result(0, "ok");
    }
}
