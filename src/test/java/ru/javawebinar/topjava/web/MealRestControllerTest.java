package ru.javawebinar.topjava.web;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + (MEAL1_ID + 3)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(MEAL4));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + (MEAL1_ID + 2)))
                .andDo(print())
                .andExpect(status().isOk());
        MATCHER.assertListEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL2, MEAL1), mealService.getAll(USER_ID));
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_WITH_EXCEED_BEAN_MATCHER.contentListMatcher(MealsUtil.getWithExceeded(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), MealsUtil.DEFAULT_CALORIES_PER_DAY)));
    }

    @Test
    public void testCreate() throws Exception {
        Meal expected = new Meal(null, LocalDateTime.of(2016, Month.MAY, 30, 13, 0), "newPass", 500);
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected))).andExpect(status().isCreated());

        Meal returned = MATCHER.fromJsonAction(action);
        expected.setId(returned.getId());

        MATCHER.assertEquals(expected, returned);
        MATCHER.assertListEquals(Arrays.asList(expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), mealService.getAll(USER_ID));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = new Meal(MEAL5.getId(), MEAL5.getDateTime(), MEAL5.getDescription(), MEAL5.getCalories());
        updated.setDescription("UpdatedMeal");
        updated.setCalories(600);
        mockMvc.perform(put(REST_URL + MEAL5.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk());

        MATCHER.assertEquals(updated, mealService.get(MEAL5.getId(), USER_ID));
    }

    @Test
    public void testGetBetween() throws Exception {
        LocalDateTime start = LocalDateTime.of(2015, Month.MAY, 31, 8, 0);
        LocalDateTime end = LocalDateTime.of(2015, Month.MAY, 31, 19, 0);
        mockMvc.perform(get(REST_URL + "filter")
                .param("start", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(start))
                .param("end", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(end)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_WITH_EXCEED_BEAN_MATCHER.contentListMatcher(
                        MealsUtil.getFilteredWithExceeded(Arrays.asList(MEAL6, MEAL5, MEAL4), start.toLocalTime(), end.toLocalTime(), MealsUtil.DEFAULT_CALORIES_PER_DAY)));
    }
}