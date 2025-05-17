package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.LoginPage;

@Epic("Login Module")
@Feature("Login Functionality")
public class LoginTest extends BaseTest {

    @Test(description = "Verify user can log in with valid username and password")
    @Story("Valid login scenario")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify successful login using valid credentials.")
    public void validLoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs("standard_user", "secret_sauce");
    }
}
