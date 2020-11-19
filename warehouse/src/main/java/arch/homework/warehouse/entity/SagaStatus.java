package arch.homework.warehouse.entity;

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
