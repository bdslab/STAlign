package it.unicam.cs.bdslab.stalign;

import org.biojava.nbio.structure.*;
import org.biojava.nbio.structure.io.PDBFileReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the RNA/Protein's tertiary structure representation and distances calculations methods
 *
 * @author Marco Serenelli
 *
 */
class TertiaryStructureTest {

    @Test
    @DisplayName("Distance matrix with default method")
    void testCalculateDistanceMatrix(){
        //Load a PROTEIN file
        Structure struc = loadFile("3mge");
        TertiaryStructure tertiaryStructure = new TertiaryStructure(struc);

        tertiaryStructure.setDistanceMatrixCalculationMethod("Default");
        double[][] resultMatrix = tertiaryStructure.getDistanceMatrix();

        //Check matrix length
        assertEquals(204, resultMatrix.length, "Matrix length should be 204");

        //Test the first value with himself
        assertEquals(0,resultMatrix[0][0], "Distance should be 0");

        //Test the last value with himself
        assertEquals(0,resultMatrix[resultMatrix.length-1][resultMatrix.length-1], "Distance should be 0");

        //Test on the first two CA distance
        assertEquals(3.7951635010892466, resultMatrix[0][1], "Distance between the first two CA should be 3.7951...");

        //Test on two randoms CA distance in the middle
        assertEquals(5.029965009818657, resultMatrix[35][32], "Distance between two CA values in the middle should be 5.0299...");

        //Test on the last two CA distance
        assertEquals(3.799415876157805, resultMatrix[resultMatrix.length-2][resultMatrix.length-1], "Distance between the last two CA values should be 3.7994...");

        //Load an RNA file
        struc = loadFile("4gxy");
        tertiaryStructure = new TertiaryStructure(struc);
        tertiaryStructure.setDistanceMatrixCalculationMethod("Default");
        resultMatrix = tertiaryStructure.getDistanceMatrix();

        //Check matrix length
        assertEquals(162, resultMatrix.length, "Matrix length should be 162");

        //Test the first value with himself
        assertEquals(0,resultMatrix[0][0], "Distance should be 0");

        //Test the last value with himself
        assertEquals(0,resultMatrix[resultMatrix.length-1][resultMatrix.length-1], "Distance should be 0");

        //Test on the first two P distance
        assertEquals(6.002236000025325, resultMatrix[0][1], "Distance between the first two P should be 6.0022...");

        //Test on two randoms P distance in the middle
        assertEquals(17.053572382348516, resultMatrix[35][32], "Distance between two P values in the middle should be 17.0535...");

        //Test on the last two P distance
        assertEquals(6.141912731389141, resultMatrix[resultMatrix.length-2][resultMatrix.length-1], "Distance between the last two P values should be 6.1419...");
    }

    @Test
    @DisplayName("Distance matrix with center of mass")
    void testCalculateDistanceMatrixCenterOfMass(){

        //Load a PROTEIN file
        Structure struc = loadFile("3mge");

        TertiaryStructure tertiaryStructure = new TertiaryStructure(struc);

        tertiaryStructure.setDistanceMatrixCalculationMethod("CenterOfMass");
        double[][] resultMatrix = tertiaryStructure.getDistanceMatrix();

        //Check matrix length
        assertEquals(204, resultMatrix.length, "Matrix length should be 204");

        //Test the first value with himself
        assertEquals(0,resultMatrix[0][0], "Distance should be 0");

        //Test the last value with himself
        assertEquals(0.0,resultMatrix[resultMatrix.length-1][resultMatrix.length-1], "Distance should be 0");

        //Test on the first two distance
        assertEquals(4.620100901228067, resultMatrix[0][1], "Distance should be 4.6201...");

        //Test on two randoms distances in the middle
        assertEquals(5.236362921223696, resultMatrix[35][32], "Distance should be 5.2363...");

        //Test on the last two CA distance
        assertEquals(5.751582341272176, resultMatrix[resultMatrix.length-2][resultMatrix.length-1], "Distance should be 5.7515...");


        //Load an RNA file
        struc = loadFile("4gxy");
        tertiaryStructure = new TertiaryStructure(struc);

        tertiaryStructure.setDistanceMatrixCalculationMethod("CenterOfMass");
        resultMatrix = tertiaryStructure.getDistanceMatrix();

        //Check matrix length
        assertEquals(162, resultMatrix.length, "Matrix length should be 175");

        //Test the first value with himself
        assertEquals(0,resultMatrix[0][0], "Distance should be 0");

        //Test the last value with himself
        assertEquals(0,resultMatrix[resultMatrix.length-1][resultMatrix.length-1], "Distance should be 0");

        //Test on the first two distance
        assertEquals(4.235092167400089, resultMatrix[0][1], "Distance should be 4.2350...");

        //Test on two randoms distance in the middle
        assertEquals(17.071554573913147, resultMatrix[35][32], "Distance should be 17.0715...");

        assertEquals(34.05658516896883, resultMatrix[160][32], "Distance should be 34.0565...");

        //Test on the last two P distance
        assertEquals(6.53266416258413, resultMatrix[resultMatrix.length-2][resultMatrix.length-1], "Distance should be 6.5326...");
    }

    @Test
    @DisplayName("Contact matrix using default distance matrix")
    void testGetContactMatrixDefault(){

        //Load a PROTEIN file
        Structure struc = loadFile("3mge");
        TertiaryStructure tertiaryStructure = new TertiaryStructure(struc);
        tertiaryStructure.setThreshold(12);

        tertiaryStructure.setDistanceMatrixCalculationMethod("Default");
        boolean[][] actualContactMatrix = tertiaryStructure.getContactMatrix();

        //USING 12 AS THRESHOLD

        //Check if matrix length is right
        assertEquals(actualContactMatrix.length, tertiaryStructure.getDistanceMatrix().length, "contact matrix length should be the same as distance matrix length");

        //First value should be false.
        assertFalse(actualContactMatrix[0][0], "first value should always be false");

        //Last value should be false.
        assertFalse(actualContactMatrix[actualContactMatrix.length-1][actualContactMatrix.length-1], "last value should always be false");

        //Should be true, distance is 3.7951...
        assertTrue(actualContactMatrix[0][1], "first with second value should be true, distance is 3.7951...");

        //Should be true, distance is 3.7994
        assertTrue(actualContactMatrix[actualContactMatrix.length-1][actualContactMatrix.length-2], "last two values should be true, distance is 3.7994...");

        //Should be true, distance is 5.0299...
        assertTrue(actualContactMatrix[32][35], "32 - 35 value should be true, distance is 5.0299...");

        //Should be false, distance is 13.3787...
        assertFalse(actualContactMatrix[7][3], "7 - 3 value should be false, distance is 13.3787...");

        //Changing threshold to 14 should make 7 - 3 true
        tertiaryStructure.setThreshold(14);
        //Re-calculate contact matrix with new threshold
        actualContactMatrix = tertiaryStructure.getContactMatrix();
        //Should be true
        assertTrue(actualContactMatrix[7][3], "7 - 3 should be true, with threshold 14, value is 13.3787...");

        //Changing threshold to 4 should make 32 - 35 false
        tertiaryStructure.setThreshold(4);
        //Re-calculate contact matrix with new threshold
        actualContactMatrix = tertiaryStructure.getContactMatrix();
        //Should be false
        assertFalse(actualContactMatrix[32][35], "32 - 35 should be false with threshold 4, value is 5.0299...");

        //Changing threshold to 51
        tertiaryStructure.setThreshold(51);
        //Re-calculate contact matrix with new threshold
        actualContactMatrix = tertiaryStructure.getContactMatrix();
        //Should be false
        assertFalse(actualContactMatrix[190][2], "190 - 2 should be false with threshold 51, value is 51.8775...");


        //Load an RNA file
        struc = loadFile("4gxy");
        tertiaryStructure = new TertiaryStructure(struc);
        tertiaryStructure.setThreshold(12);
        actualContactMatrix = tertiaryStructure.getContactMatrix();

        tertiaryStructure.printContactMatrix();
        System.out.println(actualContactMatrix.length);
        tertiaryStructure.printDistanceMatrix();


        //USING 12 AS THRESHOLD

        //Check if matrix length is right
        assertEquals(actualContactMatrix.length, tertiaryStructure.getContactMatrix().length, "contact matrix length should be the same as distance matrix length");

        //First value should be equal to false.
        assertFalse(actualContactMatrix[0][0], "first value should always false");

        //Last value should be equal to false.
        assertFalse(actualContactMatrix[actualContactMatrix.length-1][actualContactMatrix.length-1], "last value should always be false");

        //Should be true, distance is 4.6201...
        assertTrue(actualContactMatrix[0][1], "first with second value should be true, distance is 6.0022...");

        //Should be true, distance is 6.1419
        assertTrue(actualContactMatrix[actualContactMatrix.length-1][actualContactMatrix.length-2], "last two values should be true, distance is 6.1419...");

        //Should be false, distance is 17.0535...
        assertFalse(actualContactMatrix[32][35], "32 - 35 value should be true, distance is 17.0535...");

        //Should be false, distance is 19.8870...
        assertFalse(actualContactMatrix[7][3], "7 - 3 value should be false, distance is 19.8870...");

        //Changing threshold to 20 should make 7 - 3 true
        tertiaryStructure.setThreshold(20);
        //Re-calculate contact matrix with new threshold
        actualContactMatrix = tertiaryStructure.getContactMatrix();
        //Should be true
        assertTrue(actualContactMatrix[7][3], "7 - 3 should be true, with threshold 20, value is 19.8870...");

        //Changing threshold to 4 should make 32 - 35 false
        tertiaryStructure.setThreshold(4);
        //Re-calculate contact matrix with new threshold
        actualContactMatrix = tertiaryStructure.getContactMatrix();
        //Should be false
        assertFalse(actualContactMatrix[32][35], "32 - 35 should be false with threshold 4, value is 17.0535...");

        //Changing threshold to 41
        tertiaryStructure.setThreshold(41);
        //Re-calculate contact matrix with new threshold
        actualContactMatrix = tertiaryStructure.getContactMatrix();
        //Should be false
        assertFalse(actualContactMatrix[132][35], "132 - 35 should be false with threshold 41, value is 41.3573...");
    }

    @Test
    @DisplayName("Contact matrix using center of mass distance matrix")
    void testGetContactMatrixDistanceCenterOfMass(){
        //Load a PROTEIN file
        Structure struc = loadFile("3mge");
        TertiaryStructure tertiaryStructure = new TertiaryStructure(struc);
        tertiaryStructure.setThreshold(12);

        tertiaryStructure.setDistanceMatrixCalculationMethod("CenterOfMass");
        boolean[][] actualContactMatrix = tertiaryStructure.getContactMatrix();

        //USING 12 AS THRESHOLD

        //Check if matrix length is right
        assertEquals(actualContactMatrix.length, tertiaryStructure.getContactMatrix().length, "contact matrix length should be the same as distance matrix length");

        //First should be false
        assertFalse(actualContactMatrix[0][0], "first value should be false");

        //Last value should be equal false.
        assertFalse(actualContactMatrix[actualContactMatrix.length-1][actualContactMatrix.length-1], "last value should be false");

        //Should be true, distance is 4.6201...
        assertTrue(actualContactMatrix[0][1], "first with second value should be true, distance is 4.6201...");

        //Should be true, distance is 3.7994
        assertTrue(actualContactMatrix[actualContactMatrix.length-1][actualContactMatrix.length-2], "last two values should be true, distance is 3.7994...");

        //Should be true, distance is 5.2363...
        assertTrue(actualContactMatrix[32][35], "32 - 35 value should be true, distance is 5.2363...");

        //Should be false, distance is 13.6747...
        assertFalse(actualContactMatrix[7][3], "7 - 3 value should be false, distance is 13.6747...");

        //Changing threshold to 14 should make 7 - 3 true
        tertiaryStructure.setThreshold(14);
        //Re-calculate contact matrix with new threshold
        actualContactMatrix = tertiaryStructure.getContactMatrix();
        //Should be true
        assertTrue(actualContactMatrix[7][3], "7 - 3 should be true, with threshold 14, value is 13.6747...");

        //Changing threshold to 4 should make 32 - 35 false
        tertiaryStructure.setThreshold(4);
        //Re-calculate contact matrix with new threshold
        actualContactMatrix = tertiaryStructure.getContactMatrix();
        //Should be false
        assertFalse(actualContactMatrix[32][35], "32 - 35 should be false with threshold 4, value is 5.2363...");

        //Changing threshold to 51
        tertiaryStructure.setThreshold(51);
        //Re-calculate contact matrix with new threshold
        actualContactMatrix = tertiaryStructure.getContactMatrix();
        //Should be false
        assertFalse(actualContactMatrix[190][2], "190 - 2 should be false with threshold 51, value is 51.5449...");


        //Load an RNA file
        struc = loadFile("4gxy");
        tertiaryStructure = new TertiaryStructure(struc);
        tertiaryStructure.setThreshold(12);
        actualContactMatrix = tertiaryStructure.getContactMatrix();

        tertiaryStructure.printContactMatrix();
        tertiaryStructure.printDistanceMatrix();

        //USING 12 AS THRESHOLD

        //Check if matrix length is right
        assertEquals(actualContactMatrix.length, tertiaryStructure.getContactMatrix().length, "contact matrix length should be the same as distance matrix length");

        //First value should be not be equal to himself.
        assertFalse(actualContactMatrix[0][0], "first value should be false");

        //Last value should not be false.
        assertFalse(actualContactMatrix[actualContactMatrix.length-1][actualContactMatrix.length-1], "last value should be false");

        //Should be true
        assertTrue(actualContactMatrix[0][1], "first with second value should be true");

        //Should be true
        assertTrue(actualContactMatrix[actualContactMatrix.length-1][actualContactMatrix.length-2], "last two values should be true");

        //Should be false, distance is 14.3414...
        assertFalse(actualContactMatrix[32][35], "32 - 35 value should be true, distance is 14.3414...");

        //Should be false, distance is 17.4826...
        assertFalse(actualContactMatrix[7][3], "7 - 3 value should be false, distance is 17.4826...");

        //Changing threshold to 20 should make 7 - 3 true
        tertiaryStructure.setThreshold(20);
        //Re-calculate contact matrix with new threshold
        actualContactMatrix = tertiaryStructure.getContactMatrix();
        //Should be true
        assertTrue(actualContactMatrix[7][3], "7 - 3 should be true, with threshold 20, value is 17.4826...");

        //Changing threshold to 4 should make 32 - 35 false
        tertiaryStructure.setThreshold(4);
        //Re-calculate contact matrix with new threshold
        actualContactMatrix = tertiaryStructure.getContactMatrix();
        //Should be false
        assertFalse(actualContactMatrix[32][35], "32 - 35 should be false with threshold 4, value is 14.3414...");

        //Changing threshold to 49
        tertiaryStructure.setThreshold(49);
        //Re-calculate contact matrix with new threshold
        actualContactMatrix = tertiaryStructure.getContactMatrix();
        //Should be false
        assertTrue(actualContactMatrix[132][35], "132 - 35 should be true with threshold 49, value is 48.6214...");

    }

    @Test
    @DisplayName("Is type right?")
    void testGetType(){
        //Load a PROTEIN file
        Structure struc = loadFile("3mge");
        TertiaryStructure tertiaryStructure = new TertiaryStructure(struc);

        //Should be protein
        assertEquals("PROTEIN", tertiaryStructure.getType());

        //Load an RNA file
        struc = loadFile("4gxy");
        tertiaryStructure = new TertiaryStructure(struc);

        //Should be rna
        assertEquals("RNA", tertiaryStructure.getType());
    }

    /**
     * Load a PDB files, returns a structure, file should be in ../resources
     * @param fileName name of the file to load
     * @return returns a structure
     */
    private Structure loadFile(String fileName){
        PDBFileReader pdbreader = new PDBFileReader();
        pdbreader.setPath("../resources");
        try{
            return pdbreader.getStructureById(fileName);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}