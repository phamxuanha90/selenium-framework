package me.selenium.framework.core.config;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import static org.springframework.test.context.support.DependencyInjectionTestExecutionListener.REINJECT_DEPENDENCIES_ATTRIBUTE;

public class WebDriverTestExecutionListener extends AbstractTestExecutionListener {
    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        WebDriverScope scope = WebDriverScope.getFrom(testContext.getApplicationContext());

        if (scope != null && scope.reset()) {
            testContext.setAttribute(REINJECT_DEPENDENCIES_ATTRIBUTE, Boolean.TRUE);
        }
    }
}
