package org.infinispan.demo.util;

import java.util.HashMap;
import java.util.Map;

public class CmdArgs {

    public static final String CACHE_NAME_KEY = "cache";
    public static final String LOGIN_KEY = "user";
    public static final String PASS_KEY = "password";
    public static final String PARAM_PREFIX = "--";
    public static final String PARAM_SEP = "=";

    public static Map<String, String> getCredentials(String[] args) {
        if (args.length < 2)
            throw new IllegalArgumentException("At least login and password required!");

        Map<String, String> userArgs = new HashMap<>();
        for (String arg : args) {
            String[] argArray = extractParam(arg);
            switch (argArray[0]) {
            case PARAM_PREFIX + CACHE_NAME_KEY:
                userArgs.put(CACHE_NAME_KEY, argArray[1]);
                break;
            case PARAM_PREFIX + LOGIN_KEY:
                userArgs.put(LOGIN_KEY, argArray[1]);
                break;
            case PARAM_PREFIX + PASS_KEY:
                userArgs.put(PASS_KEY, argArray[1]);
                break;
            default:
                throw new IllegalArgumentException("Unknown argument " + argArray[0]);
            }
        }
        return userArgs;
    }

    public static String[] extractParam(String paramStr) {
        String[] param = paramStr.split(PARAM_SEP, 2);
        if (param.length != 2)
            throw new IllegalArgumentException("Specify arguments as --key=value");
        return new String[] { param[0], param[1] };
    }

}
