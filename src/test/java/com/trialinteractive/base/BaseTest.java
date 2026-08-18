package com.trialinteractive.base;

import com.trialinteractive.pages.LoginPage;
import com.trialinteractive.utils.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        if ("true".equalsIgnoreCase(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--window-size=1440,1000");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        loginPage = new LoginPage(driver);
        driver.get(TestConfig.getUrl());
    }

    protected void requireCredentials() {
        try {
            TestConfig.getRequired("TI_EMAIL");
            TestConfig.getRequired("TI_PASSWORD");
        } catch (IllegalStateException e) {
            throw new SkipException(e.getMessage());
        }
    }

    protected void login() {
        requireCredentials();
        loginPage.login(
                TestConfig.getRequired("TI_EMAIL"),
                TestConfig.getRequired("TI_PASSWORD")
        );
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
