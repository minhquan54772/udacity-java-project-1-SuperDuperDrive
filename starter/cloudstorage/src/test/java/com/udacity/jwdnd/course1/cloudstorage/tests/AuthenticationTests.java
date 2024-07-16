package com.udacity.jwdnd.course1.cloudstorage.tests;

import com.udacity.jwdnd.course1.cloudstorage.utils.CommonTestUtils;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationTests {
    @LocalServerPort
    private int port;

    private static CommonTestUtils commonTestUtils;

    private static WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        commonTestUtils = new CommonTestUtils(driver);
    }

    @AfterAll
    static void afterAll() {
        driver.quit();
    }

    @Test
    @Order(1)
    public void should_SignUpSuccessfully_When_InputValidUsernameAndPassword() {
        commonTestUtils.doMockSignUp(driver, port, "firstName", "lastName", "validUsername", "validPassword");
        // Check if we have been redirected to the login page and show success msg
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));

    }

    @Test
    @Order(2)
    public void should_SignUpFailure_When_InputDuplicatedUsername() {
        commonTestUtils.doMockSignUp(driver, port, "firstName", "lastName", "duplicated", "password");
        commonTestUtils.doMockSignUp(driver, port, "firstName", "lastName", "duplicated", "password");

        // Should not redirect to login page
        Assertions.assertEquals("http://localhost:" + this.port + "/signup", driver.getCurrentUrl());
        // Should show duplicated username msg
        Assertions.assertTrue(driver.findElement(By.id("signup-error")).getText().contains("Username duplicated is already in use"));
    }

    @Test
    @Order(4)
    public void should_AccessHomePage_When_LoginSuccessful() {
        commonTestUtils.doMockSignUp(driver, port, "firstName", "lastName", "username", "password");
        commonTestUtils.doLogIn(driver, port, "username", "password");

        // Should redirect to Home page
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
    }

    @Test
    @Order(3)
    public void should_ShowErrorMessage_When_LoginFailed() {
        commonTestUtils.doMockSignUp(driver, port, "firstName", "lastName", "username", "password");
        commonTestUtils.doLogIn(driver, port, "invalidUsername", "password");

        Assertions.assertTrue(driver.findElement(By.id("login-error")).getText().contains("Invalid username or password"));
    }

    @Test
    @Order(6)
    public void should_RedirectLoginPage_When_AccessHomePageWithoutLogin() {
        driver.navigate().to("http://localhost:" + this.port + "/home");
        // Should redirect login page
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    @Test
    @Order(5)
    public void should_RedirectLoginPage_And_NotAccessHomePage_When_Logout() {
        commonTestUtils.doMockSignUp(driver, port, "firstName", "lastName", "username", "password");
        commonTestUtils.doLogIn(driver, port, "username", "password");

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
