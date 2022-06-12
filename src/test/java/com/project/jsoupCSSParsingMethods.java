package com.project;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Project: NIRepsWebScrapper
 * Package: com.example.nirepswebscrapper
 * Filename: NIAWebScraper
 * Created by Michael Way on 05/06/2022
 * A class containing executable methods to retrieve the details of all sitting MLAs from the Northern Ireland Assembly
 * Website and return then as a LinkedList of ElectedRep objects.
 **/
public class jsoupCSSParsingMethods
{
    //a repeatable method to return a String from a webpage and css selector fed in as formal parameters
    public static String stringFromFirstElementIfExists(Document webpage, String elementCSS)
    {
        //declaring Elements tempElements for each instance of the css selector in the webpage
        Elements tempElements = webpage.select(elementCSS);

        //if tempElements is not empty and the text of the first instance does not contain chevron tags
        if(!tempElements.isEmpty()&&!tempElements.get(0).text().contains("<"))
        {
            //return the text of the first instance of tempElements
            return tempElements.get(0).text();
        }//if
        //else
        else
        {
            //return a blank String
            return "";
        }//else
    }//stringFromFirstElementIfExists

    //a repeatable method to return a String from a webpage and css selector fed in as formal parameters
    public static String attributeFromFirstElementIfExists(Document webpage, String elementCSS)
    {
        //declaring Elements tempElements for each instance of the css selector in the webpage
        Elements tempElements = webpage.select(elementCSS);

        //if tempElements is not empty and the text of the first instance does not contain chevron tags
        if(!tempElements.isEmpty()&&!tempElements.get(0).text().contains("<"))
        {
            //return the text of the first instance of tempElements
            return Objects.requireNonNull(Objects.requireNonNull(tempElements.first()).nextElementSibling()).text();
        }//if
        //else
        else
        {
            //return a blank String
            return "";
        }//else
    }//stringFromFirstElementIfExists

    public static String attributeFromLastElementIfExists(Document webpage, String elementCSS)
    {
        //declaring Elements tempElements for each instance of the css selector in the webpage
        Elements tempElements = webpage.select(elementCSS);

        //if tempElements is not empty and the text of the first instance does not contain chevron tags
        if(!tempElements.isEmpty())
        {
            //return the text of the first instance of tempElements
            return Objects.requireNonNull(Objects.requireNonNull(tempElements.last()).nextElementSibling()).text();
        }//if
        //else
        else
        {
            //return a blank String
            return "";
        }//else
    }//stringFromFirstElementIfExists

    //a repeatable method to return a String from the last element with webpage and css selector fed in as formal parameters
    public static String stringFromLastElementIfExists(Document webpage, String elementCSS)
    {
        //declaring Elements tempElements for each instance of the css selector in the webpage
        Elements tempElements = webpage.select(elementCSS);

        //if tempElements is not empty and the text of the last instance does not equal the first instance
        if(!tempElements.isEmpty()&&!Objects.equals(tempElements.last(), tempElements.first()))
        {
            //return the text of the last instance of tempElements
            return Objects.requireNonNull(tempElements.last()).text();
        }//if
        //else
        else
        {
            //return a blank String
            return "";
        }//else
    }//stringFromFirstElementIfExists

    /*
    a repeatable method to return a String of the next sibling of an element from a webpage and css selector fed in as
    formal parameters
     */
    public static String stringFromFirstElementSiblingIfExists(Document webpage, String elementCSS)
    {
        //declaring Elements tempElements for each instance of the css selector in the webpage
        Elements tempElements = webpage.select(elementCSS);

        //if tempElements is not empty and the text of the next sibling of the first element does not contain chevron tags
        if(!tempElements.isEmpty()&&!Objects.requireNonNull(tempElements.get(0).nextSibling()).toString().contains("<"))
        {
            //return the text of the next sibling of the first element
            return Objects.requireNonNull(tempElements.get(0).nextSibling()).toString();
        }//if
        //else
        else
        {
            //return blank string
            return "";
        }//else
    }//stringFromFirstElementSiblingIfExists

    /*
    a repeatable method to return a String of the next sibling of the last instance of an element from a webpage and
    css selector fed in as formal parameters
     */
    public static String stringFromLastElementSiblingIfExists(Document webpage, String elementCSS)
    {
        //declaring Elements tempElements for each instance of the css selector in the webpage
        Elements tempElements = webpage.select(elementCSS);

        //if tempElements is not empty and the text of the next sibling of the last element does not contain chevron tags
        if(!tempElements.isEmpty()&&!Objects.requireNonNull(Objects.requireNonNull(tempElements.last()).nextSibling()).toString().contains("<"))
        {
            //return the text of the last sibling of the first element
            return Objects.requireNonNull(Objects.requireNonNull(tempElements.last()).nextSibling()).toString();
        }//if
        //else
        else
        {
            //return blank string
            return "";
        }//else
    }//stringFromLastElementSiblingIfExists

    /*
    a repeatable method to return an email String from the next sibling of an element from a webpage and css selector
    fed in as formal parameters
     */
    public static String emailFromFirstElementSiblingIfExists(Document webpage, String elementCSS)
    {
        //declaring Elements tempElements for each instance of the css selector in the webpage
        Elements tempElements = webpage.select(elementCSS);

        //if tempElements is not empty and the text of the next sibling of the first element does not contain chevron tags
        if(!tempElements.isEmpty()&&!Objects.requireNonNull(tempElements.get(0).nextSibling()).attr("href").contains("<"))
        {
            //return the text of the next sibling of the first element with "mailto:" strings replaced with blank strings
            return Objects.requireNonNull(tempElements.get(0).nextSibling()).attr("href").replace("mailto:","");
        }//if
        else
        {
            //return blank string
            return "";
        }//else
    }//emailFromFirstElementSiblingIfExists

    /*
    a repeatable method to return an email String from the next sibling of the last instance of an element from a
    webpage and css selector fed in as formal parameters
     */
    public static String emailFromLastElementSiblingIfExists(Document webpage, String elementCSS)
    {
        //declaring Elements tempElements for each instance of the css selector in the webpage
        Elements tempElements = webpage.select(elementCSS);

        /*
        if tempElements is not empty and the text of the next sibling of the last element does not contain chevron tags
        and the href attribute of the first and last instance do not match
         */
        if(!tempElements.isEmpty()&&!Objects.requireNonNull(Objects.requireNonNull(tempElements.last()).nextSibling()).attr("href").contains("<")&&
              !Objects.requireNonNull(Objects.requireNonNull(tempElements.last()).nextSibling()).attr("href").equals(Objects.requireNonNull(Objects.requireNonNull(tempElements.first()).
                    nextSibling()).attr("href")))
        {
            //return the text of the next sibling of the last element with "mailto:" strings replaced with blank strings
            return Objects.requireNonNull(Objects.requireNonNull(tempElements.last()).nextSibling()).attr("href").replace("mailto:","");
        }//if
        //else
        else
        {
            //return blank String
            return "";
        }//else
    }//emailFromLastElementSiblingIfExists

    public static void address1FromCSSSelectedParagraph(Document webpage, String elementCSS, ElectedRep tempRep)
    {
        String fullAddress = webpage.select(elementCSS).text().trim();

        Elements breaks = webpage.select(elementCSS).select("br");

        if(!breaks.isEmpty())
        {
            String[] populatedAddress = new String[]{" "," "," "," "," "," "};

            switch (breaks.size())
            {
                case 5 ->
                {
                    tempRep.setAdd1Postcode(Objects.requireNonNull(breaks.get(4).nextSibling()).toString().trim());
                    populatedAddress[4] = tempRep.getAdd1Postcode();
                    tempRep.setAdd1Line5(Objects.requireNonNull(breaks.get(3).nextSibling()).toString().trim());
                    populatedAddress[3] = tempRep.getAdd1Line5();
                    tempRep.setAdd1Line4(Objects.requireNonNull(breaks.get(2).nextSibling()).toString().trim());
                    populatedAddress[2] = tempRep.getAdd1Line4();
                    tempRep.setAdd1Line3(Objects.requireNonNull(breaks.get(1).nextSibling()).toString().trim());
                    populatedAddress[1] = tempRep.getAdd1Line3();
                    tempRep.setAdd1Line2(Objects.requireNonNull(breaks.get(0).nextSibling()).toString().trim());
                    populatedAddress[0] = tempRep.getAdd1Line2();
                    for (String address : populatedAddress)
                    {
                        if (!address.equals(" "))
                        {
                            fullAddress = fullAddress.trim().replace(address, "");
                            fullAddress = fullAddress.trim();
                        }//if
                    }//for
                    tempRep.setAdd1Line1(fullAddress.trim());
                }//case 5
                case 4 ->
                {
                    tempRep.setAdd1Postcode(Objects.requireNonNull(breaks.get(3).nextSibling()).toString().trim());
                    populatedAddress[3] = tempRep.getAdd1Postcode();
                    tempRep.setAdd1Line4(Objects.requireNonNull(breaks.get(2).nextSibling()).toString().trim());
                    populatedAddress[2] = tempRep.getAdd1Line4();
                    tempRep.setAdd1Line3(Objects.requireNonNull(breaks.get(1).nextSibling()).toString().trim());
                    populatedAddress[1] = tempRep.getAdd1Line3();
                    tempRep.setAdd1Line2(Objects.requireNonNull(breaks.get(0).nextSibling()).toString().trim());
                    populatedAddress[0] = tempRep.getAdd1Line2();
                    for (String address : populatedAddress)
                    {
                        if (!address.equals(" "))
                        {
                            fullAddress = fullAddress.trim().replace(address, "");
                            fullAddress = fullAddress.trim();
                        }//if
                    }//for
                    tempRep.setAdd1Line1(fullAddress.trim());
                }//case 4
                case 3 ->
                {
                    tempRep.setAdd1Postcode(Objects.requireNonNull(breaks.get(2).nextSibling()).toString().trim());
                    populatedAddress[2] = tempRep.getAdd1Postcode();
                    tempRep.setAdd1Line3(Objects.requireNonNull(breaks.get(1).nextSibling()).toString().trim());
                    populatedAddress[1] = tempRep.getAdd1Line3();
                    tempRep.setAdd1Line2(Objects.requireNonNull(breaks.get(0).nextSibling()).toString().trim());
                    populatedAddress[0] = tempRep.getAdd1Line2();
                    for (String address : populatedAddress)
                    {
                        if (!address.equals(" "))
                        {
                            fullAddress = fullAddress.trim().replace(address, "");
                            fullAddress = fullAddress.trim();
                        }//if
                    }//for
                    tempRep.setAdd1Line1(fullAddress.trim());
                }//case 3
                case 2 ->
                {
                    tempRep.setAdd1Postcode(Objects.requireNonNull(breaks.get(1).nextSibling()).toString().trim());
                    populatedAddress[1] = tempRep.getAdd1Postcode();
                    tempRep.setAdd1Line2(Objects.requireNonNull(breaks.get(0).nextSibling()).toString().trim());
                    populatedAddress[0] = tempRep.getAdd1Line2();
                    for (String address : populatedAddress)
                    {
                        if (!address.equals(" "))
                        {
                            fullAddress = fullAddress.trim().replace(address, "");
                            fullAddress = fullAddress.trim();
                        }//if
                    }//for
                    tempRep.setAdd1Line1(fullAddress.trim());
                }//case 2
                default ->
                {
                }//default
            }//switch
        }//if

    }//addressFromCSSSelectedParagraph

    public static void address2FromCSSSelectedParagraph(Document webpage, String elementCSS, ElectedRep tempRep)
    {
        String fullAddress = webpage.select(elementCSS).text().trim();

        Elements breaks = webpage.select(elementCSS).select("br");

        if(!breaks.isEmpty())
        {
            String[] populatedAddress = new String[]{" "," "," "," "," "," "};

            switch (breaks.size())
            {
                case 5 ->
                {
                    tempRep.setAdd2Postcode(Objects.requireNonNull(breaks.get(4).nextSibling()).toString().trim());
                    populatedAddress[4] = tempRep.getAdd2Postcode();
                    tempRep.setAdd2Line5(Objects.requireNonNull(breaks.get(3).nextSibling()).toString().trim());
                    populatedAddress[3] = tempRep.getAdd2Line5();
                    tempRep.setAdd2Line4(Objects.requireNonNull(breaks.get(2).nextSibling()).toString().trim());
                    populatedAddress[2] = tempRep.getAdd2Line4();
                    tempRep.setAdd2Line3(Objects.requireNonNull(breaks.get(1).nextSibling()).toString().trim());
                    populatedAddress[1] = tempRep.getAdd2Line3();
                    tempRep.setAdd2Line2(Objects.requireNonNull(breaks.get(0).nextSibling()).toString().trim());
                    populatedAddress[0] = tempRep.getAdd2Line2();
                    for (String address : populatedAddress)
                    {
                        if (!address.equals(" "))
                        {
                            fullAddress = fullAddress.trim().replace(address, "");
                            fullAddress = fullAddress.trim();
                        }//if
                    }//for
                    tempRep.setAdd2Line1(fullAddress.trim());
                }
                case 4 ->
                {
                    tempRep.setAdd2Postcode(Objects.requireNonNull(breaks.get(3).nextSibling()).toString().trim());
                    populatedAddress[3] = tempRep.getAdd2Postcode();
                    tempRep.setAdd2Line4(Objects.requireNonNull(breaks.get(2).nextSibling()).toString().trim());
                    populatedAddress[2] = tempRep.getAdd2Line4();
                    tempRep.setAdd2Line3(Objects.requireNonNull(breaks.get(1).nextSibling()).toString().trim());
                    populatedAddress[1] = tempRep.getAdd2Line3();
                    tempRep.setAdd2Line2(Objects.requireNonNull(breaks.get(0).nextSibling()).toString().trim());
                    populatedAddress[0] = tempRep.getAdd2Line2();
                    for (String address : populatedAddress)
                    {
                        if (!address.equals(" "))
                        {
                            fullAddress = fullAddress.trim().replace(address, "");
                            fullAddress = fullAddress.trim();
                        }//if
                    }//for
                    tempRep.setAdd2Line1(fullAddress.trim());
                }
                case 3 ->
                {
                    tempRep.setAdd2Postcode(Objects.requireNonNull(breaks.get(2).nextSibling()).toString().trim());
                    populatedAddress[2] = tempRep.getAdd2Postcode();
                    tempRep.setAdd2Line3(Objects.requireNonNull(breaks.get(1).nextSibling()).toString().trim());
                    populatedAddress[1] = tempRep.getAdd2Line3();
                    tempRep.setAdd2Line2(Objects.requireNonNull(breaks.get(0).nextSibling()).toString().trim());
                    populatedAddress[0] = tempRep.getAdd2Line2();
                    for (String address : populatedAddress)
                    {
                        if (!address.equals(" "))
                        {
                            fullAddress = fullAddress.trim().replace(address, "");
                            fullAddress = fullAddress.trim();
                        }//if
                    }//for
                    tempRep.setAdd2Line1(fullAddress.trim());
                }
                case 2 ->
                {
                    tempRep.setAdd2Postcode(Objects.requireNonNull(breaks.get(1).nextSibling()).toString().trim());
                    populatedAddress[1] = tempRep.getAdd2Postcode();
                    tempRep.setAdd2Line2(Objects.requireNonNull(breaks.get(0).nextSibling()).toString().trim());
                    populatedAddress[0] = tempRep.getAdd2Line2();
                    for (String address : populatedAddress)
                    {
                        if (!address.equals(" "))
                        {
                            fullAddress = fullAddress.trim().replace(address, "");
                            fullAddress = fullAddress.trim();
                        }//if
                    }//for
                    tempRep.setAdd2Line1(fullAddress.trim());
                }
                default ->
                {
                }
            }//switch
        }//if

    }//addressFromCSSSelectedParagraph

    public static void address1FromCSSSelectedParagraphWithCommas(Elements address, Elements postcode, ElectedRep tempRep)
    {
        String[] splitAddress = address.first().nextSibling().toString().trim().split(", ");

        LinkedList<String> listAddress = new LinkedList<>();

        Collections.addAll(listAddress,splitAddress);

        if(!postcode.isEmpty())
        {
            listAddress.add(postcode.first().nextSibling().toString());
        }//if

        switch (listAddress.size())
        {
            case 6 ->
            {
                tempRep.setAdd1Line1(listAddress.get(0));
                tempRep.setAdd1Line2(listAddress.get(1));
                tempRep.setAdd1Line3(listAddress.get(2));
                tempRep.setAdd1Line4(listAddress.get(3));
                tempRep.setAdd1Line5(listAddress.get(4));
                tempRep.setAdd1Postcode(listAddress.get(5));
            }//case 6
            case 5 ->
            {
                tempRep.setAdd1Line1(listAddress.get(0));
                tempRep.setAdd1Line2(listAddress.get(1));
                tempRep.setAdd1Line3(listAddress.get(2));
                tempRep.setAdd1Line4(listAddress.get(3));
                tempRep.setAdd1Postcode(listAddress.get(4));
            }//case 5
            case 4 ->
            {
                tempRep.setAdd1Line1(listAddress.get(0));
                tempRep.setAdd1Line2(listAddress.get(1));
                tempRep.setAdd1Line3(listAddress.get(2));
                tempRep.setAdd1Postcode(listAddress.get(3));
            }//case 4
            case 3 ->
            {
                tempRep.setAdd1Line1(listAddress.get(0));
                tempRep.setAdd1Line2(listAddress.get(1));
                tempRep.setAdd1Postcode(listAddress.get(2));
            }//case 3
            case 2 ->
            {
                tempRep.setAdd1Line1(listAddress.get(0));

                if(listAddress.get(1).contains("BT"))
                {
                    tempRep.setAdd1Postcode(listAddress.get(1));
                }//if
                else
                {
                    tempRep.setAdd1Line2(listAddress.get(1));
                }//else
            }//case 2
            case 1 ->
            {
                tempRep.setAdd1Postcode(listAddress.get(0).substring(listAddress.get(0).indexOf("BT")));
                String tempAddress1 = listAddress.get(0).replace(tempRep.getAdd1Postcode(),"").trim();
                String tempAddress2 = tempAddress1.substring(tempAddress1.indexOf("Room"))+2;
                tempAddress1.replace(tempAddress2,"");
                tempRep.setAdd1Line1(tempAddress1);
            }//case 1
            default ->
            {

            }//default
        }//switch
    }//addressFromCSSSelectedParagraph
}
