package me.selenium.framework.core.config.page.object;

import me.selenium.framework.core.annotation.EnablePageObjects;
import me.selenium.framework.core.annotation.PageObject;
import me.selenium.framework.core.config.webdriver.WebDriverScope;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class PageObjectDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attrs = metadata.getAnnotationAttributes(EnablePageObjects.class.getName());

        String[] basePackages = (String[]) attrs.get("basePackages");
        if (basePackages.length == 1 && StringUtils.isEmpty(basePackages[0])) {
            String basePackage = ((StandardAnnotationMetadata) metadata).getIntrospectedClass().getPackage().getName();
            basePackages = new String[] { basePackage };
        }

        findPageObjectClasses(basePackages).forEach(beanDef -> {
            beanDef.setScope(WebDriverScope.NAME);
            beanDef.setLazyInit(true);
            registry.registerBeanDefinition(beanDef.getBeanClassName(), beanDef);
        });
    }

    private Set<BeanDefinition> findPageObjectClasses(String... basePackages) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(PageObject.class));

        Set<BeanDefinition> beanDefs = new HashSet<>();

        Stream.of(basePackages)
                .forEach(basePackage -> beanDefs.addAll(scanner.findCandidateComponents(basePackage)));

        return beanDefs;
    }
}
