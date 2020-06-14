package me.selenium.framework.core.config;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Collections;

@Configuration
@EnableConfigurationProperties(WebDriverSettings.class)
public class CoreConfig {
    @Bean
    public CustomScopeConfigurer registerWebDriverScope() {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        configurer.setScopes(Collections.singletonMap(WebDriverScope.NAME, new WebDriverScope()));

        return configurer;
    }

    @Bean
    @Scope(WebDriverScope.NAME)
    public WebDriver webDriver(@Autowired WebDriverSettings settings) {
        return WebDriverFactory.newInstance(settings);
    }

    @Bean
    public PageObjectPostProcessor pageObjectProcessor(@Autowired ApplicationContext appContext) {
        return new PageObjectPostProcessor(appContext);
    }
}
