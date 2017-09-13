package ru.javawebinar.topjava.service.test.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.test.AbstractUserServiceTest;

import java.util.Arrays;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;


@ActiveProfiles("datajpa")
public class DataJPAUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void testGetByIdWithUser() throws Exception {
        User admin = service.getByIdWithMeals(ADMIN_ID);
        MATCHER.assertCollectionEquals(admin.getMeals(), Arrays.asList(ADMIN_MEAL2, ADMIN_MEAL1));
    }
}