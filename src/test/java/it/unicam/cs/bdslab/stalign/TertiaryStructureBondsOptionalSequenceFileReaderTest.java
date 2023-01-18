package it.unicam.cs.bdslab.stalign;

import org.biojava.nbio.structure.contact.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for the RNA/Protein's sequence and bons file reader
 *
 * @author Marco Serenelli
 *
 */
class TertiaryStructureBondsOptionalSequenceFileReaderTest {

    @Test
    @DisplayName("Parsing sequence from file")
    void testReadSequence() throws IOException {

        //1
        String sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/sequenceTests/sequenceTest1.txt");
        //expectedSequence
        String expectedSequence = "GGGAAG";
        assertEquals(expectedSequence, sequence, "1st comparison failed");

        //2
        sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/sequenceTests/sequenceTest2.txt");
        //expectedSequence
        expectedSequence = "gaggaaagucccgccUCCAGAUCAAGGGAAGUCCCGCGAGGGACAAGGGU" +
                "AGUACCCUUGGCAACUGCACAGAAAACUUACCCCUAAAUAUUCAAUGAGG" +
                "AUUUGAUUCGACUCUUACCUUGGCGACAAGGUAAGAUAGAUGAAGAGAAU" +
                "AUUUAGGGGUUGAAACGCAGUCCUUCCCGGAGCAAGUAGGGGGGUCAAUG" +
                "AGAAUGAUCUGAAGACCUCCCUUGACGCAUAGUCGAAUCCCCCAAAUaca" +
                "gaagcgggcuua";
        assertEquals(expectedSequence, sequence, "2nd comparison failed");

        //3
        sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/sequenceTests/sequenceTest3.txt");
        //expectedSequence
        expectedSequence = "gaggaaagucccgccUCCUGGCCUAAAGGAGUCUCUAUAGAGACAAGGGC" +
                "AACACCCUUGGCAACUAUACAGAAACAAGUACCUUGAAAGCUACAUGUGA" +
                "AAAUGAUUGUGCCCCUUCCUCUGGUAACGGAGGAAGAAACAUGAGAGUAG" +
                "CUUUCAAGGAUGAAAAGAUAGACCUCCUAGGAGCAAGUAGAGGGAAAGAU" +
                "GAGACUAGGCCCGAUUUCCCUCUAGGACGCAUAGCCAAAUCCCCCAACCA" +
                "UUacaaaagcgggcuua";
        assertEquals(expectedSequence, sequence, "3th comparison failed");

        //4
        sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/sequenceTests/sequenceTest4.txt");
        //expectedSequence
        expectedSequence = "GCGGGGAAAGGAGGCGAGGCAGUUGCGGCUCAGGCUUCGGUUAUGGGCUG" +
                "AGGAAAGUCCGGGCUCCCAAAAGACCAGACUUGCUGGGUAACGCCCAGUG" +
                "CGGGUGACCGUGAGGAGAGUGCCACAGAAACAUACCGCCGAUGGCCUGCU" +
                "UGCAGGCACAGGUAAGGGUGCAAGGGUGCGGUAAGAGCGCACCAGCAACA" +
                "UCGAGAGGUGUUGGCUCGGUAAACCCCGGUUGGGAGCAAGGUGGAGGGAC" +
                "AACGGUUGGUCUUUUACCUGUUCCGUUUAUGGACCGCUAGAGGUGGCUAG" +
                "UAAUAGCCAUCCCAGAGAGAUAACAGCCCUCUGUCUUCGACAGAGAACAG" +
                "AACCCGGCUUAUGUCCUGCUUUCCCUACUUUAUUU";
        assertEquals(expectedSequence, sequence, "4th comparison failed");

        //5th
        sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/sequenceTests/sequenceTest5.txt");
        //expectedSequence
        expectedSequence = "aacuaugacuCAACGUUCUGAGGGAUGUAUAUUUCACAGCGGGGUUUGGC" +
                "AACUAAACUUCGAGAGGGUCAAGUAACGUAAAAGGCGUUGCUAGUGGGCA" +
                "GUGCUGACCUCUGGACCGGCGAUCCGCGACGAGAGACGCCCCCGCGACAU" +
                "CAUCAAAUUGCGGGGACUCCCUAAAGCUGUGUGCUACUAAGCAGGUGCCG" +
                "AAACGACCUGUGGCCGGGGUAAUGACCUGGGGUAUAGUAAAAACGCACCA" +
                "GAUGCCACAAUGGGUGAUCCGCAGCCAAGUCCUAAGGCAACAGGCCUCUU" +
                "CCCGCCCGUGGGGGAGAGUGUCCCGCGCUAUGGAUGCAGUUCAACGACUA" +
                "AACGGUGAUGGGCUAGCGUAAUGAAUGCGUUCUGCACACUCUUACGCCGG" +
                "CUUAAGAUAUAGUCUACUCCCACUCCGAAAGGAGGGGUACUAAAAGcucu" +
                "uaaggu";
        assertEquals(expectedSequence, sequence, "5th comparison failed");

        //6th
        sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/sequenceTests/sequenceTest6.txt");
        //expectedSequence
        expectedSequence = "CAGAAAAGUUACCACAGGGAUGUGCUAAGUCUAUUAAGACAAAUUUACAA" +
                "CAUAACGUCCCAGGCAUCGACUCCCAUUGAUGCCUAGUCCGGCUAUAGCU" +
                "UACUUGUGCUUGCUAUAGUUAGCUCUCUAGAAGAGUGUAAGGGCAACACG" +
                "UCUGGAUGCGGGGAACUCUCGCUAGGUCUUUGGUACCAAACAUUGGCGGU" +
                "AACACCCCAAUGUAGAGUCUGUUGGGGCAGACGGGUAAAAAUCCAAAGAA" +
                "UAGAGACAAUCCGCAGCUGACCUGGCCAAAAGUAUAUUUACAAAGUAGCU" +
                "AGGCAGUUCAACGCUCGCUAAGAUGUGGGUUGACCAUUAAUGGUCGGCUU" +
                "AAGGUACGGGCUACGCCACCCGAGAGGGUGCAACUACCUACCGGGCGCAC" +
                "AACCUGAGAAGUAGUUGGUCAUGGUAGUACGGAACUGGCUUGUGGCAGUC";
        assertEquals(sequence, expectedSequence, "6th comparison failed");

        //7th
        sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/sequenceTests/sequenceTest7.txt");
        //expectedSequence
        expectedSequence = "gaggaaagucccgccUCCAGAUCAAGGGAAGUCCCGCGAGGGACAAGGGU";
        assertEquals(expectedSequence, sequence, "7th comparison failed");
    }

    @Test
    @DisplayName("Parsing bondlist from file")
    void testReadBondsList() throws IOException {
        //1
        ArrayList<Pair<Integer>> actualBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsTests/bondsTest1.txt");
        //expectedBonds
        ArrayList<Pair<Integer>> expectedBonds = new ArrayList<>();
        expectedBonds.add(new Pair<>(0, 2));
        expectedBonds.add(new Pair<>(0, 3));
        expectedBonds.add(new Pair<>(1, 2));
        expectedBonds.add(new Pair<>(1, 3));
        expectedBonds.add(new Pair<>(2, 1));
        assertEquals(expectedBonds, actualBonds, "1st comparison failed");
        actualBonds.clear();
        expectedBonds.clear();

        //2
        actualBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsTests/bondsTest2.txt");
        //expectedBonds
        expectedBonds.add(new Pair<>(1, 15));
        expectedBonds.add(new Pair<>(3, 13));
        expectedBonds.add(new Pair<>(4, 7));
        expectedBonds.add(new Pair<>(10, 18));
        expectedBonds.add(new Pair<>(20, 44));
        expectedBonds.add(new Pair<>(21, 26));
        expectedBonds.add(new Pair<>(22, 24));
        expectedBonds.add(new Pair<>(28, 35));
        expectedBonds.add(new Pair<>(29, 33));
        expectedBonds.add(new Pair<>(31, 34));
        expectedBonds.add(new Pair<>(36, 47));
        expectedBonds.add(new Pair<>(41, 50));
        expectedBonds.add(new Pair<>(53, 59));
        expectedBonds.add(new Pair<>(55, 61));
        assertEquals(expectedBonds, actualBonds, "2nd comparison failed");
        actualBonds.clear();
        expectedBonds.clear();

        //3
        //expected an error as you can't create two equals bonds
        assertThrows(IllegalArgumentException.class, () -> TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsTests/bondsTest3.txt"), "3th comparison failed");
        actualBonds.clear();
        expectedBonds.clear();

        //4
        actualBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsTests/bondsTest4.txt");
        //expectedBonds
        expectedBonds.add(new Pair<>(1,4));
        expectedBonds.add(new Pair<>(1,8));
        expectedBonds.add(new Pair<>(1,12));
        expectedBonds.add(new Pair<>(8,10));
        expectedBonds.add(new Pair<>(8,12));
        expectedBonds.add(new Pair<>(12,16));
        expectedBonds.add(new Pair<>(17,22));
        expectedBonds.add(new Pair<>(18,20));
        expectedBonds.add(new Pair<>(19,21));
        assertEquals(expectedBonds, actualBonds, "4th comparison failed");
        actualBonds.clear();
        expectedBonds.clear();

        //5
        actualBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsTests/bondsTest5.txt");
        //expectedBonds
        expectedBonds.add(new Pair<>(1,4));
        expectedBonds.add(new Pair<>(1,7));
        expectedBonds.add(new Pair<>(7,10));
        expectedBonds.add(new Pair<>(7,12));
        expectedBonds.add(new Pair<>(12,17));
        expectedBonds.add(new Pair<>(19,26));
        expectedBonds.add(new Pair<>(20,23));
        expectedBonds.add(new Pair<>(22,25));
        assertEquals(expectedBonds, actualBonds, "5th comparison failed");
        actualBonds.clear();
        expectedBonds.clear();

        //6
        actualBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsTests/bondsTest6.txt");
        //expectedBonds
        expectedBonds.add(new Pair<>(1,6));
        expectedBonds.add(new Pair<>(1,12));
        expectedBonds.add(new Pair<>(6,12));
        expectedBonds.add(new Pair<>(12,16));
        expectedBonds.add(new Pair<>(17,21));
        expectedBonds.add(new Pair<>(19,24));
        assertEquals(expectedBonds, actualBonds, "6th comparison failed");
        actualBonds.clear();
        expectedBonds.clear();

        //7
        actualBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsTests/bondsTest7.txt");
        //expectedBonds
        expectedBonds.add(new Pair<>(1,6));
        expectedBonds.add(new Pair<>(1,12));
        expectedBonds.add(new Pair<>(6,12));
        expectedBonds.add(new Pair<>(7,17));
        expectedBonds.add(new Pair<>(12,17));
        expectedBonds.add(new Pair<>(18,22));
        expectedBonds.add(new Pair<>(18,24));
        expectedBonds.add(new Pair<>(20,24));
        assertEquals(expectedBonds, actualBonds, "7th comparison failed");
        actualBonds.clear();
        expectedBonds.clear();
    }

    @Test
    @DisplayName("Parsing bonds and sequence from file")
    void testReadBondsSequence() throws IOException {
        //1
        ArrayList<Pair<Integer>> actualBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsSequenceTests/bondsSequenceTest1.txt");
        String sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/bondsSequenceTests/bondsSequenceTest1.txt");
        //expectedSequence
        String expectedSequence = "GGGAAG";
        //expectedBonds
        ArrayList<Pair<Integer>> expectedBonds = new ArrayList<>();
        expectedBonds.add(new Pair<>(0, 2));
        expectedBonds.add(new Pair<>(0, 3));
        expectedBonds.add(new Pair<>(1, 2));
        expectedBonds.add(new Pair<>(1, 3));
        expectedBonds.add(new Pair<>(2, 1));
        assertEquals(expectedBonds, actualBonds, "1st bonds comparison failed");
        assertEquals(sequence, expectedSequence, "1st structure comparison failed");
        actualBonds.clear();
        expectedBonds.clear();

        //2
        actualBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsSequenceTests/bondsSequenceTest2.txt");
        sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/bondsSequenceTests/bondsSequenceTest2.txt");
        //expectedSequence
        expectedSequence = "gaggaaagucccgccUCCAGAUCAAGGGAAGUCCCGCGAGGGACAAGGGU";
        //expectedBonds
        expectedBonds = new ArrayList<>();
        expectedBonds.add(new Pair<>(1,2));
        expectedBonds.add(new Pair<>(1,6));
        expectedBonds.add(new Pair<>(1,12));
        expectedBonds.add(new Pair<>(6,12));
        expectedBonds.add(new Pair<>(12,16));
        expectedBonds.add(new Pair<>(17,21));
        expectedBonds.add(new Pair<>(19,24));
        assertEquals(expectedBonds, actualBonds, "2nd bonds comparison failed");
        assertEquals(sequence, expectedSequence, "2nd structure comparison failed");
        actualBonds.clear();
        expectedBonds.clear();


        //3
        actualBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsSequenceTests/bondsSequenceTest3.txt");
        sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/bondsSequenceTests/bondsSequenceTest3.txt");
        //expectedSequence
        expectedSequence = "CUUUCAAGGAUGAAAAGAUAGACCUCCUAGGAGCAAGUAGAGGGAAAGAU";
        //expectedBonds
        expectedBonds = new ArrayList<>();
        expectedBonds.add(new Pair<>(1,6));
        expectedBonds.add(new Pair<>(1,12));
        expectedBonds.add(new Pair<>(6,12));
        expectedBonds.add(new Pair<>(7,17));
        expectedBonds.add(new Pair<>(12,17));
        expectedBonds.add(new Pair<>(18,22));
        expectedBonds.add(new Pair<>(18,24));
        expectedBonds.add(new Pair<>(20,24));
        assertEquals(expectedBonds, actualBonds, "3th bonds comparison failed");
        assertEquals(sequence, expectedSequence, "3th structure comparison failed");
        actualBonds.clear();
        expectedBonds.clear();

        //4
        actualBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsSequenceTests/bondsSequenceTest4.txt");
        sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/bondsSequenceTests/bondsSequenceTest4.txt");
        //expectedSequence
        expectedSequence = "aacuaugacuCAACGUUCUGAGGGAUGUAUAUUUCACAGCGGGGUUUGGC";
        //expectedBonds
        expectedBonds.add(new Pair<>(1,4));
        expectedBonds.add(new Pair<>(1,8));
        expectedBonds.add(new Pair<>(1,12));
        expectedBonds.add(new Pair<>(8,10));
        expectedBonds.add(new Pair<>(8,12));
        expectedBonds.add(new Pair<>(12,16));
        expectedBonds.add(new Pair<>(17,22));
        expectedBonds.add(new Pair<>(18,20));
        expectedBonds.add(new Pair<>(19,21));
        assertEquals(expectedBonds, actualBonds, "4th bonds comparison failed");
        assertEquals(sequence, expectedSequence, "4th structure comparison failed");
        actualBonds.clear();
        expectedBonds.clear();

        //5
        actualBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsSequenceTests/bondsSequenceTest5.txt");
        sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/bondsSequenceTests/bondsSequenceTest5.txt");
        //expectedSequence
        expectedSequence = "";
        //expectedBonds
        expectedBonds.add(new Pair<>(1,4));
        expectedBonds.add(new Pair<>(1,7));
        expectedBonds.add(new Pair<>(7,10));
        expectedBonds.add(new Pair<>(7,12));
        expectedBonds.add(new Pair<>(12,17));
        expectedBonds.add(new Pair<>(19,26));
        expectedBonds.add(new Pair<>(20,23));
        expectedBonds.add(new Pair<>(22,25));
        assertEquals(expectedBonds, actualBonds, "5th bonds comparison failed");
        assertEquals(sequence, expectedSequence, "4th structure comparison failed");
        actualBonds.clear();
        expectedBonds.clear();

        //5
        actualBonds = TertiaryStructureBondsOptionalSequenceFileReader.readBondsList("src/test/resources/bondsSequenceTests/bondsSequenceTest6.txt");
        sequence = TertiaryStructureBondsOptionalSequenceFileReader.readSequence("src/test/resources/bondsSequenceTests/bondsSequenceTest6.txt");
        //expectedSequence
        expectedSequence = "gaggaaagucccgccUCCAGAUCAAGGGAAGUCCCGCGAGGGACAAGGGU";
        //expectedBonds
        assertEquals(expectedBonds, actualBonds, "5th bonds comparison failed");
        assertEquals(sequence, expectedSequence, "5th structure comparison failed");
        actualBonds.clear();
        expectedBonds.clear();

    }
}