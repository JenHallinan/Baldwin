/*----------------------------------------------------------------------------------------------*\
|                                   Class Population                                             |
|              Used to create both the parent and the child populations                          |
|                              Author: Jennifer Hallinan                                         |
|                                Commenced: 29/01/2024                                           |
|                               Last edited: 10/02/2024                                          |
\*----------------------------------------------------------------------------------------------*/

import java.util.ArrayList;
import java.util.Random;

public class Population {
    // constructor - random
    public Population
        (Random rgen, int maxHostGenes, int maxMicrobeGenes, int numGenes, int carbs,
         int protein, int fat, int penalty, int cMax, int pMax, int fMax, int amyID){
        this.thePop = new ArrayList<Host>();
        for (int i = 0; i < Baldwin.popSize; i++){
            Host h = new Host
                    (rgen, maxHostGenes, maxMicrobeGenes, numGenes, carbs, protein, fat, penalty,
                            cMax, pMax, fMax, Baldwin.hostAmyGenes, Baldwin.micAmyGenes, amyID);
            this.thePop.add(h);
        }
    }

    // constructor - blank
    public Population(){
        this.thePop = new ArrayList<Host>();
    }
    //print to stdout
    public void printPop(){
        for (int i = 0; i < this.thePop.size(); i++){
            Host h = this.thePop.get(i);
            System.out.println("Host " + i);
            h.printHost();
        }
    }

    // print statistics
    public void printStats(int protein, int fat, int carbs){
        for (int i = 0; i < this.thePop.size(); i++){
            Host h = this.thePop.get(i);
            h.printStats(protein, fat, carbs);
        }
    }

    // gets and sets
    public Host getHost(int which){
        return(thePop.get(which));
    }

    public void addHost(Host newHost){
        thePop.add(newHost);
    }

    public int getSize(){
        return this.thePop.size();
    }

    // return a female host between the ages of 1 and 7 using tournament selection
    public Host selectBreedingFemale(Random rgen, int tSize){
        double maxFitness = 0.0;
        int numChecked = 0;
        int currentHost = 0;
        while (numChecked < tSize){
            int which = rgen.nextInt(this.thePop.size());
            Host h = this.thePop.get(which);
            if (h.female){
                if (h.getAge() >= 1 && h.getAge() <= 7){
                    numChecked++;
                    if (h.getFitness() > maxFitness){
                            maxFitness = h.getFitness();
                            currentHost = which;
                    }
                }
            }
        }
        Host h = this.thePop.get(currentHost);
        return(h);
    }

    // Select a male over the age of 1
    public Host selectBreedingMale(Random rgen) {
        while (true) {
            int which = rgen.nextInt(this.thePop.size());
            Host h = this.thePop.get(which);
            if (!h.getSex()) {
                int age = h.getAge();
                if (age >= 1) {
                    return (h);
                }
            }
        }
    }

    // kill off hosts who are below the fitness cutoff
    public void cull(int cutoff){
        for (int i = 0; i < this.thePop.size(); i++){
            Host h = this.thePop.get(i);
            double nxtFit = h.getFitness();
            if (nxtFit < cutoff){
                this.thePop.remove(h);
            }
        }
    }

    // private variables
    ArrayList<Host> thePop;
}
