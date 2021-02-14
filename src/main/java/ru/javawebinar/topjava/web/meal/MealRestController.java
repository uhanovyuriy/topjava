package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        final int userId = SecurityUtil.authUserId();
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void update(Meal meal, int mealId) {
        final int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, mealId);
        service.update(meal, mealId, userId);
    }

    public void delete(int mealId) {
        final int userId = SecurityUtil.authUserId();
        service.delete(mealId, userId);
    }

    public MealTo get(int mealId) {
        final int userId = SecurityUtil.authUserId();
        return service.get(mealId, userId);
    }

    public List<MealTo> getAll() {
        final int userId = SecurityUtil.authUserId();
        return service.getAll(userId);
    }

    public List<MealTo> getBetweenDate(LocalDateTime start, LocalDateTime end) {
        final int userId = SecurityUtil.authUserId();
        return service.getBetweenDate(userId, start, end);
    }
}