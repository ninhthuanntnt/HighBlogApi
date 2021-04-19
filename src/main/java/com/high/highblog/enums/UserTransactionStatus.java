package com.high.highblog.enums;

public enum UserTransactionStatus {
    CREATED, IN_PROGRESS, FINISHED, FAILED;

    public static boolean isFinalStatus(final UserTransactionStatus status) {
        if (status == FINISHED || status == FAILED) {
            return true;
        }
        return false;
    }
}
