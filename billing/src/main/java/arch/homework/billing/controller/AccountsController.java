package arch.homework.billing.controller;

import arch.homework.billing.entity.GetBalanceResponse;
import arch.homework.billing.entity.MakeReplenishmentRequest;
import arch.homework.billing.entity.Result;
import arch.homework.billing.service.AccountsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
public class AccountsController {

    public AccountsService accountsService;

    public static final String BASE_PATH = "/billing";
    public static final String GET_BALANCE = BASE_PATH + "/get_balance";
    public static final String MAKE_REPLENISHMENT = BASE_PATH + "/make_replenishment";

    @GetMapping(GET_BALANCE)
    public GetBalanceResponse getBalance(HttpServletRequest httpServletRequest) {
        return accountsService.getBalance(Long.valueOf(httpServletRequest.getHeader("X-User-Id")));
    }

    @PostMapping(MAKE_REPLENISHMENT)
    public Result makeReplenishment(HttpServletRequest httpServletRequest, @RequestBody MakeReplenishmentRequest request) {
        return accountsService.makeReplenishment(Long.valueOf(httpServletRequest.getHeader("X-User-Id")), request);
    }


}
