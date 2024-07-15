package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static com.udacity.jwdnd.course1.cloudstorage.utils.CommonTestUtils.doLogIn;
import static com.udacity.jwdnd.course1.cloudstorage.utils.CommonTestUtils.doMockSignUp;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationTests {
    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void should_SignUpSuccessfully_When_InputValidUsernameAndPassword() {
        doMockSignUp(driver, port, "firstName", "lastName", "validUsername", "validPassword");
        // Check if we have been redirected to the login page and show success msg
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));

    }

    @Test
    public void should_SignUpFailure_When_InputDuplicatedUsername() {
        doMockSignUp(driver, port, "firstName", "lastName", "duplicated", "password");
        doMockSignUp(driver, port, "firstName", "lastName", "duplicated", "password");

        // Should not redirect to login page
        Assertions.assertEquals("http://localhost:" + this.port + "/signup", driver.getCurrentUrl());
        // Should show duplicated username msg
        Assertions.assertTrue(driver.findElement(By.id("signup-error")).getText().contains("Username duplicated is already in use"));
    }

    @Test
    public void should_AccessHomePage_When_LoginSuccessful() {
        doMockSignUp(driver, port, "firstName", "lastName", "username", "password");
        doLogIn(driver, port, "username", "password");

        // Should redirect to Home page
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
    }

    @Test
    public void should_ShowErrorMessage_When_LoginFailed() {
        doMockSignUp(driver, port, "firstName", "lastName", "username", "password");
        doLogIn(driver, port, "invalidUsername", "password");

        Assertions.assertTrue(driver.findElement(By.id("login-error")).getText().contains("Invalid username or password"));
    }

    @Test
    public void should_RedirectLoginPage_When_AccessHomePageWithoutLogin() {
        driver.get("http://localhost:" + this.port + "/home");

        // Should redirect login page
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    @Test
    public void should_RedirectLoginPage_And_NotAccessHomePage_When_Logout() {
        doMockSignUp(driver, port, "firstName", "lastName", "username", "password");
        doLogIn(driver, port, "username", "password");

        // Should redirect to Home page
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-btn")));
        WebElement logoutButton = driver.findElement(By.id("logout-btn"));
        logoutButton.click();

        // Should redirect to Login page
        webDriverWait.until(ExpectedConditions.titleContains("Login"));

        // Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
        // Assertions.assertTrue(driver.findElement(By.id("logout-msg")).getText().contains("You have been logged out"));

        // Can not access home page after logout without logging in again
        driver.navigate().to("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }
}
