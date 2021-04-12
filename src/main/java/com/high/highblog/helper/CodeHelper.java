package com.high.highblog.helper;

import java.util.UUID;

public class CodeHelper {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
