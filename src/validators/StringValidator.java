package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator implements Validator {
    private Pattern p = Pattern.compile("^[A-ZА-Яa-zа-я]{4,45}$");

    @Override
    public boolean validate(String vString) {
        Matcher m = p.matcher(vString);
        return m.find();
    }
}
