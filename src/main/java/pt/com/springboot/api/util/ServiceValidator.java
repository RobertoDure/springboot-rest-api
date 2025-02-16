package pt.com.springboot.api.util;

public class ServiceValidator {

    // Id validation
    public static boolean filterValidation(String filter, String value) {
        if (filter.equals("id")) {
            try {
                Long.parseLong(value);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        if (filter.equals("name")) {
            return isStringValid(value);
        }
        return true;
    }

    public static boolean idValid(String id) {
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // String validation
    public static boolean isStringValid(String string) {
        return string != null && !string.trim().isEmpty();
    }

}
