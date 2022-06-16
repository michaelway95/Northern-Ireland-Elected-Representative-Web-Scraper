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
        LinkedList<ElectedRep> electedReps = new LinkedList<>();

        electedReps.addAll(new HoC().getMPs());

        electedReps.addAll(new NIA().getMLAs());

        electedReps.addAll(new AAndNBC().getCouncillors());
        electedReps.addAll(new ACBandCBC().getCouncillors());

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
