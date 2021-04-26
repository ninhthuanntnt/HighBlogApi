package com.high.highblog.enums;

public enum ThirdPartyTransactionStatus {
    CREATED, APPROVED, PENDING, COMPLETED, FAILED, CANCELED,

    //Payout status
    SUCCESS, UNCLAIMED, ONHOLD, BLOCKED;

    public boolean isFinalStatus() {
        return this == COMPLETED
                || this == SUCCESS
                || this == FAILED
                || this == CANCELED;
    }
}
