package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testSave() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.of(2017, 9, 3, 10, 0), "secondBreakfast", 300);
        Meal created = service.save(newMeal, UserTestData.USER_ID);
        newMeal.setId(created.getId());
        MATCHER.assertCollectionEquals(Arrays.asList(DINNER1, LUNCH1, newMeal, BREAKFAST1), service.getAll(UserTestData.USER_ID));
    }

    @Test(expected = DataAccessException.class)
    public void testDuplicateDateTimeSave() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.of(2017, 9, 3, 8, 0), "secondBreakfastError", 300);
        service.save(newMeal, UserTestData.USER_ID);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(BREAKFAST1_ID, UserTestData.USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(DINNER1, LUNCH1), service.getAll(UserTestData.USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteBadUser() throws Exception {
        service.delete(LUNCH2_ID, UserTestData.USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete() throws Exception {
        service.delete(1, UserTestData.USER_ID);
    }

    @Test
    public void testGet() throws Exception {
        Meal meal = service.get(LUNCH1_ID, UserTestData.USER_ID);
        MATCHER.assertEquals(LUNCH1, meal);
    }

    @Test(expected = NotFoundException.class)
    public void testGetBadUser() throws Exception {
        service.get(DINNER1_ID, UserTestData.ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        service.get(1, UserTestData.USER_ID);
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        List<Meal> meals = service.getBetweenDateTimes(
                LocalDateTime.of(2017, 9, 3, 13, 0),
                LocalDateTime.of(2017, 9, 3, 23, 0),
                UserTestData.ADMIN_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(DINNER2, LUNCH2), meals);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<Meal> all = service.getAll(UserTestData.ADMIN_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(DINNER2, LUNCH2, BREAKFAST2), all);
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = new Meal(DINNER1);
        updated.setDescription("UpdatedDinner1");
        updated.setCalories(670);
        service.update(updated, UserTestData.USER_ID);
        MATCHER.assertEquals(updated, service.get(DINNER1_ID, UserTestData.USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateBadUser() throws Exception {
        Meal updated = new Meal(LUNCH1);
        updated.setDescription("UpdatedLunch1");
        updated.setCalories(670);
        service.update(updated, UserTestData.ADMIN_ID);
    }
}