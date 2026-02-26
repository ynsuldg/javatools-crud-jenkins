package se.iths.yunus.javatools.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import se.iths.yunus.javatools.model.Drink;
import se.iths.yunus.javatools.repository.DrinkRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DrinkIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DrinkRepository drinkRepository;

    @BeforeEach
    void setup() {
        drinkRepository.deleteAll();
    }

    @Test
    void createDrink_andVerifyItIsSaved() throws Exception {
        mockMvc.perform(post("/drinks")
                        .param("name", "Nocco")
                        .param("flavour", "Black Orange")
                        .param("price", "25.00")
                        .param("expireDate", Date.valueOf(LocalDate.now().plusDays(10)).toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/drinks"));

        var all = drinkRepository.findAll();
        org.junit.jupiter.api.Assertions.assertEquals(1, all.size());
        org.junit.jupiter.api.Assertions.assertEquals("Nocco", all.get(0).getName());
    }

    @Test
    void getDrinkById_afterCreatingWithRepository_verifyItExists() throws Exception {
        Drink saved = drinkRepository.save(new Drink(
                "Powerking",
                "Strawberry",
                new BigDecimal("15.00"),
                Date.valueOf(LocalDate.now().plusDays(30))
        ));

        mockMvc.perform(get("/drinks/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("drink-findbyid"))
                .andExpect(model().attributeExists("drink"))
                .andExpect(model().attribute("drink", hasProperty("id", is(saved.getId()))))
                .andExpect(model().attribute("drink", hasProperty("name", is("Powerking"))));
    }

    @Test
    void getAllDrinks_afterCreatingSeveralWithRepository_verifyCount() throws Exception {
        drinkRepository.save(new Drink("A", "X", new BigDecimal("25.00"), Date.valueOf(LocalDate.now().plusDays(10))));
        drinkRepository.save(new Drink("B", "Y", new BigDecimal("20.00"), Date.valueOf(LocalDate.now().plusDays(11))));
        drinkRepository.save(new Drink("C", "Z", new BigDecimal("15.00"), Date.valueOf(LocalDate.now().plusDays(12))));

        mockMvc.perform(get("/drinks"))
                .andExpect(status().isOk())
                .andExpect(view().name("drink-findall"))
                .andExpect(model().attributeExists("drinks"))
                .andExpect(model().attribute("drinks", hasSize(3)));
    }
}