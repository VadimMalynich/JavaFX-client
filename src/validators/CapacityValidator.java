package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapacityValidator implements Validator{
    Pattern p = Pattern.compile("^[1-9]\\d{3}$|^[1][0][0][0][0]$");

    @Override
    public boolean validate(String vString) {
        Matcher m = p.matcher(vString);
        return m.find();
    }
}
