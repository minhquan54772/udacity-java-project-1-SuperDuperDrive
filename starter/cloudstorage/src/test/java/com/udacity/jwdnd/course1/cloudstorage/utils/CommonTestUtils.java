package com.udacity.jwdnd.course1.cloudstorage.utils;

import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignUpPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class CommonTestUtils {
    private int port;


    private final WebDriverWait webDriverWait;
    private final SignUpPage signUpPage;
    private final LoginPage loginPage;

    public CommonTestUtils(WebDriver driver) {
        webDriverWait = new WebDriverWait(driver, 2);
        signUpPage = new SignUpPage(driver);
        loginPage = new LoginPage(driver);
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    public void doMockSignUp(WebDriver driver, int port, String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        driver.get("http://localhost:" + port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        signUpPage.fillInputFirstName(firstName);
        signUpPage.fillInputLastName(lastName);
        signUpPage.fillInputUsername(userName);
        signUpPage.fillInputPassword(password);
        signUpPage.clickSignUp();
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    public void doLogIn(WebDriver driver, int port, String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + port + "/login");
        webDriverWait.until(ExpectedConditions.titleContains("Login"));

        loginPage.fillInputUsername(userName);
        loginPage.fillInputPassword(password);
        loginPage.clickLogin();
    }
}
