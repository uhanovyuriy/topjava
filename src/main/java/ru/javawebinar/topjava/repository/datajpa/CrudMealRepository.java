package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    @Query("delete from Meal m where m.id=:id and m.user.id=:uid")
    int delete(@Param("id") int id, @Param("uid") int userId);


    List<Meal> findAllByUserIdOrderByDateTimeDesc(int userId);

    @Modifying
    @Query("from Meal m where m.user.id=:uid and m.dateTime>=:start and m.dateTime<:end order by m.dateTime desc")
    List<Meal> getBetweenHalfOpen(@Param("uid") int userId, @Param("start") LocalDateTime start
            , @Param("end") LocalDateTime end);
}
