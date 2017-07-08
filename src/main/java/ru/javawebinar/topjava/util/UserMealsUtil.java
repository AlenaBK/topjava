package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDate.from;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> temp = new HashMap<>();

        for (UserMeal usM : mealList) {
            int sum;
            if (!temp.containsKey(usM.getDateTime().toLocalDate())) {
                sum = usM.getCalories();
                temp.put(usM.getDateTime().toLocalDate(), sum);
            } else {
                sum = usM.getCalories() + temp.get(usM.getDateTime().toLocalDate());
                temp.put(usM.getDateTime().toLocalDate(), sum);
            }
        }

        List<UserMealWithExceed> list = new ArrayList<>();

        for (UserMeal um : mealList) {
            if (temp.get(um.getDateTime().toLocalDate()) > caloriesPerDay) {
                list.add(new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(), true));
            } else
                list.add(new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(), false));
        }

        List<UserMealWithExceed> results = new ArrayList<>();
        for (UserMealWithExceed umw : list) {
            if (TimeUtil.isBetween(umw.getDateTime().toLocalTime(), startTime, endTime)) {
                results.add(umw);
            }
        }

        return results;

    }

    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        return LocalDate.from(localDateTime);
    }

    public static LocalTime toLocalTime(LocalDateTime localDateTime) {
        return LocalTime.from(localDateTime);
    }
}
