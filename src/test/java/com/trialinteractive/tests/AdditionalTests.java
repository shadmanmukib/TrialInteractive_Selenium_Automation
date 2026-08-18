package com.trialinteractive.tests;

import com.trialinteractive.base.BaseTest;
import com.trialinteractive.pages.DashboardPage;
import com.trialinteractive.utils.TestConfig;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class AdditionalTests extends BaseTest {

    @Test(description = "A1 - Login page loads and email field is displayed")
    public void loginPageLoads() {
        Assert.assertTrue(loginPage.isEmailDisplayed(),
                "Email input should be visible on the login page.");
    }

    @Test(description = "A2 - Next button is displayed and enabled")
    public void nextButtonIsEnabled() {
        Assert.assertTrue(loginPage.isLoginButtonEnabled(),
                "Next button should be enabled.");
    }

    @Test(description = "A3 - Blank email submission is rejected")
    public void blankEmailIsRejected() {
        loginPage.clickNext();
        String validation = loginPage.getEmailValidationMessage();

        Assert.assertFalse(validation.isBlank(),
                "Browser/application should report that email is required.");
    }

    @Test(description = "A4 - Invalid email format is rejected")
    public void invalidEmailFormatIsRejected() {
        loginPage.enterEmail("invalid-email");
        loginPage.clickNext();

        String validation = loginPage.getEmailValidationMessage();
        Assert.assertFalse(validation.isBlank(),
                "Invalid email format should be rejected.");
    }

    @Test(description = "A5 - Logout ends the authenticated session")
    public void logoutEndsSession() {
        requireCredentials();
        login();
        DashboardPage dashboard = new DashboardPage(driver);
        dashboard.waitForDashboard();

        Assert.assertTrue(dashboard.isDashboard(), "User should be on Dashboard after login.");
        Assert.assertTrue(dashboard.isLogoutVisible(), "Logout should be available.");

        dashboard.logout();
        Assert.assertFalse(driver.getCurrentUrl().contains("/my/"),
                "User should leave the authenticated dashboard after logout.");
    }

    @Test(description = "A6 - Dashboard requires authentication after logout")
    public void dashboardRequiresLoginAfterLogout() {
        requireCredentials();
        login();

        DashboardPage dashboard = new DashboardPage(driver);
        dashboard.waitForDashboard();
        dashboard.logout();

        driver.get(TestConfig.getDashboardUrl());

        Assert.assertFalse(driver.getCurrentUrl().contains("/my/"),
                "Unauthenticated user should not remain on the protected dashboard.");
    }
}
