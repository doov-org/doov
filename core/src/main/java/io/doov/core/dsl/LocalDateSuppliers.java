package io.doov.core.dsl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.function.Supplier;

public class LocalDateSuppliers {

    private LocalDateSuppliers() {
    }

    public static Supplier<LocalDate> today() {
        return LocalDate::now;
    }

    public static Supplier<LocalDate> todayPlus(int amountToAdd, TemporalUnit unit) {
        return () -> LocalDate.now().plus(amountToAdd, unit);
    }

    public static Supplier<LocalDate> todayPlusDays(int daysToAdd) {
        return todayPlus(daysToAdd, ChronoUnit.DAYS);
    }

    public static Supplier<LocalDate> todayPlusYears(int yearsToAdd) {
        return todayPlus(yearsToAdd, ChronoUnit.YEARS);
    }

    public static Supplier<LocalDate> todayMinus(int amountToSubstract, TemporalUnit unit) {
        return () -> LocalDate.now().minus(amountToSubstract, unit);
    }

    public static Supplier<LocalDate> todayMinusDays(int daysToSubstract) {
        return todayMinus(daysToSubstract, ChronoUnit.DAYS);
    }

    public static Supplier<LocalDate> todayMinusYears(int yearsToSubstract) {
        return todayMinus(yearsToSubstract, ChronoUnit.YEARS);
    }

    public static Supplier<LocalDate> date(int year, int month, int dayOfMonth) {
        return () -> LocalDate.of(year, month, dayOfMonth);
    }

}