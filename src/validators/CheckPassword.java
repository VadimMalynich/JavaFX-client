package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface CheckPassword {
    Pattern p = Pattern.compile("(?=.*[0-9])(?=.*[a-z])[0-9a-z]{6,}");
    default boolean isValidPassword(String string) {
        Matcher m = p.matcher(string);
        if (m.find() && string.length() <= 20) {
            return true;
        }
        return false;
    }
}
