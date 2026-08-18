package com.trialinteractive.tests;

import com.trialinteractive.base.BaseTest;
import com.trialinteractive.pages.DashboardPage;
import com.trialinteractive.utils.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class LearnerPdcTests extends BaseTest {

    private DashboardPage dashboard() {
        DashboardPage page = new DashboardPage(driver);
        page.waitForDashboard();
        return page;
    }

    private void require(String key) {
        if (TestConfig.getOptional(key).isBlank()) {
            throw new SkipException(
                    "Precondition data is not configured: " + key);
        }
    }

    @Test(description = "L1 - Welcome message on first login")
    public void welcomeMessageOnFirstLogin() {

        requireCredentials();

        login();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/my/"),
                "Learner should be redirected to My Dashboard after first login."
        );
    }

    @Test(description = "L2 - Welcome message on each login")
    public void welcomeMessageOnEachLogin() {

        requireCredentials();

        for (int attempt = 1; attempt <= 2; attempt++) {

            login();

            Assert.assertTrue(
                    driver.getCurrentUrl().contains("/my/"),
                    "User should be redirected to My Dashboard after login (attempt " + attempt + ")."
            );

            DashboardPage dashboard = dashboard();
            dashboard.logout();

            if (attempt < 2) {
                driver.get(TestConfig.getUrl());
            }
        }
    }

    @Test(description = "L3 - Single course with certificate")
    public void singleCourseWithCertificate() {

        requireCredentials();
        require("EXPECTED_COURSE");

        login();
        DashboardPage dashboard = dashboard();

        String expectedCourse = TestConfig.getOptional("EXPECTED_COURSE");

        By course = By.xpath(
                "//a[contains(normalize-space(.),'" +
                        expectedCourse.replace("'", "") +
                        "')]"
        );

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(course))
                .click();

        Assert.assertTrue(
                driver.getPageSource().contains("Your Training Content"),
                "Course page should contain 'Your Training Content'."
        );
    }

    @Test(description = "L4 - E-sign flow with correct credentials")
    public void eSignFlow() {
        requireCredentials();
        require("EXPECTED_COURSE");

        login();
        dashboard();

        By course = By.xpath(
                "//a[contains(normalize-space(.),'" +
                TestConfig.getOptional("EXPECTED_COURSE").replace("'", "") + "')]"
        );

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(course))
                .click();

        Assert.assertTrue(
                driver.getPageSource().contains("eSign"),
                "Configured eSign course should expose eSign-related content."
        );
    }

    @Test(description = "L5 - My profile block")
    public void myProfileBlock() {
        requireCredentials();
        require("PROFILE_FIRST_NAME");
        require("PROFILE_LAST_NAME");
        require("PROFILE_ROLE");

        login();
        dashboard();

        String page = driver.getPageSource();

        Assert.assertTrue(page.contains(TestConfig.getOptional("PROFILE_FIRST_NAME")),
                "First name should be displayed.");
        Assert.assertTrue(page.contains(TestConfig.getOptional("PROFILE_LAST_NAME")),
                "Last name should be displayed.");
        Assert.assertTrue(page.contains(TestConfig.getOptional("PROFILE_ROLE")),
                "Role should be displayed.");
    }

    @Test(description = "L6 - Enrolled courses and logout")
    public void enrolledCoursesAndLogout() {
        requireCredentials();
        require("ENROLLED_COURSES");

        login();
        DashboardPage dashboard = dashboard();

        List<String> expectedCourses = Arrays.stream(
                TestConfig.getOptional("ENROLLED_COURSES").split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        List<String> actualCourses = dashboard.getVisibleCourseNames();

        for (String expected : expectedCourses) {
            Assert.assertTrue(
                    actualCourses.stream().anyMatch(c -> c.equalsIgnoreCase(expected)),
                    "Expected enrolled course was not found: " + expected
            );
        }

        Assert.assertTrue(dashboard.isLogoutVisible(),
                "Logout should be present in the user menu.");

        dashboard.logout();

        driver.get(TestConfig.getDashboardUrl());

        Assert.assertFalse(driver.getCurrentUrl().contains("/my/"),
                "Dashboard should require authentication after logout.");
    }
}
