package com.armpits.nice.utils;

import java.util.Arrays;
import java.util.List;

public class Const {
    public static final String SP_LOGGED_IN = "User has logged in";
    public static final String SP_ERROR = "error not found";

    public static final String SP_USERNAME = "Username";
    public static final String SP_PASSWORD = "Password";

    public static final String SP_UPDATE_FREQUENCY = "update frequency";

    public static final List<String> UPDATE_FREQUENCIES =
            Arrays.asList("30 minutes", "hour", "8 hours", "day", "week");
}
