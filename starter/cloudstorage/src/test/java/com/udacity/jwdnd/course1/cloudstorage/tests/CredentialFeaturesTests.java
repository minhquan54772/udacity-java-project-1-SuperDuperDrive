package com.udacity.jwdnd.course1.cloudstorage.tests;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.pages.CredentialsPage;
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

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CredentialFeaturesTests {
    @LocalServerPort
    private int port;

    private static CommonTestUtils commonTestUtils;
    private static WebDriver driver;
    private static WebDriverWait webDriverWait;

    private CredentialsPage credentialsPage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        commonTestUtils = new CommonTestUtils(driver);
    }

    @BeforeEach
    public void beforeEach() {
        webDriverWait = new WebDriverWait(driver, 2);
        commonTestUtils.doMockSignUp(driver, port, "adding", "notes", "username", "password");
        commonTestUtils.doLogIn(driver, port, "username", "password");
        clickOnCredentialsTab(driver, port, webDriverWait);
        credentialsPage = new CredentialsPage(driver);
    }

    @AfterAll
    static void afterAll() {
        if (driver != null) {
            driver.quit();
        }
    }


    private void clickOnCredentialsTab(WebDriver driver, int port, WebDriverWait webDriverWait) {
        driver.get("http://localhost:" + port + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        // Click to switch to tab Credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();
    }

    private void doCreateUpdateCredentials(boolean isEdit, String url, String username, String password) {
        if (isEdit) {
            credentialsPage.showUpdateCredentialModal();
        } else {
            credentialsPage.showCreateCredentialModal();
        }
        // Fill out credential details
        credentialsPage.fillInputUrl(url);
        credentialsPage.fillInputUsername(username);
        credentialsPage.fillInputPassword(password);

        credentialsPage.clickSaveCredentialBtn();
    }

    @Test
    @Order(1)
    public void should_ShowCredentialDetails_When_AddCredentialSuccessfully() {
        this.doCreateUpdateCredentials(false, "http://test.create.url", "testCreateUsername", "testCreatePassword");
        // Should be on result page with success msg
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("http://localhost:" + this.port + "/credentials", driver.getCurrentUrl());
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));
        // Navigate to home page and Credentials tab
        clickOnCredentialsTab(driver, port, webDriverWait);

        // Get list of credentials
        List<WebElement> credentialRows = credentialsPage.getCredentialRows();
        Assertions.assertFalse(credentialRows.isEmpty());

        Credential firstCredential = credentialsPage.getFirstCredential();
        Assertions.assertEquals("http://test.create.url", firstCredential.getUrl());
        Assertions.assertEquals("testCreateUsername", firstCredential.getUsername());
        // Assertions.assertEquals("testCreatePassword", firstCredential.getPassword());
    }

    @Test
    @Order(2)
    public void should_UpdateCredentialDetails_When_UpdateCredentialSuccessfully() {
        this.doCreateUpdateCredentials(true, "http://test.update.url", "testUpdateUsername", "testUpdatePassword");
        // Should be on result page with success msg
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("http://localhost:" + this.port + "/credentials", driver.getCurrentUrl());
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));
        // Navigate to home page and Credentials tab
        clickOnCredentialsTab(driver, port, webDriverWait);

        // Get list of credentials
        List<WebElement> credentialRows = credentialsPage.getCredentialRows();
        Assertions.assertFalse(credentialRows.isEmpty());

        Credential firstCredential = credentialsPage.getFirstCredential();
        Assertions.assertEquals("http://test.update.url", firstCredential.getUrl());
        Assertions.assertEquals("testUpdateUsername", firstCredential.getUsername());
        // Assertions.assertEquals("testUpdatePassword", firstCredential.getPassword()); -> need encrypt
    }

    @Test
    @Order(3)
    public void should_DeleteCredentials_When_DeleteCredentialSuccessfully() {
        List<WebElement> credentialRows = credentialsPage.getCredentialRows();
        credentialsPage.deleteFirstCredential();

        // Should be on success page
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

        clickOnCredentialsTab(driver, port, webDriverWait);
        List<WebElement> updatedCredentialRows = credentialsPage.getCredentialRows();
        Assertions.assertEquals(credentialRows.size() - 1, updatedCredentialRows.size());
    }

}
