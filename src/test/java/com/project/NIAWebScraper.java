package com.project;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

/**
 * Project: NIRepsWebScrapper
 * Package: com.example.nirepswebscrapper
 * Filename: NIAWebScrapper
 * Created by Michael Way on 05/06/2022
 **/
public class NIAWebScrapper
{
    public static void main(String[] args) throws InterruptedException
    {
        final String NIA_URL = "http://aims.niassembly.gov.uk/mlas/search.aspx";

        WebDriver driver = headlessFirefox();
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(30));

        LinkedList<ElectedRep> mLALinkedList = new LinkedList<>();
        LinkedList<String> mLAURLs = new LinkedList<>();

        driver.get(NIA_URL);

        for(int i = 0; i < 18; i++)
        {
            WebElement dropDown1 = driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_SelectionDropDownList\"]"));

            Select drpCondition = new Select(dropDown1);

            drpCondition.selectByVisibleText("Constituency");

            WebElement dropDown2 = driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_ConstituencyDropDownList\"]"));

            Select drpConstituency = new Select(dropDown2);

            List<WebElement> Constituencies = drpConstituency.getOptions();

            drpConstituency.selectByVisibleText(Constituencies.get(i).getText());

            final WebElement mLAs = driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_UpdatePanel1\"]/main"));
            final List<WebElement> newMLAs = mLAs.findElements(By.tagName("a"));

            for(int a = 0; a < newMLAs.size(); a++)
            {
                mLAURLs.add(newMLAs.get(a).getAttribute("href"));
            }//for
        }//for

        driver.close();

        for(int i = 0; i < mLAURLs.size(); i++)
        {
            mLALinkedList.add(mLAFromURL(mLAURLs.get(i)));
        }//for
    }//main

    private static WebDriver headlessFirefox()
    {
        File driverLocation = new File("geckodriver.exe");
        System.setProperty("webdriver.gecko.driver",driverLocation.getAbsolutePath());
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
        options.addArguments("--headless");
        WebDriver driver = new FirefoxDriver(options);
        return driver;
    }//headlessFirefox

    private static ElectedRep mLAFromURL(String mLAURL)
    {
        WebDriver driver = headlessFirefox();
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(30));
        ElectedRep tempER = new ElectedRep();

        driver.get(mLAURL);

        String fullNameAndTitle = driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_FullNameLabel\"]")).getText();

        tempER.setElectedBody("Northern Ireland Assembly");
        tempER.setTitle("Member of the Legislative Assembly");

        tempER.setPreNominal(driver.findElement(By.xpath("//*[@id=\"aspnetForm\"]/div[3]/div/div/div[1]/div/div/main/div/div/div[1]/div/div/div[2]/div/p/strong[1]")).getText());
        tempER.setOfficialSurname(driver.findElement(By.xpath("//*[@id=\"aspnetForm\"]/div[3]/div/div/div[1]/div/div/main/div/div/div[1]/div/div/div[2]/div/p/strong[2]")).getText());
        tempER.setOfficialForename(driver.findElement(By.xpath("//*[@id=\"aspnetForm\"]/div[3]/div/div/div[1]/div/div/main/div/div/div[1]/div/div/div[2]/div/p/strong[3]")).getText());
        tempER.setUsualForename(driver.findElement(By.xpath("//*[@id=\"aspnetForm\"]/div[3]/div/div/div[1]/div/div/main/div/div/div[1]/div/div/div[2]/div/p/strong[4]")).getText());

        fullNameAndTitle.replace(tempER.getPreNominal(),"");
        fullNameAndTitle.replace(tempER.getOfficialSurname(),"");
        fullNameAndTitle.replace(tempER.getOfficialForename(),"");
        fullNameAndTitle.replace(tempER.getUsualForename(),"");
        fullNameAndTitle.replace(" ","");
        tempER.setPostNominal(fullNameAndTitle);

        tempER.setParty(driver.findElement(By.xpath("//*[@id=\"aspnetForm\"]/div[3]/div/div/div[1]/div/div/main/div/div/div[1]/div/div/div[2]/div/p/strong[5]")).getText());
        tempER.setElectoralArea(driver.findElement(By.xpath("//*[@id=\"aspnetForm\"]/div[3]/div/div/div[1]/div/div/main/div/div/div[1]/div/div/div[2]/div/p/strong[6]")).getText());

        tempER.setAdd1Line1(driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_AccordionPane2_content_ContactsPanel\"]/div/div[2]/p/strong[1]")).getText());
        tempER.setAdd1Line2(driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_AccordionPane2_content_ContactsPanel\"]/div/div[2]/p/strong[2]")).getText());
        tempER.setAdd1Line3(driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_AccordionPane2_content_ContactsPanel\"]/div/div[2]/p/strong[3]")).getText());
        tempER.setAdd1Line4(driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_AccordionPane2_content_ContactsPanel\"]/div/div[2]/p/strong[4]")).getText());
        tempER.setAdd1Line5(driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_AccordionPane2_content_ContactsPanel\"]/div/div[2]/p/strong[5]")).getText());
        tempER.setAdd1Postcode(driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_AccordionPane2_content_ContactsPanel\"]/div/div[2]/p/strong[6]")).getText());

        tempER.setEmail1(driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_AccordionPane2_content_ContactsPanel\"]/div/div[2]/p/strong[7]")).getText());

        System.out.println();

        driver.close();

        return tempER;
    }//mLAFromURL
}
