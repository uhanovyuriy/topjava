package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.userOneMeals.forEach(meal -> this.save(meal, SecurityUtil.authUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        repository.putIfAbsent(userId, new HashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.get(userId).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.getOrDefault(userId, null).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.getOrDefault(userId, null).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getBetweenDate(userId, LocalDateTime.MIN, LocalDateTime.MAX);
    }

    @Override
    public List<Meal> getBetweenDate(int userId, LocalDateTime start, LocalDateTime end) {
        return repository.getOrDefault(userId, null).values().stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), start, end))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

