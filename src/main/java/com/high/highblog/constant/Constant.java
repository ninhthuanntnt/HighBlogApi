package com.high.highblog.constant;

public final class Constant {
    public static final String SYSTEM_ACCOUNT = "system";

    public static final Long FIVE_MINUTE_EXPIRATION = 300000L;

    public static final Long ONE_MINUTE_EXPIRATION = 60000L;

    public static final String FILE_NAME_REGEX = "([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+(.)$";

    public static final String REGISTRATION_CONFIRMATION_CODE_ID_COOKIE= "registrationConfirmationCodeId";

    public static class Expiration {
        public static final Long CONFIRMATION_CODE_EXPIRATION = FIVE_MINUTE_EXPIRATION;
    }
}
