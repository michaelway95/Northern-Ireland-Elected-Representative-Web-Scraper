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
                //set postNominal of tempCouncillor to the last valye in listNameAndTitle
                tempCouncillor.setPostNominal(tempCouncillor.getPostNominal() + " " + listNameAndTitle
                      .get(listNameAndTitle.size()-1));

                //removing last value from listNameAndTitle
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

            //declaring Element councillor and selecting division of page with all contact details
            Element councillor = webpage.selectFirst("div.td-block-span6");

            //declaring altTitle elements on CSS selector h5 within councillor Element
            Elements altTitle = councillor.select("h5");

            //if altTitle elements is not empty
            if(!altTitle.isEmpty())
            {
                //set altTitle of tempCouncillor to text associated with CSS selector "strong"
                tempCouncillor.setAltTitle(altTitle.select("strong").text());
            }//if

            //setParty to the text associated with first html anchor in councillor Element
            tempCouncillor.setParty(Objects.requireNonNull(Objects.requireNonNull(councillor)
                  .selectFirst("a")).text());

            //declaring String dEAURL on second sibling of html map marker icon to String
            String dEAURL = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(councillor
                  .selectFirst("i.fa.fa-map-marker")).nextSibling()).nextSibling()).toString();

            //set tempCouncillor to the text contained within the first ">" and the second "<"
            tempCouncillor.setElectoralArea(dEAURL.substring(dEAURL.indexOf(">")+1)
                  .replace("</a>",""));

            /*
            email of councillor is encrypted. Creating pseudo email address using the url and multiple replace calls
             */
            tempCouncillor.setEmail1(url
                  .replace("https://www.armaghbanbridgecraigavon.gov.uk/councillors/","")
                  .replace("councillor-","").replace("alderman-","")
                  .replace("-",".").replace("/","")
                  + "@armaghbanbridgecraigavon.gov.uk");

            //declaring Elements phone for instances of html phone icon within Councillor element
            Elements phone = councillor.select("i.fa.fa-phone");

            //if phone is not empty
            if(!phone.isEmpty())
            {
                //set tempCouncillor phone1 to the string of the first sibling of phone replacing spaces and "Phone:"
                tempCouncillor.setPhone1(Objects.requireNonNull(Objects.requireNonNull(phone.first()).nextSibling())
                      .toString().replace("Phone:","").replace(" ",""));
            }//if

            //declaring Elements mobile for instances of html mobile phone icon within Councillor element
            Elements mobile = councillor.select("i.fa.fa-mobile-phone");

            //if mobile is not empty
            if(!mobile.isEmpty())
            {
                //if tempCouncillor phone1 is empty
                if(tempCouncillor.getPhone1().equals(""))
                {
                    //set tempCouncillor phone1 to the string of the first sibling of mobile replacing spaces and "Mobile:"
                    tempCouncillor.setPhone1(Objects.requireNonNull(Objects.requireNonNull(mobile.first()).nextSibling())
                          .toString().replace("Mobile:","").replace(" ",""));
                }//if
                //else
                else
                {
                    //set tempCouncillor phone2 to the string of the first sibling of mobile replacing spaces and "Mobile:"
                    tempCouncillor.setPhone2(Objects.requireNonNull(Objects.requireNonNull(mobile.first()).nextSibling())
                          .toString().replace("Mobile:","").replace(" ",""));
                }//else
            }//if

            //declaring Elements addressBreak for breaks within paragraphs containing "Address" in councillor
            Elements addressBreak = councillor.select("p:contains(Address)").select("br");

            //declaring Elements postcodeBreak for 3rd break within paragraphs containing "Address" in councillor
            Elements postcodeBreak = councillor.select("p:contains(Address)").select("br:nth-child(3)");

            //calling address1FromCSSSelectedParagraphWithCommas on addressBreak, postcodeBreak and tempCouncillor
            address1FromCSSSelectedParagraphWithCommas(addressBreak,postcodeBreak,tempCouncillor);
        }//try
        //catch block
        catch (Exception e)
        {
            //print exception e
            System.out.println(e);
        }//catch

        //return tempCouncillor
        return tempCouncillor;
    }//councillorFromURL
}//class
