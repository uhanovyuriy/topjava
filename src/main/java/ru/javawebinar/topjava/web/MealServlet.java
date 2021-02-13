package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryInMemory;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MealServlet extends HttpServlet {
    private final MealRepository repository;

    public MealServlet() {
        this.repository = new MealRepositoryInMemory();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String action = req.getParameter("action");

        if (action == null) {
            req.setAttribute("mealTos", toMealTo(repository.getList()));
            req.getRequestDispatcher("meals.jsp").forward(req, resp);
        } else if (action.equalsIgnoreCase("update")) {
            final int id = Integer.parseInt(req.getParameter("id"));
            final Meal meal = repository.get(id);
            req.setAttribute("meal", meal);
            req.getRequestDispatcher("meal.jsp").forward(req, resp);
        } else if (action.equalsIgnoreCase("delete")) {
            final int id = Integer.parseInt(req.getParameter("id"));
            repository.delete(id);
            req.setAttribute("mealTos", toMealTo(repository.getList()));
            req.getRequestDispatcher("meals.jsp").forward(req, resp);
        } else if (action.equalsIgnoreCase("create")) {
            resp.sendRedirect("meal.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        final LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"), formatter);
        final String description = req.getParameter("description");
        final int calories = Integer.parseInt(req.getParameter("calories"));
        final Meal meal = new Meal(dateTime, description, calories);

        if (req.getParameter("id") != null) {
            final int id = Integer.parseInt(req.getParameter("id"));
            meal.setId(id);
            repository.update(meal, id);
        } else {
            repository.create(meal);
        }

        req.setAttribute("mealTos", toMealTo(repository.getList()));
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }

    private List<MealTo> toMealTo(List<Meal> meals) {
        return MealsUtil.filteredByStreams(meals, LocalTime.of(0, 0),
                LocalTime.of(23, 59), 2000);
    }
}
