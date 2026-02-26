package se.iths.yunus.javatools.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.iths.yunus.javatools.model.Drink;
import se.iths.yunus.javatools.service.DrinkService;

@Controller
@RequestMapping("/drinks")
public class DrinkController {

    private static final Logger log = LoggerFactory.getLogger(DrinkController.class);

    private final DrinkService drinkService;

    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    // LIST ALL
    @GetMapping
    public String getAllDrinks(Model model) {
        log.info("HTTP GET /drinks");
        model.addAttribute("drinks", drinkService.getAllDrinks());
        return "drink-findall";
    }

    // SHOW ONE
    @GetMapping("/{id}")
    public String getDrink(@PathVariable Long id, Model model) {
        log.info("HTTP GET /drinks/{}", id);
        model.addAttribute("drink", drinkService.getDrink(id));
        return "drink-findbyid";
    }

    // SHOW CREATE FORM
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        log.info("HTTP GET /drinks/new");
        model.addAttribute("drink", new Drink());
        return "drink-post";
    }

    // CREATE
    @PostMapping
    public String postNewDrink(@ModelAttribute Drink drink) {
        log.info("HTTP POST /drinks (name={}, flavour={}, price={}, expireDate={})",
                drink.getName(), drink.getFlavour(), drink.getPrice(), drink.getExpireDate());
        drinkService.postNewDrink(drink);
        return "redirect:/drinks";
    }

    // SHOW EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.info("HTTP GET /drinks/edit/{}", id);
        model.addAttribute("drink", drinkService.getDrink(id));
        return "drink-put";
    }

    // UPDATE
    @PutMapping("/{id}")
    public String putExistingDrink(@PathVariable Long id, @ModelAttribute Drink drink) {
        log.info("HTTP PUT /drinks/{} (name={}, flavour={}, price={}, expireDate={})",
                id, drink.getName(), drink.getFlavour(), drink.getPrice(), drink.getExpireDate());
        drinkService.putExistingDrink(id, drink);
        return "redirect:/drinks";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteDrink(@PathVariable Long id) {
        log.info("HTTP DELETE /drinks/{}", id);
        drinkService.deleteDrink(id);
        return "redirect:/drinks";
    }
}