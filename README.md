# STAlign - Structural Tree Alignment v0.9

STAlign builds Structural Trees of 3D biomolecules (RNA, Proteins) and computes 
the Algebraic Structural Alignment (ASA) Distance.

<!---If you use STAlign please cite us:
- Quadrini, M., Tesei, L. "Pippo", Submitted (2023).
--->

## Accepted Input file formats 

* PDB format <https://www.rcsb.org/>
* Arc Annotated Sequence: includes the sequence (optional) and the bonds, expressed as a list `(i_1,j_1);(i_2,j_2); ... ;(i_m,j_m)` where each index `i_k, j_k` belongs to the interval `[1,n]` where `n` is the length
of the primary sequence and `i_k < j_k`  for all `k`.


## STAlign is distributed with two executable jar files
* STAlign.jar (basic comparator and tree builder) and 
* STAlignWorkbench.jar (workbench comparator)

## STAlign.jar usage examples

* `> java -jar STAlign.jar -sc 1o0b -o 1o0b.txt`

Produce file `1o0b.txt` containing the linearised tree text of the structural tree
corresponding to the PDB molecule with code `1o0b`.

* `> java -jar STAlign.jar -d -af examples/tRNA/1o0b.pdb examples/tRNA/1o0c.pdb`

Print on the standard output the ASA distance between the two structural trees 
derived from the given PDB files.

See folder `examples` for some sample input files in both notations
coming from public databases or from the paper

## Output formats

Default output format is a linearised tree description of the form 

`("node-label", [list-of-children])`

An alternative output format is `LaTeX` code that can be processed with 
LaTeX to produce a graphical representation of the tree in a pdf file.
LaTeX format should be used for relative small structures, otherwise
the pdf file may not be produced or may be not readable.  

Default output stream is Standard Output. Output can be sent to a file 
using option -o 

## Configuration

The costs for the basic operations of alignment are specified in the default
configuration file `STAlign-config.txt`. The values can be changed directly 
in this file or by using a different configuration file specified with 
option -n. The default configuration file must reside in the same folder in 
which the command is launched, while the file specified with option -n 
can reside in any folder.

## STAlignWorkbench.jar usage examples

* `> java -jar STAlignWorkbench.jar -f examples/tRNA`

Processes all the files in folder `tRNA`. Each file is read as a
PDB file or Arc Annotated Sequence format. Comma-separated values files 
`STAlignProcessedStructures.csv` and
`STAlignComparisonResults.csv` are created in the folder
"`tRNA`". The former contains the description of all the
biomolecules that were found and correctly processed. The latter contains,
for each pair of processed biomolecules, the ASA Distance between the two
correspondig structural trees and execution time information.

* `>java -jar STAlignWorkbench.jar -f examples/tRNA -o stucts.csv cmpr.csv -n my-config.txt`

Processes all the files in folder `tRNA` as above but produce
the description of processed structures in file `structs.csv` and
comparison results in file `cmpr.csv`. Instead of using
`STAling-config.txt` default configuration file, use `my-config.txt` as
configuration file.

See folder `examples` for some sample input folders containing structures 
coming from public databases.

# Installation

Download the zip file of last version of STAlign from folder `download` at <https://github.com/bdslab/STAlign/>

Direct link: <https://github.com/bdslab/STAlign/tree/master/download>

and put it in any position of your drive. 

Unzip the file with the facilities of your operating system. The folder 
`STAlign-<VersionNumber>` is created containing the following files:

- STAlign.jar --- executable jar of the basic STAlign comparison
- STAlignWorkbench.jar --- executable jar for the STAlign workbench comparator
- STAlign-config.txt --- default STAlign configuration file
- STAlign-config-alternative.txt --- alternative STAlign configuration file
- examples --- folder containing sample input and output files
- INSTALL.txt --- information on STAlign installation
- README.md --- STAlign description and usage information
- COPYING.txt --- copyright information
- LICENSE --- full GNU GPL Version 3 License
- CHANGELOG.txt --- information about the evolution of STAlign versions

The executable jar files runs on every Linux, Windows and Mac OS platform
in which a Java SE Runtime Environment 8 is installed. 

For information and installing the Java Runtime Environment see
<http://www.oracle.com/technetwork/java/javase/downloads/index.html>

## Using STAlign

Open a terminal window of your operating system and use the change directory 
(cd) command to move to a folder in which the executable jar(s) and the
configuration file(s) were placed. To launch the basic STAlign comparator 
digit:

`> java -jar STAlign.jar <options>`

The following <options> can be used:

    -a,--align <input-file1 input-file2>   Align two given structures
                                           producing alignment tree and
                                           distance
    -c,--chkpair                           Check the presence of only
                                           standard Watson-Crick and wobble
                                           base pairing (disabled by default)
    -d,--outdist                           Output only distance, no alignment
                                           tree (works only with option -a)
    -e,--showscores                        Show current values of edit scores
                                           used for alignment
    -g,--alg <input-file>                  Produce the algebraic RNA tree
                                           corresponding to the given
                                           structure
    -h,--help                              Show usage information
    -i,--info                              Show license and other info
    -l,--latexout                          Output in LaTeX format instead of
                                           linearised tree
    -n,--useconffile <conf-file>           Use the specified configuration
                                           file instead of the default one
    -o,--out <output-file>                 Output result on the given file
                                           instead of standard output
    -s,--struct <input-file>               Produce the structural RNA tree
                                           corresponding to the given
                                           structure

## Using STAlignWorkbench

Open a terminal window of your operating system and use the change directory 
(cd) command to move to a folder in which the executable jar(s) and the
configuration file(s) were placed. To launch the basic STAlignWorkbench 
comparator digit:

`> java -jar STAlignWorkbench.jar <options>`

The following <options> can be used:

    -c,--chkpair                   Check the presence of only standard
                                   Watson-Crick and wobble base pairing
                                   (disabled by default)
    -e,--showscores                Show current values of edit scores used
                                   for alignment
    -f,--input <input-folder>      Process the files in the given folder
    -h,--help                      Show usage information
    -i,--info                      Show license and other info
    -n,--useconffile <conf-file>   Use the specified configuration file
                                   instead of the default one
    -o,--output <file-1 file-2>    Output structure descriptions on file-1
                                   and comparison results on file-2 instead
                                   of generating the default ouput files

# Copyright and License

STAling Copyright (C) 2022 BioShape and Data Science Lab at the University of Camerino,
Italy 
- Web: <http://www.emanuelamerelli.eu/bigdata/> 
- GitHub: <https://github.com/bdslab>

This program is free software: you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation, either version 3 of the License, or any later
version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License along
with this program.  If not, see <http://www.gnu.org/licenses/>.

# Credits

STAlign is developed and tested by:

- Filippo Lampa, University of Camerino
- Marco Serenelli, University of Camerino
- Luca Tesei, University of Camerino

STAlign is tested by:

- Michela Quadrini, University of Camerino

# Contact Information

Please report any issue to luca.tesei@unicam.it or to Luca Tesei, Polo
Informatico, via Madonna delle Carceri 7, 62032 Camerino (MC) Italy.


