package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
public class CreateProductFunctionalTest
{
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest()
    {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createProduct_isSuccessful(ChromeDriver driver) throws Exception
    {
        // Click the create button
        driver.get(baseUrl + "/product/list");
        WebElement createButton = driver.findElement(By.id("create-button"));
        createButton.click();

        // Verify we are directed to the create product form page
        assertEquals(baseUrl+"/product/create", driver.getCurrentUrl());

        // Fill out the product form
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        nameInput.sendKeys("Sampo Cap Galih");

        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        quantityInput.sendKeys(String.valueOf(67));

        // Submit the form
        WebElement submitButton = driver.findElement(By.id("submit-button"));
        submitButton.click();

        // Verify we are directed to the product list page
        assertEquals(baseUrl + "/product/list", driver.getCurrentUrl());

        // Check if the product is in the list
        List<WebElement> cells = driver.findElements(By.tagName("td"));
        boolean productFound = false;

        for (WebElement cell: cells)
        {
            if (cell.getText().equals("Sampo Cap Galih"))
            {
                productFound = true;
                break;
            }
        }
        assertTrue(productFound, "The created product should be visible in the product list.");
    }
}
