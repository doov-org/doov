package io.doov.sample.validation;

import static java.time.temporal.ChronoUnit.YEARS;

import java.time.LocalDate;

import io.doov.sample.model.*;

public class RulesOld {

    public static boolean validateEmail(Account account) {
        if (account == null) {
            return true;
        }
        if (account.getEmail() == null) {
            return true;
        }
        if (account.getEmail().matches("\\w+[@]\\w+\\.com")) {
            return true;
        }
        if (account.getEmail().matches("\\w+[@]\\w+\\.fr")) {
            return true;
        }
        return false;
    }

    public static boolean validateAccount(User user, Account account, Configuration config) {
        if (config == null) {
            return false;
        }
        if (user == null || user.getBirthDate() == null) {
            return false;
        }
        if (account == null || account.getCountry() == null || account.getPhoneNumber() == null) {
            return false;
        }
        if (YEARS.between(user.getBirthDate(), LocalDate.now()) >= 18
                        && account.getEmail().length() <= config.getMaxEmailSize()
                        && account.getCountry().equals(Country.FR) && account.getPhoneNumber().startsWith("+33")) {
            return true;
        }
        return false;
    }

}
