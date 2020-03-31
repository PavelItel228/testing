package kpi.prject.testing.testing.validators;

import kpi.prject.testing.testing.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.Locale;

import static jdk.nashorn.internal.runtime.ECMAErrors.getMessage;


public class NewUserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        Locale locale = LocaleContextHolder.getLocale();
        if(user.getPassword().length() < 8) {
            errors.rejectValue("password", "1", "password must contain at least 8 characters");
            return;
        }
        try {
            double d = Double.parseDouble(user.getPassword());
            errors.rejectValue("password", "2", "password must contain letters" );
            return;
        } catch (NumberFormatException nfe) {
            nfe.getStackTrace();
        }

        if(!user.getConfirmPassword().equals(user.getPassword())){
            errors.rejectValue("confirmPassword", "4", "passwords do not match" );
        }
    }
}
