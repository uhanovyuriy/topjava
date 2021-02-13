package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepositoryInMemory implements MealRepository {
    private final static AtomicInteger counter = new AtomicInteger();

    private final static Map<Integer, Meal> repo = new ConcurrentHashMap<>();

    @Override
    public Meal create(Meal meal) {
        meal.setId(counter.incrementAndGet());
        return repo.putIfAbsent(meal.getId(), meal);
    }

    @Override
    public void update(Meal meal, int id) {
        repo.put(id, meal);
    }

    @Override
    public void delete(int id) {
        repo.remove(id);
    }

    @Override
    public Meal get(int id) {
        return repo.get(id);
    }

    @Override
    public List<Meal> getList() {
        return new ArrayList<>(repo.values());
    }
}
