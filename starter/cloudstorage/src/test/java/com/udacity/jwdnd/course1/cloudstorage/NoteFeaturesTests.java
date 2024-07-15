package com.udacity.jwdnd.course1.cloudstorage;

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
public class NoteFeaturesTests {
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

    private void clickOnNoteTab(WebDriver driver, int port, WebDriverWait webDriverWait) {
        driver.get("http://localhost:" + port + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        // Click to switch to tab Note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteNavTab = driver.findElement(By.id("nav-notes-tab"));
        noteNavTab.click();
    }

    /**
     * Use this func after login and on home page
     * This func is used to auto do create/update a note
     * @param isEdit is Editing a note or Creating a new note
     * @param noteTitle input note title
     * @param noteDescription input note description
     */
    private void doCreateUpdateNote(WebDriver driver, int port, boolean isEdit, String noteTitle, String noteDescription) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        clickOnNoteTab(driver, port, webDriverWait);

        if (isEdit) {
            // Click on "+ Add a New Note" button
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("edit-note-btn")));
            List<WebElement> editNoteButtons = driver.findElements(By.className("edit-note-btn"));
            if (!editNoteButtons.isEmpty()) {
                WebElement editNoteButton = editNoteButtons.get(0);
                editNoteButton.click();
            } else {
                throw new RuntimeException("No edit-note button found");
            }
        } else {
            // Click on "+ Add a New Note" button
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note-btn")));
            WebElement addNoteBtn = driver.findElement(By.id("add-note-btn"));
            addNoteBtn.click();
        }
        // Show add note modal
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));
        // Fill out note details
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitleElement = driver.findElement(By.id("note-title"));
        noteTitleElement.click();
        noteTitleElement.clear();
        noteTitleElement.sendKeys(noteTitle);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescriptionElement = driver.findElement(By.id("note-description"));
        noteDescriptionElement.click();
        noteDescriptionElement.clear();
        noteDescriptionElement.sendKeys(noteDescription);
        // Click button submit
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-btn")));
        WebElement saveChangesBtn = driver.findElement(By.id("save-note-btn"));
        saveChangesBtn.click();
    }

    @Test
    @Order(1)
    public void should_ShowNoteDetails_When_AddNoteSuccessfully() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        CommonTestUtils.doMockSignUp(driver, port, "adding", "notes", "username", "password");
        CommonTestUtils.doLogIn(driver, port, "username", "password");

        this.doCreateUpdateNote(driver, port, false, "Test Create Title", "Test Create Description");
        // Should be on result page with success msg
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("http://localhost:" + this.port + "/notes", driver.getCurrentUrl());
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));
        // Navigate to home page
        clickOnNoteTab(driver, port, webDriverWait);

        // Get list of notes
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"userTable\"]/tbody/tr")));
        List<WebElement> noteElements = driver.findElements(By.xpath("//*[@id=\"userTable\"]/tbody/tr"));
        Assertions.assertFalse(noteElements.isEmpty());
        WebElement noteTitleElement = noteElements.get(0).findElement(By.tagName("th"));
        Assertions.assertEquals("Test Create Title", noteTitleElement.getText());
        WebElement noteDescriptionElement = noteElements.get(0).findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]"));
        Assertions.assertEquals("Test Create Description", noteDescriptionElement.getText());
    }



    @Test
    @Order(2)
    public void should_UpdateNoteDetails_When_UpdateNoteSuccessfully() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        CommonTestUtils.doMockSignUp(driver, port, "adding", "notes", "username", "password");
        CommonTestUtils.doLogIn(driver, port, "username", "password");

        this.doCreateUpdateNote(driver, port, true, "Test Update Title", "Test Update Description");

        // Should be on result page with success msg
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("http://localhost:" + this.port + "/notes", driver.getCurrentUrl());
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));
        // Navigate to home page
        clickOnNoteTab(driver, port, webDriverWait);

        // Get list of notes
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"userTable\"]/tbody/tr")));
        List<WebElement> updatedNotesListElements = driver.findElements(By.xpath("//*[@id=\"userTable\"]/tbody/tr"));
        Assertions.assertFalse(updatedNotesListElements.isEmpty());
        WebElement updatedNoteTitleElement = updatedNotesListElements.get(0).findElement(By.tagName("th"));
        Assertions.assertEquals("Test Update Title", updatedNoteTitleElement.getText());
        WebElement updatedNoteDescriptionElement = updatedNotesListElements.get(0).findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]"));
        Assertions.assertEquals("Test Update Description", updatedNoteDescriptionElement.getText());
    }

    @Test
    @Order(3)
    public void should_DeleteNote_When_DeleteNoteSuccessfully() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        CommonTestUtils.doMockSignUp(driver, port, "adding", "notes", "username", "password");
        CommonTestUtils.doLogIn(driver, port, "username", "password");

        clickOnNoteTab(driver, port, webDriverWait);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[1]/a")));
        List<WebElement> deleteNoteButtons = driver.findElements(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[1]/a"));
        int numberOfNotes = deleteNoteButtons.size();
        deleteNoteButtons.get(0).click();

        // Should be on success page
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

        clickOnNoteTab(driver, port, webDriverWait);
        List<WebElement> updatedDeleteNoteButtons = driver.findElements(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[1]/a"));
        Assertions.assertEquals(numberOfNotes - 1, updatedDeleteNoteButtons.size());
    }

}
