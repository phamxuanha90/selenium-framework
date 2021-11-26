package me.selenium.framework.test.page;

import me.selenium.framework.core.BasePage;
import me.selenium.framework.core.annotation.PageObject;
import me.selenium.framework.core.element.DropDown;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.swing.text.Element;

@PageObject
public class LoginPage extends BasePage {

    @FindBy(css = "a.gb_f")
    private WebElement gmailLink;


//    public WebElement getGmailLink() {
//        return driver.findElement(By.cssSelector("a.gb_f"));
//    }

//    public String getGmailLinkText() {
//        return getGmailLink().getText();
//    }
    public String getGmailLinkText() {
        return gmailLink.getText();
    }

    @Override
    protected String getPageUri() {
        return "/";
    }
}
