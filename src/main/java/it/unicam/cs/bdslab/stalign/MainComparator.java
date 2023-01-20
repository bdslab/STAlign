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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import at.unisalzburg.dbresearch.apted.costmodel.PerEditOperationStringNodeDataCostModel;
import at.unisalzburg.dbresearch.apted.distance.APTED;
import at.unisalzburg.dbresearch.apted.node.Node;
import at.unisalzburg.dbresearch.apted.node.StringNodeData;
import at.unisalzburg.dbresearch.apted.parser.BracketStringInputParser;
import fr.orsay.lri.varna.models.treealign.*;
import org.apache.commons.cli.*;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.contact.Pair;
import org.biojava.nbio.structure.io.PDBFileReader;

/**
 * MainComparator class interacting with the user through command line options.
 *
 * @author Luca Tesei, Filippo Lampa, Marco Serenelli
 *
 */
public class MainComparator {

    public static void main(String[] args) {
        // Use Apache Commons CLI 1.4
        // create Options object for Command Line Definition
        Options options = new Options();
        // define command line options
        Option o1 = new Option("sc","structcode",true,"Produce the structural RNA/Protein tree corresponding to the given structure by PDB code");
        o1.setArgName("input-pdb-code");
        options.addOption(o1);
        Option o2 = new Option("sf","structfile",true,"Produce the structural RNA/Protein tree corresponding to the given structure by PDB file");
        o2.setArgName("input-file");
        options.addOption(o2);
        Option o12 = new Option("sm","structcustom",true,"Produce the structural RNA/Protein tree corresponding to the AAS file");
        o12.setArgName("input-file");
        options.addOption(o12);
        Option o3 = new Option("ac","aligncode",true,"Align two given structures by PDB code producing alignment tree and distance");
        o3.setArgs(2);
        o3.setArgName("input-pdb-code1 input-pdb-code2");
        options.addOption(o3);
        Option o11 = new Option("af","alignfile",true,"Align two given structures by PDB file producing alignment tree and distance");
        o11.setArgs(2);
        o11.setArgName("input-file1 input-file2");
        options.addOption(o11);
        Option o13 = new Option("am","aligncustom",true,"Align two given structures by AAS file producing alignment tree and distance");
        o13.setArgs(2);
        o13.setArgName("input-file1 input-file2");
        options.addOption(o13);
        Option o17 = new Option("edc","editdistancecode",true,"Calculates the edit distance of two given structures by PDB code");
        o17.setArgs(2);
        o17.setArgName("input-pdb-code1 input-pdb-code2");
        options.addOption(o17);
        Option o18 = new Option("edf","editdistancefile",true,"Calculates the edit distance of two given structures by PDB file");
        o18.setArgs(2);
        o18.setArgName("input-file1 input-file2");
        options.addOption(o18);
        Option o19 = new Option("edm","editdistancecustom",true,"Calculates the edit distance of two given structures by AAS file");
        o19.setArgs(2);
        o19.setArgName("input-file1 input-file2");
        options.addOption(o19);
        Option o4 = new Option("o","out",true,"Output result on the given file instead of standard output");
        o4.setArgName("output-file");
        options.addOption(o4);
        Option o5 = new Option("l","latexout",false,"Output in LaTeX format instead of linearised tree");
        options.addOption(o5);
        Option o6 = new Option("i","info",false,"Show license and other info");
        options.addOption(o6);
        Option o7 = new Option("h","help",false,"Show usage information");
        options.addOption(o7);
        Option o8 = new Option("d","outdist",false,"Output only distance, no alignment tree");
        options.addOption(o8);
        Option o9 = new Option("e","showscores",false,"Show current values of edit scores used for alignment");
        options.addOption(o9);
        Option o10 = new Option("n","useconffile",true,"Use the specified configuration file instead of the default one");
        o10.setArgName("conf-file");
        options.addOption(o10);
        Option o14 = new Option("cm","centerofmass",false,"Calculate the distance matrix with center of mass method");
        options.addOption(o14);
        Option o15 = new Option("t","threshold",true,"Set a threshold");
        o15.setArgName("threshold");
        options.addOption(o15);
        Option o16 = new Option("p","selectchains",true,"Calculate only the specific chains of a structure");
        o16.setArgs(Option.UNLIMITED_VALUES);
        o16.setArgName("chain-id");
        options.addOption(o16);
        Option o20 = new Option("ssc", "sscode", true, "Produce the secondary structure structural RNA/Protein tree corresponding to the given structure by PDB code");
        o20.setArgName("input-file");
        options.addOption(o20);
        Option o21 = new Option("ssf", "ssfile", true, "Produce the secondary structure structural RNA/Protein tree corresponding to the given structure by PDB file");
        o21.setArgName("input-file");
        options.addOption(o21);
        Option o22 = new Option("ssac","ssaligncode",true,"Align two given secondary structures by PDB code producing alignment tree and distance");
        o22.setArgs(2);
        o22.setArgName("input-file1 input-file2");
        options.addOption(o22);
        Option o23 = new Option("ssaf","ssalignfile",true,"Align two given secondary structures by PDB file producing alignment tree and distance");
        o23.setArgs(2);
        o23.setArgName("input-file1 input-file2");
        options.addOption(o23);

        // Parse command line
        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            // oops, something went wrong
            System.err.println("ERROR: Command Line parsing failed.  Reason: " + e.getMessage() + "\n");
            formatter.printHelp(CommandLineMessages.LAUNCH_COMMAND, CommandLineMessages.HEADER, options,
                    CommandLineMessages.USAGE_EXAMPLES + CommandLineMessages.COPYRIGHT
                            + CommandLineMessages.SHORT_NOTICE + CommandLineMessages.REPORT_TO,
                    true);
            System.exit(1);
        }

        // Manage Option h
        if (cmd.hasOption("h")) {
            formatter.printHelp(CommandLineMessages.LAUNCH_COMMAND, CommandLineMessages.HEADER, options,
                    CommandLineMessages.USAGE_EXAMPLES + CommandLineMessages.COPYRIGHT
                            + CommandLineMessages.SHORT_NOTICE + CommandLineMessages.REPORT_TO,
                    true);
            return;
        }

        // Manage Option i
        if (cmd.hasOption("i")) {
            Options optionsEmpty = new Options();
            formatter
                    .printHelp(CommandLineMessages.LAUNCH_COMMAND, "", optionsEmpty,
                            CommandLineMessages.COPYRIGHT + CommandLineMessages.LONG_NOTICE
                                    + CommandLineMessages.REPORT_TO + "\n\nUse option -h for full usage information",
                            true);
            return;
        }

        // Manage Option n
        String configurationFileName = ScoringFunction.DEFAULT_PROPERTY_FILE;
        if (cmd.hasOption("n")) {
            configurationFileName = cmd.getOptionValue("n");
        }

        // Manage Option e
        if (cmd.hasOption("e")) {
            ScoringFunction f = new ScoringFunction(configurationFileName);
            String scores = "STAlign current costs:\n\n" + "Cost for Operator Insertion = "
                    + f.getInsertOperatorCost() + "\nCost for Operator Deletion = " + f.getDeleteOperatorCost()
                    + "\nCost for Operator Replacement with Operator = " + f.getReplaceOperatorCost()
                    + "\nCost for Hairpin Insertion = " + f.getInsertHairpinCost() + "\nCost for Hairpin Deletion = "
                    + f.getDeleteHairpinCost() + "\nCost for One Crossing Mismatch (Local Cost) = "
                    + f.getCrossingMismatchCost() + "\nCost for Edit Distance Insertion (Local Cost) = "
                    + f.getEditdistanceInsertCost() + "\nCost for Edit Distance Deletion (Local Cost) = "
                    + f.getEditdistanceDeleteCost() + "\nCost for Edit Distance Renaming (Local Cost) = "
                    + f.getEditdistanceRenameCost();
            System.out.println(scores);
            return;
        }

        //Manage option ss (Secondary Structure)
        if(cmd.hasOption("ssc") || cmd.hasOption("ssf")) {
            boolean filepath = cmd.hasOption("ssf");
            SecondaryStructure secondaryStructure = null;
            Structure struc;
            try{
                if(filepath) {
                    String filename = cmd.getOptionValue("ssf");
                    PDBFileReader pdbreader = new PDBFileReader();
                    struc = pdbreader.getStructure(filename);
                } else {
                    struc = StructureIO.getStructure(cmd.getOptionValue("ssc"));
                }
                secondaryStructure = new SecondaryStructure(struc);
            } catch (Exception e) {
                System.err.println("ERROR:" + e.getMessage());
                System.exit(3);
            }

            // Construct the TERSAtree
            Tree<String> t;
            TERSAlignTree tree = new TERSAlignTree(secondaryStructure);

            // get the structural RNA/Protein tree
            t = tree.getStructuralTree();
            // Produce Output
            String output;
            if (cmd.hasOption("l"))
                // produce LaTeX
                output = TreeOutputter.toLatex(t);
            else
                // produce linearised tree
                output = TreeOutputter.treeToString(t);

            // Write Output on proper file or on standard output
            if (cmd.hasOption("o")) {
                String outputFile = cmd.getOptionValue("o");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, false))) {
                    writer.write(output);
                } catch (FileNotFoundException e) {
                    System.err.println("ERROR: Output file " + outputFile + " cannot be created.");
                    System.exit(3);
                } catch (IOException e) {
                    System.err.println("ERROR: Output file" + outputFile + " cannot be written.");
                    System.exit(3);
                }
            } else
                System.out.println(output);
            return;
        }

        // Manage option s
        if (cmd.hasOption("sc") || cmd.hasOption("sf") || cmd.hasOption("sm")) {
            boolean filepath = cmd.hasOption("sf");
            boolean custom = cmd.hasOption("sm");
            TertiaryStructure tertiaryStructure = null;
            Structure struc;
            try {
                if(!custom) {
                    if (filepath) {
                        String filename = cmd.getOptionValue("sf");
                        PDBFileReader pdbreader = new PDBFileReader();
                        struc = pdbreader.getStructure(filename);
                    } else {
                        struc = StructureIO.getStructure(cmd.getOptionValue("sc"));
                    }
                    tertiaryStructure = new TertiaryStructure(struc);
                } else {
                    struc = StructureIO.getStructure("3mge");
                    tertiaryStructure = new TertiaryStructure(struc);
                    tertiaryStructure.setBondList(TertiaryStructureBondsOptionalSequenceFileReader.readBondsList(cmd.getOptionValue("sm")));
                    tertiaryStructure.setSequence(TertiaryStructureBondsOptionalSequenceFileReader.readSequence(cmd.getOptionValue("sm")));
                }
            } catch (Exception e) {
                System.err.println("ERROR:" + e.getMessage());
                System.exit(3);
            }

            if(cmd.hasOption("p")){
                ArrayList<String> chainIds = new ArrayList<>();
                Collections.addAll(chainIds, cmd.getOptionValues("p"));
                tertiaryStructure.setSpecifiedChains(chainIds);
            }

            if (cmd.hasOption("t")) {
                double threshold = Double.parseDouble(cmd.getOptionValue("t"));
                tertiaryStructure.setThreshold(threshold);
            }

            //manage option cm
            if(cmd.hasOption("cm"))
                tertiaryStructure.setDistanceMatrixCalculationMethod("centerofmass");

            // Construct the TERSAtree
            Tree<String> t = null;
            TERSAlignTree tree = new TERSAlignTree(tertiaryStructure);
            if(custom)
                tree.setSequenceLength(calculateLastSequenceIndex(tertiaryStructure.getBondList()) + 1);
            // get the structural RNA/Protein tree
            try {
                t = tree.getStructuralTree();
            }catch (StackOverflowError e){
                System.err.println("The molecule is too big, can't generate the associated tree");
                System.exit(1);
            }
            // Produce Output
            String output;
            if (cmd.hasOption("l"))
                // produce LaTeX
                output = TreeOutputter.toLatex(t);
            else
                // produce linearised tree
                output = TreeOutputter.treeToString(t);

            // Write Output on proper file or on standard output
            if (cmd.hasOption("o")) {
                String outputFile = cmd.getOptionValue("o");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, false))) {
                    writer.write(output);
                } catch (FileNotFoundException e) {
                    System.err.println("ERROR: Output file " + outputFile + " cannot be created.");
                    System.exit(3);
                } catch (IOException e) {
                    System.err.println("ERROR: Output file" + outputFile + " cannot be written.");
                    System.exit(3);
                }
            } else
                System.out.println(output);
            return;
        }

        // Manage Option a
        if (cmd.hasOption("ac") || cmd.hasOption("af") || cmd.hasOption("am")) {
            boolean filePath = cmd.hasOption("af");
            boolean custom = cmd.hasOption("am");
            // variables for structural RNA trees
            Tree<String> t1 = null;
            Tree<String> t2 = null;
            // Parse the first input file for the secondary structure
            Structure struc;
            TertiaryStructure tertiaryStructure = null;
            try {
                if(!custom) {
                    if (filePath) {
                        PDBFileReader pdbreader = new PDBFileReader();
                        struc = pdbreader.getStructure(cmd.getOptionValues("af")[0]);
                    } else {
                        struc = StructureIO.getStructure(cmd.getOptionValues("ac")[0]);
                    }
                    tertiaryStructure = new TertiaryStructure(struc);
                } else {
                    struc = StructureIO.getStructure("3mge");
                    tertiaryStructure = new TertiaryStructure(struc);
                    tertiaryStructure.setBondList(TertiaryStructureBondsOptionalSequenceFileReader.readBondsList(cmd.getOptionValues("am")[0]));
                }
            } catch (Exception e) {
                System.err.println("ERROR:" + e.getMessage());
                System.exit(3);
            }

            if(cmd.hasOption("p")){
                ArrayList<String> chainIds = new ArrayList<>();
                Collections.addAll(chainIds, cmd.getOptionValues("p"));
                tertiaryStructure.setSpecifiedChains(chainIds);
            }

            if (cmd.hasOption("t")) {
                double threshold = Double.parseDouble(cmd.getOptionValue("t"));
                tertiaryStructure.setThreshold(threshold);
            }

            //manage option cm
            if(cmd.hasOption("cm"))
                tertiaryStructure.setDistanceMatrixCalculationMethod("centerofmass");

            // Construct structural RNA/Protein tree 1
            TERSAlignTree s1 = new TERSAlignTree(tertiaryStructure);
            if(custom)
                s1.setSequenceLength(calculateLastSequenceIndex(tertiaryStructure.getBondList()) + 1);
            try {
                t1 = s1.getStructuralTree();
            }catch (StackOverflowError e){
                System.err.println("The molecule is too big, can't generate the associated tree");
                System.exit(1);
            }

            // Parse the second input file for the secondary structure
            Structure struc2;
            TertiaryStructure tertiaryStructure2 = null;
            try {
                if(!custom) {
                    if (filePath) {
                        PDBFileReader pdbreader = new PDBFileReader();
                        struc2 = pdbreader.getStructure(cmd.getOptionValues("af")[1]);
                    } else {
                        struc2 = StructureIO.getStructure(cmd.getOptionValues("ac")[1]);
                    }
                    tertiaryStructure2 = new TertiaryStructure(struc2);
                } else {
                    struc2 = StructureIO.getStructure("3mge");
                    tertiaryStructure2 = new TertiaryStructure(struc2);
                    tertiaryStructure2.setBondList(TertiaryStructureBondsOptionalSequenceFileReader.readBondsList(cmd.getOptionValues("am")[1]));
                }
            } catch (Exception e) {
                System.err.println("ERROR:" + e.getMessage());
                System.exit(3);
            }

            if(cmd.hasOption("p")){
                ArrayList<String> chainIds = new ArrayList<>();
                Collections.addAll(chainIds, cmd.getOptionValues("p"));
                tertiaryStructure2.setSpecifiedChains(chainIds);
            }

            if (cmd.hasOption("t")) {
                double threshold = Double.parseDouble(cmd.getOptionValue("t"));
                tertiaryStructure2.setThreshold(threshold);
            }

            //manage option cm
            if(cmd.hasOption("cm"))
                tertiaryStructure2.setDistanceMatrixCalculationMethod("centerofmass");

            // Construct structural RNA/Protein tree 2
            TERSAlignTree s2 = new TERSAlignTree(tertiaryStructure2);
            if(custom)
                s2.setSequenceLength(calculateLastSequenceIndex(tertiaryStructure2.getBondList()) + 1);
            try {
                t2 = s2.getStructuralTree();
            }catch (StackOverflowError e){
                System.err.println("The molecule is too big, can't generate the associated tree");
                System.exit(1);
            }

            //Align tree
            alignTrees(t1, t2, configurationFileName, cmd);
            return;
        }

        // Manage Option SSa
        if (cmd.hasOption("ssac") || cmd.hasOption("ssaf")) {
            boolean filePath = cmd.hasOption("ssaf");

            // variables for structural RNA trees
            Tree<String> t1 = null;
            Tree<String> t2 = null;
            // Parse the first input file for the secondary structure
            Structure struc;
            SecondaryStructure secondaryStructure = null;
            try {
                if (filePath) {
                    PDBFileReader pdbreader = new PDBFileReader();
                    struc = pdbreader.getStructure(cmd.getOptionValues("ssaf")[0]);
                } else {
                    struc = StructureIO.getStructure(cmd.getOptionValues("ssac")[0]);
                }
                secondaryStructure = new SecondaryStructure(struc);
            } catch (Exception e) {
                System.err.println("ERROR:" + e.getMessage());
                System.exit(3);
            }

            // Construct structural RNA/Protein tree 1
            TERSAlignTree s1 = new TERSAlignTree(secondaryStructure);
            try {
                t1 = s1.getStructuralTree();
            }catch (StackOverflowError e){
                System.err.println("The molecule is too big, can't generate the associated tree");
                System.exit(1);
            }

            // Parse the second input file for the secondary structure
            Structure struc2;
            SecondaryStructure secondaryStructure2 = null;
            try {
                if (filePath) {
                    PDBFileReader pdbreader = new PDBFileReader();
                    struc2 = pdbreader.getStructure(cmd.getOptionValues("ssaf")[1]);
                } else {
                    struc2 = StructureIO.getStructure(cmd.getOptionValues("ssac")[1]);
                }
                secondaryStructure2 = new SecondaryStructure(struc2);
            } catch (Exception e) {
                System.err.println("ERROR:" + e.getMessage());
                System.exit(3);
            }

            // Construct structural RNA/Protein tree 2
            TERSAlignTree s2 = new TERSAlignTree(secondaryStructure2);
            try {
                t2 = s2.getStructuralTree();
            }catch (StackOverflowError e){
                System.err.println("The molecule is too big, can't generate the associated tree");
                System.exit(1);
            }

            //Align trees
            alignTrees(t1, t2, configurationFileName, cmd);
            return;
        }

        // Manage Option e
        if (cmd.hasOption("edc") || cmd.hasOption("edf") || cmd.hasOption("edm")) {
            boolean filePath = cmd.hasOption("edf");
            boolean custom = cmd.hasOption("edm");
            // variables for structural RNA trees
            Tree<String> t1 = null;
            Tree<String> t2 = null;
            // Parse the first input file for the secondary structure
            Structure struc;
            TertiaryStructure tertiaryStructure = null;
            try {
                if(!custom) {
                    if (filePath) {
                        PDBFileReader pdbreader = new PDBFileReader();
                        struc = pdbreader.getStructure(cmd.getOptionValues("edf")[0]);
                    } else {
                        struc = StructureIO.getStructure(cmd.getOptionValues("edc")[0]);
                    }
                    tertiaryStructure = new TertiaryStructure(struc);
                } else {
                    struc = StructureIO.getStructure("3mge");
                    tertiaryStructure = new TertiaryStructure(struc);
                    tertiaryStructure.setBondList(TertiaryStructureBondsOptionalSequenceFileReader.readBondsList(cmd.getOptionValues("edm")[0]));
                }
            } catch (Exception e) {
                System.err.println("ERROR:" + e.getMessage());
                System.exit(3);
            }

            if(cmd.hasOption("p")){
                ArrayList<String> chainIds = new ArrayList<>();
                Collections.addAll(chainIds, cmd.getOptionValues("p"));
                tertiaryStructure.setSpecifiedChains(chainIds);
            }

            if (cmd.hasOption("t")) {
                double threshold = Double.parseDouble(cmd.getOptionValue("t"));
                tertiaryStructure.setThreshold(threshold);
            }

            //manage option cm
            if(cmd.hasOption("m"))
                tertiaryStructure.setDistanceMatrixCalculationMethod("centerofmass");

            // Construct structural RNA/Protein tree 1
            TERSAlignTree s1 = new TERSAlignTree(tertiaryStructure);
            if(custom)
                s1.setSequenceLength(calculateLastSequenceIndex(tertiaryStructure.getBondList()) + 1);
            try {
                t1 = s1.getStructuralTree();
            }catch (StackOverflowError e){
                System.err.println("The molecule is too big, can't generate the associated tree");
                System.exit(1);
            }

            // Parse the second input file for the secondary structure
            Structure struc2;
            TertiaryStructure tertiaryStructure2 = null;
            try {
                if(!custom) {
                    if (filePath) {
                        PDBFileReader pdbreader = new PDBFileReader();
                        struc2 = pdbreader.getStructure(cmd.getOptionValues("edf")[1]);
                    } else {
                        struc2 = StructureIO.getStructure(cmd.getOptionValues("edc")[1]);
                    }
                    tertiaryStructure2 = new TertiaryStructure(struc2);
                } else {
                    struc2 = StructureIO.getStructure("3mge");
                    tertiaryStructure2 = new TertiaryStructure(struc2);
                    tertiaryStructure2.setBondList(TertiaryStructureBondsOptionalSequenceFileReader.readBondsList(cmd.getOptionValues("edm")[1]));
                }
            } catch (Exception e) {
                System.err.println("ERROR:" + e.getMessage());
                System.exit(3);
            }

            if(cmd.hasOption("p")){
                ArrayList<String> chainIds = new ArrayList<>();
                Collections.addAll(chainIds, cmd.getOptionValues("p"));
                tertiaryStructure2.setSpecifiedChains(chainIds);
            }

            if (cmd.hasOption("t")) {
                double threshold = Double.parseDouble(cmd.getOptionValue("t"));
                tertiaryStructure2.setThreshold(threshold);
            }

            //manage option cm
            if(cmd.hasOption("cm"))
                tertiaryStructure2.setDistanceMatrixCalculationMethod("centerofmass");

            // Construct structural RNA/Protein tree 2
            TERSAlignTree s2 = new TERSAlignTree(tertiaryStructure2);
            if(custom)
                s2.setSequenceLength(calculateLastSequenceIndex(tertiaryStructure2.getBondList()) + 1);
            try {
                t2 = s2.getStructuralTree();
            }catch (StackOverflowError e){
                System.err.println("The molecule is too big, can't generate the associated tree");
                System.exit(1);
            }

            // Edit t1 and t2, which contain two structural RNA trees

            ScoringFunction f = new ScoringFunction(configurationFileName);
            float distance;
            BracketStringInputParser parser = new BracketStringInputParser();
            Node<StringNodeData> editDistanceTree1 = parser.fromString("{" + TreeOutputter.treeToAptedInput(t1) + "}");
            Node<StringNodeData> editDistanceTree2 = parser.fromString("{" + TreeOutputter.treeToAptedInput(t2) + "}");
            APTED<PerEditOperationStringNodeDataCostModel, StringNodeData> apted = new APTED<>(new PerEditOperationStringNodeDataCostModel((float)f.getEditdistanceDeleteCost(),(float)f.getEditdistanceInsertCost(),(float)f.getEditdistanceRenameCost()));
            distance = apted.computeEditDistance(editDistanceTree1, editDistanceTree2);
            // Produce Output
            if (!cmd.hasOption("d")) {
                StringBuilder output = new StringBuilder();
                // Output edit distance nodes mapping
                output.append("{");
                apted.computeEditMapping().forEach(entry-> {
                    output.append("[");
                    output.append(entry[0]).append(",").append(entry[1]);
                    output.append("]");
                    output.append(";");
                });
                output.append("}");

                // Write Output on proper file or on standard output
                if (cmd.hasOption("o")) {
                    String outputFile = cmd.getOptionValue("o");
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, false))) {
                        writer.write(output.toString());
                    } catch (FileNotFoundException e) {
                        System.err.println("ERROR: Output file " + outputFile + " cannot be created.");
                        System.exit(3);
                    } catch (IOException e) {
                        System.err.println("ERROR: Output file" + outputFile + " cannot be written.");
                        System.exit(3);
                    }
                } else
                    System.out.println(output + "\n");
            }
            // Output distance
            System.out.println("Distance = " + distance);
            return;
        }

        // If no option is given, output usage
        System.err.println("No operation specified...");
        formatter.printHelp(CommandLineMessages.LAUNCH_COMMAND, CommandLineMessages.HEADER, options,
                CommandLineMessages.USAGE_EXAMPLES + CommandLineMessages.COPYRIGHT + CommandLineMessages.SHORT_NOTICE
                        + CommandLineMessages.REPORT_TO,
                true);
    }

    private static void alignTrees(Tree<String> t1, Tree<String> t2, String configurationFileName, CommandLine cmd) {
        // Align t1 and t2, which contain two structural RNA trees
        AlignmentResult r = null;
        ScoringFunction f = new ScoringFunction(configurationFileName);
        try {
            r = new AlignmentResult(t1, t2, f);
        } catch (TreeAlignException e) {
            System.err.println("ERROR: Alignment Exception: " + e.getMessage());
            System.exit(4);
        }

        // Produce Output
        if (!cmd.hasOption("d")) {
            Tree<AlignedNode<String, String>> t = r.getAlignedTree();
            String output;
            if (cmd.hasOption("l"))
                // produce LaTeX
                output = TreeOutputter.toLatexAligned(t);
            else
                // produce linearised tree
                output = TreeOutputter.treeToStringAligned(t);
            // Write Output on proper file or on standard output
            if (cmd.hasOption("o")) {
                String outputFile = cmd.getOptionValue("o");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, false))) {
                    writer.write(output);
                } catch (FileNotFoundException e) {
                    System.err.println("ERROR: Output file " + outputFile + " cannot be created.");
                    System.exit(3);
                } catch (IOException e) {
                    System.err.println("ERROR: Output file" + outputFile + " cannot be written.");
                    System.exit(3);
                }
            } else
                System.out.println(output + "\n");
        }
        // Output distance
        System.out.println("Distance = " + r.getDistance());
    }


    private static int calculateLastSequenceIndex(ArrayList<Pair<Integer>> bondList) {
        int lastIndex = 0;
        for(Pair<Integer> currentPair : bondList)
            if(currentPair.getSecond() > lastIndex)
                lastIndex = currentPair.getSecond();
        return lastIndex;
    }
}
