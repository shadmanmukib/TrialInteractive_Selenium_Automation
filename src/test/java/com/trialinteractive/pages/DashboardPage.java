package com.trialinteractive.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Confirmed from actual DOM inspection
    private final By userMenuToggle = By.xpath("//*[@id=\"action-menu-toggle-1\"]/span/span/span/span");
    private final By logoutLink = By.id("actionmenuaction-5");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForDashboard() {
        wait.until(ExpectedConditions.urlContains("/my/"));
    }

    public boolean isDashboard() {
        return driver.getCurrentUrl().contains("/my/");
    }

    public void openUserMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(userMenuToggle)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(logoutLink));
    }

    public boolean isLogoutVisible() {
        try {
            openUserMenu();
            return wait.until(ExpectedConditions.visibilityOfElementLocated(logoutLink)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        openUserMenu();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("/my/")
        ));
    }

    public List<String> getVisibleCourseNames() {
        List<String> names = new ArrayList<>();
        By courseLinks = By.cssSelector("a[href*='/course/']");
        driver.findElements(courseLinks).forEach(element -> {
            String text = element.getText().trim();
            if (!text.isBlank() && !names.contains(text)) {
                names.add(text);
            }
        });
        return names;
    }
}