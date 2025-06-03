package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By emailInputLocator = By.xpath("/html/body/div[1]/div/div/div[2]/div[1]/div/div/div/div/div/div/div/form/div[2]/div[1]/div/div[1]/div/input");
    private By continueButtonLocator = By.xpath("/html/body/div[1]/div/div/div[2]/div[1]/div/div/div/div/div/div/div/form/div[2]/button");
    private By emptyEmailErrorLocator = By.xpath("/html/body/div[1]/div/div/div[2]/div[1]/div/div/div/div/div/div/div/form/div[2]/div[1]/div/div[2]");
    private By invalidEmailErrorLocator = By.xpath("/html/body/div[1]/div/div/div[2]/div[1]/div/div/div/div/div/div/div/form/div[2]/div[1]/div/div[2]");
    private By facebookLoginLocator = By.xpath("/html/body/div[1]/div/div/div[2]/div[1]/div/div/div/div/div/div/div/form/div[2]/div[2]/div[2]/a[3]");
    private By googleLoginLocator = By.xpath("/html/body/div[1]/div/div/div[2]/div[1]/div/div/div/div/div/div/div/form/div[2]/div[2]/div[2]/a[1]");
    private By facebookEmailInputLocator = By.id("email");
    private By facebookPasswordInputLocator = By.id("pass");
    private By facebookLoginButtonLocator = By.xpath("//button[@name='login']");
    private By googleEmailInputLocator = By.id("identifierId");
    private By googleNextButtonLocator = By.xpath("//button[contains(., 'Next')]");
    private By googlePasswordInputLocator = By.name("Passwd");
    private By googleNextButtonLocator2 = By.xpath("//button[contains(., 'Next')]");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    // Methods for Login Page
    public void enterEmail(String email) {
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(emailInputLocator));
        emailInput.sendKeys(email);
    }

    public void clickContinue() {
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(continueButtonLocator));
        continueButton.click();
    }

    public String getEmptyEmailError() {
        WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(emptyEmailErrorLocator));
        return errorMessage.getText();
    }

    public String getInvalidEmailError() {
        WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(invalidEmailErrorLocator));
        return errorMessage.getText();
    }

    public void clickFacebookLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(facebookLoginLocator)).click();
    }

    public void clickGoogleLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(googleLoginLocator)).click();
    }

    // Methods for Facebook Login
    public void enterFacebookEmail(String email) {
        driver.findElement(facebookEmailInputLocator).sendKeys(email);
    }

    public void enterFacebookPassword(String password) {
        driver.findElement(facebookPasswordInputLocator).sendKeys(password);
    }

    public void clickFacebookLoginButton() {
        driver.findElement(facebookLoginButtonLocator).click();
    }

    // Methods for Google Login
    public void enterGoogleEmail(String email) {
        wait.until(ExpectedConditions.presenceOfElementLocated(googleEmailInputLocator)).sendKeys(email);
    }

    public void clickGoogleNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(googleNextButtonLocator)).click();
    }

    public void enterGooglePassword(String password) {
        wait.until(ExpectedConditions.presenceOfElementLocated(googlePasswordInputLocator)).sendKeys(password);
    }

    public void clickGoogleNextButton2() {
        wait.until(ExpectedConditions.elementToBeClickable(googleNextButtonLocator2)).click();
    }
}
