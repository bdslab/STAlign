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
 * along with STAlign. If not, see <http://www.gnu.org/licenses/>.
 */
package it.unicam.cs.bdslab.stalign;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.secstruc.BetaBridge;
import org.biojava.nbio.structure.secstruc.SecStrucCalc;
import org.biojava.nbio.structure.secstruc.SecStrucState;
import org.biojava.nbio.structure.secstruc.SecStrucType;

import java.util.ArrayList;
import java.util.List;
/**
 * Creates the secondary structure, and generate the bond list of the structure
 * by analysing and interpreting the output of the DSSP algorithm.
 *
 * @author Marco Serenelli
 *
 */
public class SecondaryStructure {

    private final Structure secondaryStructure;

    private List<SecStrucState> secStrucState;


    private ArrayList<Integer>[] p;

    public SecondaryStructure(Structure secondaryStructure){
        this.secondaryStructure = secondaryStructure;
        calculateSecondaryStructure();
    }

    /**
     * Start the search of the bonds in the SS
     * @return Bonds in SS
     */
    public ArrayList<Integer>[] getBondsList(){
        if (this.p == null)
            dsspToHairpins();
        return this.p;
    }

    public List<SecStrucState> getSecStrucState() {
        return secStrucState;
    }

    /**
     * Calculate the secondary structure of the protein/RNA
     */
    private void calculateSecondaryStructure(){
        SecStrucCalc secStrucCalc = new SecStrucCalc();
        try {
            this.secStrucState = secStrucCalc.calculate(this.secondaryStructure, true);
        } catch (StructureException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starting point for the calculation of the bonds in the SS
     */
    private void dsspToHairpins() {

        int absolutePosition = 0;
        int arrayPos = 0;

        //init arrays
        this.p = new ArrayList[secStrucState.size() + 1];
        for (int i = 0; i < this.p.length; i++) {
            this.p[i] = new ArrayList<>();
        }

        //Init start array
        ArrayList<Character>[] start = new ArrayList[secStrucState.size() + 1];

        //Initializing the start array
        initStart(secStrucState, start);

        //Get DSSP states
        for (SecStrucState state : secStrucState) {
            //initialize variables
            arrayPos = arrayPos + 1;

            //Get letter
            SecStrucType strucType = state.getType();

            //Check the bonds by BP
            checkBpBonds(absolutePosition, state, arrayPos);

            //Starting 3-Structure-column alg.
            threeStructureColumn(arrayPos, start, strucType);

            absolutePosition++;
        }

    }


    /**
     * Initialization of the start array by parsing the DSSP three-Structure-Column and adding
     *  < (End) , > (Start) , X (Start and end), or numbers accordingly.
     *
     * @param secStrucState DSSP parsed SS states
     * @param start Start Array
     */
    private void initStart(List<SecStrucState> secStrucState, ArrayList<Character>[] start){
        int starting = 0;
        int ending = 0;
        int count = 1;
        for (SecStrucState state : secStrucState) {
            //Check three col
            char[] strucCol = state.getTurn();
            for (char c : strucCol) {
                if(start[count] == null)
                    start[count] = new ArrayList<>();

                //If there is a start
                if (c == '>') {
                    starting++;
                    start[count].add('>');
                }
                else if (c == '<') {
                    start[count].add('<');
                    ending++;
                }
                else if (c == 'X'){
                    start[count].add('>');
                    start[count].add('<');
                }
                else if(Character.isDigit(c)){
                    start[count].add(c);
                }
            }
            count  = count + 1;
        }
        assert (starting == ending) : "Numbers of starting does not match numbers of ending";
    }

    /**
     * Start the BetaBridges parsing by looking for bonds and create them if they exist.
     *
     * @param absolutePosition DSSP row
     * @param state DSSP state
     * @param arrayPos array position
     */
    private void checkBpBonds(int absolutePosition, SecStrucState state, int arrayPos) {
        //Get bp1 and bp2
        int bp1 = getBp(absolutePosition, state.getBridge1());
        int bp2 = getBp(absolutePosition, state.getBridge2());

        //Check bonds with bp1 and bp2
        if(bp1 != 0){
            this.p[arrayPos].add(bp1);
        }
        if(bp2 != 0){
            this.p[arrayPos].add(bp2);
        }
    }


    /**
     * Parse the given BetaBridge and check for possible bonds
     * @param absolutePosition DSSP row
     * @param bpBridge DSSP BetaBridge to parse
     * @return bond position
     */
    private int getBp(int absolutePosition, BetaBridge bpBridge) {

        int bp = 0;
        if (bpBridge != null) {
            if (bpBridge.getPartner1() != absolutePosition) {
                bp = bpBridge.getPartner1() + 1;
            } else {
                bp = bpBridge.getPartner2() + 1;
            }
        }
        return bp;
    }

    /**
     * Start the three-Structure-Column parsing by looking at the combination of >, <, X and numbers
     * and create bonds accordingly.
     *
     * @param arrayPos DSSP row
     * @param start Start array
     */
    private void threeStructureColumn(int arrayPos, ArrayList<Character>[] start, SecStrucType strucType) {
        //Look for an ending
        for (int i = 0; i < start[arrayPos].size(); i++) {
            if(start[arrayPos].get(i) == '<'){
                //Look for a letter in the ending row
                int letterWeight = checkLetterWeight(strucType);
                if(letterWeight != -1 ){
                    checkLetterBond(arrayPos, letterWeight, start);
                }
                else{
                    numberCheck(arrayPos, start);
                }
            }
        }
    }

    private void numberCheck(int arrayPos, ArrayList<Character>[] start) {
        int number = 0;
        //Go back and look for numbers
        for (int i = 1; i < 6; i++) {
            int rowCheck = arrayPos - i;
            //Check if we are going out of the structure
            if(rowCheck > 0){
                for (int j = 0; j < start[rowCheck].size(); j++) {
                    if(Character.isDigit(start[rowCheck].get(j))){
                        int possibleNumber = Character.getNumericValue(start[rowCheck].get(j));

                        //There is a digit check if in the digit row there is a start
                        if(checkStart(arrayPos, possibleNumber, start)){
                            if(possibleNumber > number){
                                number = possibleNumber;
                            }
                        }
                    }
                }
            }
        }

        if (number != 0){
            this.p[arrayPos].add(arrayPos - number);
            this.p[arrayPos - number].add(arrayPos);
            //delete start
            start[arrayPos-number].remove(Character.valueOf('>'));
            //Delete number occurrences
            deleteNumbers(arrayPos, arrayPos-number, number, start);
            return;
        }

        //if there is no starting point, it should look for a "normal closing",
        // "normal closing" means looking for the closer start for the corresponding ending
        normalClosing(arrayPos, start);
    }

    private void deleteNumbers(int arrayPos, int startPos, int number, ArrayList<Character>[] start) {
        for (int i = startPos; i < arrayPos; i++) {
            for (int j = 0; j < start[i].size(); j++) {
                if(Character.isDigit(start[i].get(j))){
                    if(Character.getNumericValue(start[i].get(j)) == number){
                        start[i].remove(j);
                    }
                }
            }
        }
    }

    /**
     * Check if there is a start in the last 5 rows before arrayPos
     * @param arrayPos ending row
     * @param start start array
     */
    private void normalClosing(int arrayPos, ArrayList<Character>[] start) {
        for (int i = 3; i < 6; i++) {
            int positionToCheck = arrayPos - i;

            for (int j = 0; j < start[positionToCheck].size(); j++) {
                //if there is a start, create a bond
                if(start[positionToCheck].get(j) == '>'){
                    this.p[positionToCheck].add(arrayPos);
                    this.p[arrayPos].add(positionToCheck);
                    //remove the start from the start array
                    start[positionToCheck].remove(j);
                    return;
                }
            }
        }
    }

    private boolean checkStart(int arrayPos, int number, ArrayList<Character>[] start) {
        int positionToCheck = arrayPos - number;
        if(positionToCheck > 0){
            for (int i = 0; i < start[positionToCheck].size(); i++) {
                if (start[positionToCheck].get(i) == '>') {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if there is a bond created by the letter in the three-Structure-Column
     *
     * @param arrayPos ending row
     * @param letterWeight weight of the letter
     * @param start start array
     */
    private void checkLetterBond(int arrayPos, int letterWeight, ArrayList<Character>[] start) {
        //if a letter H,I or G is found, calculate the expected position
        int expectedStartPosition = arrayPos - letterWeight;
        if(expectedStartPosition > 0){
            //loop inside the expected start position looking for a start
            for (int j = 0; j < start[expectedStartPosition].size(); j++) {
                //if a start is found, create a bond
                if(start[expectedStartPosition].get(j) == '>'){
                    //if a start is found, create the bond
                    this.p[expectedStartPosition].add(arrayPos);
                    this.p[arrayPos].add(expectedStartPosition);
                    //remove the start from the start array
                    start[expectedStartPosition].remove(j);
                    return;
                }
            }
        }
        //Letter found didn't work out should try number method
        numberCheck(arrayPos, start);
    }

    /**
     * Parse the DSSP state type, looking for helix3, helix4 and helix5
     * and return its weight accordingly.
     *
     * @param strucType DSSP state type
     */
    private int checkLetterWeight(SecStrucType strucType) {
        //Check bonds with letter code
        switch (strucType) {
            case helix3 -> {
               return 3;
            }
            case helix4 -> {
                return 4;
            }
            case helix5 -> {
                return 5;
            }
            default -> {
                return -1;
            }
        }
    }
}