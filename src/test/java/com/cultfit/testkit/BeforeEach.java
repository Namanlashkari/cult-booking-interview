package com.cultfit.testkit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method annotated with {@code @BeforeEach} runs before every {@code @Test}
 * in the same class, on a freshly created instance. Mirrors JUnit's
 * {@code @BeforeEach}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeEach {
}
