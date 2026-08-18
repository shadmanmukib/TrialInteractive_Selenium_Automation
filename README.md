# Trial Interactive GlobalLearn - Selenium Automation

## 1. Overview

This project automates the learner-only GlobalLearn Post-Deployment test cases supplied with the assignment.

Technology:
- Java 17
- Selenium WebDriver
- TestNG
- Maven
- Page Object Model (POM)
- Selenium Manager for browser-driver management

The implementation intentionally uses simple Java and avoids unnecessary framework complexity.

## 2. Automated sample test cases

| ID | Test case | Automation status |
|---|---|---|
| L1 | Welcome message on first login | Automated; requires first-login configuration/data |
| L2 | Welcome message on each login | Automated; requires each-login configuration/data |
| L3 | Single course with certificate | Automated; requires enrolled certificate course |
| L4 | E-sign flow (correct credentials) | Automated; requires configured eSign course |
| L5 | My profile block | Automated; requires configured learner profile |
| L6 | Enrolled courses on My Dashboard and logout | Automated |

The exact sample cases and expected results are taken from the supplied test-case specification. See the PDF for the original wording. fileciteturn0file0

## 3. Additional automated tests

| ID | Additional test |
|---|---|
| A1 | Login page loads and email field is displayed |
| A2 | Next button is displayed and enabled on login page |
| A3 | Blank email submission is rejected |
| A4 | Invalid email format is rejected |
| A5 | Logout ends the authenticated session |
| A6 | Dashboard cannot be accessed after logout |

These are deliberately low-risk tests and do not attempt an incorrect password, which could cause account lockout or security alerts.

## 4. Prerequisites

- Java 17 or later
- Maven 3.9+
- Chrome or another Selenium-supported browser
- Network access to the staging environment
- A valid learner account
- Required learner-side test data/configuration for L1-L6

No WebDriver binary needs to be downloaded manually; Selenium Manager handles browser-driver discovery.

## 5. Test data / environment variables

Credentials are intentionally NOT hard-coded into source control.

Set:

```text
TI_EMAIL=<learner email>
TI_PASSWORD=<learner password>
```

Optional values for L1-L5:

```text
WELCOME_TEXT=<expected welcome text>
EXPECTED_COURSE=<course name>
ESIGN_REASON=<reason visible in dropdown>
PROFILE_TITLE=<expected title>
PROFILE_FIRST_NAME=<expected first name>
PROFILE_LAST_NAME=<expected last name>
PROFILE_ROLE=<expected role>
PROFILE_COMPANY=<expected company>
PROFILE_LOCATION=<expected location>
PROFILE_TRAINING_GROUP=<expected training group>
PROFILE_SITE=<expected site>
```

For L6, expected enrolled course names can be supplied as:

```text
ENROLLED_COURSES=Course A,Course B
```

If a test needs a specific precondition that has not been supplied, the test is skipped with a clear reason rather than using guessed data.

## 6. Installation

Clone/extract the project and run:

```bash
mvn clean test
```

Headless execution:

```bash
mvn clean test -Dheadless=true
```

Example Windows environment variables:

```cmd
set TI_EMAIL=your-email
set TI_PASSWORD=your-password
mvn clean test
```

PowerShell:

```powershell
$env:TI_EMAIL="your-email"
$env:TI_PASSWORD="your-password"
mvn clean test
```

## 7. Reports

After execution:

- TestNG/Surefire results: `target/surefire-reports/`
- XML results: `target/surefire-reports/*.xml`
- HTML report can be generated with:

```bash
mvn surefire-report:report
```

The generated HTML report will be under the Maven reporting output.

A submission-time execution-status report is also included in `reports/`. It is clearly marked as blocked/not executed where the supplied environment did not provide enough information or browser access.

## 8. Project structure

```text
TrialInteractive_Selenium_Automation/
├── pom.xml
├── README.md
├── reports/
│   └── execution-report.html
└── src/
    └── test/
        ├── java/
        │   └── com/trialinteractive/
        │       ├── base/
        │       │   └── BaseTest.java
        │       ├── pages/
        │       │   ├── LoginPage.java
        │       │   └── DashboardPage.java
        │       ├── tests/
        │       │   ├── AdditionalTests.java
        │       │   └── LearnerPdcTests.java
        │       └── utils/
        │           └── TestConfig.java
        └── resources/
            └── testng.xml
```

## 9. Synchronization strategy

No unnecessary `Thread.sleep()` calls are used.

The framework uses:
- Explicit waits for visibility/clickability
- URL waits where navigation is expected
- Page-level helper methods

## 10. Defects / observations identified

1. The staging login page is reachable, but automated execution requires a real browser session and valid test data.
2. The supplied test specification does not provide the configured welcome text/logo, course names, eSign reason, profile values, or enrolled-course list. Those values are therefore externalized rather than guessed.
3. The login page currently displays a "Browser cookies disabled" warning when accessed by a non-cookie-aware crawler. A real Selenium browser with cookies enabled should be used for SSO/login validation.
4. L1 is inherently stateful because it requires the learner's first login.
5. L3/L4/L5/L6 depend on learner-specific backend data and cannot be reliably validated from credentials alone.

## 11. Design decisions

- POM separates page locators/actions from test cases.
- Assertions are kept close to the business expectation.
- Credentials are read from environment variables.
- Test data is not embedded in page objects.
- Tests fail on genuine assertion failures and skip when required external preconditions are missing.
- The framework remains intentionally simple and suitable for a 3-year SQA profile using basic Java.
