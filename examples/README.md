# Examples of use of STAlign

## Create Structural Trees from AAS files and align them

Folder `examples/aas` contains example AAS files of the paper:

Quadrini, Michela and Tesei, Luca and Merelli, Emanuela, "Proximity-based 
Comparison of RNA and Protein 3D Structures", 2023 (Submitted)

Example 1 corresponds to Figure 5 of the paper:
  * example1-diagram.pdf - image of the AAS as a diagram
  * example1.aas - AAS file of the diagram
  * example1.tex - structural tree as LaTeX file generated with STAlign
  * example1.pdf - Compiled LaTeX file
  
Commands to generate structural tree as LaTeX file and corresponding pdf ran 
from a folder containing the file `STAlign.jar` and the folder `examples`:

	> java -jar STAlign.jar -sm examples/aas/example1.aas -l -o examples/aas/example1.tex
	> latex examples/aas/example1.tex

Example 2 corresponds to Figure 10 of the paper:
  * example2-diagram.pdf - image of the AAS as a diagram
  * example2.aas - AAS file of the diagram
  * example2.tex - structural tree as LaTeX file generated with STAlign
  * example2.pdf - Compiled LaTeX file
  
Command to generate structural tree as LaTeX file from a folder containing the 
STAlign jar files and the folder examples:

	> java -jar STAlign.jar -sm examples/aas/example2.aas -l -o examples/aas/example2.tex
	> latex examples/aas/example2.tex

The alignment of the two structures is obtained with the following command:

	> java -jar STAlign.jar -am examples/aas/example1.aas examples/aas/example2.aas -l -o examples/aas/example12aligned.tex
	
This displays:

	Distance = 200.0

and a minimum aligned tree is produced in file `examples/aas/example12aligned.tex`, which can be compiled into a pdf:

	> latex examples/aas/example12aligned.tex
	
## Align a folder containing PDB files

We selected three sets of PDB files corresponding to a tRNA of Escherichia Coli. 
We demonstrate that the distances computed by STAlign take into account spatial
information and differentiate the molecules with a positive distance. On the 
contrary, the alignment of secondary structures of the same molecules results in 
equality. 

The following commands assumes that they are ran from a folder containing the files 
`STAlign.jar`, `STAlignWorkbench.jar` and the sub-folder `examples`.

Process all the files in folder "tRNA-1". Each file is read as a PDB file. 
Comma-separated values files "STAlignProcessedStructures.csv" and 
"STAlignComparisonResults.csv" are created in the folder "tRNA-1". The former 
contains the description of all the biomolecules that were found and correctly 
processed. The latter contains, for each pair of processed biomolecules, the ASA 
Distance between the two corresponding structural trees and execution time 
information.

	> java -jar STAlignWorkbench.jar -f examples/tRNA-1

Process all the files in folder "tRNA-2" using a threshold of 10 ångström (Å) 
instead of the default of 4 Å because with the default threshold no bonds are detected.

	> java -jar STAlignWorkbench.jar -t 10 -f examples/tRNA-2       

Process all the files in folder "tRNA-3" using a threshold of 6 Å because with
a higher threshold an out of memory error is generated as the molecule 1gtr.pdb 
is too big and the corresponding tree cannot be generated. Moreover, the Java 
virtual machine is instructed to augment the allocation of memory with the 
option -Xmx6G. In this case the requested amount is 6GB of memory (6G in the 
option). With less memory the alignment library StatAlign generates an out of 
memory error:

	> java -Xmx6G -jar STAlignWorkbench.jar -t 6 -f examples/tRNA-3

The files STAlignComparisonResults.csv in each folder show that (mostly) all 
the AAS are not equal, having an ASA distance greater than 0.

## Aligning secondary structures of the three set of PDB files

To determine the secondary Structure of the PDB of a given RNA molecule  
the [RNApdbee 2.0 website](http://rnapdbee.cs.put.poznan.pl) can be used. 
Proceed as follows:

1.	upload the RNA 3D structure from PDB using the ID of the molecule (es. 1gtr)
2.	select the first model only 
3.	identify base pairs of the secondary structure using the DNA/DSSR algorithm 
without including non-canonical base pairs 
4.	resolve and encode the secondary structure topology using the Hybrid Algorithm
5.	identify structural elements treating pseudoknots as paired residues

We determined the secondary structures of all the tree sets of PDB files and we 
put them in the following folders inside the folder `examples`:
  * `tRNA2ndStructures-1` 
  * `tRNA2ndStructures-2`
  * `tRNA2ndStructures-3`

ASPRAlign can be used to align these secondary structures. ASPRAlign can be 
downloaded from <https://github.com/bdslab/aspralign>. We suppose that the file
`ASPRAlignWorkbench.jar` is put in the same folder as above. 

Compute the ASPRA distance between the secondary structures of all molecules in the
folders `tRNA2ndStructures-1`, `tRNA2ndStructures-2` and `tRNA2ndStructures-3`. 
Comma-separated values files "ASPRAlignProcessedStructures.csv" and 
"ASPRAlignComparisonResults.csv" are created in the corresponding folders. 
The former file contains the description of all the biomolecules that were found and 
correctly processed. The latter one contains, for each pair of processed biomolecules, 
the ASPRA Distance between the secondary structures and execution time information.

	> java -jar ASPRAlignWorkbench.jar -f examples/tRNA2ndStructures-1

	> java -jar ASPRAlignWorkbench.jar -f examples/tRNA2ndStructures-2
	
	> java -jar ASPRAlignWorkbench.jar -f examples/tRNA2ndStructures-3

The files ASPRAlignComparisonResults.csv in each folder show that all 
the secondary structures are equal, having ASPRA distance equal to 0.
