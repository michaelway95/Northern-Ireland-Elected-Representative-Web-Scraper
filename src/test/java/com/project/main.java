package com.project;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * Project: NIRepsWebScrapper
 * Package: com.project
 * Filename: main
 * Created by Michael Way on 05/06/2022
 **/
public class main
{
    public static void main(String[] args)
    {
        LinkedList<ElectedRep> electedReps = new AAndNBC().getAAndNBCCouncillors();
        //electedReps.addAll(new HoCWebScraper().getNIMPs());
        //electedReps.addAll(new NIAWebScraper().getNIAMLAs());
        //electedReps.addAll(new AAndNBC().getAAndNBCCouncillors());


        for(int i = 0; i < electedReps.size(); i++)
        {
            System.out.println(electedReps.get(i).toTSVLine());
        }//for

        try (PrintWriter writer = new PrintWriter("electedReps.tsv")) {

            StringBuilder sb = new StringBuilder();

            sb.append(ElectedRep.tSVHeader());
            sb.append("\n");

            for(int i = 0; i < electedReps.size(); i++)
            {
                sb.append(electedReps.get(i).toTSVLine());
                sb.append("\n");
            }//for

            writer.write(sb.toString());
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }//main
}//main
