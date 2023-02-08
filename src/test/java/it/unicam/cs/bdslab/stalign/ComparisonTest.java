package it.unicam.cs.bdslab.stalign;
import fr.orsay.lri.varna.models.treealign.AlignedNode;
import fr.orsay.lri.varna.models.treealign.Tree;
import fr.orsay.lri.varna.models.treealign.TreeAlignException;
import org.apache.commons.io.FilenameUtils;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.contact.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Test class for comparison given by Professors
 *
 * @author Marco Serenelli
 *
 */
class ComparisonTest {

    @Test
    @DisplayName("Archaea/5S comparison")
    void archeaFiveSComparsionTest() throws IOException {
        //Load folder
        File inputFolder = new File("src/test/resources/TestComparison/Archaea/5S/");
        FileWriter outputFile = new FileWriter("src/test/resources/TestComparison/ComparisonResults/Archaea/5S/comparisonResults.txt");
        createTreeAndCompare(inputFolder,outputFile);
    }

    @Test
    @DisplayName("Archaea/16S comparison")
    void archeaSixteenSComparsionTest() throws IOException {
        //Load folder
        File inputFolder = new File("src/test/resources/TestComparison/Archaea/16S/");
        FileWriter outputFile = new FileWriter("src/test/resources/TestComparison/ComparisonResults/Archaea/16S/comparisonResults.txt");
        createTreeAndCompare(inputFolder,outputFile);
    }


    @Test
    @DisplayName("Archaea/23S comparison")
    void archeaTwentyThreeSComparsionTest() throws IOException {
        //Load folder
        File inputFolder = new File("src/test/resources/TestComparison/Archaea/23S/");
        FileWriter outputFile = new FileWriter("src/test/resources/TestComparison/ComparisonResults/Archaea/23S/comparisonResults.txt");
        createTreeAndCompare(inputFolder,outputFile);
    }

    @Test
    @DisplayName("Bacteria/5S comparison")
    void bacteriaFiveSComparisonTest() throws IOException {
        //Load folder
        File inputFolder = new File("src/test/resources/TestComparison/Bacteria/5S/");
        FileWriter outputFile = new FileWriter("src/test/resources/TestComparison/ComparisonResults/Bacteria/5S/comparisonResults.txt");
        createTreeAndCompare(inputFolder,outputFile);
    }


    @Test
    @DisplayName("Bacteria/16S comparison")
    void bacteriaSixteenSComparisonTest() throws IOException {
        //Load folder
        File inputFolder = new File("src/test/resources/TestComparison/Bacteria/16S/");
        FileWriter outputFile = new FileWriter("src/test/resources/TestComparison/ComparisonResults/Bacteria/16S/comparisonResults.txt");
        createTreeAndCompare(inputFolder,outputFile);
    }

    @Test
    @DisplayName("Bacteria/23S comparison")
    void bacteriaTwentyThreeSComparisonTest() throws IOException {
        //Load folder
        File inputFolder = new File("src/test/resources/TestComparison/Bacteria/23S/");
        FileWriter outputFile = new FileWriter("src/test/resources/TestComparison/ComparisonResults/Bacteria/23S/comparisonResults.txt");
        createTreeAndCompare(inputFolder,outputFile);
    }

    private void createTreeAndCompare(File inputFolder, FileWriter outputFile) throws IOException {
        //Load fileList
        File[] fileList = inputFolder.listFiles();

        //Load scoring function
        String configurationFileName = ScoringFunction.DEFAULT_PROPERTY_FILE;
        ScoringFunction f = new ScoringFunction(configurationFileName);

        if (fileList != null) {
            for (File file : fileList) {
                ArrayList<Pair<Integer>> parsedBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList(inputFolder + "/" + file.getName());
                Tree<String> parsedTree = createStructuralTree(parsedBonds);
                for (File file2 : fileList) {
                    if (!file.isDirectory()) {
                        if (!file.isHidden()) {
                            if ((FilenameUtils.getExtension(file.getName()).equals("txt"))) {
                                ArrayList<Pair<Integer>> parsedBonds2 = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList(inputFolder + "/" + file2.getName());
                                Tree<String> parsedTree2 = createStructuralTree(parsedBonds2);
                                //Starting comparison
                                outputFile.write("Comparing: " + file.getName() + " with " + file2.getName());
                                //Creating trees
                                outputFile.write("\n" + file.getName() + " tree: " + TreeOutputter.treeToString(parsedTree));
                                outputFile.write("\n" + file2.getName() + " tree: " + TreeOutputter.treeToString(parsedTree2));
                                //Comparison & distance
                                outputFile.write("\n" + file.getName() + " aligned with " + file2.getName() + ": ");
                                double distance = compareTreesAndGetDistance(parsedTree, parsedTree2, f, outputFile);
                                outputFile.write("\nDistance between " + file.getName() + " and " + file2.getName() + "= " + distance + "\n\n");
                            } else
                                System.err.println("WARNING: Skipping unrecognized file " + file.getName() + " ...");
                        } else
                            System.err.println("WARNING: Skipping hidden file " + file.getName() + " ...");
                    }
                    else
                        System.err.println("WARNING: Skipping subfolder " + file.getName() + " ...");
                }
            }
        }
        outputFile.close();
    }

    private Tree<String> createStructuralTree(ArrayList<Pair<Integer>> parsedBonds){
        Structure structure = null;
        try {
            structure = StructureIO.getStructure("3mge");
        } catch (IOException | StructureException e) {
            e.printStackTrace();
        }
        TertiaryStructure tertiaryStructure = new TertiaryStructure(structure);
        tertiaryStructure.setBondList(parsedBonds);
        //Creo l'albero, imposto la giusta sequenza e prendo l'albero strutturale
        TERSAlignTree tree = new TERSAlignTree(tertiaryStructure);
        tree.setSequenceLength(setSequenceLengthRight(parsedBonds));
        return tree.getStructuralTree();
    }

    private int setSequenceLengthRight(ArrayList<Pair<Integer>> parsedBonds){
        int max = 0;
        for (Pair<Integer> pair : parsedBonds) {
            if(pair.getFirst() > max){
                max = pair.getFirst();
            }

            if(pair.getSecond() > max){
                max = pair.getSecond();
            }
        }
        return max+1;
    }

    private double compareTreesAndGetDistance(Tree<String> tree1,  Tree<String> tree2, ScoringFunction f, FileWriter resultsFile) throws IOException {
        AlignmentResult r = null;
        try {
            r = new AlignmentResult(tree1, tree2, f);
        } catch (TreeAlignException e) {
            System.err.println("ERROR: Alignment Exception: " + e.getMessage());
            System.exit(4);
        }
        Tree<AlignedNode<String, String>> t = r.getAlignedTree();
        resultsFile.write(TreeOutputter.treeToStringAligned(t));
        return r.getDistance();
    }
}
