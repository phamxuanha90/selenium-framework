package me.selenium.framework.test.stepdefs;

import cucumber.api.java.en.Given;

public class LoginStepDefs extends BaseStepDefs {
    @Given("^I open the Base page$")
    public void openBasePage() {
        loginPage.open();
    }
}
