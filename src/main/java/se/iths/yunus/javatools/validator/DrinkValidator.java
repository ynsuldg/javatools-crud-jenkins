package se.iths.yunus.javatools.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.iths.yunus.javatools.exception.InvalidDrinkExpireDateException;
import se.iths.yunus.javatools.exception.InvalidDrinkFlavourException;
import se.iths.yunus.javatools.exception.InvalidDrinkNameException;
import se.iths.yunus.javatools.exception.InvalidDrinkPriceException;
import se.iths.yunus.javatools.model.Drink;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DrinkValidator {
    private static final Logger log = LoggerFactory.getLogger(DrinkValidator.class);

    public void validate(Drink drink) {

        // NAME
        if (drink.getName() == null || drink.getName().isBlank()) {
            log.warn("Validation failed: name is empty");
            throw new InvalidDrinkNameException("Drink name cannot be empty");
        }

        // FLAVOUR
        if (drink.getFlavour() == null || drink.getFlavour().isBlank()) {
            log.warn("Validation failed: flavour is empty");
            throw new InvalidDrinkFlavourException("Flavour cannot be empty");
        }

        // PRICE
        if (drink.getPrice() == null || drink.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Validation failed: invalid price={}", drink.getPrice());
            throw new InvalidDrinkPriceException("Price must be greater than 0");
        }

        // EXPIRE DATE
        if (drink.getExpireDate() == null) {
            log.warn("Validation failed: expireDate is null");
            throw new InvalidDrinkExpireDateException("Expire date cannot be null");
        }

        LocalDate expire = drink.getExpireDate().toLocalDate();
        if (expire.isBefore(LocalDate.now())) {
            log.warn("Validation failed: expireDate in past. expireDate={}", expire);
            throw new InvalidDrinkExpireDateException("Expire date cannot be in the past");
        }
    }
}