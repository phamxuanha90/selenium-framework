package me.selenium.framework.core.helper;

public class AutomationTestException extends RuntimeException {
    public AutomationTestException(String message) {
        super(message);
    }

    public AutomationTestException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutomationTestException(Throwable cause) {
        super(cause);
    }

    public AutomationTestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
