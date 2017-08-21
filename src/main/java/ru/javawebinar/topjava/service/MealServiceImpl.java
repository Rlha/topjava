package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUserId(userId);
        return repository.save(meal, userId);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public List<Meal> getAllFiltered(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return repository.getAll(userId).stream().filter(mealWithExceed ->
                DateTimeUtil.isBetween(mealWithExceed.getDateTime().toLocalDate(), startDate, endDate) &&
                        DateTimeUtil.isBetween(mealWithExceed.getDateTime().toLocalTime(), startTime, endTime)).collect(Collectors.toList());
    }

    @Override
    public void update(Meal meal, int userId) {
        //TODO: check without finding
        meal.setUserId(userId);
        checkNotFoundWithId(repository.get(meal.getId(), userId), meal.getId());
        repository.save(meal, userId);
    }

}