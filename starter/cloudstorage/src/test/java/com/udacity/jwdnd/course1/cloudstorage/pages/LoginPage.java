package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    @FindBy(id = "inputUsername")
    private WebElement inputUsername;
    @FindBy(id = "inputPassword")
    private WebElement inputPassword;
    @FindBy(id = "login-button")
    private WebElement loginButton;
    @FindBy(id = "login-error")
    private WebElement errorMessage;
    @FindBy(id = "success-msg")
    private WebElement successMessage;

    private final WebDriverWait webDriverWait;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        webDriverWait = new WebDriverWait(driver, 2);
    }

    public void fillInputUsername(String username) {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputUsername));
        inputUsername.click();
        inputUsername.clear();
        inputUsername.sendKeys(username);
    }

    public void fillInputPassword(String password) {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputPassword));
        inputPassword.click();
        inputPassword.clear();
        inputPassword.sendKeys(password);
    }

    public void clickLogin() {
        webDriverWait.until(ExpectedConditions.visibilityOf(loginButton));
        loginButton.click();
    }
}
