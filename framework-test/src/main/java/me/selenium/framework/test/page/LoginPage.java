package me.selenium.framework.test.page;

import me.selenium.framework.core.BasePage;
import me.selenium.framework.core.annotation.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.swing.text.Element;

@PageObject
public class LoginPage extends BasePage {

    public WebElement getGmailLink() {
        return driver.findElement(By.xpath("//a[@class=\"gb_g\"]"));
    }

    public String getGmailLinkText() {
        return getGmailLink().getText();
    }

    @Override
    protected String getPageUri() {
        return "/";
    }
}
