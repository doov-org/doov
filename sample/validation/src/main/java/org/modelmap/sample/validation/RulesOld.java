package org.modelmap.sample.validation;

import org.modelmap.sample.model.*;

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

    public static boolean validateCountry(Account account) {
        if (account == null) {
            return true;
        }
        if (account.getCountry() == null) {
            return true;
        }
        if (account.getLanguage() == null) {
            return true;
        }
        if (account.getPhoneNumber() == null) {
            return true;
        }
        if (account.getCountry().equals(Country.FR)
                        && account.getLanguage().equals(Language.FR)
                        && account.getPhoneNumber().startsWith("+33")) {
            return true;
        }
        if (account.getCountry().equals(Country.UK)
                        && account.getLanguage().equals(Language.EN)
                        && account.getPhoneNumber().startsWith("+45")) {
            return true;
        }
        return false;
    }

}
