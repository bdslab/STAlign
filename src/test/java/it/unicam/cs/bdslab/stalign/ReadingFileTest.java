package it.unicam.cs.bdslab.stalign;

import fr.orsay.lri.varna.models.treealign.Tree;
import fr.orsay.lri.varna.models.treealign.TreeAlignException;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.CifFileReader;
import org.biojava.nbio.structure.io.PDBFileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Test class for reading different file formats
 *
 * @author Marco Serenelli
 *
 */

public class ReadingFileTest {

    @Test
    @DisplayName("Reading PDB files")
    void readPDBFile() {
        File fileFolder = new File("src/test/resources/pdb/");
        PDBFileReader pdbreader = new PDBFileReader();
        for (File f: Objects.requireNonNull(fileFolder.listFiles())) {
            try{
                pdbreader.getStructure(fileFolder+ "/" + f.getName());
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    @Test
    @DisplayName("Reading CIF files")
    void readCIFFile() {
        File fileFolder = new File("src/test/resources/cifAndPDB/CIF");
        CifFileReader cifFileReader = new CifFileReader();
        for (File f: Objects.requireNonNull(fileFolder.listFiles())) {
            try{
               cifFileReader.getStructure(fileFolder+ "/" + f.getName());
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Reading and comparing PDB and CIf files")
    void readAndCompare() throws IOException {
        File filefolderPDB = new File("src/test/resources/cifAndPDB/PDB");
        File fileFolderCIF = new File("src/test/resources/cifAndPDB/CIF");
        PDBFileReader pdbreader = new PDBFileReader();
        CifFileReader cifFileReader = new CifFileReader();

        //Compute all pdb trees
        ArrayList<Tree<String>> pdbTrees = new ArrayList<>();
        for (File f: Objects.requireNonNull(filefolderPDB.listFiles())) {
            Structure structure = pdbreader.getStructure(filefolderPDB + "/" +f.getName());
            TertiaryStructure tertiaryStructure = new TertiaryStructure(structure);
            TERSAlignTree tersAlignTree = new TERSAlignTree(tertiaryStructure);
            Tree<String> parsedTree = tersAlignTree.getStructuralTree();
            pdbTrees.add(parsedTree);
        }

        //Compute all CIF trees
        ArrayList<Tree<String>> cifTrees = new ArrayList<>();
        for (File f: Objects.requireNonNull(fileFolderCIF.listFiles())) {
            Structure structure = cifFileReader.getStructure(fileFolderCIF + "/" + f.getName());
            TertiaryStructure tertiaryStructure = new TertiaryStructure(structure);
            TERSAlignTree tersAlignTree = new TERSAlignTree(tertiaryStructure);
            Tree<String> parsedTree = tersAlignTree.getStructuralTree();
            cifTrees.add(parsedTree);
        }

        //Compare the lists
        String configurationFileName = ScoringFunction.DEFAULT_PROPERTY_FILE;
        ScoringFunction f = new ScoringFunction(configurationFileName);

        for (int i = 0; i < pdbTrees.size(); i++) {
            AlignmentResult r = null;
            try {
                r = new AlignmentResult(pdbTrees.get(i), cifTrees.get(i), f);
            } catch (TreeAlignException e) {
                System.err.println("ERROR: Alignment Exception: " + e.getMessage());
                System.exit(4);
            }
            Assertions.assertEquals(r.getDistance(), 0, "The distance between the trees is not 0");
        }
    }

}
