package com.udacity.jwdnd.course1.cloudstorage.pages;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CredentialsPage {
    @FindBy(id="add-credential-btn")
    private WebElement addCredentialBtn;

    @FindBy(id="credentialModal")
    private WebElement credentialModal;

    @FindBy(id="credential-url")
    private WebElement inputCredentialUrl;

    @FindBy(id="credential-username")
    private WebElement inputCredentialUsername;

    @FindBy(id="credential-password")
    private WebElement inputCredentialPassword;

    @FindBy(id="save-credential-btn")
    private WebElement saveCredentialBtn;


    private final WebDriverWait webDriverWait;

    public CredentialsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        webDriverWait = new WebDriverWait(driver, 2);
    }

    public void showCreateCredentialModal() {
        webDriverWait.until(ExpectedConditions.visibilityOf(addCredentialBtn));
        addCredentialBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialModal));
    }

    public void showUpdateCredentialModal() {
        WebElement firstEditNoteBtn = getFirstEditCredential();
        webDriverWait.until(ExpectedConditions.visibilityOf(firstEditNoteBtn));
        firstEditNoteBtn.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialModal));
    }

    public void deleteFirstCredential() {
        WebElement firstDeleteNoteBtn = getFirstDeleteCredentialBtn();
        webDriverWait.until(ExpectedConditions.visibilityOf(firstDeleteNoteBtn));
        firstDeleteNoteBtn.click();
    }

    public void fillInputUrl(String url) {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputCredentialUrl));
        inputCredentialUrl.clear();
        inputCredentialUrl.sendKeys(url);
    }

    public void fillInputUsername(String username) {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputCredentialUsername));
        inputCredentialUsername.clear();
        inputCredentialUsername.sendKeys(username);
    }

    public void fillInputPassword(String password) {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputCredentialPassword));
        inputCredentialPassword.clear();
        inputCredentialPassword.sendKeys(password);
    }

    public void clickSaveCredentialBtn() {
        webDriverWait.until(ExpectedConditions.visibilityOf(saveCredentialBtn));
        saveCredentialBtn.click();
    }

    public List<WebElement> getCredentialRows() {
        WebElement tableBody = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"credentialTable\"]/tbody")));
        return tableBody.findElements(By.tagName("tr"));
    }

    public Credential getFirstCredential() {
        String url = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[1]/th"))).getText();
        String username = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[1]/td[2]"))).getText();
        String password = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr[1]/td[2]"))).getText();

        Credential credential = new Credential();
        credential.setUrl(url);
        credential.setUsername(username);
        credential.setPassword(password);

        return credential;
    }

    public WebElement getFirstEditCredential() {
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[1]/button")));
    }

    public WebElement getFirstDeleteCredentialBtn() {
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[1]/a")));
    }
}
