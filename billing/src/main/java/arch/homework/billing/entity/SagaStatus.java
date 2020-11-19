package arch.homework.billing.entity;

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
