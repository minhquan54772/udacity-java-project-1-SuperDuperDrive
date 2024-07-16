package com.udacity.jwdnd.course1.cloudstorage.tests;

import com.udacity.jwdnd.course1.cloudstorage.pages.CredentialsPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.FilesPage;
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

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileFeaturesTests {
    @LocalServerPort
    private int port;

    private static CommonTestUtils commonTestUtils;
    private static WebDriver driver;
    private static WebDriverWait webDriverWait;

    private FilesPage filesPage;

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
        clickOnFilesTab(driver, port, webDriverWait);
        filesPage = new FilesPage(driver);
    }

    @AfterAll
    static void afterAll() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void clickOnFilesTab(WebDriver driver, int port, WebDriverWait webDriverWait) {
        driver.get("http://localhost:" + port + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        // Click to switch to tab Credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     *
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     *
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Try to upload an arbitrary large file
        String fileName = "upload5m.zip";

        filesPage.fillInputFileUpload(new File(fileName).getAbsolutePath());
        filesPage.clickUploadButton();

        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

    @Test
    public void should_ShowFileDetails_When_UploadSuccessful() {
        this.testLargeUpload();

        clickOnFilesTab(driver, port, webDriverWait);

        List<WebElement> fileRows = filesPage.getFileRows();
        Assertions.assertFalse(fileRows.isEmpty());

        com.udacity.jwdnd.course1.cloudstorage.models.File firstFile = filesPage.getFirstFile();
        Assertions.assertEquals("upload5m.zip", firstFile.getFilename());
    }

    @Test
    public void should_FileDisappear_When_DeleteFileSuccessful() {
        List<WebElement> fileRows = filesPage.getFileRows();
        filesPage.deleteFirstFile();

        // Should be on success page
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

        clickOnFilesTab(driver, port, webDriverWait);
        List<WebElement> updatedFileRows = filesPage.getFileRows();
        Assertions.assertEquals(fileRows.size() - 1, updatedFileRows.size());
    }


}
