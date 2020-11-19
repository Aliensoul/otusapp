package arch.homework.billing.service.impl;

import arch.homework.billing.entity.*;
import arch.homework.billing.repository.AccountRepository;
import arch.homework.billing.repository.OperationsRepository;
import arch.homework.billing.service.OrderProcessingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class OrderProcessingServiceImpl implements OrderProcessingService {

    private final AccountRepository accountRepository;
    private final OperationsRepository operationsRepository;

    @Override
    @Transactional
    public OrderStatusChangedEvent pay(Long orderId, CreateOrderEvent request) {
        Account account = accountRepository.lockAccountForPayment(request.getUserId());

        if (account == null) {
            return new OrderStatusChangedEvent(-400, "Could not find account for userId" + request.getUserId())
                    .setOrderId(orderId).setStatus(SagaStatus.FAILED.toString());
        }

        if (account.getBalance().compareTo(request.getPrice()) == -1) {
            return new OrderStatusChangedEvent(-300, "Not enough balance for operation")
                    .setOrderId(orderId).setStatus(SagaStatus.FAILED.toString());
        }

        accountRepository.pay(account.getId(), request.getPrice());
        operationsRepository.saveOperation(account.getId(), orderId, request.getPrice(), OrderBillingStatus.AUTHORIZED.toString());
        return new OrderStatusChangedEvent(0, null).setOrderId(orderId).setStatus(SagaStatus.PREPARED.toString());
    }

    @Override
    @Transactional
    public OrderStatusChangedEvent cancel(OrderStatusChangedEvent event) {
        Operation operation = operationsRepository.getOperationByOrderId(event.getOrderId());

        if (operation != null){
            operationsRepository.updateOperationStatus(operation.getOrderId(), OrderBillingStatus.CANCELLED.toString());
            accountRepository.makeReplenishment(new MakeReplenishmentRequest().setAmount(operation.getAmount()), event.getUserId());
        }

        return event.setStatus(SagaStatus.CANCELED.toString());
    }

    @Override
    public OrderStatusChangedEvent confirm(OrderStatusChangedEvent event) {
        Operation operation = operationsRepository.getOperationByOrderId(event.getOrderId());
        operationsRepository.updateOperationStatus(operation.getOrderId(), OrderBillingStatus.BILLED.toString());

        return event.setStatus(SagaStatus.COMPLETED.toString());
    }
}
