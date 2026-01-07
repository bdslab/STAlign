/**
 * STAlign - Structural Tree Alignment
 * 
 * Copyright (C) 2022 BioShape and Data Science Lab at the University of Camerino, Italy - 
 * http://www.emanuelamerelli.eu/bigdata/
 *  
 * This file is part of STAlign.
 * 
 * STAlign is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * STAlign is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ASPRAlign. If not, see <http://www.gnu.org/licenses/>.
 */
package it.unicam.cs.bdslab.stalign;

import org.biojava.nbio.structure.*;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.GroupType;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.contact.Pair;
import org.biojava.nbio.structure.secstruc.SecStrucCalc;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Representation of an RNA/Protein structure, including its secondary structure and the methods to extract a bond list from its tertiary structure
 *
 * @author Filippo Lampa, Marco Serenelli
 */
public class TertiaryStructure {

    private final Structure structure;
    private double threshold; //Value between 4.5 and 12 ångström
    private SecStrucCalc secondaryStructure;
    private ArrayList<Pair<Integer>> bondList;
    private boolean[][] contactMatrix;
    private double[][] distanceMatrix;
    private String distanceMatrixCalculationMethod;
    private String sequence;
    private List<Chain> specifiedChains;
    /**
     * Creates a new TertiaryStructure from a PDB file's structure
     * @param structure the structure extracted from the PDB file
     */
    public TertiaryStructure(Structure structure) {
        this.structure = structure;
        this.sequence = null;
        this.threshold = 4;
        this.secondaryStructure = null;
        this.bondList = null;
        this.contactMatrix = null;
        this.distanceMatrix = null;
        this.distanceMatrixCalculationMethod = "default";
        this.specifiedChains = null;
    }

    /**
     * Returns a list containing the indexes of bonded nucleotides/aminos, that is all nucleotides/aminos closer than the specified threshold as represented
     * within the contact map.
     * @return bond list
     */
    public ArrayList<Pair<Integer>> getBondList(){
        if(this.bondList == null)
            calculateBondList();
        return this.bondList;
    }

    private void calculateBondList() {
        boolean[][]contactMap = this.getContactMatrix();
        ArrayList<Pair<Integer>>bondList = new ArrayList<>();
        int colCount = 1;
        for(int i=1; i<contactMap.length + 1; i++) {
            for (int j = colCount; j < contactMap.length + 1; j++)
                if (contactMap[i-1][j-1]) {
                    bondList.add(new Pair<>(i, j));
                }
            colCount++;
        }
        this.bondList = bondList;
    }


    /**
     * Returns a boolean matrix, values are true if their distance (taken from default calculation)
     * is less than threshold value.
     * @return boolean contact matrix
     */
    public boolean[][] getContactMatrix(){
        if(this.contactMatrix == null)
            calculateContactMatrix();
        return this.contactMatrix;
    }

    private void calculateContactMatrix(){
        double[][] distanceMatrix = getDistanceMatrix();
        boolean[][] contactMatrix = new boolean[distanceMatrix.length][distanceMatrix.length];
        for (int i=0; i<distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                contactMatrix[i][j] = (distanceMatrix[i][j] <= this.threshold) && i != j;
            }
        }
        this.contactMatrix = contactMatrix;
    }


    /**
     * Calculates the structure's distance matrix, considering aminos / nucleotide's center of mass or taking distance between P/CA atoms as comparison method,
     * depending on distance matrix calculation method
     * @return distance matrix
     */
    public double[][] getDistanceMatrix(){
        if(this.distanceMatrixCalculationMethod.equals("default")){
            this.calculateDistanceMatrixDefault();
        }
        else if (this.distanceMatrixCalculationMethod.equals("centerofmass")){
            this.calculateDistanceMatrixCenterOfMass();
        }
        return this.distanceMatrix;
    }

    private void calculateDistanceMatrixCenterOfMass() {
        List<Group> groupsToCompute = this.getNonHetatmGroups();
        List<Atom[]> atomsArrays = this.getAtmosArrays(groupsToCompute);

        double[][] distanceMatrix = new double[groupsToCompute.size()][];
        for (int i = 0; i < atomsArrays.size(); i++) {
            distanceMatrix[i] = new double[i + 1];
            for (int j = 0; j <= i; j++) {
                distanceMatrix[i][j] = Calc.getDistance(
                        Calc.centerOfMass(atomsArrays.get(i)),
                        Calc.centerOfMass(atomsArrays.get(j))
                );
            }
        }
        this.distanceMatrix = distanceMatrix;
    }

    private void calculateDistanceMatrixRingCentroid() {
        List<Group> groupsToCompute = this.getNonHetatmGroups();
        List<Atom[]> atomsArrays = this.getRingAtmosArrays(groupsToCompute);
        double[][] distanceMatrix = new double[groupsToCompute.size()][];
        for (int i = 0; i < atomsArrays.size(); i++) {
            distanceMatrix[i] = new double[i + 1];
            for (int j = 0; j <= i; j++) {
                distanceMatrix[i][j] = Calc.getDistance(
                    Calc.getCentroid(atomsArrays.get(i)),
                    Calc.getCentroid(atomsArrays.get(j))
                );
            }
        }
        this.distanceMatrix = distanceMatrix;
    }

    private void calculateDistanceMatrixDefault(){
        Atom[] representativeAtomsArray = this.getRepresentativeAtomArrayFromSpecifiedChains(this.getChains());
        double[][] distanceMatrix = new double[representativeAtomsArray.length][representativeAtomsArray.length];
        for(int i=0; i<representativeAtomsArray.length; i++)
            for(int j=0; j<representativeAtomsArray.length; j++)
                distanceMatrix[i][j] = Calc.getDistance(representativeAtomsArray[i], representativeAtomsArray[j]);
        this.distanceMatrix = distanceMatrix;
    }

    private Atom[] getRepresentativeAtomArrayFromSpecifiedChains(List<Chain> chainsList) {
        List<Atom> tempRepresentativeAtomsArray = new ArrayList<>();
        chainsList.forEach(chain -> tempRepresentativeAtomsArray.addAll(Arrays.asList(StructureTools.getRepresentativeAtomArray(chain))));
        return tempRepresentativeAtomsArray.toArray(new Atom[tempRepresentativeAtomsArray.size()]);
    }

    private ArrayList<Chain> getSpecifiedChainsByIds(List<String>chainIds){
        ArrayList<Chain> selectedChainsList = new ArrayList<>();
        this.structure.getChains().forEach(chain -> {
            if(chainIds.stream().anyMatch(chain.getName()::equalsIgnoreCase))
                selectedChainsList.add(chain);
        });
        return selectedChainsList;
    }

    private int getNonHetatmGroupsCounter(List<Chain> chainList){
        int nonHetatmGroupsCounter = 0;
        for(Chain currentChain : chainList)
            for(Group currentGroup : currentChain.getAtomGroups())
                if(currentGroup.getType() != GroupType.HETATM)
                    nonHetatmGroupsCounter++;
        return nonHetatmGroupsCounter;
    }

    /**
     * Print the distance matrix
     */
    public void printDistanceMatrix(){
        for (int i=0; i<this.distanceMatrix.length; i++) {
            for (int j=0; j<this.distanceMatrix.length; j++) {
                System.out.print("pos " + i + " " + j + ": " + this.distanceMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Print the contact matrix
     */
    public void printContactMatrix(){
        for (int i=0; i<this.contactMatrix.length; i++) {
            for (int j=0; j<this.contactMatrix.length; j++) {
                System.out.print("pos " + i + " " + j + ": " + this.contactMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Returns the structure's type as string
     * @return structure type (RNA/Protein)
     */
    public String getType(){
        for(Chain c : this.structure.getChains()){
            if(c.getPredominantGroupType() == GroupType.AMINOACID){
                return "PROTEIN";
            }
            else if(c.getPredominantGroupType() == GroupType.NUCLEOTIDE){
                return "RNA";
            }
        }
        return null;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
        this.calculateContactMatrix();
    }

    public Structure getStructure() {
        return structure;
    }

    public String getSequence(){
        StringBuilder builder = new StringBuilder();
        for(Chain currentChain : this.structure.getChains())
            for(Group currentGroup : currentChain.getAtomGroups())
                if(currentGroup.isAminoAcid() || currentGroup.isNucleotide())
                    builder.append(StructureTools.get1LetterCode(currentGroup.getPDBName()));
        return builder.toString();
    }

    /**
     * Sets the distance matrix calculation method.
     * @param calculationMethod chosen calculation method, can be either "default" or "centerofmass"
     */
    public void setDistanceMatrixCalculationMethod(String calculationMethod){
        if(calculationMethod.toLowerCase(Locale.ROOT).equals("default") || calculationMethod.toLowerCase(Locale.ROOT).equals("centerofmass"))
            this.distanceMatrixCalculationMethod = calculationMethod.toLowerCase(Locale.ROOT);
    }

    /**
     * Prints the distance matrix to a csv file
     */
    public void printDistanceMatrixToCSV(){
        try {
            double[][] distanceMatrix = this.getDistanceMatrix();
            FileWriter writer;
            if(this.distanceMatrixCalculationMethod.equals("default"))
                writer = new FileWriter("src/main/resources/DefaultDistanceMatrix.csv");
            else
                writer = new FileWriter("src/main/resources/DistanceMatrixCenterOfMass.csv");
            for (int i = 0; i < distanceMatrix.length; i++) {
                for (int j = 0; j < distanceMatrix.length; j++) {
                    writer.append(String.valueOf(i)).append(" ").append(String.valueOf(j)).append(":").append(" ").append(String.valueOf(distanceMatrix[i][j])).append("   ");
                }
                writer.append("\n");
                writer.append("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the contact matrix to a csv file
     */
    public void printContactMatrixToCSV(){
        try {
            boolean[][] contactMatrix = this.getContactMatrix();
            FileWriter writer;
            writer = new FileWriter("src/main/resources/ContactMatrix.csv");
            for (int i = 0; i < contactMatrix.length; i++) {
                for (int j = 0; j < contactMatrix.length; j++) {
                    writer.append(String.valueOf(i)).append(" ").append(String.valueOf(j)).append(":").append(" ").append(String.valueOf(contactMatrix[i][j])).append("  ");
                }
                writer.append("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Replace current bonds list with a new one, removing all the symmetric bonds.
     * @param bondList new bond list
     */
    public void setBondList(ArrayList<Pair<Integer>> bondList) {
        Pair<Integer>currentBond;
        Pair<Integer> symmetricBond;
        for(int i=0; i<bondList.size(); i++){
            currentBond = bondList.get(i);
            symmetricBond = new Pair<>(currentBond.getSecond(), currentBond.getFirst());
            if(bondList.contains(symmetricBond))
                bondList.remove(symmetricBond);
        }
        this.bondList = bondList;
    }

    /**
     * Sets the chains on which to calculate the matrices.
     * @param chainIds IDs of the selected chains
     */
    public void setSpecifiedChains(ArrayList<String> chainIds) {
        this.specifiedChains = this.getSpecifiedChainsByIds(chainIds);
    }

    /**
     * Replace current sequence with a new one
     * @param sequence new sequence
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    private void calculateDistanceMatrixC1(){
        List<Group> nonHetatmGroups = this.getNonHetatmGroups();

        // Create a cache to store the C1' atoms to avoid multiple calls to getAtom which is expensive
        List<Atom> c1Atoms = new ArrayList<>(nonHetatmGroups.size());
        for (Group group : nonHetatmGroups) {
            Atom c1Atom = group.getAtom("C1'");
            if (c1Atom != null)
                c1Atoms.add(c1Atom);
        }
        int groupsNumber = c1Atoms.size();
        double[][] distanceMatrix = new double[groupsNumber][];

        for (int i = 0; i < c1Atoms.size(); i++) {
            distanceMatrix[i] = new double[i + 1];
            distanceMatrix[i][i] = 0;
            Atom c1i = c1Atoms.get(i);
            for (int j = 0; j < i; j++) {
                Atom c1j = c1Atoms.get(j);
                distanceMatrix[i][j] = Calc.getDistance(c1i, c1j);
            }
        }
        this.distanceMatrix = distanceMatrix;
    }

    /**
     * Find the best chain in the structure
     * @return the specified chain or the first chain if no chain is found
     */
    protected List<Chain> getChains() {
        if (this.specifiedChains != null) {
            return this.specifiedChains;
        }
        if (structure.getChains().size() == 1){
            this.specifiedChains = structure.getChains().stream()
                    .filter(c -> c.isNucleicAcid() || c.isProtein())
                    .toList();
        }
        return this.specifiedChains;
    }

    private static boolean isNotHETATM(Group group) {
        return group.getType() != GroupType.HETATM;
    }

    private List<Group> getNonHetatmGroups(){
        List<Group> nonHetatmGroups = new ArrayList<>(this.getChains().stream().mapToInt(c -> c.getAtomGroups().size()).sum());
        for(Chain currentChain : this.getChains())
            for(Group currentGroup : currentChain.getAtomGroups())
                if(currentGroup.getType() != GroupType.HETATM)
                    nonHetatmGroups.add(currentGroup);
        return nonHetatmGroups;
    }

    private List<Atom[]> getAtmosArrays(List<Group> groups) {
        List<Atom[]> atoms = new ArrayList<>(groups.size());
        for (Group group : groups) {
            atoms.add(group.getAtoms().toArray(new Atom[group.getAtoms().size()]));
        }
        return atoms;
    }

    private List<Atom[]> getRingAtmosArrays(List<Group> groups) {
        List<Atom[]> atoms = new ArrayList<>(groups.size());
        for (Group group : groups) {
            List<Atom> ringAtoms = new ArrayList<>(group.getAtoms().size());
            for (Atom atom : group.getAtoms()) {
                String name = atom.getName().trim();
                if (isPurine(group) && PURINE_RING_ATOMS.contains(name))
                    ringAtoms.add(atom);
                else if (!isPurine(group) && PYRIMIDINE_RING_ATOMS.contains(name))
                    ringAtoms.add(atom);
            }
            atoms.add(ringAtoms.toArray(new Atom[ringAtoms.size()]));
        }
        return atoms;
    }

    private static final Set<String> PURINE_RING_ATOMS = Set.of("N1","C2","N3","C4","C5","C6","N7","C8","N9");
    private static final Set<String> PYRIMIDINE_RING_ATOMS = Set.of("N1","C2","N3","C4","C5","C6");

    private static boolean isPurine(Group g) {
        //TODO: Check for G3, A5...
        String name = g.getPDBName().trim();
        return name.equals("A") || name.equals("G");
    }


}
