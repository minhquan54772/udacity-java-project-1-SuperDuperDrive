package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignUpPage {

    @FindBy(id = "inputFirstName")
    private WebElement inputFirstName;

    @FindBy(id = "inputLastName")
    private WebElement inputLastName;

    @FindBy(id = "inputUsername")
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id = "buttonSignUp")
    private WebElement buttonSignUp;


    private final WebDriverWait webDriverWait;

    public SignUpPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        webDriverWait = new WebDriverWait(driver, 2);

    }

    public void fillInputFirstName(String firstName) {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputFirstName));
        inputFirstName.click();
        inputFirstName.clear();
        inputFirstName.sendKeys(firstName);
    }

    public void fillInputLastName(String lastName) {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputLastName));
        inputLastName.click();
        inputLastName.clear();
        inputLastName.sendKeys(lastName);
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

    public void clickSignUp() {
        webDriverWait.until(ExpectedConditions.visibilityOf(buttonSignUp));
        buttonSignUp.click();
    }
}
