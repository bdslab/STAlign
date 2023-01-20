/**
 * STAlign - Structural Tree Alignment
 * 
 * Copyright (C) 2022 BioShape and Data Science Lab at the University of Camerino, Italy - 
 * http://www.emanuelamerelli.eu/bigdata/
 * https://github.com/bdslab
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
import fr.orsay.lri.varna.models.treealign.*;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.contact.Pair;
import org.biojava.nbio.structure.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Test class for the RNA/Protein's structural tree builder
 *
 * @author Marco Serenelli
 *
 */
class TERSAlignTreeTest {

    @Test
    @DisplayName("Comparing expected and actual trees")
    void recBuildStructural() {

        ArrayList<Pair<Integer>> bonds = new ArrayList<>();
        //Simple tests
        // (1,4); (6,9); concat
        bonds.add(new Pair<>(1, 4));
        bonds.add(new Pair<>(6, 9));
        Tree<String> expectedTree = testConcat();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds, 12), "Concat test failed");
        bonds.clear();

        // (1,5); (5,10); meet
        bonds.add(new Pair<>(1, 5));
        bonds.add(new Pair<>(5, 10));
        expectedTree = testMeet();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,11), "Meet test failed");
        bonds.clear();


        // (1,9); (3,6); nesting
        bonds.add(new Pair<>(1, 9));
        bonds.add(new Pair<>(3, 6));
        expectedTree = testNesting();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,10), "Nesting test failed");
        bonds.clear();

        // (1,5); (3,5); ending
        bonds.add(new Pair<>(1, 5));
        bonds.add(new Pair<>(3, 5));
        expectedTree = testEnding();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,7), "Ending test failed");
        bonds.clear();

        // (1,7); (1,5); starting
        bonds.add(new Pair<>(1, 7));
        bonds.add(new Pair<>(1, 5));
        expectedTree = testStarting();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,9), "Starting test failed");
        bonds.clear();

        // (1,6); (3,9); crossing
        bonds.add(new Pair<>(1, 6));
        bonds.add(new Pair<>(3, 9));
        expectedTree = testCrossing();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,10), "Crossing test failed");
        bonds.clear();

        // (1,6); (1,3); (3,6); diamond
        bonds.add(new Pair<>(1, 6));
        bonds.add(new Pair<>(1, 3));
        bonds.add(new Pair<>(3, 6));
        expectedTree = testDiamond();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,7), "Diamond test failed");
        bonds.clear();

        //More advanced tests

        // (1,3); (2,5); (3,4); testCrossingMeet
        bonds.add(new Pair<>(1, 3));
        bonds.add(new Pair<>(2, 5));
        bonds.add(new Pair<>(3, 4));
        expectedTree = testCrossingMeet();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,16), "CrossingMeet test failed");
        bonds.clear();

        // (1,5); (5,10); (12,15); testConcatMeet
        bonds.add(new Pair<>(1, 5));
        bonds.add(new Pair<>(5, 10));
        bonds.add(new Pair<>(12, 15));
        expectedTree = testConcatMeet();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,16), "ConcatMeet test failed");
        bonds.clear();

        //(1,3) (5,8) (10,13) (15,18) testFourConcat
        bonds.add(new Pair<>(1, 3));
        bonds.add(new Pair<>(5, 8));
        bonds.add(new Pair<>(10, 13));
        bonds.add(new Pair<>(15, 18));
        expectedTree = testThreeConcat();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,20), "FourConcat test failed");
        bonds.clear();

        //(1,13); (2,6); (4,8); (8,12); testNestingMeetCrossing
        bonds.add(new Pair<>(1, 13));
        bonds.add(new Pair<>(2, 6));
        bonds.add(new Pair<>(4, 8));
        bonds.add(new Pair<>(8, 12));
        expectedTree = testNestingMeetCrossing();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,14), "NestingMeetCrossing test failed");
        bonds.clear();

        // (1,5);(5,10); (15,20);(20;25); testMeetConcatMeet
        bonds.add(new Pair<>(1, 5));
        bonds.add(new Pair<>(5, 10));
        bonds.add(new Pair<>(15, 20));
        bonds.add(new Pair<>(20, 25));
        expectedTree = testMeetConcatMeet();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,26), "MeetConcatMeet test failed");
        bonds.clear();

        // (1,3); (3,6); (6,9); (12,15); (18,21); (21,24); testMeetConcatConcatMeetMeet
        bonds.add(new Pair<>(1, 3));
        bonds.add(new Pair<>(3, 6));
        bonds.add(new Pair<>(6, 9));
        bonds.add(new Pair<>(12, 15));
        bonds.add(new Pair<>(18, 21));
        bonds.add(new Pair<>(21, 24));
        expectedTree = testMeetConcatConcatMeetMeet();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,26), "MeetConcatConcatMeetMeet test failed");
        bonds.clear();

        //(1,7); (2,7); (4,8); (8,12); testMeetCrossEnding
        bonds.add(new Pair<>(1, 7));
        bonds.add(new Pair<>(2, 7));
        bonds.add(new Pair<>(4, 8));
        bonds.add(new Pair<>(8, 12));
        expectedTree = testMeetCrossEnding();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,13), "MeetCrossEnding test failed");
        bonds.clear();

        //(1,3); (1,12); (6,9); (7,9); (9,12); testMeetConcCrossNestDiamond
        bonds.add(new Pair<>(1, 3));
        bonds.add(new Pair<>(1, 12));
        bonds.add(new Pair<>(6, 9));
        bonds.add(new Pair<>(7, 9));
        bonds.add(new Pair<>(9, 12));
        expectedTree = testMeetEndConcatDiamond();
        //Check this expected tree with real tree using given bonds
        assertTrue(isEquals(expectedTree, bonds,13), "MeetConcCrossNestDiamond test failed");
        bonds.clear();

        //(1,9) (2,4) (3,5) (4,6) (4,7) (4,8)
        bonds.add(new Pair<>(1,9));
        bonds.add(new Pair<>(2,4));
        bonds.add(new Pair<>(3,5));
        bonds.add(new Pair<>(4,6));
        bonds.add(new Pair<>(4,7));
        bonds.add(new Pair<>(4,8));
        expectedTree = testNotMeet();
        assertTrue(isEquals(expectedTree,bonds, 15));
        bonds.clear();

        //(9,96); (8,96); (93,97); (96,98);
        bonds.add(new Pair<>(8,96));
        bonds.add(new Pair<>(9,96));
        bonds.add(new Pair<>(93,97));
        bonds.add(new Pair<>(96,98));
        expectedTree = testMeet2();
        assertTrue(isEquals(expectedTree,bonds, 99));
        bonds.clear();

        //(10,16); (12,16); (12,19); (16,19); (16,22)
        bonds.add(new Pair<>(10,16));
        bonds.add(new Pair<>(12,16));
        bonds.add(new Pair<>(12,19));
        bonds.add(new Pair<>(16,19));
        bonds.add(new Pair<>(16,22));
        expectedTree = testMeet3();
        assertTrue(isEquals(expectedTree,bonds, 23));
        bonds.clear();

        //Professor tests

        //(1,4); (1,8); (1,12); (8,10); (8,12); (12,16); (17,22); (18,20); (19,21);
        bonds.add(new Pair<>(1,4));
        bonds.add(new Pair<>(1,8));
        bonds.add(new Pair<>(1,12));
        bonds.add(new Pair<>(8,10));
        bonds.add(new Pair<>(8,12));
        bonds.add(new Pair<>(12,16));
        bonds.add(new Pair<>(17,22));
        bonds.add(new Pair<>(18,20));
        bonds.add(new Pair<>(19,21));
        expectedTree = testP1();
        assertTrue(isEquals(expectedTree, bonds, 23));
        bonds.clear();

        //(1,4); (1,7); (7,10); (7,12); (12,17); (19,26); (20,23); (22,25);
        bonds.add(new Pair<>(1,4));
        bonds.add(new Pair<>(1,7));
        bonds.add(new Pair<>(7,10));
        bonds.add(new Pair<>(7,12));
        bonds.add(new Pair<>(12,17));
        bonds.add(new Pair<>(19,26));
        bonds.add(new Pair<>(20,23));
        bonds.add(new Pair<>(22,25));
        expectedTree = testP2();
        assertTrue(isEquals(expectedTree, bonds, 27));
        bonds.clear();

        //(1,6); (1,12); (6,12); (12,16); (17,21); (19,24);
        bonds.add(new Pair<>(1,6));
        bonds.add(new Pair<>(1,12));
        bonds.add(new Pair<>(6,12));
        bonds.add(new Pair<>(12,16));
        bonds.add(new Pair<>(17,21));
        bonds.add(new Pair<>(19,24));
        expectedTree = testP3();
        assertTrue(isEquals(expectedTree, bonds, 25));
        bonds.clear();

        //(1,6); (1,12); (6,12); (7,17); (12,17); (18,24); (18,22); (20,24);
        bonds.add(new Pair<>(1,6));
        bonds.add(new Pair<>(1,12));
        bonds.add(new Pair<>(6,12));
        bonds.add(new Pair<>(7,17));
        bonds.add(new Pair<>(12,17));
        bonds.add(new Pair<>(18,24));
        bonds.add(new Pair<>(18,22));
        bonds.add(new Pair<>(20,24));
        expectedTree = testP4();
        assertTrue(isEquals(expectedTree, bonds, 25));
        bonds.clear();

        //Compare Professor's trees
        compareTrees();

    }

    private Tree<String> testCrossingMeet() {
        // (1,3); (2,5); (3,4);
        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue("(" + Operators.CROSSING_LABEL + ",1)");

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        //update tree
        ArrayList<Tree<String>> crossingChilds = new ArrayList<>();
        crossingChilds.add(left);
        crossingChilds.add(right);
        structuralTree.replaceChildrenListBy(crossingChilds);

        left.setValue(Operators.MEETING_LABEL);
        right.setValue(Operators.HAIRPIN_LABEL + "(2,5)");

        //2nd level

        Tree<String> left2 = new Tree<>();
        Tree<String> right2 = new Tree<>();

        //update tree
        ArrayList<Tree<String>> meetingChilds = new ArrayList<>();
        meetingChilds.add(left2);
        meetingChilds.add(right2);
        left.replaceChildrenListBy(meetingChilds);

        right2.setValue(Operators.HAIRPIN_LABEL + "(3,4)");
        left2.setValue(Operators.HAIRPIN_LABEL + "(1,3)");

        return structuralTree;
    }

    private Tree<String> testConcat() {
        // (1,4); (6,9); concat
        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.CONCATENATION_LABEL);

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        //update tree
        ArrayList<Tree<String>> concatChilds = new ArrayList<>();
        concatChilds.add(left);
        concatChilds.add(right);
        structuralTree.replaceChildrenListBy(concatChilds);

        right.setValue(Operators.HAIRPIN_LABEL + "(6,9)");
        left.setValue(Operators.HAIRPIN_LABEL + "(1,4)");

        return structuralTree;
    }

    private Tree<String> testMeet() {
        // (1,5); (5,10); meet
        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.MEETING_LABEL);

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        //update tree
        ArrayList<Tree<String>> meetChilds = new ArrayList<>();
        meetChilds.add(left);
        meetChilds.add(right);
        structuralTree.replaceChildrenListBy(meetChilds);

        left.setValue(Operators.HAIRPIN_LABEL + "(1,5)");
        right.setValue(Operators.HAIRPIN_LABEL + "(5,10)");

        return structuralTree;
    }

    private Tree<String> testNesting() {
        // (1,9); (3,6);
        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.NESTING_LABEL);

        Tree<String> right = new Tree<>();
        Tree<String> left = new Tree<>();

        //update tree
        ArrayList<Tree<String>> nestingChilds = new ArrayList<>();
        nestingChilds.add(left);
        nestingChilds.add(right);
        structuralTree.replaceChildrenListBy(nestingChilds);

        right.setValue(Operators.HAIRPIN_LABEL +"(1,9)" );
        left.setValue(Operators.HAIRPIN_LABEL + "(3,6)" );
        return structuralTree;
    }

    private Tree<String> testEnding() {
        // (1,5); (3,5); ending
        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.ENDING_LABEL);

        Tree<String> right = new Tree<>();
        Tree<String> left = new Tree<>();

        //update tree
        ArrayList<Tree<String>> endingChilds = new ArrayList<>();
        endingChilds.add(left);
        endingChilds.add(right);
        structuralTree.replaceChildrenListBy(endingChilds);

        right.setValue(Operators.HAIRPIN_LABEL + "(1,5)" );
        left.setValue(Operators.HAIRPIN_LABEL + "(3,5)");
        return structuralTree;
    }

    private Tree<String> testStarting() {
        // (1,7); (1,5); starting
        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.STARTING_LABEL);

        Tree<String> right = new Tree<>();
        Tree<String> left = new Tree<>();

        //update tree
        ArrayList<Tree<String>> startingChilds = new ArrayList<>();
        startingChilds.add(left);
        startingChilds.add(right);
        structuralTree.replaceChildrenListBy(startingChilds);

        right.setValue(Operators.HAIRPIN_LABEL + "(1,7)");
        left.setValue(Operators.HAIRPIN_LABEL + "(1,5)");

        return structuralTree;
    }

    private Tree<String> testCrossing() {
        // (1,6); (3,9); crossing
        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue("(" + Operators.CROSSING_LABEL + ",1)");

        Tree<String> right = new Tree<>();
        Tree<String> left = new Tree<>();

        //update tree
        ArrayList<Tree<String>> crossingChilds = new ArrayList<>();
        crossingChilds.add(left);
        crossingChilds.add(right);
        structuralTree.replaceChildrenListBy(crossingChilds);

        right.setValue(Operators.HAIRPIN_LABEL + "(3,9)");
        left.setValue(Operators.HAIRPIN_LABEL + "(1,6)");

        return structuralTree;
    }

    private Tree<String> testDiamond() {
        // (1,6); (1,3); (3,6); diamond
        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.DIAMOND_LABEL);

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        //update tree
        ArrayList<Tree<String>> diamondChilds = new ArrayList<>();
        diamondChilds.add(left);
        diamondChilds.add(right);
        structuralTree.replaceChildrenListBy(diamondChilds);

        left.setValue(Operators.MEETING_LABEL);
        right.setValue(Operators.HAIRPIN_LABEL + "(1,6)");

        //2nd level

        Tree<String> left2 = new Tree<>();
        Tree<String> right2 = new Tree<>();

        //update tree
        ArrayList<Tree<String>> concatChilds2 = new ArrayList<>();
        concatChilds2.add(left2);
        concatChilds2.add(right2);
        left.replaceChildrenListBy(concatChilds2);

        right2.setValue(Operators.HAIRPIN_LABEL + "(3,6)");
        left2.setValue(Operators.HAIRPIN_LABEL + "(1,3)");

        return structuralTree;
    }

    private Tree<String> testNestingMeetCrossing() {
        //(1,13); (2,6); (4,8); (8,12);
        Tree<String> structuralTree = new Tree<>();
        structuralTree.setValue(Operators.NESTING_LABEL);

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        //update tree
        ArrayList<Tree<String>> nestingChilds = new ArrayList<>();
        nestingChilds.add(left);
        nestingChilds.add(right);
        structuralTree.replaceChildrenListBy(nestingChilds);

        right.setValue(Operators.HAIRPIN_LABEL + "(1,13)");
        left.setValue(Operators.MEETING_LABEL);

        //2nd level
        Tree<String> left2 = new Tree<>();
        Tree<String> right2 = new Tree<>();

        //update tree
        ArrayList<Tree<String>> meetingChilds = new ArrayList<>();
        meetingChilds.add(left2);
        meetingChilds.add(right2);
        left.replaceChildrenListBy(meetingChilds);

        right2.setValue(Operators.HAIRPIN_LABEL + "(8,12)");
        left2.setValue("(" + Operators.CROSSING_LABEL + ",1)");

        //3rd level
        Tree<String> left3 = new Tree<>();
        Tree<String> right3 = new Tree<>();

        //update tree
        ArrayList<Tree<String>> meetingChilds2 = new ArrayList<>();
        meetingChilds2.add(left3);
        meetingChilds2.add(right3);
        left2.replaceChildrenListBy(meetingChilds2);

        right3.setValue(Operators.HAIRPIN_LABEL + "(4,8)");
        left3.setValue(Operators.HAIRPIN_LABEL + "(2,6)");

        return structuralTree;
    }

    private Tree<String> testMeetConcatConcatMeetMeet() {

        // (1,3); (3,6); (6,9); (12,15); (18,21); (21,24);
        Tree<String> structuralTree = new Tree<>();
        structuralTree.setValue(Operators.MEETING_LABEL);

        // create the node for building the left part
        Tree<String> left = new Tree<>();
        // create node for building the right part
        Tree<String> right = new Tree<>();

        // update tree
        ArrayList<Tree<String>> meetingChilds = new ArrayList<>();
        meetingChilds.add(left);
        meetingChilds.add(right);
        structuralTree.replaceChildrenListBy(meetingChilds);

        left.setValue(Operators.CONCATENATION_LABEL);
        right.setValue(Operators.HAIRPIN_LABEL + "(21,24)");

        // create the node for building the left part
        Tree<String> left2 = new Tree<>();
        // create node for building the right part
        Tree<String> right2 = new Tree<>();

        // update tree
        ArrayList<Tree<String>> concatChilds = new ArrayList<>();
        concatChilds.add(left2);
        concatChilds.add(right2);
        left.replaceChildrenListBy(concatChilds);

        left2.setValue(Operators.CONCATENATION_LABEL);
        right2.setValue(Operators.HAIRPIN_LABEL + "(18,21)");

        // create the node for building the left part
        Tree<String> left3 = new Tree<>();
        // create node for building the right part
        Tree<String> right3 = new Tree<>();

        // update tree
        ArrayList<Tree<String>> concatChilds2 = new ArrayList<>();
        concatChilds2.add(left3);
        concatChilds2.add(right3);
        left2.replaceChildrenListBy(concatChilds2);

        left3.setValue(Operators.MEETING_LABEL);
        right3.setValue(Operators.HAIRPIN_LABEL + "(12,15)");

        // create the node for building the left part
        Tree<String> left4 = new Tree<>();
        // create node for building the right part
        Tree<String> right4 = new Tree<>();

        // update tree
        ArrayList<Tree<String>> meetingChilds2 = new ArrayList<>();
        meetingChilds2.add(left4);
        meetingChilds2.add(right4);
        left3.replaceChildrenListBy(meetingChilds2);

        left4.setValue(Operators.MEETING_LABEL);
        right4.setValue(Operators.HAIRPIN_LABEL + "(6,9)");

        // create the node for building the left part
        Tree<String> left5 = new Tree<>();
        // create node for building the right part
        Tree<String> right5 = new Tree<>();

        // update tree
        ArrayList<Tree<String>> meetingChilds3 = new ArrayList<>();
        meetingChilds3.add(left5);
        meetingChilds3.add(right5);
        left4.replaceChildrenListBy(meetingChilds3);

        left5.setValue(Operators.HAIRPIN_LABEL + "(1,3)");
        right5.setValue(Operators.HAIRPIN_LABEL + "(3,6)");

        return structuralTree;
    }

    private Tree<String> testMeetConcatMeet() {
        Tree<String> structuralTree = new Tree<>();

        // (1,5);(5,10); (15,20);(20;25); first we encounter a meet, then a concat, then a meet
        structuralTree.setValue(Operators.MEETING_LABEL);

        // create the node for building the left part
        Tree<String> left = new Tree<>();
        // create node for building the right part
        Tree<String> right = new Tree<>();

        // update tree
        ArrayList<Tree<String>> meetingChilds = new ArrayList<>();
        meetingChilds.add(left);
        meetingChilds.add(right);
        structuralTree.replaceChildrenListBy(meetingChilds);

        right.setValue(Operators.HAIRPIN_LABEL + "(20,25)");
        left.setValue(Operators.CONCATENATION_LABEL);

        //2nd level
        // create the node for building the left part
        Tree<String> left2 = new Tree<>();
        // create node for building the right part
        Tree<String> right2 = new Tree<>();

        // update tree
        ArrayList<Tree<String>> concatChilds = new ArrayList<>();
        concatChilds.add(left2);
        concatChilds.add(right2);
        left.replaceChildrenListBy(concatChilds);

        right2.setValue(Operators.HAIRPIN_LABEL + "(15,20)");
        left2.setValue(Operators.MEETING_LABEL);

        //3rd level
        // create the node for building the left part
        Tree<String> left3 = new Tree<>();
        // create node for building the right part
        Tree<String> right3 = new Tree<>();

        // update tree
        ArrayList<Tree<String>> meetingChilds2 = new ArrayList<>();
        meetingChilds2.add(left3);
        meetingChilds2.add(right3);
        left2.replaceChildrenListBy(meetingChilds2);

        left3.setValue(Operators.HAIRPIN_LABEL + "(1,5)");
        right3.setValue(Operators.HAIRPIN_LABEL + "(5,10)");

        return structuralTree;
    }

    private Tree<String> testConcatMeet() {
        Tree<String> structuralTree = new Tree<>();

        // (1,5); (5,10); (12,15); first we encounter a concat, then a zero interval, then a meet
        structuralTree.setValue(Operators.CONCATENATION_LABEL);

        // create the node for building the left part
        Tree<String> left = new Tree<>();

        // create node for building the right part
        Tree<String> right = new Tree<>();

        // update tree
        ArrayList<Tree<String>> concatChilds = new ArrayList<>();
        concatChilds.add(left);
        concatChilds.add(right);
        structuralTree.replaceChildrenListBy(concatChilds);

        right.setValue(Operators.HAIRPIN_LABEL + "(12,15)");
        left.setValue(Operators.MEETING_LABEL);

        //2nd level
        Tree<String> left2 = new Tree<>();
        Tree<String> right2 = new Tree<>();

        // update tree
        ArrayList<Tree<String>> meetingChilds = new ArrayList<>();
        meetingChilds.add(left2);
        meetingChilds.add(right2);
        left.replaceChildrenListBy(meetingChilds);

        right2.setValue(Operators.HAIRPIN_LABEL + "(5,10)");
        left2.setValue(Operators.HAIRPIN_LABEL + "(1,5)");

        return structuralTree;
    }

    private Tree<String> testThreeConcat() {
        //(1,3) (5,8) (10,13) (15,18)
        Tree<String> structuralTree = new Tree<>();
        structuralTree.setValue(Operators.CONCATENATION_LABEL);
        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();
        ArrayList<Tree<String>> concatChilds = new ArrayList<>();
        concatChilds.add(left);
        concatChilds.add(right);
        structuralTree.replaceChildrenListBy(concatChilds);
        right.setValue(Operators.HAIRPIN_LABEL + "(15,18)");
        left.setValue(Operators.CONCATENATION_LABEL);
        //2nd level
        Tree<String> left2 = new Tree<>();
        Tree<String> right2 = new Tree<>();
        ArrayList<Tree<String>> concatChilds2 = new ArrayList<>();
        concatChilds2.add(left2);
        concatChilds2.add(right2);
        left.replaceChildrenListBy(concatChilds2);
        right2.setValue(Operators.HAIRPIN_LABEL + "(10,13)");
        left2.setValue(Operators.CONCATENATION_LABEL);
        //3rd level
        Tree<String> left3 = new Tree<>();
        Tree<String> right3 = new Tree<>();
        ArrayList<Tree<String>> concatChilds3 = new ArrayList<>();
        concatChilds3.add(left3);
        concatChilds3.add(right3);
        left2.replaceChildrenListBy(concatChilds3);
        right3.setValue(Operators.HAIRPIN_LABEL + "(5,8)");
        left3.setValue(Operators.HAIRPIN_LABEL + "(1,3)");

        return structuralTree;
    }

    private Tree<String> testMeetCrossEnding() {
        //(1,7); (2,7); (4,8); (8,12);
        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.MEETING_LABEL);

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        ArrayList<Tree<String>> meetingChilds = new ArrayList<>();
        meetingChilds.add(left);
        meetingChilds.add(right);
        structuralTree.replaceChildrenListBy(meetingChilds);

        right.setValue(Operators.HAIRPIN_LABEL + "(8,12)");
        left.setValue("(" + Operators.CROSSING_LABEL + ",2)");

        //2nd level
        Tree<String> left2 = new Tree<>();
        Tree<String> right2 = new Tree<>();

        ArrayList<Tree<String>> crossingChilds = new ArrayList<>();
        crossingChilds.add(left2);
        crossingChilds.add(right2);
        left.replaceChildrenListBy(crossingChilds);

        right2.setValue(Operators.HAIRPIN_LABEL + "(4,8)");
        left2.setValue(Operators.ENDING_LABEL);

        //3rd level
        Tree<String> left3 = new Tree<>();
        Tree<String> right3 = new Tree<>();

        ArrayList<Tree<String>> endingChilds = new ArrayList<>();
        endingChilds.add(left3);
        endingChilds.add(right3);
        left2.replaceChildrenListBy(endingChilds);

        right3.setValue(Operators.HAIRPIN_LABEL + "(1,7)");
        left3.setValue(Operators.HAIRPIN_LABEL + "(2,7)");

        return structuralTree;
    }

    private Tree<String> testMeetEndConcatDiamond() {
        //(1,2); (1,12); (6,9); (7,9); (9,12);
        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.DIAMOND_LABEL);

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        ArrayList<Tree<String>> diamondChilds = new ArrayList<>();
        diamondChilds.add(left);
        diamondChilds.add(right);
        structuralTree.replaceChildrenListBy(diamondChilds);

        right.setValue(Operators.HAIRPIN_LABEL + "(1,12)");
        left.setValue(Operators.MEETING_LABEL);

        //2nd level
        Tree<String> left2 = new Tree<>();
        Tree<String> right2 = new Tree<>();

        ArrayList<Tree<String>> meetChilds = new ArrayList<>();
        meetChilds.add(left2);
        meetChilds.add(right2);
        left.replaceChildrenListBy(meetChilds);

        right2.setValue(Operators.HAIRPIN_LABEL + "(9,12)");
        left2.setValue(Operators.CONCATENATION_LABEL);

        //3rd level
        Tree<String> left3 = new Tree<>();
        Tree<String> right3 = new Tree<>();

        ArrayList<Tree<String>> concatChilds = new ArrayList<>();
        concatChilds.add(left3);
        concatChilds.add(right3);
        left2.replaceChildrenListBy(concatChilds);

        right3.setValue(Operators.ENDING_LABEL);
        left3.setValue(Operators.HAIRPIN_LABEL + "(1,3)");

        //4th level
        Tree<String> left4 = new Tree<>();
        Tree<String> right4 = new Tree<>();

        ArrayList<Tree<String>> endingChilds = new ArrayList<>();
        endingChilds.add(left4);
        endingChilds.add(right4);
        right3.replaceChildrenListBy(endingChilds);

        right4.setValue(Operators.HAIRPIN_LABEL + "(6,9)");
        left4.setValue(Operators.HAIRPIN_LABEL + "(7,9)");


        return structuralTree;
    }

    private Tree<String> testMeet2(){
        //(9,96); (8,96); (93,97); (96,98);
        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue("("+ Operators.CROSSING_LABEL + ",1)");

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        ArrayList<Tree<String>> crossingChilds = new ArrayList<>();
        crossingChilds.add(left);
        crossingChilds.add(right);
        structuralTree.replaceChildrenListBy(crossingChilds);

        right.setValue(Operators.HAIRPIN_LABEL + "(96,98)");
        left.setValue("("+ Operators.CROSSING_LABEL + ",2)");

        //2nd level
        Tree<String> left2 = new Tree<>();
        Tree<String> right2 = new Tree<>();

        ArrayList<Tree<String>> crossingChilds2 = new ArrayList<>();
        crossingChilds2.add(left2);
        crossingChilds2.add(right2);
        left.replaceChildrenListBy(crossingChilds2);

        right2.setValue(Operators.HAIRPIN_LABEL + "(93,97)");
        left2.setValue(Operators.ENDING_LABEL);

        //3th level
        Tree<String> left3 = new Tree<>();
        Tree<String> right3 = new Tree<>();

        ArrayList<Tree<String>> endingChilds = new ArrayList<>();
        endingChilds.add(left3);
        endingChilds.add(right3);
        left2.replaceChildrenListBy(endingChilds);

        right3.setValue(Operators.HAIRPIN_LABEL + "(8,96)");
        left3.setValue(Operators.HAIRPIN_LABEL + "(9,96)");

        return structuralTree;
    }

    private Tree<String> testMeet3(){
        //(10,16); (12,16); (12,19); (16,19); (16,22)

        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue("("+Operators.CROSSING_LABEL+",1)");

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        ArrayList<Tree<String>> crossingChilds = new ArrayList<>();
        crossingChilds.add(left);
        crossingChilds.add(right);
        structuralTree.replaceChildrenListBy(crossingChilds);

        right.setValue(Operators.HAIRPIN_LABEL + "(16,22)");
        left.setValue("("+Operators.CROSSING_LABEL+",1)");

        //2nd level
        Tree<String> left2 = new Tree<>();
        Tree<String> right2 = new Tree<>();

        ArrayList<Tree<String>> crossingChilds2 = new ArrayList<>();
        crossingChilds2.add(left2);
        crossingChilds2.add(right2);
        left.replaceChildrenListBy(crossingChilds2);

        right2.setValue(Operators.HAIRPIN_LABEL + "(12,19)");
        left2.setValue(Operators.MEETING_LABEL);

        //3rd level
        Tree<String> left3 = new Tree<>();
        Tree<String> right3 = new Tree<>();

        ArrayList<Tree<String>> meetingChilds = new ArrayList<>();
        meetingChilds.add(left3);
        meetingChilds.add(right3);
        left2.replaceChildrenListBy(meetingChilds);

        right3.setValue(Operators.HAIRPIN_LABEL + "(16,19)");
        left3.setValue(Operators.ENDING_LABEL);

        //4th level
        Tree<String> left4 = new Tree<>();
        Tree<String> right4 = new Tree<>();

        ArrayList<Tree<String>> endingChilds = new ArrayList<>();
        endingChilds.add(left4);
        endingChilds.add(right4);
        left3.replaceChildrenListBy(endingChilds);

        right4.setValue(Operators.HAIRPIN_LABEL + "(10,16)");
        left4.setValue(Operators.HAIRPIN_LABEL + "(12,16)");

        return structuralTree;
    }

    private Tree<String> testNotMeet(){
        //(1,9); (2,4); (3,5); (4,6); (4,7); (4,8);

        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.NESTING_LABEL);

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        ArrayList<Tree<String>> nestingChilds = new ArrayList<>();
        nestingChilds.add(left);
        nestingChilds.add(right);
        structuralTree.replaceChildrenListBy(nestingChilds);

        right.setValue(Operators.HAIRPIN_LABEL + "(1,9)");
        left.setValue("("+Operators.CROSSING_LABEL+",1)");

        //2nd level
        Tree<String> left2 = new Tree<>();
        Tree<String> right2 = new Tree<>();

        ArrayList<Tree<String>> crossingChilds = new ArrayList<>();
        crossingChilds.add(left2);
        crossingChilds.add(right2);
        left.replaceChildrenListBy(crossingChilds);

        right2.setValue(Operators.HAIRPIN_LABEL + "(4,8)");
        left2.setValue("("+Operators.CROSSING_LABEL+",1)");

        //3rd level
        Tree<String> left3 = new Tree<>();
        Tree<String> right3 = new Tree<>();

        ArrayList<Tree<String>> crossingChilds2 = new ArrayList<>();
        crossingChilds2.add(left3);
        crossingChilds2.add(right3);
        left2.replaceChildrenListBy(crossingChilds2);

        right3.setValue(Operators.HAIRPIN_LABEL + "(4,7)");
        left3.setValue("("+Operators.CROSSING_LABEL+",1)");

        //4th level
        Tree<String> left4 = new Tree<>();
        Tree<String> right4 = new Tree<>();

        ArrayList<Tree<String>> crossingChilds3 = new ArrayList<>();
        crossingChilds3.add(left4);
        crossingChilds3.add(right4);
        left3.replaceChildrenListBy(crossingChilds3);

        right4.setValue(Operators.HAIRPIN_LABEL + "(4,6)");
        left4.setValue("("+Operators.CROSSING_LABEL+",1)");

        //5th level
        Tree<String> left5 = new Tree<>();
        Tree<String> right5 = new Tree<>();

        ArrayList<Tree<String>> crossingChilds4 = new ArrayList<>();
        crossingChilds4.add(left5);
        crossingChilds4.add(right5);
        left4.replaceChildrenListBy(crossingChilds4);

        right5.setValue(Operators.HAIRPIN_LABEL + "(3,5)");
        left5.setValue(Operators.HAIRPIN_LABEL + "(2,4)");

        return structuralTree;
    }

    private Tree<String> testP1(){
        //(1,4); (1,8); (1,12); (8,10); (8,12); (12,16); (17,22); (18,20); (19,21);

        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.CONCATENATION_LABEL);

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        ArrayList<Tree<String>> concatChilds = new ArrayList<>();
        concatChilds.add(left);
        concatChilds.add(right);
        structuralTree.replaceChildrenListBy(concatChilds);

        right.setValue(Operators.NESTING_LABEL);
        left.setValue(Operators.MEETING_LABEL);

        //2nd level Left
        Tree<String> left2l = new Tree<>();
        Tree<String> right2l = new Tree<>();

        ArrayList<Tree<String>> meetingChilds = new ArrayList<>();
        meetingChilds.add(left2l);
        meetingChilds.add(right2l);
        left.replaceChildrenListBy(meetingChilds);

        right2l.setValue(Operators.HAIRPIN_LABEL + "(12,16)");
        left2l.setValue(Operators.DIAMOND_LABEL);

        //2nd level Right
        Tree<String> left2r = new Tree<>();
        Tree<String> right2r = new Tree<>();

        ArrayList<Tree<String>> nestingChilds = new ArrayList<>();
        nestingChilds.add(left2r);
        nestingChilds.add(right2r);
        right.replaceChildrenListBy(nestingChilds);

        right2r.setValue(Operators.HAIRPIN_LABEL + "(17,22)");
        left2r.setValue("(" + Operators.CROSSING_LABEL + ",1)");

        //3rd level for second level left
        Tree<String> left3ll = new Tree<>();
        Tree<String> right3ll = new Tree<>();

        ArrayList<Tree<String>> crossingChilds = new ArrayList<>();
        crossingChilds.add(left3ll);
        crossingChilds.add(right3ll);
        left2r.replaceChildrenListBy(crossingChilds);

        right3ll.setValue(Operators.HAIRPIN_LABEL + "(19,21)");
        left3ll.setValue(Operators.HAIRPIN_LABEL + "(18,20)");

        //3rd level
        Tree<String> left3 = new Tree<>();
        Tree<String> right3 = new Tree<>();

        ArrayList<Tree<String>> diamondChilds = new ArrayList<>();
        diamondChilds.add(left3);
        diamondChilds.add(right3);
        left2l.replaceChildrenListBy(diamondChilds);

        right3.setValue(Operators.HAIRPIN_LABEL + "(1,12)");
        left3.setValue(Operators.MEETING_LABEL);

        //4th level
        Tree<String> left4 = new Tree<>();
        Tree<String> right4 = new Tree<>();

        ArrayList<Tree<String>> meetingChilds2 = new ArrayList<>();
        meetingChilds2.add(left4);
        meetingChilds2.add(right4);
        left3.replaceChildrenListBy(meetingChilds2);

        right4.setValue(Operators.STARTING_LABEL);
        left4.setValue(Operators.STARTING_LABEL);

        //5th level left
        Tree<String> left5l = new Tree<>();
        Tree<String> right5l = new Tree<>();

        ArrayList<Tree<String>> startingChilds = new ArrayList<>();
        startingChilds.add(left5l);
        startingChilds.add(right5l);
        left4.replaceChildrenListBy(startingChilds);

        right5l.setValue(Operators.HAIRPIN_LABEL + "(1,8)");
        left5l.setValue(Operators.HAIRPIN_LABEL + "(1,4)");

        //5th level right
        Tree<String> left5r = new Tree<>();
        Tree<String> right5r = new Tree<>();

        ArrayList<Tree<String>> startingChilds2 = new ArrayList<>();
        startingChilds2.add(left5r);
        startingChilds2.add(right5r);
        right4.replaceChildrenListBy(startingChilds2);

        right5r.setValue(Operators.HAIRPIN_LABEL + "(8,12)");
        left5r.setValue(Operators.HAIRPIN_LABEL + "(8,10)");

        return structuralTree;
    }

    private Tree<String> testP2(){
        //(1,4); (1,7); (7,10); (7,12); (12,17); (19,26); (20,23); (22,25);

        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.CONCATENATION_LABEL);

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        ArrayList<Tree<String>> concatChilds = new ArrayList<>();
        concatChilds.add(left);
        concatChilds.add(right);
        structuralTree.replaceChildrenListBy(concatChilds);

        right.setValue(Operators.NESTING_LABEL);
        left.setValue(Operators.MEETING_LABEL);

        //2nd level Left
        Tree<String> left2l = new Tree<>();
        Tree<String> right2l = new Tree<>();

        ArrayList<Tree<String>> meetingChilds = new ArrayList<>();
        meetingChilds.add(left2l);
        meetingChilds.add(right2l);
        left.replaceChildrenListBy(meetingChilds);

        right2l.setValue(Operators.HAIRPIN_LABEL + "(12,17)");
        left2l.setValue(Operators.MEETING_LABEL);

        //2nd level Right
        Tree<String> left2r = new Tree<>();
        Tree<String> right2r = new Tree<>();

        ArrayList<Tree<String>> nestingChilds = new ArrayList<>();
        nestingChilds.add(left2r);
        nestingChilds.add(right2r);
        right.replaceChildrenListBy(nestingChilds);

        right2r.setValue(Operators.HAIRPIN_LABEL + "(19,26)");
        left2r.setValue("(" + Operators.CROSSING_LABEL + ",1)");

        //3rd level for second level left
        Tree<String> left3ll = new Tree<>();
        Tree<String> right3ll = new Tree<>();

        ArrayList<Tree<String>> crossingChilds = new ArrayList<>();
        crossingChilds.add(left3ll);
        crossingChilds.add(right3ll);
        left2r.replaceChildrenListBy(crossingChilds);

        right3ll.setValue(Operators.HAIRPIN_LABEL + "(22,25)");
        left3ll.setValue(Operators.HAIRPIN_LABEL + "(20,23)");

        //3rd level
        Tree<String> left3 = new Tree<>();
        Tree<String> right3 = new Tree<>();

        ArrayList<Tree<String>> meetingChilds2 = new ArrayList<>();
        meetingChilds2.add(left3);
        meetingChilds2.add(right3);
        left2l.replaceChildrenListBy(meetingChilds2);

        right3.setValue(Operators.STARTING_LABEL);
        left3.setValue(Operators.STARTING_LABEL);

        //4th level left
        Tree<String> left4l = new Tree<>();
        Tree<String> right4l = new Tree<>();

        ArrayList<Tree<String>> startingChilds = new ArrayList<>();
        startingChilds.add(left4l);
        startingChilds.add(right4l);
        left3.replaceChildrenListBy(startingChilds);

        right4l.setValue(Operators.HAIRPIN_LABEL + "(1,7)");
        left4l.setValue(Operators.HAIRPIN_LABEL + "(1,4)");

        //4th level right
        Tree<String> left4r = new Tree<>();
        Tree<String> right4r = new Tree<>();

        ArrayList<Tree<String>> startingChilds2 = new ArrayList<>();
        startingChilds2.add(left4r);
        startingChilds2.add(right4r);
        right3.replaceChildrenListBy(startingChilds2);

        right4r.setValue(Operators.HAIRPIN_LABEL + "(7,12)");
        left4r.setValue(Operators.HAIRPIN_LABEL + "(7,10)");

        return structuralTree;
    }

    private Tree<String> testP3(){
        //(1,6); (1,12); (6,12); (12,16); (17,21); (19,24);

        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.CONCATENATION_LABEL);

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        ArrayList<Tree<String>> concatChilds = new ArrayList<>();
        concatChilds.add(left);
        concatChilds.add(right);
        structuralTree.replaceChildrenListBy(concatChilds);

        right.setValue("(" + Operators.CROSSING_LABEL + ",1)");
        left.setValue(Operators.MEETING_LABEL);

        //2nd level Left
        Tree<String> left2l = new Tree<>();
        Tree<String> right2l = new Tree<>();

        ArrayList<Tree<String>> meetingChilds = new ArrayList<>();
        meetingChilds.add(left2l);
        meetingChilds.add(right2l);
        left.replaceChildrenListBy(meetingChilds);

        right2l.setValue(Operators.HAIRPIN_LABEL + "(12,16)");
        left2l.setValue(Operators.DIAMOND_LABEL);

        //2nd level Right
        Tree<String> left2r = new Tree<>();
        Tree<String> right2r = new Tree<>();

        ArrayList<Tree<String>> nestingChilds = new ArrayList<>();
        nestingChilds.add(left2r);
        nestingChilds.add(right2r);
        right.replaceChildrenListBy(nestingChilds);

        right2r.setValue(Operators.HAIRPIN_LABEL + "(19,24)");
        left2r.setValue(Operators.HAIRPIN_LABEL + "(17,21)");

        //3rd level
        Tree<String> left3 = new Tree<>();
        Tree<String> right3 = new Tree<>();

        ArrayList<Tree<String>> diamondChilds = new ArrayList<>();
        diamondChilds.add(left3);
        diamondChilds.add(right3);
        left2l.replaceChildrenListBy(diamondChilds);

        right3.setValue(Operators.HAIRPIN_LABEL + "(1,12)");
        left3.setValue(Operators.MEETING_LABEL);

        //4th level
        Tree<String> left4 = new Tree<>();
        Tree<String> right4 = new Tree<>();

        ArrayList<Tree<String>> meetingChilds2 = new ArrayList<>();
        meetingChilds2.add(left4);
        meetingChilds2.add(right4);
        left3.replaceChildrenListBy(meetingChilds2);

        right4.setValue(Operators.HAIRPIN_LABEL + "(6,12)");
        left4.setValue(Operators.HAIRPIN_LABEL + "(1,6)");

        return structuralTree;
    }

    private Tree<String> testP4(){
        //(1,6); (1,12); (6,12); (7,17); (12,17); (18,24); (18,22); (20,24);
        Tree<String> structuralTree = new Tree<>();

        structuralTree.setValue(Operators.CONCATENATION_LABEL);

        Tree<String> left = new Tree<>();
        Tree<String> right = new Tree<>();

        ArrayList<Tree<String>> concatChilds = new ArrayList<>();
        concatChilds.add(left);
        concatChilds.add(right);
        structuralTree.replaceChildrenListBy(concatChilds);

        right.setValue(Operators.DIAMOND_LABEL);
        left.setValue("(" + Operators.CROSSING_LABEL + ",2)");

        //2nd level Left
        Tree<String> left2l = new Tree<>();
        Tree<String> right2l = new Tree<>();

        ArrayList<Tree<String>> crossingChilds = new ArrayList<>();
        crossingChilds.add(left2l);
        crossingChilds.add(right2l);
        left.replaceChildrenListBy(crossingChilds);

        right2l.setValue(Operators.HAIRPIN_LABEL + "(7,17)");
        left2l.setValue(Operators.MEETING_LABEL);

        //2nd level Right
        Tree<String> left2r = new Tree<>();
        Tree<String> right2r = new Tree<>();

        ArrayList<Tree<String>> diamondChilds = new ArrayList<>();
        diamondChilds.add(left2r);
        diamondChilds.add(right2r);
        right.replaceChildrenListBy(diamondChilds);

        right2r.setValue(Operators.HAIRPIN_LABEL + "(18,24)");
        left2r.setValue("(" + Operators.CROSSING_LABEL + ",1)");

        //3rd level for second level left
        Tree<String> left3ll = new Tree<>();
        Tree<String> right3ll = new Tree<>();

        ArrayList<Tree<String>> crossingChilds2 = new ArrayList<>();
        crossingChilds2.add(left3ll);
        crossingChilds2.add(right3ll);
        left2r.replaceChildrenListBy(crossingChilds2);

        right3ll.setValue(Operators.HAIRPIN_LABEL + "(20,24)");
        left3ll.setValue(Operators.HAIRPIN_LABEL + "(18,22)");

        //3rd level
        Tree<String> left3 = new Tree<>();
        Tree<String> right3 = new Tree<>();

        ArrayList<Tree<String>> meetingChilds = new ArrayList<>();
        meetingChilds.add(left3);
        meetingChilds.add(right3);
        left2l.replaceChildrenListBy(meetingChilds);

        right3.setValue(Operators.HAIRPIN_LABEL + "(12,17)");
        left3.setValue(Operators.DIAMOND_LABEL);

        //4th level
        Tree<String> left4 = new Tree<>();
        Tree<String> right4 = new Tree<>();

        ArrayList<Tree<String>> diamondChilds2 = new ArrayList<>();
        diamondChilds2.add(left4);
        diamondChilds2.add(right4);
        left3.replaceChildrenListBy(diamondChilds2);

        right4.setValue(Operators.HAIRPIN_LABEL + "(1,12)");
        left4.setValue(Operators.MEETING_LABEL);

        //5th level
        Tree<String> left5 = new Tree<>();
        Tree<String> right5 = new Tree<>();

        ArrayList<Tree<String>> meetingChilds2 = new ArrayList<>();
        meetingChilds2.add(left5);
        meetingChilds2.add(right5);
        left4.replaceChildrenListBy(meetingChilds2);

        right5.setValue(Operators.HAIRPIN_LABEL + "(6,12)");
        left5.setValue(Operators.HAIRPIN_LABEL + "(1,6)");

        return structuralTree;
    }

    //Compare prof's trees and print their distances
    private void compareTrees() {

        Tree<String> tree1 = testP1();
        Tree<String> tree2 = testP2();
        Tree<String> tree3 = testP3();
        Tree<String> tree4 = testP4();

        String configurationFileName = ScoringFunction.DEFAULT_PROPERTY_FILE;
        ScoringFunction f = new ScoringFunction(configurationFileName);

        //1 with others
        System.out.println("\nComparing 1 with 1");
        compareTreesAndPrintDistance(tree1, tree1, f);
        System.out.println("\nComparing 1 with 2");
        compareTreesAndPrintDistance(tree1, tree2, f);
        System.out.println("\nComparing 1 with 3");
        compareTreesAndPrintDistance(tree1, tree3, f);
        System.out.println("\nComparing 1 with 4");
        compareTreesAndPrintDistance(tree1, tree4, f);

        //2 with others
        System.out.println("\nComparing 2 with 1");
        compareTreesAndPrintDistance(tree2, tree1, f);
        System.out.println("\nComparing 2 with 2");
        compareTreesAndPrintDistance(tree2, tree2, f);
        System.out.println("\nComparing 2 with 3");
        compareTreesAndPrintDistance(tree2, tree3, f);
        System.out.println("\nComparing 2 with 4");
        compareTreesAndPrintDistance(tree2, tree4, f);

        //3 with others
        System.out.println("\nComparing 3 with 1");
        compareTreesAndPrintDistance(tree3, tree1, f);
        System.out.println("\nComparing 3 with 2");
        compareTreesAndPrintDistance(tree3, tree2, f);
        System.out.println("\nComparing 3 with 3");
        compareTreesAndPrintDistance(tree3, tree3, f);
        System.out.println("\nComparing 3 with 4");
        compareTreesAndPrintDistance(tree3, tree4, f);

        //4 with others
        System.out.println("\nComparing 4 with 1");
        compareTreesAndPrintDistance(tree4, tree1, f);
        System.out.println("\nComparing 4 with 2");
        compareTreesAndPrintDistance(tree4, tree2, f);
        System.out.println("\nComparing 4 with 3");
        compareTreesAndPrintDistance(tree4, tree3, f);
        System.out.println("\nComparing 4 with 4");
        compareTreesAndPrintDistance(tree4, tree4, f);
    }

    private void compareTreesAndPrintDistance(Tree<String> tree1,  Tree<String> tree2, ScoringFunction f){
        AlignmentResult r = null;
        try {
            r = new AlignmentResult(tree1, tree2, f);
        } catch (TreeAlignException e) {
            System.err.println("ERROR: Alignment Exception: " + e.getMessage());
            System.exit(4);
        }
        Tree<AlignedNode<String, String>> t = r.getAlignedTree();
        System.out.println(TreeOutputter.treeToStringAligned(t));
        double distance = r.getDistance();
        System.out.println("Distance = " + distance);
    }

    /**
     * Iterate the trees and checks if they have the same values in each nodes
     * @param expectedTree the expected tree
     * @param bonds bonds for the actual tree
     * @return true if the trees are equals false otherwise
     */
    private boolean isEquals(Tree<String> expectedTree, ArrayList<Pair<Integer>> bonds, int sequenceLenght ) {

        System.out.println("\nNew Tree\n");

        TERSAlignTree TERSAlignTree = CreateStructuralTreeWithCustomBonds(bonds,sequenceLenght);
        Tree<String> actualTree = TERSAlignTree.getStructuralTree();

        //If the trees don't have the same number of nodes return false
        if(expectedTree.countNodes() != actualTree.countNodes()) {
            System.out.println("Trees length is different: expected " + expectedTree.countNodes() + " actual " + actualTree.countNodes());
            return false;
        }

        Iterator<Tree<String>> expectedIterator = expectedTree.iterator();
        Iterator<Tree<String>> actualIterator = actualTree.iterator();

        while(expectedIterator.hasNext() && actualIterator.hasNext()) {
            Tree<String> expectedNode = expectedIterator.next();
            Tree<String> actualNode = actualIterator.next();
            System.out.println(expectedNode.getValue() + " vs " + actualNode.getValue());
            if(!expectedNode.getValue().equals(actualNode.getValue())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Creates a new structual tree by loading a random structure but replacing the current bonds with
     * the bonds passed as parameter
     * @param bonds bonds for the actual tree
     * @param sequenceLength sequence length of the actual tree
     * @return the new structual tree
     */
    private TERSAlignTree CreateStructuralTreeWithCustomBonds(ArrayList<Pair<Integer>> bonds, int sequenceLength) {
        Structure structure = null;
        try {
            structure = StructureIO.getStructure("3mge");
        } catch (IOException | StructureException e) {
            e.printStackTrace();
        }
        TertiaryStructure tertiaryStructure = new TertiaryStructure(structure);
        tertiaryStructure.setBondList(bonds);
        TERSAlignTree treeGenerator = new TERSAlignTree(tertiaryStructure);
        treeGenerator.setSequenceLength(sequenceLength);
        return treeGenerator;
    }
}