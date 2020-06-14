package me.selenium.framework.core.config.page.object;

import me.selenium.framework.core.annotation.ComponentObject;
import me.selenium.framework.core.annotation.PageObject;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

public class PageObjectPostProcessor implements BeanPostProcessor {
    private final ApplicationContext appContext;

    public PageObjectPostProcessor(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class beanType = bean.getClass();
        if (beanType.isAnnotationPresent(ComponentObject.class) || beanType.isAnnotationPresent(PageObject.class)) {
            PageFactory.initElements(new SpringSeleniumFieldDecorator(appContext), bean);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
