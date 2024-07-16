package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.List;

public class FilesPage {
    @FindBy(id = "fileUpload")
    private WebElement inputFileUpload;

    @FindBy(id = "uploadButton")
    private WebElement uploadButton;


    private final WebDriverWait webDriverWait;

    public FilesPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        webDriverWait = new WebDriverWait(driver, 2);
    }

    public void fillInputFileUpload(String fileName) {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputFileUpload));
        inputFileUpload.clear();
        inputFileUpload.sendKeys(new File(fileName).getAbsolutePath());
    }

    public void clickUploadButton() {
        webDriverWait.until(ExpectedConditions.visibilityOf(uploadButton));
        uploadButton.click();
    }

    public void downloadFirstFile() {
        WebElement firstDownloadButton = getFirstDownloadButton();
        firstDownloadButton.click();
    }

    public void deleteFirstFile() {
        WebElement firstDeleteButton = getFirstDeleteButton();
        firstDeleteButton.click();
    }

    public List<WebElement> getFileRows() {
        WebElement tableBody = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"fileTable\"]/tbody")));
        return tableBody.findElements(By.tagName("tr"));
    }

    public com.udacity.jwdnd.course1.cloudstorage.models.File getFirstFile() {
        String uploadedFileName = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"fileTable\"]/tbody/tr/th"))).getText();
        com.udacity.jwdnd.course1.cloudstorage.models.File firstFile = new com.udacity.jwdnd.course1.cloudstorage.models.File();
        firstFile.setFilename(uploadedFileName);

        return firstFile;
    }

    public WebElement getFirstDownloadButton() {
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"fileTable\"]/tbody/tr/td/a[1]")));
    }

    public WebElement getFirstDeleteButton() {
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"fileTable\"]/tbody/tr/td/a[2]")));
    }
}
