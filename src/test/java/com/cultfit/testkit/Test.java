package com.cultfit.testkit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a test case. Mirrors JUnit's {@code @Test} so the habit
 * transfers, but needs no external library — this whole kit is a handful of
 * plain Java files with zero dependencies.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}
