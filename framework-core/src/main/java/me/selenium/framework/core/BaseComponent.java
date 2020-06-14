package me.selenium.framework.core;

import org.openqa.selenium.WebElement;

public abstract class BaseComponent {
    // don't directly use WebElement type to avoid Selenium auto-injection
    private Object parentElement;

    public void setParentElement(WebElement parentElement) {
        this.parentElement = parentElement;
    }

    public WebElement getParentElement() {
        return (WebElement) parentElement;
    }
}
