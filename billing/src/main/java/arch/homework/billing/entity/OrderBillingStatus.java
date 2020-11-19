package arch.homework.billing.entity;

import lombok.Getter;

@Getter
public enum OrderBillingStatus {

    AUTHORIZED, BILLED, CANCELLED;
}
