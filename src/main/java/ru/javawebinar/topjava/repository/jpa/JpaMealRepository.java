package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUser(new User(userId));
            em.persist(meal);
            return meal;
        } else {
            if (meal.getUser().getId() == userId) {
                return em.merge(meal);
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("uid", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = em.find(Meal.class, id);
        return meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.GET_ALL)
                .setParameter("uid", userId)
                .getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.GET_BETWEEN_HALF_OPEN)
                .setParameter("uid", userId)
                .setParameter("sdt", startDateTime)
                .setParameter("edt", endDateTime)
                .getResultList();
    }
}