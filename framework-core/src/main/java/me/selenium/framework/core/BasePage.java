package me.selenium.framework.core;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class BasePage {
    @Value("${webapp.baseuri}")
    private String baseUri;

    @Value("${webdriver.element-timeout:30}")
    private int elementTimeout;

    @Value("${webapp.pageload-timeout:10}")
    private int pageLoadTimeout;

    @FindBy(id = "big_headline")
    private WebElement headlinTitle;

    @Autowired
    protected WebDriver driver;

    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    private String previousStyle;

    private static final String SCRIPT_HIGHLIGHT_ELEMENT = "arguments[0].setAttribute('style', 'background: yellow;border: 3px solid red;')";
    private static final String SCRIPT_UNHIGHLIGHT_ELEMENT = "arguments[0].setAttribute('style', '%s')";

    public BasePage open() {
        driver.get(getPageUrl());

        return this;
    }

    public void waitForLoad() {
        ExpectedCondition<Boolean> condition = webDriver -> webDriver.getCurrentUrl().equals(getPageUrl());

        WebDriverWait wait = new WebDriverWait(driver, pageLoadTimeout);
        wait.until(condition);
    }

    public String getPageUrl() {
        return baseUri + getPageUri();
    }

    public WebElement findVisibleElement(By by) {
        return findVisibleElement(by, elementTimeout);
    }

    public WebElement findVisibleElement(By by, int timeout) {
        try {
            Wait<WebDriver> wait = new WebDriverWait(this.driver, (long) timeout);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception var4) {
            logger.warn(String.format("findVisibleElement By(%s) not found before timeout(%d) expired", by.toString(), timeout));
            return null;
        }
    }

    protected void waitFor(By by, int timeout) throws Exception {
        timeout = timeout > pageLoadTimeout ? pageLoadTimeout : timeout;
        ExpectedCondition<Boolean> condition = webDriver -> !webDriver.findElements(by).isEmpty();

        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(condition);
    }

    public boolean shouldBeDisplayed(String title) {
        return headlinTitle.getText().equals(title);
    }

    public void waitForPageTitle() throws Exception {
        waitFor(By.cssSelector("h2#big_headline"), 5);
    }

    public WebElement findAndHighlightElement(By by) {
        WebElement element = driver.findElement(by);
        highLightElement(element);
        return element;
    }

    public void highLightElement(WebElement element) {
        if (driver instanceof JavascriptExecutor) {
            previousStyle = element.getAttribute("style");
            ((JavascriptExecutor) driver).executeScript(SCRIPT_HIGHLIGHT_ELEMENT, element);
        }
    }

    public void unHighlightElement(WebElement element) {
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript(String.format(SCRIPT_UNHIGHLIGHT_ELEMENT, previousStyle), element);
        }
    }

    public String getPageTitle() {
        return headlinTitle.getText();
    }

    protected abstract String getPageUri();
}
