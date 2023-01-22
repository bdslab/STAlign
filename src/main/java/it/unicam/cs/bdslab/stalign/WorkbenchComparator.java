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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

//import javax.swing.JFileChooser;
//import javax.swing.JOptionPane;

import at.unisalzburg.dbresearch.apted.costmodel.PerEditOperationStringNodeDataCostModel;
import at.unisalzburg.dbresearch.apted.distance.APTED;
import at.unisalzburg.dbresearch.apted.node.Node;
import at.unisalzburg.dbresearch.apted.node.StringNodeData;
import at.unisalzburg.dbresearch.apted.parser.BracketStringInputParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fr.orsay.lri.varna.models.treealign.Tree;
import fr.orsay.lri.varna.models.treealign.TreeAlignException;
import org.apache.commons.io.FilenameUtils;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.contact.Pair;
import org.biojava.nbio.structure.io.PDBFileReader;

/**
 * This class contains a main that runs the STAlign comparison algorithm among
 * all the RNA secondary structures (with arbitrary pseudoknots) in a given
 * folder.
 *
 * Two comma-separated-values files are produced with the description of the
 * processed files and the distance among all the pairs of molecules. Additional
 * information about the size of the molecules and the execution times is output
 * as well.
 *
 * @author Luca Tesei
 *
 */
public class WorkbenchComparator {

    public static void main(String[] args) {
        // Use Apache Commons CLI 1.4
        // create Options object for Command Line Definition
        Options options = new Options();

        // define command line options
        Option o1 = new Option("f","input",true,"Process the files in the given folder");
        o1.setArgName("input-folder");
        options.addOption(o1);
        Option o2 = new Option("o","output",true,"Output structure descriptions on file-1 and comparison results on file-2 instead of generating the default ouput files");
        o2.setArgs(2);
        o2.setArgName("file-1 file-2");
        options.addOption(o2);
        Option o6 = new Option("i","info",false,"Show license and other info");
        options.addOption(o6);
        Option o7 = new Option("h","help",false,"Show usage information");
        options.addOption(o7);
        Option o9 = new Option("e","showscores",false,"Show current values of edit scores used for alignment");
        options.addOption(o9);
        Option o10 = new Option("n","useconffile",true,"Use the specified configuration file instead of the default one");
        o10.setArgName("conf-file");
        options.addOption(o10);
        Option o11 = new Option("fm","inputcustom",true,"Process the AAS files in the given folder");
        o11.setArgName("input-folder");
        options.addOption(o11);
        Option o12 = new Option("cm","centerofmass",false,"Calculate the distance matrix with center of mass method");
        options.addOption(o12);
        Option o13 = new Option("t","threshold",true,"Set a threshold");
        o13.setArgName("threshold");
        options.addOption(o13);
        Option o14 = new Option("edfm","editdistanceinputcustom",true,"Process the AAS files in the given folder and calculate edit distance");
        o14.setArgName("input-folder");
        options.addOption(o14);
        Option o15 = new Option("edf","editdistanceinput",true,"Process the files in the given folder and calculate edit distance");
        o15.setArgName("input-folder");
        options.addOption(o15);

        // Parse command line
        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            // oops, something went wrong
            System.err.println("ERROR: Command Line parsing failed.  Reason: " + e.getMessage() + "\n");
            formatter.printHelp(CommandLineMessages.LAUNCH_COMMAND_WB, CommandLineMessages.HEADER_WB, options,
                    CommandLineMessages.USAGE_EXAMPLES_WB + CommandLineMessages.COPYRIGHT
                            + CommandLineMessages.SHORT_NOTICE + CommandLineMessages.REPORT_TO,
                    true);
            System.exit(1);
        }

        // Manage Option h
        if (cmd.hasOption("h")) {
            formatter.printHelp(CommandLineMessages.LAUNCH_COMMAND_WB, CommandLineMessages.HEADER_WB, options,
                    CommandLineMessages.USAGE_EXAMPLES_WB + CommandLineMessages.COPYRIGHT
                            + CommandLineMessages.SHORT_NOTICE + CommandLineMessages.REPORT_TO,
                    true);
            return;
        }

        // Manage Option i
        if (cmd.hasOption("i")) {
            Options optionsEmpty = new Options();
            formatter
                    .printHelp(CommandLineMessages.LAUNCH_COMMAND_WB, "", optionsEmpty,
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

        // Manage option f
        if (cmd.hasOption("f") || cmd.hasOption("fm")) {
            boolean custom = cmd.hasOption("fm");
            // Process a folder
            // Get folder file from command line
            File inputDirectory;
            if(custom)
                inputDirectory = new File(cmd.getOptionValue("fm"));
            else
                inputDirectory = new File(cmd.getOptionValue("f"));
            // Variables for counting execution time
            long startTimeNano;
            long elapsedTimeNano;
            // Maps for holding all the structures to be processed and their associated
            // processing time
            Map<File, TERSAlignTree> structures = new HashMap<>();
            Map<File, Long> structuresProcessingTime = new HashMap<>();
            // List for holding all the structures files
            List<File> structuresList = new ArrayList<>();
            // Set of skipped files
            Set<File> skippedFiles = new HashSet<>();
            int numStructures = 1;

            // Process input files
            if (!inputDirectory.isDirectory()) {
                System.err.println("ERROR: Input file " + cmd.getOptionValue("f") + " is not a folder");
                System.exit(1);
            }
            File[] fs = inputDirectory.listFiles();
            // Filter only files and put them in the list
            for (File file : fs)
                if (!file.isDirectory())
                    if (!file.isHidden())
                        if((FilenameUtils.getExtension(file.getName()).equals("pdb") && !custom) || (FilenameUtils.getExtension(file.getName()).equals("txt") && custom))
                            structuresList.add(file);
                        else
                            System.err.println("WARNING: Skipping unrecognized file " + file.getName() + " ...");
                    else
                        System.err.println("WARNING: Skipping hidden file " + file.getName() + " ...");
                else
                    System.err.println("WARNING: Skipping subfolder " + file.getName() + " ...");

            // Order files to be processed
            Collections.sort(structuresList);

            // Output files creation
            PrintStream outputStream = null;
            PrintStream structuresStream = null;
            String outputStreamName = inputDirectory.getAbsolutePath() + "/" + "STAlignComparisonResults.csv";
            String structuresStreamName = inputDirectory.getAbsolutePath() + "/" + "STAlignProcessedStructures.csv";

            // Manage option "o"
            if (cmd.hasOption("o")) {
                String[] names = cmd.getOptionValues("o");
                structuresStreamName = names[0];
                outputStreamName = names[1];
            }

            try {
                outputStream = new PrintStream(outputStreamName);
                structuresStream = new PrintStream(structuresStreamName);
            } catch (FileNotFoundException e) {
                System.err.println("ERROR: creation of output file "
                        + (outputStream == null ? outputStreamName : structuresStreamName) + " failed");
                System.exit(3);
            }
            // Write column names on the csv output files
            if(!custom) {
                structuresStream.println("Num,FileName,NumberOfNucleotides,NumberOfWeakBonds,TimeToGenerateStructuralTree[ns]");
                outputStream.println(
                        "FileName1,NumberOfNucleotides1,NumberOfWeakBonds1,TimeToGenerateStructuralTree1[ns],"
                                + "FileName2,NumberOfNucleotides2,NumberOfWeakBonds2,TimeToGenerateStructuralTree2[ns],"
                                + "MaxNumberOfNucleotides1-2,ASADistance,TimeToCalculateASADistance[ns]");
            } else {
                structuresStream.println("Num,FileName,NumberOfWeakBonds,TimeToGenerateStructuralTree[ns]");
                outputStream.println(
                        "FileName1,NumberOfWeakBonds1,TimeToGenerateStructuralTree1[ns],"
                                + "FileName2,NumberOfWeakBonds2,TimeToGenerateStructuralTree2[ns],"
                                + "ASADistance,TimeToCalculateASADistance[ns]");
            }
            // Load configuration file for costs
            ScoringFunction f = new ScoringFunction(configurationFileName);

            // Main Loop
            ListIterator<File> extIt = structuresList.listIterator();
            while (extIt.hasNext()) {
                // Compare the next element with all the subsequent elements
                int currentExtIndex = extIt.nextIndex();
                // Process File 1
                File f1 = extIt.next();
                // Check if skipped
                if (skippedFiles.contains(f1))
                    // skip this file
                    continue;

                // Retrieve the Structural RNA Tree for the structure 1
                TERSAlignTree st1;
                Tree<String> t1 = null;
                // Check if this structure has already been processed
                if (!structures.containsKey(f1)) {
                    // Parse the input file f1 for the secondary structure
                    TertiaryStructure tertiaryStructure1;
                    Structure struc;
                    try {
                        if(!custom) {
                            PDBFileReader pdbreader = new PDBFileReader();
                            struc = pdbreader.getStructure(f1.getPath());
                            tertiaryStructure1 = new TertiaryStructure(struc);
                        } else {
                            struc = StructureIO.getStructure("3mge");
                            tertiaryStructure1 = new TertiaryStructure(struc);
                            tertiaryStructure1.setBondList(TertiaryStructureBondsOptionalSequenceFileReader.readBondsList(f1.getPath()));
                        }
                    } catch (Exception e) {
                        System.err.println("WARNING: Skipping file " + f1.getName() + " ... " + e.getMessage());
                        // skip this structure
                        skippedFiles.add(f1);
                        continue;
                    }

                    if (cmd.hasOption("t")) {
                        double threshold = Double.parseDouble(cmd.getOptionValue("t"));
                        tertiaryStructure1.setThreshold(threshold);
                    }

                    //manage option cm
                    if(cmd.hasOption("cm"))
                        tertiaryStructure1.setDistanceMatrixCalculationMethod("centerofmass");

                    // Create the Structural RNA Tree and put the object into the map
                    st1 = new TERSAlignTree(tertiaryStructure1);
                    if(custom)
                        st1.setSequenceLength(calculateLastSequenceIndex(tertiaryStructure1.getBondList()) + 1);
                    // Build Structural RNA Tree and measure building time
                    startTimeNano = System.nanoTime();
                    try {
                        t1 = st1.getStructuralTree();
                    }catch (StackOverflowError e){
                        System.err.println("The molecule in file " + f1.getName() + " is too big, can't generate the associated tree");
                        System.exit(1);
                    }
                    elapsedTimeNano = System.nanoTime() - startTimeNano;
                    // Insert Object in maps
                    structures.put(f1, st1);
                    structuresProcessingTime.put(f1, elapsedTimeNano);
                    // Output values in the structures output file
                    if(!custom) {
                        structuresStream.println(numStructures + "," + "\"" + f1.getName() + "\","
                                + st1.getTertiaryStructure().getSequence().length() + ","
                                + st1.getTertiaryStructure().getBondList().size() + ","
                                + elapsedTimeNano);
                    } else {
                        structuresStream.println(numStructures + "," + "\"" + f1.getName() + "\","
                                + st1.getTertiaryStructure().getBondList().size() + ","
                                + elapsedTimeNano);
                    }
                    numStructures++;
                } else {
                    st1 = structures.get(f1);
                    try {
                        t1 = st1.getStructuralTree();
                    }catch (StackOverflowError e){
                        System.err.println("The molecule in file " + f1.getName() + " is too big, can't generate the associated tree");
                        System.exit(1);
                    }
                }

                // Internal Loop - Compare structure 1 with all the subsequent ones
                ListIterator<File> intIt = structuresList.listIterator(currentExtIndex + 1);
                while (intIt.hasNext()) {
                    // Process File 2
                    File f2 = intIt.next();
                    // Check if skipped
                    if (skippedFiles.contains(f2))
                        // skip this file
                        continue;

                    // Retrieve the Structural RNA Tree for the structure 2
                    TERSAlignTree st2 = null;
                    Tree<String> t2 = null;
                    // Check if this structure has already been processed
                    if (!structures.containsKey(f2)) {
                        // Parse the input file f2 for the secondary structure
                        TertiaryStructure tertiaryStructure2;
                        Structure struc2;
                        try {
                            if(!custom) {
                                PDBFileReader pdbreader = new PDBFileReader();
                                struc2 = pdbreader.getStructure(f2.getPath());
                                tertiaryStructure2 = new TertiaryStructure(struc2);
                            } else {
                                struc2 = StructureIO.getStructure("3mge");
                                tertiaryStructure2 = new TertiaryStructure(struc2);
                                tertiaryStructure2.setBondList(TertiaryStructureBondsOptionalSequenceFileReader.readBondsList(f2.getPath()));
                            }
                        } catch (Exception e) {
                            System.err.println("WARNING: Skipping file " + f2.getName() + " ... " + e.getMessage());
                            // skip this structure
                            skippedFiles.add(f2);
                            continue;
                        }

                        if (cmd.hasOption("t")) {
                            double threshold = Double.parseDouble(cmd.getOptionValue("t"));
                            tertiaryStructure2.setThreshold(threshold);
                        }

                        //manage option cm
                        if(cmd.hasOption("cm"))
                            tertiaryStructure2.setDistanceMatrixCalculationMethod("centerofmass");

                        // Create the Structural RNA Tree and put the object into the map
                        st2 = new TERSAlignTree(tertiaryStructure2);
                        if(custom)
                            st2.setSequenceLength(calculateLastSequenceIndex(tertiaryStructure2.getBondList()) + 1);
                        // Build Structural RNA Tree and measure building time
                        startTimeNano = System.nanoTime();
                        try {
                            t2 = st2.getStructuralTree();
                        }catch (StackOverflowError e){
                            System.err.println("The molecule in file " + f1.getName() + " is too big, can't generate the associated tree");
                            System.exit(1);
                        }
                        elapsedTimeNano = System.nanoTime() - startTimeNano;
                        // Insert Object in maps
                        structures.put(f2, st2);
                        structuresProcessingTime.put(f2, elapsedTimeNano);
                        // Output values in the structures output file
                        if(!custom) {
                            structuresStream.println(numStructures + "," + "\"" + f2.getName() + "\","
                                    + st2.getTertiaryStructure().getSequence().length() + ","
                                    + st2.getTertiaryStructure().getBondList().size() + ","
                                    + elapsedTimeNano);
                        } else {
                            structuresStream.println(numStructures + "," + "\"" + f2.getName() + "\","
                                    + st2.getTertiaryStructure().getBondList().size() + ","
                                    + elapsedTimeNano);
                        }
                        numStructures++;
                    } else {
                        st2 = structures.get(f2);
                        try {
                            t2 = st2.getStructuralTree();
                        }catch (StackOverflowError e){
                            System.err.println("The molecule in file " + f1.getName() + " is too big, can't generate the associated tree");
                            System.exit(1);
                        }
                    }

                    // Compare the two structural RNA Trees t1 and t2 to determine the distance
                    System.out.println("Processing files: " + f1.getName() + " and " + f2.getName());
                    AlignmentResult r;
                    try {
                        startTimeNano = System.nanoTime();
                        r = new AlignmentResult(t1, t2, f);
                        elapsedTimeNano = System.nanoTime() - startTimeNano;
                    } catch (TreeAlignException e) {
                        System.err.println("WARNING: Skipping the comparison of pair (" + f1.getName() + ","
                                + f2.getName() + ") ... " + "Alignment Exception: " + e.getMessage());
                        // Skip this pair
                        continue;
                    }
                    // Write the output file
                    if(!custom) {
                        outputStream.println("\"" + f1.getName() + "\"," + st1.getTertiaryStructure().getSequence().length() + ","
                                + st1.getTertiaryStructure().getBondList().size() + ","
                                + structuresProcessingTime.get(f1) + "," + "\"" + f2.getName() + "\","
                                + st2.getTertiaryStructure().getSequence().length() + ","
                                + st2.getTertiaryStructure().getBondList().size() + ","
                                + structuresProcessingTime.get(f2) + ","
                                + (Math.max(st1.getTertiaryStructure().getSequence().length(), st2.getTertiaryStructure().getSequence().length()))
                                + "," + r.getDistance() + "," + elapsedTimeNano);
                    } else {
                        outputStream.println("\"" + f1.getName() + "\","
                                + st1.getTertiaryStructure().getBondList().size() + ","
                                + structuresProcessingTime.get(f1) + "," + "\"" + f2.getName() + "\","
                                + st2.getTertiaryStructure().getBondList().size() + ","
                                + structuresProcessingTime.get(f2) + ","
                                + r.getDistance() + "," + elapsedTimeNano);
                    }
                    // End of Internal Loop
                }
                // End of External Loop
            }

            // Close streams
            structuresStream.close();
            outputStream.close();
            return;
        } // End Option f


        // Manage option ed
        if (cmd.hasOption("edf") || cmd.hasOption("edfm")) {
            boolean custom = cmd.hasOption("edfm");
            // Process a folder
            // Get folder file from command line
            File inputDirectory;
            if(custom)
                inputDirectory = new File(cmd.getOptionValue("edfm"));
            else
                inputDirectory = new File(cmd.getOptionValue("edf"));
            // Variables for counting execution time
            long startTimeNano;
            long elapsedTimeNano;
            // Maps for holding all the structures to be processed and their associated
            // processing time
            Map<File, TERSAlignTree> structures = new HashMap<>();
            Map<File, Long> structuresProcessingTime = new HashMap<>();
            // List for holding all the structures files
            List<File> structuresList = new ArrayList<>();
            // Set of skipped files
            Set<File> skippedFiles = new HashSet<>();
            int numStructures = 1;

            // Process input files
            if (!inputDirectory.isDirectory()) {
                System.err.println("ERROR: Input file " + cmd.getOptionValue("f") + " is not a folder");
                System.exit(1);
            }
            File[] fs = inputDirectory.listFiles();
            // Filter only files and put them in the list
            for (File file : fs)
                if (!file.isDirectory())
                    if (!file.isHidden())
                        if((FilenameUtils.getExtension(file.getName()).equals("pdb") && !custom) || (FilenameUtils.getExtension(file.getName()).equals("txt") && custom))
                            structuresList.add(file);
                        else
                            System.err.println("WARNING: Skipping unrecognized file " + file.getName() + " ...");
                    else
                        System.err.println("WARNING: Skipping hidden file " + file.getName() + " ...");
                else
                    System.err.println("WARNING: Skipping subfolder " + file.getName() + " ...");

            // Order files to be processed
            Collections.sort(structuresList);

            // Output files creation
            PrintStream outputStream = null;
            PrintStream structuresStream = null;
            String outputStreamName = inputDirectory.getAbsolutePath() + "/" + "STAlignComparisonResults.csv";
            String structuresStreamName = inputDirectory.getAbsolutePath() + "/" + "STAlignProcessedStructures.csv";

            // Manage option "o"
            if (cmd.hasOption("o")) {
                String[] names = cmd.getOptionValues("o");
                structuresStreamName = names[0];
                outputStreamName = names[1];
            }

            try {
                outputStream = new PrintStream(outputStreamName);
                structuresStream = new PrintStream(structuresStreamName);
            } catch (FileNotFoundException e) {
                System.err.println("ERROR: creation of output file "
                        + (outputStream == null ? outputStreamName : structuresStreamName) + " failed");
                System.exit(3);
            }
            // Write column names on the csv output files
            if(!custom) {
                structuresStream.println("Num,FileName,NumberOfNucleotides,NumberOfWeakBonds,TimeToGenerateStructuralTree[ns]");
                outputStream.println(
                        "FileName1,NumberOfNucleotides1,NumberOfWeakBonds1,TimeToGenerateStructuralTree1[ns],"
                                + "FileName2,NumberOfNucleotides2,NumberOfWeakBonds2,TimeToGenerateStructuralTree2[ns],"
                                + "MaxNumberOfNucleotides1-2,ASAEditDistance,TimeToCalculateASAEditDistance[ns]");
            } else {
                structuresStream.println("Num,FileName,NumberOfWeakBonds,TimeToGenerateStructuralTree[ns]");
                outputStream.println(
                        "FileName1,NumberOfWeakBonds1,TimeToGenerateStructuralTree1[ns],"
                                + "FileName2,NumberOfWeakBonds2,TimeToGenerateStructuralTree2[ns],"
                                + "ASAEditDistance,TimeToCalculateASAEditDistance[ns]");
            }
            // Load configuration file for costs
            ScoringFunction f = new ScoringFunction(configurationFileName);

            // Main Loop
            ListIterator<File> extIt = structuresList.listIterator();
            while (extIt.hasNext()) {
                // Compare the next element with all the subsequent elements
                int currentExtIndex = extIt.nextIndex();
                // Process File 1
                File f1 = extIt.next();
                // Check if skipped
                if (skippedFiles.contains(f1))
                    // skip this file
                    continue;

                // Retrieve the Structural RNA Tree for the structure 1
                TERSAlignTree st1;
                Tree<String> t1 = null;
                // Check if this structure has already been processed
                if (!structures.containsKey(f1)) {
                    // Parse the input file f1 for the secondary structure
                    TertiaryStructure tertiaryStructure1;
                    Structure struc;
                    try {
                        if(!custom) {
                            PDBFileReader pdbreader = new PDBFileReader();
                            struc = pdbreader.getStructure(f1.getPath());
                            tertiaryStructure1 = new TertiaryStructure(struc);
                        } else {
                            struc = StructureIO.getStructure("3mge");
                            tertiaryStructure1 = new TertiaryStructure(struc);
                            tertiaryStructure1.setBondList(TertiaryStructureBondsOptionalSequenceFileReader.readBondsList(f1.getPath()));
                        }
                    } catch (Exception e) {
                        System.err.println("WARNING: Skipping file " + f1.getName() + " ... " + e.getMessage());
                        // skip this structure
                        skippedFiles.add(f1);
                        continue;
                    }

                    if (cmd.hasOption("t")) {
                        double threshold = Double.parseDouble(cmd.getOptionValue("t"));
                        tertiaryStructure1.setThreshold(threshold);
                    }

                    //manage option cm
                    if(cmd.hasOption("cm"))
                        tertiaryStructure1.setDistanceMatrixCalculationMethod("centerofmass");

                    // Create the Structural RNA Tree and put the object into the map
                    st1 = new TERSAlignTree(tertiaryStructure1);
                    if(custom)
                        st1.setSequenceLength(calculateLastSequenceIndex(tertiaryStructure1.getBondList()) + 1);
                    // Build Structural RNA Tree and measure building time
                    startTimeNano = System.nanoTime();
                    try {
                        t1 = st1.getStructuralTree();
                    }catch (StackOverflowError e){
                        System.err.println("The molecule in file " + f1.getName() + " is too big, can't generate the associated tree");
                        System.exit(1);
                    }
                    elapsedTimeNano = System.nanoTime() - startTimeNano;
                    // Insert Object in maps
                    structures.put(f1, st1);
                    structuresProcessingTime.put(f1, elapsedTimeNano);
                    // Output values in the structures output file
                    if(!custom) {
                        structuresStream.println(numStructures + "," + "\"" + f1.getName() + "\","
                                + st1.getTertiaryStructure().getSequence().length() + ","
                                + st1.getTertiaryStructure().getBondList().size() + ","
                                + elapsedTimeNano);
                    } else {
                        structuresStream.println(numStructures + "," + "\"" + f1.getName() + "\","
                                + st1.getTertiaryStructure().getBondList().size() + ","
                                + elapsedTimeNano);
                    }
                    numStructures++;
                } else {
                    st1 = structures.get(f1);
                    try {
                        t1 = st1.getStructuralTree();
                    }catch (StackOverflowError e){
                        System.err.println("The molecule in file " + f1.getName() + " is too big, can't generate the associated tree");
                        System.exit(1);
                    }
                }

                // Internal Loop - Compare structure 1 with all the subsequent ones
                ListIterator<File> intIt = structuresList.listIterator(currentExtIndex + 1);
                while (intIt.hasNext()) {
                    // Process File 2
                    File f2 = intIt.next();
                    // Check if skipped
                    if (skippedFiles.contains(f2))
                        // skip this file
                        continue;

                    // Retrieve the Structural RNA Tree for the structure 2
                    TERSAlignTree st2 = null;
                    Tree<String> t2 = null;
                    // Check if this structure has already been processed
                    if (!structures.containsKey(f2)) {
                        // Parse the input file f2 for the secondary structure
                        TertiaryStructure tertiaryStructure2;
                        Structure struc2;
                        try {
                            if(!custom) {
                                PDBFileReader pdbreader = new PDBFileReader();
                                struc2 = pdbreader.getStructure(f2.getPath());
                                tertiaryStructure2 = new TertiaryStructure(struc2);
                            } else {
                                struc2 = StructureIO.getStructure("3mge");
                                tertiaryStructure2 = new TertiaryStructure(struc2);
                                tertiaryStructure2.setBondList(TertiaryStructureBondsOptionalSequenceFileReader.readBondsList(f2.getPath()));
                            }
                        } catch (Exception e) {
                            System.err.println("WARNING: Skipping file " + f2.getName() + " ... " + e.getMessage());
                            // skip this structure
                            skippedFiles.add(f2);
                            continue;
                        }

                        if (cmd.hasOption("t")) {
                            double threshold = Double.parseDouble(cmd.getOptionValue("t"));
                            tertiaryStructure2.setThreshold(threshold);
                        }

                        //manage option cm
                        if(cmd.hasOption("cm"))
                            tertiaryStructure2.setDistanceMatrixCalculationMethod("centerofmass");

                        // Create the Structural RNA Tree and put the object into the map
                        st2 = new TERSAlignTree(tertiaryStructure2);
                        if(custom)
                            st2.setSequenceLength(calculateLastSequenceIndex(tertiaryStructure2.getBondList()) + 1);
                        // Build Structural RNA Tree and measure building time
                        startTimeNano = System.nanoTime();
                        try {
                            t2 = st2.getStructuralTree();
                        }catch (StackOverflowError e){
                            System.err.println("The molecule in file " + f1.getName() + " is too big, can't generate the associated tree");
                            System.exit(1);
                        }
                        elapsedTimeNano = System.nanoTime() - startTimeNano;
                        // Insert Object in maps
                        structures.put(f2, st2);
                        structuresProcessingTime.put(f2, elapsedTimeNano);
                        // Output values in the structures output file
                        if(!custom) {
                            structuresStream.println(numStructures + "," + "\"" + f2.getName() + "\","
                                    + st2.getTertiaryStructure().getSequence().length() + ","
                                    + st2.getTertiaryStructure().getBondList().size() + ","
                                    + elapsedTimeNano);
                        } else {
                            structuresStream.println(numStructures + "," + "\"" + f2.getName() + "\","
                                    + st2.getTertiaryStructure().getBondList().size() + ","
                                    + elapsedTimeNano);
                        }
                        numStructures++;
                    } else {
                        st2 = structures.get(f2);
                        try {
                            t2= st2.getStructuralTree();
                        }catch (StackOverflowError e){
                            System.err.println("The molecule in file " + f1.getName() + " is too big, can't generate the associated tree");
                            System.exit(1);
                        }
                    }

                    // Compare the two structural RNA Trees t1 and t2 to determine the distance
                    System.out.println("Processing files: " + f1.getName() + " and " + f2.getName());

                    float distance;
                    startTimeNano = System.nanoTime();
                    BracketStringInputParser parser = new BracketStringInputParser();
                    Node<StringNodeData> editDistanceTree1 = parser.fromString("{" + TreeOutputter.treeToAptedInput(t1) + "}");
                    Node<StringNodeData> editDistanceTree2 = parser.fromString("{" + TreeOutputter.treeToAptedInput(t2) + "}");
                    APTED<PerEditOperationStringNodeDataCostModel, StringNodeData> apted = new APTED<>(new PerEditOperationStringNodeDataCostModel((float)f.getEditdistanceDeleteCost(),(float)f.getEditdistanceInsertCost(),(float)f.getEditdistanceRenameCost()));
                    distance = apted.computeEditDistance(editDistanceTree1, editDistanceTree2);
                    elapsedTimeNano = System.nanoTime() - startTimeNano;

                    // Write the output file
                    if(!custom) {
                        outputStream.println("\"" + f1.getName() + "\"," + st1.getTertiaryStructure().getSequence().length() + ","
                                + st1.getTertiaryStructure().getBondList().size() + ","
                                + structuresProcessingTime.get(f1) + "," + "\"" + f2.getName() + "\","
                                + st2.getTertiaryStructure().getSequence().length() + ","
                                + st2.getTertiaryStructure().getBondList().size() + ","
                                + structuresProcessingTime.get(f2) + ","
                                + (Math.max(st1.getTertiaryStructure().getSequence().length(), st2.getTertiaryStructure().getSequence().length()))
                                + "," + distance + "," + elapsedTimeNano);
                    } else {
                        outputStream.println("\"" + f1.getName() + "\","
                                + st1.getTertiaryStructure().getBondList().size() + ","
                                + structuresProcessingTime.get(f1) + "," + "\"" + f2.getName() + "\","
                                + st2.getTertiaryStructure().getBondList().size() + ","
                                + structuresProcessingTime.get(f2) + ","
                                + distance + "," + elapsedTimeNano);
                    }
                    // End of Internal Loop
                }
                // End of External Loop
            }

            // Close streams
            structuresStream.close();
            outputStream.close();
            return;
        } // End Option ed


        // If no option is given, output usage
        formatter.printHelp(CommandLineMessages.LAUNCH_COMMAND_WB, CommandLineMessages.HEADER_WB, options,
                CommandLineMessages.USAGE_EXAMPLES_WB + CommandLineMessages.COPYRIGHT + CommandLineMessages.SHORT_NOTICE
                        + CommandLineMessages.REPORT_TO,
                true);
    }

    private static int calculateLastSequenceIndex(ArrayList<Pair<Integer>> bondList) {
        int lastIndex = 0;
        for(Pair<Integer> currentPair : bondList)
            if(currentPair.getSecond() > lastIndex)
                lastIndex = currentPair.getSecond();
        return lastIndex;
    }

}
