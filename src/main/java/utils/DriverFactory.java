package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver initDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Suitable headless mode for CI
        options.addArguments("--no-sandbox"); // Bypass OS security model (required in CI)
        options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource issues
        options.addArguments("--user-data-dir=/tmp/chrome-user-data"); // Unique temp dir

        WebDriver webDriver = new ChromeDriver(options);
        driver.set(webDriver);
        getDriver().manage().window().maximize();
        return getDriver();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }
}
