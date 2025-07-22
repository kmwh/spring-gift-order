package gift.global.util;

public class Assert {
    public static void check(boolean condition, RuntimeException exception) {
        if (!condition) {
            throw exception;
        }
    }
}
