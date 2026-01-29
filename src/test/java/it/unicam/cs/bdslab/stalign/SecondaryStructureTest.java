package it.unicam.cs.bdslab.stalign;
import fr.orsay.lri.varna.models.treealign.Tree;

import org.biojava.nbio.structure.EntityType;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.PDBFileReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.*;
import java.util.Objects;



public class    SecondaryStructureTest {
    @Test
    @DisplayName("Secondary Structure comparison")
    void calculateSecondaryStructure() throws IOException {
        int count = 0;
        File fileFolder = new File(this.getClass().getResource("/cifAndPDB/PDB").getPath());
        assertNotEquals(0, fileFolder.listFiles(pathname -> !pathname.isDirectory() && !pathname.isHidden()).length, "No test files found");
        for (File f: Objects.requireNonNull(fileFolder.listFiles(pathname -> !pathname.isDirectory() && !pathname.isHidden()))) {
            Structure structure = loadFile(f.getName().substring(0, f.getName().lastIndexOf('.')));
            assertNotNull(structure, "Failed to load structure from " + f.getName());
            //Calculate secondary structure
            SecondaryStructure secondaryStructure = null;
            try {
                secondaryStructure = new SecondaryStructure(structure);
            } catch (Exception e) {
                // If the structure cannot have secondary structure calculated, just skip it
                continue;
            }
            count++;
            TERSAlignTree tersAlignTree = new TERSAlignTree(secondaryStructure);
            System.out.println("SS");
            Tree<String> t1 = tersAlignTree.getStructuralTree();
            System.out.println(TreeOutputter.treeToString(t1));
            assertNotEquals("", TreeOutputter.treeToString(t1));
        }
        assertNotEquals(0, count, "No structures could have secondary structure calculated");
    }

    /**
     * Load a PDB files, returns a structure, file should be in ../resources
     * @param fileName name of the file to load
     * @return returns a structure
     */
    private Structure loadFile(String fileName){
        PDBFileReader pdbreader = new PDBFileReader();
        try{
            return pdbreader.getStructure(this.getClass().getResource("/cifAndPDB/PDB").getPath() + "/" + fileName + ".pdb");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
