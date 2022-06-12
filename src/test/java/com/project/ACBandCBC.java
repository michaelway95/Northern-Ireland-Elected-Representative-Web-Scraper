package com.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

import static com.project.jsoupCSSParsingMethods.address1FromCSSSelectedParagraphWithCommas;

/**
 * Project: NIRepsWebScrapper
 * Package: com.project
 * Filename: HoCWebScraper
 * Created by Michael Way on 11/06/2022
 * A class containing executable methods to retrieve the details of all sitting Councillors from Armagh City, Banbridge
 * & Craigavon Borough Council and return then as a LinkedList of ElectedRep objects.
 **/
public class ACBandCBC
{
    /*
    a repeatable method to return a LinkedList of ElectedReps of Councillors of Armagh City, Banbridge & Craigavon
    Borough Council
     */
    public LinkedList<ElectedRep> getCouncillors()
    {
        //declaring a static constant String to hold the URL of the councillor section of the council website
        final String[] URLs = new String[]{"https://www.armaghbanbridgecraigavon.gov.uk/councillors/",
              "https://www.armaghbanbridgecraigavon.gov.uk/councillors/page/2/",
              "https://www.armaghbanbridgecraigavon.gov.uk/councillors/page/3/"};

        //declaring a LinkedList of Strings to store the councillors' URLs
        LinkedList<String> councillorURLs = new LinkedList<>();

        //declaring a LinkedList of Elected Reps to store the councillors
        LinkedList<ElectedRep> councillors = new LinkedList<>();

        //for each String in URLS
        for(String s: URLs)
        {
            //try block
            try
            {
                //creating a Document copy of the webpage using Jsoup
                final Document webpage = Jsoup.connect(s).timeout(30*1000).userAgent("Mozilla").get();

                //declaring Element repList to find the list of councillors
                Element repList = webpage.selectFirst("div.td-pb-span8.td-main-content");

                //for each councillor found
                for(Element councillor: Objects.requireNonNull(repList).select("div.council-wrap-inner"))
                {
                    //for first instance of "a" Element for each councillor, return "href" attribute to their page
                    councillorURLs.add(Objects.requireNonNull(Objects.requireNonNull(councillor
                          .selectFirst("a")).attr("href")));
                }//for
            }//try
            //catch block
            catch (Exception e)
            {
                //print exception e
                System.out.println(e);
            }//catch
        }//for

        //for number of Strings in councillorURLs
        for(String s: councillorURLs)
        {
            //call councillorFromURL method on s and add to councillors LinkedList
            councillors.add(councillorFromURL(s));
        }//for

        //return councillors LinkedList
        return councillors;
    }//getAAndNBCCouncillors

    //repeatable method to return an ElectedRep object from their personal page feed in as a String formal parameter
    private ElectedRep councillorFromURL(String url)
    {
        //declaring a temporary ElectedRep tempCouncillor
        ElectedRep tempCouncillor = new ElectedRep();

        //declaring constant String to represent the Council
        final String COUNCIL = "Armagh City, Banbridge & Craigavon Borough Council";

        //try block
        try
        {
            //creating a Document copy of the webpage using Jsoup
            final Document webpage = Jsoup.connect(url).timeout(30*1000).userAgent("Mozilla").get();

            //calling setElectedBody on COUNCIL
            tempCouncillor.setElectedBody(COUNCIL);

            //declaring String array for the split value of their title and name
            String[] splitNameAndTitle = webpage.select("h1.entry-title.td-page-title").text().split(" ");

            //declaring a LinkedList of Strings to hold the name and title
            LinkedList<String> listNameAndTitle = new LinkedList<>();

            //calling Collections.addAll to add all values from splitNameAndTitle to listNameAndTitle
            Collections.addAll(listNameAndTitle,splitNameAndTitle);

            //temporary solution for postNominal - replace with list of common postNominals
            while(!(listNameAndTitle.get(listNameAndTitle.size()-1).length() > 3))
            {
                tempCouncillor.setPostNominal(tempCouncillor.getPostNominal() + " " + listNameAndTitle
                      .get(listNameAndTitle.size()-1));

                listNameAndTitle.remove(listNameAndTitle.get(listNameAndTitle.size()-1));
            }//if

            //calling setTitle on the first index position of listNameAndTitle
            tempCouncillor.setTitle(listNameAndTitle.get(0));

            //calling getTitle to remove title from listNameAndTitle
            listNameAndTitle.remove(tempCouncillor.getTitle());

            //calling setOfficialSurname on the last index position of listNameAndTitle
            tempCouncillor.setOfficialSurname(listNameAndTitle.get(listNameAndTitle.size()-1));

            //calling getOfficialSurname to remove the surname from listNameAndTitle
            listNameAndTitle.remove(tempCouncillor.getOfficialSurname());

            //if listNameAndTitle is greater than 1
            if(listNameAndTitle.size() > 1)
            {
                //declaring StringBuild tempForename
                StringBuilder tempForename = new StringBuilder();

                //appending value in first index position of listNameAndTitle to tempForename
                tempForename.append(listNameAndTitle.get(0));

                //removing tempForename from listNameAndTitle
                listNameAndTitle.remove(tempForename.toString());

                //for the number of values remaining in listNameAndTitle
                for(String s: listNameAndTitle)
                {
                    //append a space and the value of s to tempForename
                    tempForename.append(" ").append(s);
                }//for

                //calling setOfficialForename on tempForename toString
                tempCouncillor.setOfficialForename(tempForename.toString());
            }//if
            //else
            else
            {
                //calling setOfficialForename on first index position of listNameAndTitle
                tempCouncillor.setOfficialForename(listNameAndTitle.get(0));
            }//else

            //calling setUsualForename on return of getOfficialForename method
            tempCouncillor.setUsualForename(tempCouncillor.getOfficialForename());

            Element councillor = webpage.selectFirst("div.td-block-span6");

            Elements altTitle = councillor.select("h5");

            if(!altTitle.isEmpty())
            {
                tempCouncillor.setAltTitle(altTitle.select("strong").text());
            }//if

            tempCouncillor.setParty(Objects.requireNonNull(Objects.requireNonNull(councillor)
                  .selectFirst("a")).text());

            String dEAURL = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(councillor
                  .selectFirst("i.fa.fa-map-marker")).nextSibling()).nextSibling()).toString();

            tempCouncillor.setElectoralArea(dEAURL.substring(dEAURL.indexOf(">")+1)
                  .replace("</a>",""));

            tempCouncillor.setEmail1(councillor.selectFirst("a.mailto-link").selectFirst("span")
                  .ownText());

            Elements phone = councillor.select("i.fa.fa-phone");

            if(!phone.isEmpty())
            {
                tempCouncillor.setPhone1(Objects.requireNonNull(Objects.requireNonNull(phone.first()).nextSibling())
                      .toString().replace("Phone:","").replace(" ",""));
            }//if

            Elements mobile = councillor.select("i.fa.fa-mobile-phone");

            if(!mobile.isEmpty())
            {
                tempCouncillor.setPhone1(Objects.requireNonNull(Objects.requireNonNull(mobile.first()).nextSibling())
                      .toString().replace("Mobile:","").replace(" ",""));
            }//if

            Elements addressBreak = councillor.select("p:contains(Address)").select("br");

            Elements postcodeBreak = councillor.select("p:contains(Address)").select("br:nth-child(3)");

            address1FromCSSSelectedParagraphWithCommas(addressBreak,postcodeBreak,tempCouncillor);
        }//try
        catch (Exception e)
        {
            System.out.println(e);
        }//catch

        return tempCouncillor;
    }//councillorFromURL
}//class
