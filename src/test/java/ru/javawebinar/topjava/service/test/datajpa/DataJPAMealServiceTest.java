package ru.javawebinar.topjava.service.test.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.test.AbstractMealServiceTest;

import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.MATCHER;


@ActiveProfiles("datajpa")
public class DataJPAMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void testGetByIdWithUser() throws Exception {
        Meal actual = service.getByIdWithUser(ADMIN_MEAL_ID);
        MATCHER.assertEquals(ADMIN, actual.getUser());
    }
}