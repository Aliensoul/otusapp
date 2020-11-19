package arch.homework.billing.service;


import arch.homework.billing.entity.GetBalanceResponse;
import arch.homework.billing.entity.MakeReplenishmentRequest;
import arch.homework.billing.entity.Result;

public interface AccountsService {

    GetBalanceResponse getBalance(Long userId);

    Result makeReplenishment(Long userId, MakeReplenishmentRequest request);
}
