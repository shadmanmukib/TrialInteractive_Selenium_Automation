package com.trialinteractive.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By emailInput = By.id("username");
    private final By loginButton = By.id("login-button");
    private final By passwordInput = By.id("password");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean isEmailDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).isDisplayed();
    }

    public boolean isLoginButtonEnabled() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton)).isEnabled();
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput))
                .clear();
        driver.findElement(emailInput).sendKeys(email);
    }

    public void clickNext() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        } catch (org.openqa.selenium.WebDriverException e) {
            // Page may have been mid-navigation; retry once after a short settle.
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        }
    }

    public boolean isPasswordDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).isDisplayed();
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput))
                .clear();
        driver.findElement(passwordInput).sendKeys(password);
    }

//    public void login(String email, String password) {
//        enterEmail(email);
//        clickNext();
//        enterPassword(password);
//        clickNext();
//    }

    public void login(String email, String password) {
        enterEmail(email);
        clickNext();

        if (!isPasswordDisplayed()) {
            throw new AssertionError("Password field was not displayed after submitting email.");
        }

        enterPassword(password);
        clickNext();

        wait.until(ExpectedConditions.urlContains("/my/"));
    }

    public String getEmailValidationMessage() {
        return driver.findElement(emailInput).getAttribute("validationMessage");
    }
}
