package me.selenium.framework.test.page;

import me.selenium.framework.core.BasePage;
import me.selenium.framework.core.annotation.PageObject;

@PageObject
public class LoginPage extends BasePage {

    @Override
    protected String getPageUri() {
        return "/";
    }
}
