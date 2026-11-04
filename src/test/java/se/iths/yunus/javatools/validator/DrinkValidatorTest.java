package se.iths.yunus.javatools.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.iths.yunus.javatools.exception.InvalidDrinkExpireDateException;
import se.iths.yunus.javatools.exception.InvalidDrinkFlavourException;
import se.iths.yunus.javatools.exception.InvalidDrinkNameException;
import se.iths.yunus.javatools.exception.InvalidDrinkPriceException;
import se.iths.yunus.javatools.model.Drink;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DrinkValidatorTest {

    private DrinkValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DrinkValidator();
    }

    @Test
    void validDrinkShouldPass() {
        Drink drink = new Drink("Nocco", "Orange", new BigDecimal("25.00"), Date.valueOf(LocalDate.now().plusYears(1)));
        assertDoesNotThrow(() -> validator.validate(drink));
    }

    @Test
    void blankNameShouldThrow() {
        Drink drink = new Drink(" ", "Orange", new BigDecimal("25.00"), Date.valueOf("2026-03-21"));
        assertThrows(InvalidDrinkNameException.class, () -> validator.validate(drink));
    }

    @Test
    void nullNameShouldThrow() {
        Drink drink = new Drink(null, "Strawberry", new BigDecimal("15.00"), Date.valueOf("2026-05-05"));
        assertThrows(InvalidDrinkNameException.class, () -> validator.validate(drink));
    }

    @Test
    void blankFlavourShouldThrow() {
        Drink drink = new Drink("Powerking", " ", new BigDecimal("15.00"), Date.valueOf("2026-05-05"));
        assertThrows(InvalidDrinkFlavourException.class, () -> validator.validate(drink));
    }

    @Test
    void zeroOrNegativePriceShouldThrow() {
        Drink drink = new Drink("Nocco", "Orange", new BigDecimal("0"), Date.valueOf("2026-03-21"));
        assertThrows(InvalidDrinkPriceException.class, () -> validator.validate(drink));
    }

    @Test
    void nullExpireDateShouldThrow() {
        Drink drink = new Drink("Nocco", "Orange", new BigDecimal("25.00"), null);
        assertThrows(InvalidDrinkExpireDateException.class, () -> validator.validate(drink));
    }

    @Test
    void oldExpireDateShouldThrow() {
        Drink drink = new Drink("Nocco", "Orange", new BigDecimal("25.00"), Date.valueOf("2025-12-31"));
        assertThrows(InvalidDrinkExpireDateException.class, () -> validator.validate(drink));
    }
}