package com.cultfit.testkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Minimal, dependency-free test runner. Pass it a list of fully-qualified test
 * class names; for each {@code @Test} method it builds a fresh instance, runs
 * every {@code @BeforeEach}, invokes the test, and prints PASS/FAIL. Exits with
 * a non-zero status if anything failed, so scripts and CI can tell.
 *
 * <p>This exists only so the exercise runs with nothing but a JDK — no Maven,
 * no JUnit, no network. In a real project you would use JUnit; the annotations
 * and assertions here are named to match it on purpose.
 */
public final class TestRunner {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("usage: TestRunner <fully.qualified.TestClass> ...");
            System.exit(2);
        }

        int passed = 0;
        int failed = 0;
        List<String> failures = new ArrayList<>();

        for (String className : args) {
            Class<?> cls = Class.forName(className);
            System.out.println();
            System.out.println("== " + className + " ==");

            List<Method> befores = new ArrayList<>();
            List<Method> tests = new ArrayList<>();
            for (Method m : cls.getDeclaredMethods()) {
                if (m.isAnnotationPresent(BeforeEach.class)) {
                    befores.add(m);
                }
                if (m.isAnnotationPresent(Test.class)) {
                    tests.add(m);
                }
            }
            tests.sort((a, b) -> a.getName().compareTo(b.getName()));

            for (Method test : tests) {
                try {
                    Constructor<?> ctor = cls.getDeclaredConstructor();
                    ctor.setAccessible(true);
                    Object instance = ctor.newInstance();
                    for (Method before : befores) {
                        before.setAccessible(true);
                        before.invoke(instance);
                    }
                    test.setAccessible(true);
                    test.invoke(instance);
                    System.out.println("  [PASS] " + test.getName());
                    passed++;
                } catch (Throwable t) {
                    Throwable cause = unwrap(t);
                    String line = className + "." + test.getName() + " -> "
                            + cause.getClass().getSimpleName()
                            + (cause.getMessage() == null ? "" : ": " + cause.getMessage());
                    System.out.println("  [FAIL] " + test.getName() + " -> "
                            + cause.getClass().getSimpleName()
                            + (cause.getMessage() == null ? "" : ": " + cause.getMessage()));
                    failures.add(line);
                    failed++;
                }
            }
        }

        System.out.println();
        System.out.println("---- SUMMARY: " + passed + " passed, " + failed + " failed ----");
        if (!failures.isEmpty()) {
            System.out.println("Failures:");
            for (String f : failures) {
                System.out.println("  - " + f);
            }
        }
        System.exit(failed == 0 ? 0 : 1);
    }

    /** Reflection wraps the real error in InvocationTargetException — dig it out. */
    private static Throwable unwrap(Throwable t) {
        Throwable current = t;
        while (current.getCause() != null
                && (current instanceof java.lang.reflect.InvocationTargetException
                    || current instanceof ExceptionInInitializerError)) {
            current = current.getCause();
        }
        return current;
    }

    private TestRunner() {
    }
}
