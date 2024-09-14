import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/*--------------------------------------------------------------------------------------------------------------*\
|                                                    Class Host                                                  |
|                             The host has its own genome plus an array of microbes                              |
|                                             Author: Jennifer Hallinan                                          |
|                                               Commenced: 08/03/2022                                            |
|                                              Last edited: 11/09/2024                                           |
\*--------------------------------------------------------------------------------------------------------------*/
public class Host {
    // global variables
    static int RSEED = 666;                 // random number seed
    static int maxHostGenes = 10;           // maximum number of genes in the host
    static int maxMicrobeGenes = 20;        // maximum number of genes in a microbial genome
    static int popSize = 10;                // size of the population
    static int numGenes = 5;                // maximum number of genes available in the system
    static int hostAmyGenes = 2;            // starting number of amylase genes in host
    static int micAmyGenes = 2;             // starting number of amylase genes per microbial genome
    static int carbs = 2;                   // amount of carbohydrate in the environment
    static int protein = 10;                // amount of protein in the environment
    static int fat = 10;                    // amount of fat in the environment
    static int penalty = 1;                 // penalty to obtain protein or fat (hunting)
    static int cMax = 7;                    // maximum carbohydrate that can be used
    static int pMax = 30;                   // maximum protein that can be used
    static int fMax = 63;                   // maximum fat that can be used

    // unit testing
    public static void main(String[] args){
        Random rgen = new Random(RSEED);
        int amyID = 1;
        Host h = new Host(rgen, maxHostGenes, maxMicrobeGenes, numGenes, carbs, protein, fat, penalty, cMax, pMax, fMax, hostAmyGenes, micAmyGenes, amyID);
        System.out.println("Original host");
        h.printHost();
        h.printStats(10, 10, 2);
    }

    // constructor
    public Host(Random rgen, int maxHostGenes, int maxMicrobeGenes, int numGenes, int carbs, int protein, int fat, int penalty, int cMax, int pMax, int fMax, int hostAmyGenes, int micAmyGenes, int amyID){      // host genome
        this.hostGenome = new ArrayList<Integer>();
        // Decide how many genes this host has
        int hostGenes = rgen.nextInt(maxHostGenes);
        this.AGID = amyID;

        this.hostAmyCount = hostAmyGenes;
        for (int i = 0; i < hostAmyGenes; i++){
            this.hostGenome.add(this.AGID);
        }

        for (int i = hostAmyGenes; i < hostGenes; i++){
            int nxtGene = rgen.nextInt(numGenes);
            this.hostGenome.add(nxtGene);
            }

        this.hostAge = rgen.nextInt(15);
        this.female = rgen.nextBoolean();

        // microbiome
        int numMicrobeGenes = rgen.nextInt(maxMicrobeGenes);
        this.microbeGenome = new ArrayList<Integer>();

        this.micAmyCount = micAmyGenes;
        for (int i = 0; i < micAmyGenes; i++){
            this.microbeGenome.add(this.AGID);
        }

        for (int i = micAmyGenes; i < numMicrobeGenes; i++){
            int m = rgen.nextInt(numGenes);
            this.microbeGenome.add(m);
        }

        // this.energy = rgen.nextDouble();
        this.energy = 0.5;

        // random stats
        this.fitness = calcFitness(rgen, fat, protein, carbs, fMax, pMax, cMax);
        this.hostAmyCount = countAmy(this.hostGenome);
        this.micAmyCount = countAmy(this.microbeGenome);
    }

    // constructor for new pup
    public Host(Random rgen, int amyID){
        this.hostGenome = new ArrayList<Integer>();
        this.microbeGenome = new ArrayList<Integer>();
        this.hostAge = 0;
        this.female = rgen.nextBoolean();
        this.energy = rgen.nextDouble();
    }

    // print to stdout
    public void printHost(){
        System.out.print("Host genome: ");
        for (int i = 0; i < this.hostGenome.size(); i++){
            System.out.print(this.hostGenome.get(i) + ", ");
        }
        System.out.println();
        System.out.println("Microbes: ");
        for (int i = 0; i < this.microbeGenome.size(); i++){
            System.out.print(this.microbeGenome.get(i) + ", ");
        }
        System.out.println();
        if (this.female){
            System.out.println(" Sex: Female");
        }
        else{
            System.out.println("Sex: Male");
        }
        System.out.println("Age: " + this.hostAge);
        System.out.println("Host amylase genes: " + this.hostAmyCount);
        System.out.println("Microbial amylase genes: " + this.micAmyCount);
        String formattedNumber = String.format("%.4f", fitness);
        System.out.println("fitness: " + formattedNumber);
        formattedNumber = String.format("%.2f", energy);
        System.out.println("energy: " + formattedNumber);
        System.out.println();
    }

    public double calcFitness(Random rgen, int fat, int protein, int carbs, int fMax, int pMax, int cMax){
        // proportion of energy which can be filled by protein and fat
        double propFP = ((double)fat + (double)protein) / ((double)pMax + (double)fMax);
        // String formattedNumber = String.format("%.4f", propFP);
        // System.out.println("PropFP is " + formattedNumber);
        // penalty for hunting
        double penalty = 1.0 - this.energy;
        // formattedNumber = String.format("%.4f", penalty);
        // System.out.println("penalty is " + formattedNumber);
        // proportion of energy which can be filled by carbs - depends on number of amy genes
        // magic number 30 is max recorded in dogs
        double propCarb = (double)this.hostAmyCount / 30.0 + (double)this.micAmyCount;
        // formattedNumber = String.format("%.4f", propCarb);
        // System.out.println("propCarb is " + formattedNumber);
        double fitness = propFP + propCarb - penalty;
        String formattedNumber = String.format("%.2f", fitness);
        System.out.println("fitness is " + formattedNumber);
        return(fitness);
    }

    public void printStats(int protein, int fat, int carbs){
        System.out.println("Protein: " + protein + "\t" + "Fat: " + fat + "\t" + "Carbs: " + carbs + "\t" + "Fitness: " + this.fitness);
    }

    // gets and sets
    public ArrayList<Integer> getHostGenome(){
        return (this.hostGenome);
    }

    public ArrayList<Integer> getMicrobiolGenome(){
        return(this.microbeGenome);
    }

    public int getHostAmyGenes(){
        return(this.hostAmyGenes);
    }

    public int getMicAmyGenes(){
        return(this.micAmyGenes);
    }

    public void setHostAmyGenes(int what){
        this.hostAmyGenes = what;
    }

    public double getFitness(){
        return (this.fitness);
    }
    public void setFitness(double fit){
        this.fitness = fit;
    }

    public Boolean getSex(){
        return(this.female);
    }
    public void setSex(Boolean newSex){
        this.female = newSex;
    }

    public int getAge(){
        return(this.hostAge);
    }
    public void setAge(int newAge){
        this.hostAge = newAge;
    }

    private int countAmy(ArrayList<Integer> genome){
        int agCount = 0;
        for (int i = 0; i < genome.size(); i++ ){
            int next = genome.get(i);
            if (next == AGID){
                agCount++;
            }
        }
        return (agCount);
    }

    public void incrementAge(){
        this.hostAge++;
    }

    // create a new child individual from two parents
    public Host breed(Random rgen, Host mother, Host father, int amyID){
        Host pup = new Host(rgen, amyID);
        // set up host genome
        ArrayList<Integer> mGenome = new ArrayList<Integer>();
        mGenome = mother.getHostGenome();
        ArrayList<Integer> pGenome = new ArrayList<Integer>();
        pGenome = father.getHostGenome();
        int where = 0;                                          // where to crossover
        int smallest = 0;                                       // which genome is smallest?
        if (mGenome.size() > pGenome.size()){
            where = rgen.nextInt(pGenome.size());            //genomes may be of different lengths
            smallest = pGenome.size();
        }
        else{
            where = rgen.nextInt(mGenome.size());
            smallest = mGenome.size();
        }
        for (int i = 0; i < where; i++){                        // first part of the genome comes from the mother
            int nxt = mGenome.get(i);
            pup.hostGenome.add(nxt);
        }
        for (int i = where; i < smallest; i++){
            int nxt = pGenome.get(i);
            pup.hostGenome.add(nxt);
        }

        int count = 0;
        for (int i = 0; i < pup.hostGenome.size(); i++){
            int nxt = pup.hostGenome.get(i);
            if (nxt == amyID) {
                count++;
            }
        }
        System.out.println("Host amylase genes: " + count);
        pup.hostAmyCount = count;

        // set up microbial genome
        ArrayList<Integer> micGenome = mother.getMicrobiolGenome();        // pup inherits all microbes from the mother
        for (int i = 0; i < micGenome.size(); i++){
            int nxt = micGenome.get(i);
            pup.microbeGenome.add(nxt);
        }

        count = 0;
        for (int i = 0; i < pup.microbeGenome.size(); i++){
            int nxt = pup.microbeGenome.get(i);
            if (nxt == amyID) {
                count++;
            }
        }
        System.out.println("Microbial amylase genes: " + count);
        pup.micAmyCount = count;
        double pFit = this.calcFitness(rgen, fat, protein, carbs, fMax,pMax, cMax);
        pup.fitness = pFit;

        //set other parameters
        Boolean f = rgen.nextBoolean();
        if (f){
            pup.setSex(true);
        }
        else{
            pup.setSex(false);
        }
        pup.setAge(0);
        pup.fitness =  pup.calcFitness(rgen, fat, protein, carbs, fMax, pMax, cMax);

        System.out.println("-------------------------------------------------------");
        return(pup);
    }

    public int countHostAmy(int amyID) {
        int numAmy = 0;
        for (int i = 0; i < this.hostGenome.size(); i++){
            int h = this.hostGenome.get(i);
            if (h == amyID){
                numAmy++;
            }
            this.setHostAmyGenes(numAmy);
        }
        return(numAmy);
    }

    public int countMicAmy(int amyID) {
        int numAmy = 0;
        for (int i = 0; i < this.microbeGenome.size(); i++){
            int h = this.microbeGenome.get(i);
            if (h == amyID){
                numAmy++;
            }
        }
        return(numAmy);
    }
    // private variables
    ArrayList<Integer> hostGenome;          // the genome of the host
    ArrayList<Integer> microbeGenome;       // the gut microbiome
    double fitness;                         // the fitness of this individual
    int AGID;                               // amylase gene ID
    int hostAmyCount;                       // number of amylase genes in the host
    int micAmyCount;                        // number of amylase genes in the microbiome
    int hostAge;                            // age of host
    Boolean female;                         // Hosts are either male or female; females can breed between 1 and 7 years of age
    double energy;                          // each host is born with a certain amount of energy
}
