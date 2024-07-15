package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.utils.CommonTestUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CredentialFeaturesTests {
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


    private void clickOnCredentialsTab(WebDriver driver, int port, WebDriverWait webDriverWait) {
        driver.get("http://localhost:" + port + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        // Click to switch to tab Credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();
    }

    private void doCreateUpdateCredentials(WebDriver driver, int port, boolean isEdit, String url, String username, String password) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        clickOnCredentialsTab(driver, port, webDriverWait);

        if (isEdit) {
            // Click on "+ Add a New Credential" button
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("edit-credential-btn")));
            List<WebElement> editCredentialBtns = driver.findElements(By.className("edit-credential-btn"));
            if (!editCredentialBtns.isEmpty()) {
                WebElement editNoteButton = editCredentialBtns.get(0);
                editNoteButton.click();
            } else {
                throw new RuntimeException("No edit-credential-btn found");
            }
        } else {
            // Click on "+ Add a New Credential" button
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credential-btn")));
            WebElement addCredentialBtn = driver.findElement(By.id("add-credential-btn"));
            addCredentialBtn.click();
        }
        // Show add credential modal
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
        // Fill out credential details
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement urlInputElement = driver.findElement(By.id("credential-url"));
        urlInputElement.click();
        urlInputElement.clear();
        urlInputElement.sendKeys(url);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement usernameInputElement = driver.findElement(By.id("credential-username"));
        usernameInputElement.click();
        usernameInputElement.clear();
        usernameInputElement.sendKeys(username);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement passwordInputElement = driver.findElement(By.id("credential-password"));
        passwordInputElement.click();
        passwordInputElement.clear();
        passwordInputElement.sendKeys(password);

        // Click button Save changes
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-credential-btn")));
        WebElement saveChangesBtn = driver.findElement(By.id("save-credential-btn"));
        saveChangesBtn.click();
    }

    @Test
    @Order(1)
    public void should_ShowCredentialDetails_When_AddCredentialSuccessfully() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        CommonTestUtils.doMockSignUp(driver, port, "adding", "notes", "username", "password");
        CommonTestUtils.doLogIn(driver, port, "username", "password");

        this.doCreateUpdateCredentials(driver, port, false, "http://test.create.url", "testCreateUsername", "testCreatePassword");
        // Should be on result page with success msg
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("http://localhost:" + this.port + "/credentials", driver.getCurrentUrl());
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));
        // Navigate to home page and Credentials tab
        clickOnCredentialsTab(driver, port, webDriverWait);

        // Get list of credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr")));
        List<WebElement> credentialElement = driver.findElements(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr"));
        Assertions.assertFalse(credentialElement.isEmpty());
        
        WebElement urlElement = credentialElement.get(0).findElement(By.tagName("th"));
        Assertions.assertEquals("http://test.create.url", urlElement.getText());

        WebElement usernameElement = credentialElement.get(0).findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[2]"));
        Assertions.assertEquals("testCreateUsername", usernameElement.getText());

//        WebElement passwordElement = credentialElement.get(0).findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[3]"));
//        Assertions.assertEquals("testCreatePassword", passwordElement.getText());
    }

    @Test
    @Order(2)
    public void should_UpdateCredentialDetails_When_UpdateCredentialSuccessfully() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        CommonTestUtils.doMockSignUp(driver, port, "adding", "notes", "username", "password");
        CommonTestUtils.doLogIn(driver, port, "username", "password");

        this.doCreateUpdateCredentials(driver, port, true, "http://test.update.url", "testUpdateUsername", "testUpdatePassword");
        // Should be on result page with success msg
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("http://localhost:" + this.port + "/credentials", driver.getCurrentUrl());
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));
        // Navigate to home page and Credentials tab
        clickOnCredentialsTab(driver, port, webDriverWait);

        // Get list of credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr")));
        List<WebElement> credentialElement = driver.findElements(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr"));
        Assertions.assertFalse(credentialElement.isEmpty());

        WebElement urlElement = credentialElement.get(0).findElement(By.tagName("th"));
        Assertions.assertEquals("http://test.update.url", urlElement.getText());

        WebElement usernameElement = credentialElement.get(0).findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[2]"));
        Assertions.assertEquals("testUpdateUsername", usernameElement.getText());

//        WebElement passwordElement = credentialElement.get(0).findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[3]"));
//        Assertions.assertEquals("testUpdatePassword", passwordElement.getText());
    }

    @Test
    @Order(3)
    public void should_DeleteCredentials_When_DeleteCredentialSuccessfully() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        CommonTestUtils.doMockSignUp(driver, port, "adding", "notes", "username", "password");
        CommonTestUtils.doLogIn(driver, port, "username", "password");

        clickOnCredentialsTab(driver, port, webDriverWait);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[1]/a")));
        List<WebElement> deleteCredentialButtons = driver.findElements(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[1]/a"));
        int numberOfNotes = deleteCredentialButtons.size();
        deleteCredentialButtons.get(0).click();

        // Should be on success page
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

        clickOnCredentialsTab(driver, port, webDriverWait);
        List<WebElement> updatedDeleteCredentialsButtons = driver.findElements(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[1]/a"));
        Assertions.assertEquals(numberOfNotes - 1, updatedDeleteCredentialsButtons.size());
    }

}
