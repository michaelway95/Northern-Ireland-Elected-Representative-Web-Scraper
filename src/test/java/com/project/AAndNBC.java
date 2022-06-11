package com.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.LinkedList;

import static com.project.jsoupCSSParsingMethods.*;

/**
 * Project: NIRepsWebScrapper
 * Package: com.project
 * Filename: HoCWebScraper
 * Created by Michael Way on 11/06/2022
 * A class containing executable methods to retrieve the details of all sitting Councillors from Antrim and Newtownabbey
 * Borough Council and return then as a LinkedList of ElectedRep objects.
 **/
public class AAndNBC
{
    /*
    a repeatable method to return a LinkedList of ElectedReps of Councillors of Antrim and Newtownabbey Borough Council
     */
    public LinkedList<ElectedRep> getAAndNBCCouncillors()
    {
        //declaring a static constant String to hold the URL of the councillor section of the council website
        final String URL = "https://antrimandnewtownabbey.gov.uk/councillors/";
        final String COUNCIL = "Antrim and Newtownabbey Borough Council";

        //declaring a LinkedList of Elected Reps to store the councillors
        LinkedList<ElectedRep> councillors = new LinkedList<>();

        //try block
        try
        {
            //creating a Document copy of the webpage using Jsoup
            final Document webpage = Jsoup.connect(URL).timeout(30 * 1000).userAgent("Mozilla").get();

            Element repList = webpage.selectFirst("#form > main");

            for(Element dEA : repList.select("div.wrapper-white"))
            {
                final String DEA_NAME = dEA.select("h2.title-main.blue").text().replace("DEA Councillors","").trim();

                for(Element councillor: dEA.select("div.cell"))
                {
                    for(Element p: councillor.select("p.title"))
                    {
                        if(p.text().length()>0)
                        {
                            ElectedRep tempCouncillor = new ElectedRep();

                            tempCouncillor.setElectedBody(COUNCIL);
                            tempCouncillor.setElectoralArea(DEA_NAME);

                            tempCouncillor.setParty(councillor.select("span").text());

                            String[] splitNameAndTitle = councillor.select("p.title").text().replace(tempCouncillor.getParty(),"").trim().split(" ");

                            LinkedList<String> listNameAndTitle = new LinkedList<>();

                            Collections.addAll(listNameAndTitle,splitNameAndTitle);

                            while(!(listNameAndTitle.get(listNameAndTitle.size()-1).length() > 3))
                            {
                                tempCouncillor.setPostNominal(tempCouncillor.getPostNominal() + " " + listNameAndTitle.get(listNameAndTitle.size()-1));

                                listNameAndTitle.remove(listNameAndTitle.get(listNameAndTitle.size()-1));
                            }//if

                            tempCouncillor.setTitle(listNameAndTitle.get(0));

                            listNameAndTitle.remove(tempCouncillor.getTitle());

                            tempCouncillor.setOfficialSurname(listNameAndTitle.get(listNameAndTitle.size()-1));

                            listNameAndTitle.remove(tempCouncillor.getOfficialSurname());

                            if(listNameAndTitle.size() > 1)
                            {
                                String tempForename = "";

                                for (String s : listNameAndTitle)
                                {
                                    tempForename += " " + s;
                                }//for

                                tempForename = tempForename.trim();

                                tempCouncillor.setOfficialForename(tempForename);
                            }//if
                            else
                            {
                                tempCouncillor.setOfficialForename(listNameAndTitle.get(0));
                            }//else

                            tempCouncillor.setUsualForename(tempCouncillor.getOfficialForename());

                            for(Element address : councillor.select("p:nth-child(3)"))
                            {
                                address1FromCSSSelectedParagraph(webpage,address.cssSelector(),tempCouncillor);
                            }//for

                            tempCouncillor.setPhone1(councillor.select("strong:matchesOwn(Tel:)").next().text().replace(" ",""));

                            tempCouncillor.setEmail1(councillor.select("a.btn-outline").attr("href").replace("mailto:",""));

                            tempCouncillor.removeCommasFromAddress1();

                            councillors.add(tempCouncillor);
                        }//if
                    }//for
                }//for
            }//for

            for(Element dEA : repList.select("div.wrapper-grey"))
            {
                final String DEA_NAME = dEA.select("h2.title-main.blue").text().replace("DEA Councillors","").trim();

                for(Element councillor: dEA.select("div.cell"))
                {
                    for(Element p: councillor.select("p.title"))
                    {
                        if(p.text().length()>0)
                        {
                            ElectedRep tempCouncillor = new ElectedRep();

                            tempCouncillor.setElectedBody(COUNCIL);
                            tempCouncillor.setElectoralArea(DEA_NAME);

                            tempCouncillor.setParty(councillor.select("span").text());

                            String[] splitNameAndTitle = councillor.select("p.title").text().replace(tempCouncillor.getParty(),"").trim().split(" ");

                            LinkedList<String> listNameAndTitle = new LinkedList<>();

                            Collections.addAll(listNameAndTitle,splitNameAndTitle);

                            while(!(listNameAndTitle.get(listNameAndTitle.size()-1).length() > 3))
                            {
                                tempCouncillor.setPostNominal(tempCouncillor.getPostNominal() + " " + listNameAndTitle.get(listNameAndTitle.size()-1));

                                listNameAndTitle.remove(listNameAndTitle.get(listNameAndTitle.size()-1));
                            }//if

                            tempCouncillor.setPostNominal(tempCouncillor.getPostNominal().trim());

                            tempCouncillor.setTitle(listNameAndTitle.get(0));

                            listNameAndTitle.remove(tempCouncillor.getTitle());

                            tempCouncillor.setOfficialSurname(listNameAndTitle.get(listNameAndTitle.size()-1));

                            listNameAndTitle.remove(tempCouncillor.getOfficialSurname());

                            if(listNameAndTitle.size() > 1)
                            {
                                String tempForename = "";

                                for (String s : listNameAndTitle)
                                {
                                    tempForename += " " + s;
                                }//for

                                tempForename = tempForename.trim();

                                tempCouncillor.setOfficialForename(tempForename);
                            }//if
                            else
                            {
                                tempCouncillor.setOfficialForename(listNameAndTitle.get(0));
                            }//else

                            tempCouncillor.setUsualForename(tempCouncillor.getOfficialForename());

                            for(Element address : councillor.select("p:nth-child(3)"))
                            {
                                address1FromCSSSelectedParagraph(webpage,address.cssSelector(),tempCouncillor);
                            }//for

                            tempCouncillor.setPhone1(councillor.select("strong:matchesOwn(Tel:)").next().text().replace(" ",""));

                            tempCouncillor.setEmail1(councillor.select("a.btn-outline").attr("href").replace("mailto:",""));

                            tempCouncillor.removeCommasFromAddress1();

                            councillors.add(tempCouncillor);
                        }//if
                    }//for
                }//for
            }//for
        }//try
        //catch block
        catch (Exception e)
        {
            //print exception
            System.out.println(e);
        }//catch

        //return membersOfParliament LinkedList
        return councillors;
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
}//class
