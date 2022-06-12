package com.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

import static com.project.jsoupCSSParsingMethods.*;

/**
 * Project: NIRepsWebScrapper
 * Package: com.project
 * Filename: HoCWebScraper
 * Created by Michael Way on 11/06/2022
 * A class containing executable methods to retrieve the details of all sitting Northern Ireland MPs from the House of
 * Commons Website and return then as a LinkedList of ElectedRep objects.
 **/
public class HoC
{
    //declaring a static constant String to hold the URL of the member's section of the Parliament website
    final static String URL_START = "https://members.parliament.uk";
    /*
    a repeatable method to return a LinkedList of ElectedReps of the Members of Parliament for Constituencies in
    Northern Ireland
     */
    public LinkedList<ElectedRep> getMPs()
    {
        //declaring a constant LinkedList of Strings storing the names of the NI constituencies, calling the getNIConstituencies method
        final LinkedList<String> CONSTITUENCIES = getNIConstituencies();

        //declaring a constant LinkedList of Strings storing common preNominals, calling the getPreNominals method
        final LinkedList<String> preNoms = getPreNominals();

        //declaring constant Strings to hold the URL for the member's search page
        final String URL_SEARCH = "/members/commons?SearchText=";

        //declaring a LinkedList of Elected Reps to store the members of parliament
        LinkedList<ElectedRep> membersOfParliament = new LinkedList<>();

        //for loop for the number of constituencies
        for (String constituency : CONSTITUENCIES)
        {
            //try block
            try
            {
                /*
                concatenate URL_START, URL_SEARCH and the returned String from getURLEnd method with loop number of
                LinkedList CONSTITUENCIES as an actual parameter
                 */
                String url = URL_START + URL_SEARCH + getURLEnd(constituency);

                //creating a Document copy of the webpage using Jsoup
                final Document webpage = Jsoup.connect(url).timeout(30 * 1000).userAgent("Mozilla").get();

                //for each instance of a member contact card on the webpage
                for (Element member : webpage.select("a.card.card-member"))
                {
                    //String to hold the url of the member's own webpage
                    final String memberURL = URL_START + member.attr("href");

                    //for each instance of the constituency indicator for the member
                    for (Element con : member.select("div.indicator.indicator-label"))
                    {
                        //if the constituency indicator text matches the constituency being searched for
                        if (con.text().equals(constituency))
                        {
                            //add ElectedRep to membersOfParliament by calling mPFromURL on memberURL and preNoms
                            membersOfParliament.add(mPFromURL(memberURL, preNoms));
                        }//if
                    }//for
                }//for
            }//try
            //catch block
            catch (Exception e)
            {
                //print exception
                System.out.println(e);
            }//catch
        }//for

        //return membersOfParliament LinkedList
        return membersOfParliament;
    }//getNIMPs

    //a repeatable method to load a LinkedList of Strings of NI Parliamentary Constituencies from a text file
    private static LinkedList<String> getNIConstituencies()
    {
        //declaring a LinkedList constituencies to hold the String values of the NI constituencies
        LinkedList<String> constituencies = new LinkedList();

        //try block
        try
        {
            //declaring a FileReader and a BufferedReader to read in a list of constituencies
            FileReader fr = new FileReader("constituencies.txt");
            BufferedReader br = new BufferedReader(fr);

            /*
            declaring an integer to hold the length of the file, and reading it with the Buffered Reader from the
            header line
             */
            int length = Integer.parseInt(br.readLine());

            //for loop iterating from 0 to one below the length of the file
            for(int i = 0; i < length; i++)
            {
                //reading the next line in the buffered reader and adding to constituencies LinkedList
                constituencies.add(br.readLine());
            }//for
        }//try
        //catch block
        catch(Exception e)
        {
            //print out exception
            System.out.println(e);
        }//catch

        //return constituencies LinkedList
        return constituencies;
    }//getNIConstituencies

    //a repeatable method to load a LinkedList of Strings of PreNominal Titles from a text file
    private static LinkedList<String> getPreNominals()
    {
        //declaring a LinkedList constituencies to hold the String values of preNominals
        LinkedList<String> preNoms = new LinkedList();

        //try block
        try
        {
            //declaring a FileReader and a BufferedReader to read in a list of constituencies
            FileReader fr = new FileReader("preNoms.txt");
            BufferedReader br = new BufferedReader(fr);

            /*
            declaring an integer to hold the length of the file, and reading it with the Buffered Reader from the
            header line
             */
            int length = Integer.parseInt(br.readLine());

            //for loop iterating from 0 to one below the length of the file
            for(int i = 0; i < length; i++)
            {
                //reading the next line in the buffered reader and adding to preNoms LinkedList
                preNoms.add(br.readLine());
            }//for
        }//try
        //catch block
        catch(Exception e)
        {
            //print out exception
            System.out.println(e);
        }//catch

        //return preNoms LinkedList
        return preNoms;
    }//getPreNominals

    //a repeatable method to return the ending of the search field for parliamentary constituencies
    private static String getURLEnd(String constituencyName)
    {
        //return the constituency name with spaces replaced with +
        return constituencyName.replace(" ","+");
    }//getURLEnd

    //a repeatable method to return an MP's details as an ElectedRep object from a formal parameter URL using jsoup
    private ElectedRep mPFromURL(String mPURL,LinkedList<String> preNominalTitles)
    {
        //declaring a tempER Elected Rep to store the details of the MP
        ElectedRep tempER = new ElectedRep();

        //declaring constants to hold the job title and elected body title of MPs
        final String ELECTED_BODY = "House of Commons";
        final String JOB_TITLE = "Member of Parliament";

        //try
        try
        {
            //declaring a document webpage and taking a copy of the html by connecting to the mPURL
            Document webpage = Jsoup.connect(mPURL).timeout(30*5000).userAgent("Mozilla").get();

            /*
            declaring a String to hold the fullNameAndTitle of the ElectedRep, calling the
            stringFromFirstElementIfExists method on the webpage and css tag
             */
            String fullNameAndTitle = stringFromFirstElementIfExists(webpage,"#main-content > " +
                  "div.container > article > div > div > div > div.heading > h2").replace("Contact ","");

            //for the number of preNominalTitles in the LinkedList
            for (String preNominalTitle : preNominalTitles)
            {
                //if fullNameAndTitle contains the preNominal in index position i
                if (fullNameAndTitle.contains(preNominalTitle))
                {
                    //set tempER preNominal to preNominal in index position i
                    tempER.setPreNominal(preNominalTitle);

                    //remove preNominal in index position i from fullNameAndTitle and trim
                    fullNameAndTitle = fullNameAndTitle.replace(preNominalTitle, "").trim();
                }//if
            }//for

            //split fullNameAndTitle into a String of arrays splitNameAndTitle
            String[] splitNameAndTitle = fullNameAndTitle.split(" ");

            //set officialForename and usualForename of tempER to index position 0 of splitNameAndTitle
            tempER.setOfficialForename(splitNameAndTitle[0]);
            tempER.setUsualForename(splitNameAndTitle[0]);

            //set officialSurname to last index position of splitNameAndTitle
            tempER.setOfficialSurname(splitNameAndTitle[splitNameAndTitle.length-1]);

            //call stringFromFirstElementIfExists on CSS selector to set preferred termOfAddress for tempER
            tempER.setTermOfAddress(stringFromFirstElementIfExists(webpage,"#main-content > div.container > " +
                  "article > div > div > div > p:nth-child(3) > strong"));

            //set the ElectedBody and Title of tempER to their constant values
            tempER.setElectedBody(ELECTED_BODY);
            tempER.setTitle(JOB_TITLE);

            //call address1FromCSSSelectedParagraph on CSS selector to intialise all Address 1 details for tempER
            address1FromCSSSelectedParagraph(webpage, "#main-content > div.container > article > " +
                  "div > div > div > div.sections > div > div:nth-child(1) > div > div.content > div.info > div > " +
                  "div > div.col-md-5", tempER);

            //call address2FromCSSSelectedParagraph on CSS selector to intialise all Address 2 details for tempER
            address2FromCSSSelectedParagraph(webpage, "#main-content > div.container > article > div > " +
                  "div > div > div.sections > div > div:nth-child(2) > div > div.content > div.info > div > div > " +
                  "div.col-md-5", tempER);

            //call attributeFromFirstElementIfExists on label "Phone:" to set phone1
            tempER.setPhone1(attributeFromFirstElementIfExists(webpage,"span.label:matchesOwn(Phone:)").replace(" ",""));

            //call attributeFromLastElementIfExists on label "Phone:" to set phone2
            tempER.setPhone2(attributeFromLastElementIfExists(webpage,"span.label:matchesOwn(Phone:)").replace(" ",""));


            //call attributeFromFirstElementIfExists on label "Email:" to set email1
            tempER.setEmail1(attributeFromFirstElementIfExists(webpage,"span.label:matchesOwn(Email:)").replace(" ",""));

            //call attributeFromLastElementIfExists on label "Email:" to set email2
            tempER.setEmail2(attributeFromLastElementIfExists(webpage,"span.label:matchesOwn(Email:)").replace(" ",""));

            //call partyAndElectoralAreaFromURL on css selector for "Parliamentary Career" page and tempER
            partyAndElectoralAreaFromURL(webpage.select("#main-content > div.container > article > " +
                  "div > div > ul > li:nth-child(2) > a").attr("href"), tempER);

            //calling the removeDuplicateAddress, removeDuplicateEmail, and removeDuplicatePhone methods on tempER
            tempER.removeDuplicateAddress();
            tempER.removeDuplicateEmail();
            tempER.removeDuplicatePhone();
        }//try
        //catch
        catch (Exception e)
        {
            //print exception e
            System.out.println(e);
        }//catch

        //return tempER
        return tempER;
    }//mPFromURL

    //a repeatable method to set the party and Electoral Area for a ElectedRep from their "Parliamentary Career" page
    private void partyAndElectoralAreaFromURL(String pURL, ElectedRep tempRep)
    {
        //declaring a constant partyEAURL concatenating URL_START and pURL
        final String partyEAURL = URL_START + pURL;

        //try block
        try
        {
            //declaring a document webpage and taking a copy of the html by connecting to the partyEAURL
            Document webpage = Jsoup.connect(partyEAURL).timeout(30 * 5000).userAgent("Mozilla").get();

            //calling stringFromFirstElementIfExists on CSS selector to set the Electoral Area of tempRep
            tempRep.setElectoralArea(stringFromFirstElementIfExists(webpage,"#main-content > " +
                  "div.container > article > div > div > div > div.sections > div > div > div:nth-child(2) > a > " +
                  "div > div.content > div.primary-info"));

            //calling stringFromFirstElementIfExists on CSS selector to set the Party of tempRep
            tempRep.setParty(stringFromFirstElementIfExists(webpage,"#main-content > div.container > " +
                  "article > div > div > div > div.sections > div > div > div:nth-child(4) > a > div > div.content > " +
                  "div"));
        }//try
        //catch block
        catch (Exception e)
        {
            //print exception e
            System.out.println(e);
        }//catch
    }//partyAndElectoralAreaFromURL
}//class
