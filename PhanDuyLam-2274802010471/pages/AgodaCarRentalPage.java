package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AgodaCarRentalPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Định nghĩa các phần tử (WebElements) trên trang
    private By transportButtonLocator = By.xpath("//*[@id='page-header']/section/div[1]/nav/div[2]/div[3]/div/div[2]/div/div[1]");
    private By transportDropdownLocator = By.xpath("//*[@id='page-header']/section/div[1]/nav/div[2]/div[3]/div/div[2]/div/div[2]");
    private By carRentalOptionLocator = By.xpath("//span[contains(text(),'Thuê xe')]");
    private By searchButtonLocator = By.xpath("//*[@id=\"main\"]/div[1]/div/div/div[2]/div/div/div[1]/div[2]/div/div[2]/div/div[3]/button/span");

    // Constructor
    public AgodaCarRentalPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Các phương thức tương tác với trang
    public void clickTransportButton() {
        WebElement transportButton = wait.until(ExpectedConditions.elementToBeClickable(transportButtonLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", transportButton);
    }

    public void waitForTransportDropdown() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(transportDropdownLocator));
    }

    public void clickCarRentalOption() {
        WebElement carRentalOption = wait.until(ExpectedConditions.elementToBeClickable(carRentalOptionLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", carRentalOption);
    }

    public void clickSearchButton() {
        WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(searchButtonLocator));
        // Đảm bảo button nằm trong vùng hiển thị bằng cách scroll xuống vị trí của nó
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", searchButton);
        wait.until(ExpectedConditions.elementToBeClickable(searchButtonLocator));
        // Click button bằng JavascriptExecutor để tránh trường hợp bị overlay
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);
    }

    public void waitForResultsPage() {
        wait.until(ExpectedConditions.urlContains("results"));
    }
}