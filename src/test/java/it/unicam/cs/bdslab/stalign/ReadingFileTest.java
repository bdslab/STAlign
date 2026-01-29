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
import java.util.HashMap;
import java.util.Map;
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
        File filefolderPDB = new File(this.getClass().getResource("/cifAndPDB/PDB").getPath());
        File fileFolderCIF = new File(this.getClass().getResource("/cifAndPDB/CIF").getPath());
        PDBFileReader pdbreader = new PDBFileReader();
        CifFileReader cifFileReader = new CifFileReader();

        //Compute all pdb trees
        Map<String, Tree<String>> pdbTrees = new HashMap<>();
        for (File f: Objects.requireNonNull(filefolderPDB.listFiles())) {
            Structure structure = pdbreader.getStructure(filefolderPDB + "/" +f.getName());
            TertiaryStructure tertiaryStructure = new TertiaryStructure(structure);
            tertiaryStructure.setThreshold(7);

            TERSAlignTree tersAlignTree = new TERSAlignTree(tertiaryStructure);
            Tree<String> parsedTree = tersAlignTree.getStructuralTree();
            pdbTrees.put(f.getName(), parsedTree);
        }

        //Compute all CIF trees
        Map<String, Tree<String>> cifTrees = new HashMap<>();
        for (File f: Objects.requireNonNull(fileFolderCIF.listFiles())) {
            Structure structure = cifFileReader.getStructure(fileFolderCIF + "/" + f.getName());
            TertiaryStructure tertiaryStructure = new TertiaryStructure(structure);
            tertiaryStructure.setThreshold(7);
            TERSAlignTree tersAlignTree = new TERSAlignTree(tertiaryStructure);
            Tree<String> parsedTree = tersAlignTree.getStructuralTree();
            cifTrees.put(f.getName(), parsedTree);
        }

        //Compare the lists
        String configurationFileName = ScoringFunction.DEFAULT_PROPERTY_FILE;
        ScoringFunction f = new ScoringFunction(configurationFileName);

        for (Map.Entry<String, Tree<String>> entry : pdbTrees.entrySet()) {
            String key = entry.getKey();
            Tree<String> value = entry.getValue();
            if (cifTrees.containsKey(key)) {
                AlignmentResult r = null;
                try {
                    r = new AlignmentResult(value, cifTrees.get(key), f);
                }
                catch (TreeAlignException e) {
                    Assertions.fail("Alignment Exception: " + e.getMessage());
                }
                Assertions.assertEquals(0, r.getDistance(), "The distance between the trees of "+ key + " is not 0");   
            }
        }
    }

}
