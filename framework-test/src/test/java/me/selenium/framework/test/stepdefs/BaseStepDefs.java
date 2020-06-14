package me.selenium.framework.test.stepdefs;

import me.selenium.framework.test.AppConfig;
import me.selenium.framework.test.page.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = AppConfig.class)
@SpringBootTest
public class BaseStepDefs {
    @Autowired
    protected LoginPage loginPage;
}
