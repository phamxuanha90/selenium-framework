package me.selenium.framework.test.hook;

import cucumber.api.java.After;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomHook {
    @Autowired
    WebDriver driver;
    @After
    public void afterScenarios() {
        driver.quit();
    }
}
