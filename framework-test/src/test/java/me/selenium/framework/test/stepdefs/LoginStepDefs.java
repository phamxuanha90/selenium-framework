package me.selenium.framework.test.stepdefs;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import me.selenium.framework.core.common.Asserts;
import me.selenium.framework.core.helper.AutomationTestException;

public class LoginStepDefs extends BaseStepDefs {
    @Given("^I open the Base page$")
    public void openBasePage() {
        loginPage.open();
    }

    @Then("^I should see the link \"([^\"]*)\"$")
    public void iShouldSeeTheLink(String arg0) throws AutomationTestException {
        Asserts.assertEquals(loginPage.getGmailLinkText(),"Gmail", "Verify Gmail link exists");
    }
}
