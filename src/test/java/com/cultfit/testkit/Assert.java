package com.cultfit.testkit;

/**
 * A tiny set of assertion helpers, named to match JUnit so the skill carries
 * over to a real project. A failed assertion throws {@link AssertionError};
 * the runner catches it and reports the test as failed.
 */
public final class Assert {

    private Assert() {
    }

    public static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            fail(message, "expected <" + expected + "> but was <" + actual + ">");
        }
    }

    public static void assertTrue(boolean condition) {
        assertTrue(condition, null);
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            fail(message, "expected condition to be true but was false");
        }
    }

    public static void assertFalse(boolean condition) {
        assertFalse(condition, null);
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            fail(message, "expected condition to be false but was true");
        }
    }

    /**
     * Asserts that running {@code action} throws an exception of the given type
     * (or a subtype). Returns the caught exception so callers can inspect it.
     */
    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Runnable action) {
        return assertThrows(expectedType, action, null);
    }

    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Runnable action, String message) {
        try {
            action.run();
        } catch (Throwable thrown) {
            if (expectedType.isInstance(thrown)) {
                return expectedType.cast(thrown);
            }
            fail(message, "expected " + expectedType.getSimpleName()
                    + " but a " + thrown.getClass().getSimpleName() + " was thrown");
        }
        fail(message, "expected " + expectedType.getSimpleName() + " to be thrown, but nothing was thrown");
        return null; // unreachable — fail() always throws
    }

    private static void fail(String message, String detail) {
        String prefix = (message == null || message.isEmpty()) ? "" : message + " ==> ";
        throw new AssertionError(prefix + detail);
    }
}
