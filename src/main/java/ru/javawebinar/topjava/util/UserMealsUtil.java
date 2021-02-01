package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final List<UserMealWithExcess> result = new ArrayList<>();
        int sumCalories = 0;
        for (int i = 0; i < meals.size(); i++) {
            final UserMeal meal = meals.get(i);
            sumCalories += meal != null ? meal.getCalories() : 0;
            if (isBetween(meal, startTime, endTime)) {
                final int day = meals.get(i).getDateTime().getDayOfMonth();
                int countDays = 0;
                final List<UserMealWithExcess> tempList = new ArrayList<>();
                for (int j = i; j < meals.size(); j++) {
                    final int nextDay = j + 1 < meals.size() ? meals.get(j + 1).getDateTime().getDayOfMonth() : 0;
                    final UserMeal tempMeal = meals.get(j);
                    if (day == nextDay) {
                        if (isBetween(tempMeal, startTime, endTime)) {
                            tempList.add(new UserMealWithExcess(tempMeal.getDateTime(), tempMeal.getDescription(), tempMeal.getCalories(), false));
                        }
                        sumCalories += meals.get(j + 1).getCalories();
                        countDays++;
                    }
                    if (day != nextDay) {
                        if (isBetween(tempMeal, startTime, endTime)) {
                            tempList.add(new UserMealWithExcess(tempMeal.getDateTime(), tempMeal.getDescription(), tempMeal.getCalories(), false));
                        }
                        if (sumCalories > caloriesPerDay) {
                            for (UserMealWithExcess mealWithExcess : tempList) {
                                mealWithExcess.setExcess(true);
                            }
                        }
                        i += countDays;

                        result.addAll(tempList);
                        sumCalories = 0;
                        break;
                    }
                }
            }
        }

        return result;
    }

    private static boolean isBetween(UserMeal meal, LocalTime startTime, LocalTime endTime) {
        return meal != null && meal.getDateTime().isAfter(LocalDateTime.of(LocalDate.of(meal.getDateTime().getYear(),
                meal.getDateTime().getMonth(), meal.getDateTime().getDayOfMonth()), startTime))
                && meal.getDateTime().isBefore(LocalDateTime.of(LocalDate.of(meal.getDateTime().getYear(),
                meal.getDateTime().getMonth(), meal.getDateTime().getDayOfMonth()), endTime));
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        final Predicate<UserMealWithExcess> betweenPredicate = meal -> meal.getDateTime()
                .isAfter(LocalDateTime.of(LocalDate.of(meal.getDateTime().getYear(),
                meal.getDateTime().getMonth(), meal.getDateTime().getDayOfMonth()), startTime))
                && meal.getDateTime().isBefore(LocalDateTime.of(LocalDate.of(meal.getDateTime().getYear(),
                meal.getDateTime().getMonth(), meal.getDateTime().getDayOfMonth()), endTime));

        final Predicate<List<UserMeal>> excessPredicate = userMeals -> userMeals.stream()
                .mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;

        final List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();

        meals.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().getDayOfMonth())).values().stream()
                .map(userMeals -> userMeals.stream()
                        .map(userMeal -> new UserMealWithExcess(userMeal, excessPredicate.test(userMeals)))
                        .filter(betweenPredicate)).forEach(userMealWithExcessStream -> userMealWithExcessStream
                        .forEach(mealWithExcesses::add));

        return mealWithExcesses;
    }
}
