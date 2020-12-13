package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberValidator implements Validator{
    Pattern p = Pattern.compile("^[1-4]?\\d{1,2}$|^[5][0][0]$");

    @Override
    public boolean validate(String vString) {
        Matcher m = p.matcher(vString);
        return m.find();
    }
}
