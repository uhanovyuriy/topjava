package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.List;

public interface MealRepository {
    Meal create(Meal meal);

    void update(Meal meal, int id);

    void delete(int id);

    Meal get(int id);

    List<Meal> getList();
}
