package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator implements Validator {
    private Pattern p = Pattern.compile("^\\+375+([2][459]|33|44)+[1-9]+\\d{6}$");

    @Override
    public boolean validate(String vString) {
        Matcher m = p.matcher(vString);
        return m.find();
    }
}
