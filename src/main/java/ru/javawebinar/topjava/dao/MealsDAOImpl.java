package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Администратор on 25.07.2017.
 */
public class MealsDAOImpl implements MealsDAO {

    private static Map<Long, Meal> meals;
    private static AtomicLong idSequence;

    static {
        idSequence = new AtomicLong(0);
        meals = new ConcurrentHashMap<>();
        meals.put(idSequence.incrementAndGet(), new Meal(idSequence.get(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        meals.put(idSequence.incrementAndGet(), new Meal(idSequence.get(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        meals.put(idSequence.incrementAndGet(), new Meal(idSequence.get(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        meals.put(idSequence.incrementAndGet(), new Meal(idSequence.get(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        meals.put(idSequence.incrementAndGet(), new Meal(idSequence.get(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        meals.put(idSequence.incrementAndGet(), new Meal(idSequence.get(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    public Meal read(Long id) {
        return meals.get(id);
    }

    public void create(Meal meal) {
        Long newId = idSequence.incrementAndGet();
        meal.setId(newId);
        meals.put(newId, meal);
    }

    public void update(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    public void delete(Long id) {
        meals.remove(id);
    }

    public List<MealWithExceed> list() {
        return MealsUtil.getFilteredWithExceeded(new ArrayList<>(meals.values()), LocalTime.MIN, LocalTime.MAX, 2000);
    }

}