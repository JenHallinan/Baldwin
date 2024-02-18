/*-----------------------------------------------------------------------------------------------------*\
|                                            Class Baldwin                                              |
|                            Controls the multi-species Baldwin Effect simulation                       |
|                                      Author: Jennifer Hallinan                                        |
|                                         Commence: 21/08/21                                            |
|                                       Last edited: 10/02/24                                           |
\*-----------------------------------------------------------------------------------------------------*/

import java.util.ArrayList;
import java.util.Random;

public class Baldwin {
    // global variables
    static int RSEED = 666;                 // random number seed
    static int maxHostGenes = 10;           // maximum number of genes in the host
    static int maxMicrobeGenes = 20;        // maximum number of genes in a microbial genome
    static int popSize = 5;                 // size of the population
    static int numGenes = 100;              // maximum number of genes available in the system
    static int hostAmyGenes = 2;            // starting number of amylase genes in host
    static int micAmyGenes = 2;             // starting number of amylase genes per microbial genome
    static int carbs = 2;                   // amount of carbohydrate in the environment
    static int protein = 10;                // amount of protein in the environment
    static int fat = 10;                    // amount of fat in the environment
    static int penalty = 1;                 // penalty to obtain protein or fat (hunting)
    static int cMax = 7;                    // maximum carbohydrate that can be used
    static int pMax = 30;                   // maximum protein that can be used
    static int fMax = 63;                   // maximum fat that can be used
    static int tNum = 5;                    // number of hosts considered for tournament selection
    static int fitCutoff = 2;               // fitness below which a host dies

    public static void main(String[] args){
        Random rgen = new Random(RSEED);
        int amyID = rgen.nextInt(numGenes);
        Population parentPop = new Population(rgen,  maxHostGenes,  maxMicrobeGenes,  numGenes,
                carbs, protein, fat, penalty, cMax, pMax, fMax, amyID);
        System.out.println("Parent population:");
        parentPop.printPop();
        System.out.println("Child population:");
        Population childPop = new Population();
        childPop.printPop();

        // find fittest individual and retain
        saveMaxFittest(parentPop, childPop);
        System.out.println("Elite child population:");
                childPop.printPop();

        // Select two parents one male, one female of appropriate age
            // female: 1 to 7 years, male: 1 to death
        Host mother = parentPop.selectBreedingFemale(rgen, tNum);
        System.out.println("Mother:");
        mother.printHost();
        Host father = parentPop.selectBreedingMale(rgen);
        System.out.println("Father: ");
        father.printHost();
        //produce new offspring 1 to 12 averaging 6 https://www.akc.org/expert-advice/dog-breeding/average-litter-size/
        int litterSize = rgen.nextInt(11)+1;        // to avoid zero pups
        for (int i = 0; i < litterSize; i++){
            Host pup = new Host(rgen, amyID);
            pup = pup.breed(rgen, mother, father, amyID);
            childPop.addHost(pup);
            System.out.println("Pup " + i);
            pup.printHost();
        }

        // copy child population to main population
        parentPop = childPop;
        System.out.println("New generation");
        parentPop.printPop();

        // kill off 50% of the pop least fit; c.f. Kirkwood's work
        parentPop.cull(fitCutoff);


        // increment age
        // Kill old hosts; average lifespan is 10 to 15 years https://www.akc.org/expert-advice/health/how-long-do-dogs-live/
            // chance of death == age /15

    }

    private static void saveMaxFittest(Population parentPop, Population childPop) {
        double maxFit = 0.0;
        int whichHost = 0;
        for (int i = 0; i < parentPop.getSize(); i++){
            Host nextH = parentPop.getHost(i);
            double nxtFit = nextH.getFitness();
            if (nxtFit > maxFit){
                maxFit = nxtFit;
                whichHost = i;
            }
        }
        Host nxtHost = parentPop.getHost(whichHost);
        childPop.addHost(nxtHost);
    }
}
