package arch.homework.orders.entity;

import lombok.Getter;

@Getter
public enum SagaStatus {

    NEW,
    PREPARED,
    CONFIRMED,
    FAILED,
    CANCELED,
    COMPLETED;
}
