package ru.javawebinar.topjava.web;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class RootControllerTest extends AbstractControllerTest {

    @Test
    public void testUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("users", hasItem(
                        allOf(
                                hasProperty("id", is(START_SEQ)),
                                hasProperty("name", is(USER.getName()))
                        )
                )));
    }

    @Test
    public void testMeals() throws Exception {
        mockMvc.perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(6)))
                .andExpect(model().attribute("meals", hasItems(
                        allOf(
                                hasProperty("id", is(MEAL1_ID)),
                                hasProperty("dateTime", is(LocalDateTime.of(2015, Month.MAY, 30, 10, 0))),
                                hasProperty("description", is("Завтрак")),
                                hasProperty("calories", is(500))
                        ),
                        allOf(
                                hasProperty("id", is(MEAL1_ID + 1)),
                                hasProperty("dateTime", is(LocalDateTime.of(2015, Month.MAY, 30, 13, 0))),
                                hasProperty("description", is("Обед")),
                                hasProperty("calories", is(1000))
                        ),
                        allOf(
                                hasProperty("id", is(MEAL1_ID + 2)),
                                hasProperty("dateTime", is(LocalDateTime.of(2015, Month.MAY, 30, 20, 0))),
                                hasProperty("description", is("Ужин")),
                                hasProperty("calories", is(500))
                        ),
                        allOf(
                                hasProperty("id", is(MEAL1_ID + 3)),
                                hasProperty("dateTime", is(LocalDateTime.of(2015, Month.MAY, 31, 10, 0))),
                                hasProperty("description", is("Завтрак")),
                                hasProperty("calories", is(500))
                        ),
                        allOf(
                                hasProperty("id", is(MEAL1_ID + 4)),
                                hasProperty("dateTime", is(LocalDateTime.of(2015, Month.MAY, 31, 13, 0))),
                                hasProperty("description", is("Обед")),
                                hasProperty("calories", is(1000))
                        ),
                        allOf(
                                hasProperty("id", is(MEAL1_ID + 5)),
                                hasProperty("dateTime", is(LocalDateTime.of(2015, Month.MAY, 31, 20, 0))),
                                hasProperty("description", is("Ужин")),
                                hasProperty("calories", is(510))
                        )
                )));
    }
}