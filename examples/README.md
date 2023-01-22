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

Command to process all the PDB files ran from folder containing the file 
`STAlignWorkbench.jar` and the folder `examples`:

	> java -jar STAlignWorkbench.jar -f examples/tRNA-1

Processes all the files in folder "tRNA-1". Each file is read as a PDB file. 
Comma-separated values files "STAlignProcessedStructures.csv" and 
"STAlignComparisonResults.csv" are created in the folder "tRNA-1". The former 
contains the description of all the biomolecules that were found and correctly 
processed. The latter contains, for each pair of processed biomolecules, the ASA 
Distance between the two correspondig structural trees and execution time 
information.







