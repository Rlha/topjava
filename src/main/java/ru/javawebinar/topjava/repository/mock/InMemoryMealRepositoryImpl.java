package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> {
            meal.setUserId(AuthorizedUser.id());
            this.save(meal, AuthorizedUser.id());
        });
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Meal savedMeal;
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            savedMeal = repository.put(meal.getId(), meal);
        } else {
            savedMeal = repository.compute(meal.getId(), (id, oldMeal) -> (oldMeal.getUserId() == meal.getUserId()) ? meal : oldMeal);
        }
        return savedMeal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.entrySet().removeIf(meal -> meal.getValue().getId() == id && meal.getValue().getUserId() == userId);
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.computeIfPresent(id, (integer, meal) -> meal.getUserId() == userId ? meal : null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime()))
                .collect(Collectors.toList());
    }
}

