package me.selenium.framework.core.config;

import me.selenium.framework.core.helper.AutomationTestException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class WebDriverFactory {
    private WebDriverFactory() {
    }

    public static WebDriver newInstance(WebDriverSettings settings) {
        WebDriver webDriver;

        if (!StringUtils.isEmpty(settings.getGridhubServer())) {
            webDriver = buildWebDriver(settings.getType(), settings.getPath(), settings.getGridhubServer());
        } else {
            webDriver = buildWebDriver(settings.getType(), settings.getPath());
        }

        WebDriver.Options opts = webDriver.manage();

        opts.timeouts().implicitlyWait(settings.getElementTimeout(), TimeUnit.SECONDS);
        opts.window().maximize();

        if (!StringUtils.isEmpty(settings.getPosition())) {
            Integer[] pos = Arrays.stream(settings.getPosition().trim().split(","))
                    .map(Integer::valueOf)
                    .toArray(Integer[]::new);

            opts.window().setPosition(new Point(pos[0], pos[1]));
        }

        if (!StringUtils.isEmpty(settings.getDimension())) {
            Integer[] dim = Arrays.stream(settings.getDimension().trim().split("x"))
                    .map(Integer::valueOf)
                    .toArray(Integer[]::new);

            opts.window().setSize(new Dimension(dim[0], dim[1]));
        }

        return webDriver;
    }

    private static WebDriver buildWebDriver(String browserType, String browserPath) {
        DesiredCapabilities capabilities = initCapabilities(browserType, browserPath);

        switch (browserType.toUpperCase()) {
            case "CHROME":
                return new ChromeDriver();
            case "EDGE":
                return new EdgeDriver(capabilities);
            case "SAFARI":
                return new SafariDriver(capabilities);
            case "IE":
                return new InternetExplorerDriver(capabilities);
            case "FIREFOX":
            default:
                return new FirefoxDriver(capabilities);
        }
    }

    private static WebDriver buildWebDriver(String browserType, String browserPath, String gridHubServer) {
        String gridHubUrl;
        if (gridHubServer.split(":").length == 2) {
            gridHubUrl = String.format("http://%s/wd/hub", gridHubServer);
        } else {
            gridHubUrl = String.format("http://%s:4444/wd/hub", gridHubServer);
        }

        DesiredCapabilities capabilities = initCapabilities(browserType, browserPath);
        URL hubUrl;

        try {
            hubUrl = new URL(gridHubUrl);
        } catch (MalformedURLException ex) {
            throw new AutomationTestException(ex);
        }

        return new RemoteWebDriver(hubUrl, capabilities);
    }

    private static DesiredCapabilities initCapabilities(String browserType, String browserPath) {
        switch (browserType.toUpperCase()) {
            case "CHROME":
                System.setProperty("webdriver.chrome.driver", browserPath);
                return DesiredCapabilities.chrome();
            case "EDGE":
                System.setProperty("webdriver.edge.driver", browserPath);
                return DesiredCapabilities.edge();
            case "SAFARI":
                System.setProperty("webdriver.safari.driver", browserPath);
                return DesiredCapabilities.safari();
            case "IE":
                System.setProperty("webdriver.ie.driver", browserPath);
                return DesiredCapabilities.internetExplorer();
            case "FIREFOX":
            default:
                System.setProperty("webdriver.gecko.driver", browserPath);
                return DesiredCapabilities.firefox();
        }
    }
}
