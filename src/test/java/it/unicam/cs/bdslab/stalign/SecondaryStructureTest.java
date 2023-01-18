package it.unicam.cs.bdslab.stalign;
import fr.orsay.lri.varna.models.treealign.Tree;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.PDBFileReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.*;
import java.util.Objects;



public class SecondaryStructureTest {
    @Test
    @DisplayName("Secondary Structure comparison")
    void calculateSecondaryStructure() throws IOException {
        File fileFolder = new File("src/test/resources/secondaryStructureTests/pdb/");
        for (File f: Objects.requireNonNull(fileFolder.listFiles())) {
            PDBFileReader pdbreader = new PDBFileReader();
            Structure structure = pdbreader.getStructure(fileFolder+ "/" + f.getName());
            System.out.println("analizying: " + fileFolder + "/" + f.getName());

            //Calculate secondary structure
            SecondaryStructure secondaryStructure = new SecondaryStructure(structure);
            TERSAlignTree tersAlignTree = new TERSAlignTree(secondaryStructure);
            System.out.println("SS");
            Tree<String> t1 = tersAlignTree.getStructuralTree();
            System.out.println(TreeOutputter.treeToString(t1));
        }
    }
}
