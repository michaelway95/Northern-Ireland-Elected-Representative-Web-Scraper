package com.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

import static com.project.jsoupCSSParsingMethods.*;

/**
 * Project: NIRepsWebScrapper
 * Package: com.example.nirepswebscrapper
 * Filename: NIAWebScraper
 * Created by Michael Way on 05/06/2022
 * A class containing executable methods to retrieve the details of all sitting MLAs from the Northern Ireland Assembly
 * Website and return then as a LinkedList of ElectedRep objects.
 **/
public class NIA
{
    //a repeatable method to return a LinkedList of all MLAs in the Northern Ireland Assembly using Selenium
    public LinkedList<ElectedRep> getMLAs()
    {
        //declaring a constant String for the MLA search page
        final String NIA_URL = "http://aims.niassembly.gov.uk/mlas/search.aspx";

        //declaring a headless Firefox WebDriver, with a WebDriverWait of 30 milliseconds
        WebDriver driver = headlessFirefox();
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofMillis(30));

        //declaring a LinkedList of Elected Reps to store the MLAs
        LinkedList<ElectedRep> mLALinkedList = new LinkedList<>();

        //declaring a LinkedList of Strings to store the individual URLs of the MLA profile pages
        LinkedList<String> mLAURLs = new LinkedList<>();

        //try
        try
        {
            //open URL on Firefox
            driver.get(NIA_URL);

            //declaring a constant WebElement for the first dropDown list found by its xpath
            final WebElement dropDown1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath
                  ("//*[@id=\"ctl00_MainContentPlaceHolder_SelectionDropDownList\"]")));

            //creating a constant Select for the dropdown condition of dropDown1
            final Select drpCondition = new Select(dropDown1);

            //selecting by Visible Text for the word "Constituency"
            drpCondition.selectByVisibleText("Constituency");

            //declaring a null WebElement to represent the first MLA in a Constituency list.
            WebElement firstMLAInCon = null;

            //for the number of Constituencies in List
            for(int i = 0; i < 18; i++)
            {
                //declaring a constant WebElement for the second dropDown list found by its xpath
                final WebElement dropDown2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath
                      ("//*[@id=\"ctl00_MainContentPlaceHolder_ConstituencyDropDownList\"]")));

                //creating a constant Select for the dropdown condition of dropDown2
                final Select drpConstituency = new Select(dropDown2);

                //declaring a constant list of WebElements populated by the list of options in drpConstituency
                final List<WebElement> Constituencies = drpConstituency.getOptions();

                //select the option stored in List position i by visible text
                drpConstituency.selectByVisibleText(Constituencies.get(i).getText());

                //if not the first loop
                if(i > 0)
                {
                    //wait until the firstMLA in the last constituency disappears
                    wait.until(ExpectedConditions.invisibilityOf(firstMLAInCon));

                    //wait until the first MLA in the latest constituency appears
                    wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_UpdatePanel1\"]/main/div/div/div[2]/div/div[1]/div[1]/div/h4/a"))));
                }//if

                //declaring a constant WebElement mLAs to direct to the list of MLAs by xpath
                final WebElement mLAs = driver.findElement(By.xpath("//*[@id=\"ctl00_MainContentPlaceHolder_UpdatePanel1\"]/main"));

                //declaring a constant list of WebElements to hold the elements for this constituency's MLAs
                final List<WebElement> newMLAs = mLAs.findElements(By.tagName("a"));

                //setting the value of the first MLA in the Constituency as the first value in the newMLAs list
                firstMLAInCon = newMLAs.get(0);

                //for the number of MLAs in a constituency
                for (WebElement newMLA : newMLAs)
                {
                    //add the url of the MLA to the mLAURL list
                    mLAURLs.add(newMLA.getAttribute("href"));
                }//for
            }//for
        }//try
        catch (Exception e)
        {
            //print out exception
            System.out.println(e);
        }//catch

        //close driver
        driver.close();

        //for the number of URLs in mLAURls
        for (String mLAURL : mLAURLs)
        {
            //call the mLAFromURL method on the mLAURL in position i and add to mLALinkedList
            mLALinkedList.add(mLAFromURL(mLAURL));
        }//for

        //return mLALinkedList
        return mLALinkedList;
    }//NIAWebScraper

    //a static method to return a headlessFirefox webdriver
    private static WebDriver headlessFirefox()
    {
        //declaring a File for the Firefox driver
        File driverLocation = new File("geckodriver.exe");

        //setting the absolute path of the Firefox driver
        System.setProperty("webdriver.gecko.driver",driverLocation.getAbsolutePath());

        //declaring a FirefoxOptions object
        FirefoxOptions options = new FirefoxOptions();

        //setting the binary location of the Firefox executable
        options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");

        //passing the argument to headless
        options.addArguments("--headless");

        //returning FirefoxDriver with options as formal parameter
        return new FirefoxDriver(options);
    }//headlessFirefox

    //a repeatable method to return an MLA's details as an ElectedRep object from a formal parameter URL using jsoup
    private ElectedRep mLAFromURL(String mLAURL)
    {
        //declaring a tempER Elected Rep to store the details of the MLA
        ElectedRep tempER = new ElectedRep();

        //declaring constants to hold the job title and elected body title of NIA MLAs
        final String ELECTED_BODY = "Northern Ireland Assembly";
        final String JOB_TITLE = "Member of the Legislative Assembly";

        //try
        try
        {
            //declaring a document webpage and taking a copy of the html by connecting to the mLAURL
            Document webpage = Jsoup.connect(mLAURL).timeout(30*5000).userAgent("Mozilla").get();

            /*declaring a String to hold the fullNameAndTitle of the ElectedRep, calling the
             stringFromFirstElementIfExists method on the webpage and css tag
             */
            String fullNameAndTitle = stringFromFirstElementIfExists(webpage,
                  "#ctl00_MainContentPlaceHolder_FullNameLabel");

            //set the ElectedBody and Title of tempER to their constant values
            tempER.setElectedBody(ELECTED_BODY);
            tempER.setTitle(JOB_TITLE);

            //set tempER preNominal by calling the stringFromFirstElementSiblingIfExists method on webpage and css tag
            tempER.setPreNominal(stringFromFirstElementSiblingIfExists(webpage,"strong:matchesOwn(Title)"));

            /*
            set tempER officialSurname by calling the stringFromFirstElementSiblingIfExists method on webpage and css
            tag
             */
            tempER.setOfficialSurname(stringFromFirstElementSiblingIfExists(webpage,
                  "strong:matchesOwn(Official Last Name)"));

            /*
            set tempER officialForename by calling the stringFromFirstElementSiblingIfExists method on webpage and css
            tag
             */
            tempER.setOfficialForename(stringFromFirstElementSiblingIfExists(webpage,
                  "strong:matchesOwn(Official First Name)"));

            /*
            set tempER usualForename by calling the stringFromFirstElementSiblingIfExists method on webpage and css tag
             */
            tempER.setUsualForename(stringFromFirstElementSiblingIfExists(webpage,
                  "strong:matchesOwn(Usual First Name)"));

            /*
            declaring a String array initialisedNamesAndTitle populated with tempER's PreNominal, Surname, Forenames,
            and a blank space
             */
            String[] initialisedNamesAndTitle = new String[]{tempER.getPreNominal(),tempER.getOfficialSurname(),
                  tempER.getOfficialForename(),tempER.getUsualForename()," "};

            //for the length of intialisedNamesAndTitle
            for (String s : initialisedNamesAndTitle)
            {
                /*
                set value of fullNameAndTitle as fullNameAndTitle with the String found in intiailisedNamesAndTitle index
                position 0 with an empty String
                 */
                fullNameAndTitle = fullNameAndTitle.replace(s, "");
            }//for

            //if fullNameAndTitle length is greater than zero
            if(fullNameAndTitle.length() > 0)
            {
                //set tempER postNominal to fullNameAndTitle
                tempER.setPostNominal(fullNameAndTitle);
            }//if

            //set tempER party by calling the stringFromFirstElementSiblingIfExists method on webpage and css tag
            tempER.setParty(stringFromFirstElementSiblingIfExists(webpage,"strong:matchesOwn(Party:)"));

            /*
            set tempER electoralArea by calling the stringFromFirstElementSiblingIfExists method on webpage and css tag
             */
            tempER.setElectoralArea(stringFromFirstElementSiblingIfExists(webpage,"strong:matchesOwn(Constituency:)"));

            /*
            set tempER address 1 details by calling the stringFromFirstElementSiblingIfExists method on webpage and css
            tag for each aspect of the address
             */
            tempER.setAdd1Line1(stringFromFirstElementSiblingIfExists(webpage,"strong:matchesOwn(Room:)"));
            tempER.setAdd1Line2(stringFromFirstElementSiblingIfExists(webpage,"strong:matchesOwn(Address:)"));
            tempER.setAdd1Line3(stringFromFirstElementSiblingIfExists(webpage,"strong:matchesOwn(Townland:)"));
            tempER.setAdd1Line4(stringFromFirstElementSiblingIfExists(webpage,"strong:matchesOwn(Ward)"));
            tempER.setAdd1Line5(stringFromFirstElementSiblingIfExists(webpage,"strong:matchesOwn(Town:)"));
            tempER.setAdd1Postcode(stringFromFirstElementSiblingIfExists(webpage,"strong:matchesOwn(Post Code:)"));

            /*
            set tempER address 2 details by calling the stringFromLastElementSiblingIfExists method on webpage and css
            tag for each aspect of the address
             */
            tempER.setAdd2Line1(stringFromLastElementSiblingIfExists(webpage,"strong:matchesOwn(Address 1:)"));
            tempER.setAdd2Line2(stringFromLastElementSiblingIfExists(webpage,"strong:matchesOwn(Address 2:)"));
            tempER.setAdd2Line3(stringFromLastElementSiblingIfExists(webpage,"strong:matchesOwn(Address 3:)"));
            tempER.setAdd2Line4(stringFromLastElementSiblingIfExists(webpage,"strong:matchesOwn(Townland:)"));
            tempER.setAdd2Line5(stringFromLastElementSiblingIfExists(webpage,"strong:matchesOwn(Town:)"));
            tempER.setAdd2Postcode(stringFromLastElementSiblingIfExists(webpage,"strong:matchesOwn(Post Code:)"));

            //set tempER email1 by calling the emailFromFirstElementSiblingIfExists method on webpage and css tag
            tempER.setEmail1(emailFromFirstElementSiblingIfExists(webpage,"strong:matchesOwn(Email:)"));

            //set tempER email2 by calling the emailFromLastElementSiblingIfExists method on webpage and css tag
            tempER.setEmail2(emailFromLastElementSiblingIfExists(webpage,"strong:matchesOwn(Email:)"));

            //set tempER phone1 by calling the stringFromFirstElementSiblingIfExists method on webpage and css tag
            tempER.setPhone1(stringFromFirstElementSiblingIfExists(webpage,"strong:matchesOwn(Phone:)").replace(" ",""));

            //set tempER phone2 by calling the stringFromLastElementSiblingIfExists method on webpage and css tag
            tempER.setPhone2(stringFromLastElementSiblingIfExists(webpage,"strong:matchesOwn(Phone:)").replace(" ",""));



            //calling the removeDuplicateAddress, removeDuplicateEmail, and removeDuplicatePhone methods on tempER
            tempER.constituencyException();
            tempER.removeDuplicateAddress();
            tempER.removeDuplicateEmail();
            tempER.removeDuplicatePhone();

            webpage.empty();
        }//try
        //catch
        catch (Exception e)
        {
            //print exception e
            System.out.println(e);
        }//catch

        //return tempER
        return tempER;
    }//mLAFromURL
}//NIAWebScraper
