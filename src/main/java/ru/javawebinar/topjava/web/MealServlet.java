package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class MealServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final MealRepo mealRepo = new MealRepo();
        final List<MealTo> mealTos = MealsUtil.filteredByStreams(mealRepo.getMeals(),
                LocalTime.of(0, 0), LocalTime.of(23, 59), MealTo.EXCESS);
        req.setAttribute("mealTos", mealTos);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }
}
