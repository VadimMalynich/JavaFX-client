package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface CheckLogin {
    Pattern p = Pattern.compile(".{5,20}");
    default boolean isValidLogin(String string){
        Matcher m = p.matcher(string);
        if (m.find() && string.length() <= 20) {
            return true;
        }
        return false;
    }
}
