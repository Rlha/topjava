package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.util.List;

/**
 * Created by Администратор on 26.07.2017.
 */
public interface MealsDAO {

    Meal read(Long id);

    void create(Meal meal);

    void update(Meal meal);

    void delete(Long id);

    List<MealWithExceed> list();

}
