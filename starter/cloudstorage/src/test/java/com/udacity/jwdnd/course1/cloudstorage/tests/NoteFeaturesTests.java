package com.udacity.jwdnd.course1.cloudstorage.tests;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.pages.NotesPage;
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

    private static CommonTestUtils commonTestUtils;
    private static WebDriver driver;
    private static WebDriverWait webDriverWait;

    private NotesPage notesPage;


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
        clickOnNoteTab(driver, port, webDriverWait);
        notesPage = new NotesPage(driver);
    }

    @AfterAll
    public static void afterAll() {
        if (driver != null) {
            driver.quit();
        }
    }

    private static void clickOnNoteTab(WebDriver driver, int port, WebDriverWait webDriverWait) {
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
    private void doCreateUpdateNote(boolean isEdit, String noteTitle, String noteDescription) {
        if (isEdit) {
            notesPage.showEditNoteModal();
        } else {
            notesPage.showCreateNoteModal();
        }
        // Fill out note details
        notesPage.fillInputNoteTitle(noteTitle);
        notesPage.fillInputNoteDescription(noteDescription);
        notesPage.clickSaveNoteBtn();
    }

    @Test
    @Order(1)
    public void should_ShowNoteDetails_When_AddNoteSuccessfully() {

        this.doCreateUpdateNote( false, "Test Create Title", "Test Create Description");
        // Should be on result page with success msg
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("http://localhost:" + port + "/notes", driver.getCurrentUrl());
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));
        // Navigate to home page
        clickOnNoteTab(driver, port, webDriverWait);

        // Get list of notes
        List<WebElement> noteRowElements = notesPage.getNoteRowElements();
        Assertions.assertFalse(noteRowElements.isEmpty());

        Note firstNote = notesPage.getFirstNote();
        Assertions.assertEquals("Test Create Title", firstNote.getNotetitle());
        Assertions.assertEquals("Test Create Description", firstNote.getNotedescription());
    }



    @Test
    @Order(2)
    public void should_UpdateNoteDetails_When_UpdateNoteSuccessfully() {
        this.doCreateUpdateNote(true, "Test Update Title", "Test Update Description");

        // Should be on result page with success msg
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("http://localhost:" + port + "/notes", driver.getCurrentUrl());
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));
        // Navigate to home page
        clickOnNoteTab(driver, port, webDriverWait);

        // Get list of notes
        List<WebElement> noteRowElements = notesPage.getNoteRowElements();
        Assertions.assertFalse(noteRowElements.isEmpty());

        Note firstNote = notesPage.getFirstNote();
        Assertions.assertEquals("Test Update Title", firstNote.getNotetitle());
        Assertions.assertEquals("Test Update Description", firstNote.getNotedescription());
    }

    @Test
    @Order(3)
    public void should_DeleteNote_When_DeleteNoteSuccessfully() {
        List<WebElement> noteRowElements = notesPage.getNoteRowElements();
        notesPage.deleteFirstNote();

        // Should be on success page
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

        clickOnNoteTab(driver, port, webDriverWait);
        List<WebElement> updatedNoteRowElements = notesPage.getNoteRowElements();
        Assertions.assertEquals(noteRowElements.size() - 1, updatedNoteRowElements.size());
    }

}
