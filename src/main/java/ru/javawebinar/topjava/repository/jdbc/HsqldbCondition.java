package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

/**
 * Created by Администратор on 12.09.2017.
 */
public class HsqldbCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return Arrays.asList(conditionContext.getEnvironment().getActiveProfiles()).contains("hsqldb");
    }
}
