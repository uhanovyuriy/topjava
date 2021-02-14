package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void update(Meal meal, int mealId, int userId) {
        checkNotFoundWithId(repository.save(meal, userId), mealId);
    }

    public void delete(int mealId, int userId) {
        checkNotFoundWithId(repository.delete(mealId, userId), mealId);
    }

    public MealTo get(int mealId, int userId) {
        return getAll(userId).stream()
                .filter(mealTo -> mealTo.getId() == mealId)
                .findFirst().orElseThrow(() -> new NotFoundException("Meal not found with id:" + mealId));
    }

    public List<MealTo> getAll(int userId) {
        return MealsUtil.getTos(checkNotFoundWithId(repository.getAll(userId), userId),
                MealsUtil.DEFAULT_CALORIES_PER_DAY) ;
    }

    public List<MealTo> getBetweenDate(int userId, LocalDateTime start, LocalDateTime end) {
        return MealsUtil.getTos(checkNotFoundWithId(repository.getBetweenDate(userId, start, end), userId),
                MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }
}