package com.udacity.jwdnd.course1.cloudstorage.pages;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class NotesPage {

    @FindBy(id = "add-note-btn")
    private WebElement addNoteBtn;

    @FindBy(id = "noteModal")
    private WebElement noteModal;

    @FindBy(id = "note-title")
    private WebElement inputNoteTitle;

    @FindBy(id = "note-description")
    private WebElement inputNoteDescription;

    @FindBy(id = "save-note-btn")
    private WebElement saveNoteBtn;

    private final WebDriverWait webDriverWait;

    public NotesPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        webDriverWait = new WebDriverWait(driver, 2);
    }

    public void showCreateNoteModal() {
        webDriverWait.until(ExpectedConditions.visibilityOf(addNoteBtn));
        addNoteBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(noteModal));
    }

    public void showEditNoteModal() {
        WebElement firstEditNoteBtn = getFirstEditNote();
        webDriverWait.until(ExpectedConditions.visibilityOf(firstEditNoteBtn));
        firstEditNoteBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(noteModal));
    }

    public void deleteFirstNote() {
        WebElement firstDeleteNoteBtn = getFirstDeleteNote();
        webDriverWait.until(ExpectedConditions.visibilityOf(firstDeleteNoteBtn));
        firstDeleteNoteBtn.click();
    }

    public void fillInputNoteTitle(String noteTitle) {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputNoteTitle));
        inputNoteTitle.clear();
        inputNoteTitle.sendKeys(noteTitle);
    }

    public void fillInputNoteDescription(String noteDescription) {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputNoteDescription));
        inputNoteDescription.clear();
        inputNoteDescription.sendKeys(noteDescription);
    }

    public void clickSaveNoteBtn() {
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-btn")));
        saveNoteBtn.click();
    }

    public List<WebElement> getNoteRowElements() {
        WebElement tableBody = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"userTable\"]/tbody")));
        return tableBody.findElements(By.tagName("tr"));
    }

    public Note getFirstNote() {
        String noteTitle = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"userTable\"]/tbody/tr[1]/th"))).getText();
        String noteDescription = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"userTable\"]/tbody/tr[1]/td[2]"))).getText();

        Note note = new Note();
        note.setNotetitle(noteTitle);
        note.setNotedescription(noteDescription);
        return note;
    }

    public WebElement getFirstEditNote() {
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"userTable\"]/tbody/tr[1]/td[1]/button")));
    }

    public WebElement getFirstDeleteNote() {
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"userTable\"]/tbody/tr[1]/td[1]/a")));
    }
}
