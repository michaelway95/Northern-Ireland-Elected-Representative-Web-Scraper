package com.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

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
    public LinkedList<ElectedRep> getCouncillors()
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

            //declaring Element repList to find the list of councillors
            Element repList = webpage.selectFirst("#form > main");

            //for loop for each white wrapper representing all the odd numbered DEAs on the site
            for(Element dEA : Objects.requireNonNull(repList).select("div.wrapper-white"))
            {
                /*
                declaring a constant string of the DEA name found by selecting the title for the wrapper minus the
                non-DEA bits of the String
                 */
                final String DEA_NAME = dEA.select("h2.title-main.blue").text().replace
                      (" DEA Councillors","").trim();

                //for each cell containing a councillor's details
                for(Element councillor: dEA.select("div.cell"))
                {
                    //for the Element with the tag p.title
                    for(Element p: councillor.select("p.title"))
                    {
                        //if p.text is not empty
                        if(p.text().length()>0)
                        {
                            //declare a temporary ElectedRep, tempCouncillor
                            ElectedRep tempCouncillor = new ElectedRep();

                            //using the setElectedBody and setElectoralArea methods with related constants as parameters
                            tempCouncillor.setElectedBody(COUNCIL);
                            tempCouncillor.setElectoralArea(DEA_NAME);

                            //calling the setParty method on the text of the span element of p
                            tempCouncillor.setParty(p.select("span").text());

                            //declaring String array for the split value of p.text minus the return of getParty
                            String[] splitNameAndTitle = p.text().replace(tempCouncillor.getParty(),"")
                                  .trim().split(" ");

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

                            //for instances of the 3rd paragraph in Councillor element
                            for(Element address : councillor.select("p:nth-child(3)"))
                            {
                                //calling address1FromCSSSelectedParagraph to set values for Address 1 variables
                                address1FromCSSSelectedParagraph(webpage,address.cssSelector(),tempCouncillor);
                            }//for

                            //calling setPhone1 method on next text after strong tag "Tel:" removing blank space
                            tempCouncillor.setPhone1(councillor.select("strong:matchesOwn(Tel:)").next().text()
                                  .replace(" ",""));

                            //calling setEmail1 on the href attribute of "a.btn-outline" removing "mailto:"
                            tempCouncillor.setEmail1(councillor.select("a.btn-outline").attr("href")
                                  .replace("mailto:",""));

                            //calling removeCommasFromAddress1 to remove commas from Address 1 variables
                            tempCouncillor.removeCommasFromAddress1();

                            //adding tempCouncillor to councillors
                            councillors.add(tempCouncillor);
                        }//if
                    }//for
                }//for
            }//for

            //for loop for each white wrapper representing all the odd numbered DEAs on the site
            for(Element dEA : repList.select("div.wrapper-grey"))
            {
                /*
                declaring a constant string of the DEA name found by selecting the title for the wrapper minus the
                non-DEA bits of the String
                 */
                final String DEA_NAME = dEA.select("h2.title-main.blue").text().replace
                      (" DEA Councillors","").trim();

                //for each cell containing a councillor's details
                for(Element councillor: dEA.select("div.cell"))
                {
                    //for the Element with the tag p.title
                    for(Element p: councillor.select("p.title"))
                    {
                        //if p.text is not empty
                        if(p.text().length()>0)
                        {
                            //declare a temporary ElectedRep, tempCouncillor
                            ElectedRep tempCouncillor = new ElectedRep();

                            //using the setElectedBody and setElectoralArea methods with related constants as parameters
                            tempCouncillor.setElectedBody(COUNCIL);
                            tempCouncillor.setElectoralArea(DEA_NAME);

                            //calling the setParty method on the text of the span element of p
                            tempCouncillor.setParty(p.select("span").text());

                            //declaring String array for the split value of p.text minus the return of getParty
                            String[] splitNameAndTitle = p.text().replace(tempCouncillor.getParty(),"")
                                  .trim().split(" ");

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

                            //for instances of the 3rd paragraph in Councillor element
                            for(Element address : councillor.select("p:nth-child(3)"))
                            {
                                //calling address1FromCSSSelectedParagraph to set values for Address 1 variables
                                address1FromCSSSelectedParagraph(webpage,address.cssSelector(),tempCouncillor);
                            }//for

                            //calling setPhone1 method on next text after strong tag "Tel:" removing blank space
                            tempCouncillor.setPhone1(councillor.select("strong:matchesOwn(Tel:)").next().text()
                                  .replace(" ",""));

                            //calling setEmail1 on the href attribute of "a.btn-outline" removing "mailto:"
                            tempCouncillor.setEmail1(councillor.select("a.btn-outline").attr("href")
                                  .replace("mailto:",""));

                            //calling removeCommasFromAddress1 to remove commas from Address 1 variables
                            tempCouncillor.removeCommasFromAddress1();

                            //adding tempCouncillor to councillors
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

        //return councillors LinkedList
        return councillors;
    }//getAAndNBCCouncillors
}//class
