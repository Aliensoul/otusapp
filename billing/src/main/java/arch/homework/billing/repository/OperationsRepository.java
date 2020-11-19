package arch.homework.billing.repository;

import arch.homework.billing.entity.Operation;

import java.math.BigDecimal;

public interface OperationsRepository {

    boolean orderBilled(Long orderId);

    void saveOperation(Long accountId, Long orderId, BigDecimal amount, String status);

    Operation getOperationByOrderId(Long orderId);

    void updateOperationStatus(Long orderId, String toString);
}
