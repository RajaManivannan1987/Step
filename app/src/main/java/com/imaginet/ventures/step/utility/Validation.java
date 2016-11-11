package com.imaginet.ventures.step.utility;

/**
 * Created by IM028 on 9/7/16.
 */
public class Validation {
    public static boolean emailValidation(String string) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches();
    }

    public static boolean passwordValidation(String string) {
        return string.length() > 0;
    }

}
