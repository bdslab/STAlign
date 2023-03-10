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

/**
 * Collector of strings for messages to send on the console.
 *
 * @author Luca Tesei
 *
 */
public interface CommandLineMessages {
    public static String VERSION = "0.9";

    public static String LAUNCH_COMMAND = "java -jar STAlign.jar";
    public static String HEADER = "\n\nSTAlign Comparator version " + VERSION
            + " - Build Structural Trees or "
            + "calculate ASA Distance by alignign Structural Trees of "
            + "Biomolecules.\nDefault "
            + "input file format is PDB format (see "
            + "https://www.rcsb.org/)."
            + "\nAlternatively, the Arc Annotated Sequence format can be used, "
            + "in which "
            + "bonds are expressed as a list (i_1,j_1);(i_2,j_2); ... ;"
            + "(i_m,j_m) where each index i_k, j_k belongs to the interval "
            + "[1,n] (where n is the length of the primary sequence) and "
            + "i_k < j_k for all k. In this file format the sequence is optional."
            + "Default output is a linearised tree of "
            + "the form (\"node-label\", [list-of-children]), use option -l to "
            + "change to LaTeX output. The LaTeX code can be processed with "
            + "LaTeX to produce a graphical representation of the tree in a "
            + "pdf file. Option -o for specifying output file is optional, if "
            + "not specified the result is printed on the standard output.\n\n";
    public static String USAGE_EXAMPLES = "\nUsage examples:\n\n>" + LAUNCH_COMMAND
            + " -sc 1o0b -o 1o0b.txt\n\n" + "Produce file 1o0b.txt containing the linearised tree text of the structural tree "
            	+ "corresponding to the PDB molecule with code 1o0b\n\n"
            + LAUNCH_COMMAND + " -d -af examples/tRNA/1o0b.pdb examples/tRNA/1o0c.pdb\n\nPrint on the standard output the ASA distance between the two structural trees "
            	+ "derived from the given PDB files\n\n";

    public static String LAUNCH_COMMAND_WB = "java -jar STAlignWorkbench.jar";
    // TODO modificare stringhe _WB
    public static String HEADER_WB = "\n\nSTAlign Workbench Comparator version " + VERSION
            + " - Compare all the biomolecule files in a given "
            + "input folder by computing the ASA Distance between all possible "
            + "pairs. All the files are expected to be in PDB format "
            + "or in Arc Annotated "
            + "Sequences format. The primary sequence is optional. The output is given as two "
            + "comma-separated values files describing the processed "
            + "structures and containing the ASA Distance calculated "
            + "for each pair of processed structures. By default the "
            + "output files are put in the input folder. Use option -o"
            + " file-1 file-2 to specifiy different output files.\n";
    public static String USAGE_EXAMPLES_WB = "\nUsage examples:\n\n>" + LAUNCH_COMMAND_WB
            + "  -f examples/tRNA\n\nProcesses all the files in folder "
            + "\"TestWorkBench1\". \"tRNA\". Each file is read as a "
            + "PDB file or Arc Annotated Sequence format.\n"
            + "Comma-separated values files \"STAlignProcessedStructures.csv\" and "
            + "\"STAlignComparisonResults.csv\" are created in the folder\n"
            + "\"tRNA\". The former contains the description of all the\n"
            + "biomolecules that were found and correctly processed. The latter contains,\n"
            + "for each pair of processed biomolecules, the ASA Distance between the two\n"
            + "correspondig structural trees and execution time information.\n"
            + "\n\n" + LAUNCH_COMMAND_WB
            + " -f examples/tRNA -o stucts.csv cmpr.csv -n\n"
            + "my-config.txt\n\nProcesses all the files "
            + "in folder \"tRNA\" as above but produce the description"
            + " of processed structures in file \"structs.csv\" and comparison "
            + "results in file \"cmpr.csv\". Instead of using \"STAlign-config.txt\" "
            + "default configuration file, use \"my-config.txt\" as configuration file.\n\n";

    public static String COPYRIGHT = "STAlign Copyright (C) 2022 BioShape and Data Science Lab at the University of Camerino,"
            + " Italy - http://www.emanuelamerelli.eu/bigdata/\n\n";
    public static String SHORT_NOTICE = "This program comes with ABSOLUTELY NO WARRANTY; for details use "
            + "option '-i'. This is free software, and you are welcome to redistribute it "
            + "under certain conditions; use option '-i' for more details.\n\n";
    public static String LONG_NOTICE = "This program is free software: you can redistribute it and/or modify "
            + "it under the terms of the GNU General Public License as published by "
            + "the Free Software Foundation, either version 3 of the License, or " + "any later version.\n\n"
            + "This program is distributed in the hope that it will be useful, "
            + "but WITHOUT ANY WARRANTY; without even the implied warranty of "
            + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the "
            + "GNU General Public License for more details.\n\n"
            + "You should have received a copy of the GNU General Public License "
            + "along with this program.  If not, see <http://www.gnu.org/licenses/>.\n\n";
    public static String REPORT_TO = "Please report any issue to luca.tesei@unicam.it or to Luca Tesei, "
            + "Polo Informatico, via Madonna delle Carceri 9, 62032 Camerino (MC) Italy.";

}
