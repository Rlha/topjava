package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

public class MealTestData {

    public static final int BREAKFAST1_ID = START_SEQ + 2;
    public static final Meal BREAKFAST1 = new Meal(BREAKFAST1_ID, LocalDateTime.of(2017, 9, 3, 6,0),
            "breakfast1", 800);

    public static final int LUNCH1_ID = START_SEQ + 3;
    public static final Meal LUNCH1 = new Meal(LUNCH1_ID, LocalDateTime.of(2017, 9, 3, 13,0),
            "lunch1", 700);

    public static final int DINNER1_ID = START_SEQ + 4;
    public static final Meal DINNER1 = new Meal(DINNER1_ID, LocalDateTime.of(2017, 9, 3, 18,0),
            "dinner1", 900);

    public static final int BREAKFAST2_ID = START_SEQ + 5;
    public static final Meal BREAKFAST2 = new Meal(BREAKFAST2_ID, LocalDateTime.of(2017, 9, 3, 8,0),
            "breakfast2", 500);

    public static final int LUNCH2_ID = START_SEQ + 6;
    public static final Meal LUNCH2 = new Meal(LUNCH2_ID, LocalDateTime.of(2017, 9, 3, 14,0),
            "lunch2", 700);

    public static final int DINNER2_ID = START_SEQ + 7;
    public static final Meal DINNER2 = new Meal(DINNER2_ID, LocalDateTime.of(2017, 9, 3, 19,0),
            "dinner2", 600);

    public static final BeanMatcher<Meal> MATCHER = new BeanMatcher<>(
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getDateTime(), actual.getDateTime())
                            && Objects.equals(expected.getId(), actual.getId())
                            && Objects.equals(expected.getCalories(), actual.getCalories())
                            && Objects.equals(expected.getDescription(), actual.getDescription())
                    )
    );

}
