/**
 * STAlign - Structural Tree Alignment
 * 
 * Copyright (C) 2022 BioShape and Data Science Lab at the University of Camerino, Italy - 
 * http://www.emanuelamerelli.eu/bigdata/
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
 * along with ASPRAlign. If not, see <http://www.gnu.org/licenses/>.
 */
package it.unicam.cs.bdslab.stalign;

import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.biojava.nbio.structure.contact.Pair;

import java.util.ArrayList;

public class TertiaryStructureBondsOptionalSequenceFileReader {
    /**
     * Use ANTLR 4 and the grammar defined for TertiaryStructure to read a
     * bonds list from a file.
     *
     * @param filename the name of the file to read
     */
    public static String readSequence(String filename) throws IOException {
	CharStream input = CharStreams.fromFileName(filename);
	// create a lexer that feeds off of input CharStream
	TertiaryStructureBondsOptionalSequenceLexer lexer = new TertiaryStructureBondsOptionalSequenceLexer(
		input);
	// create a buffer of tokens pulled from the lexer
	CommonTokenStream tokens = new CommonTokenStream(lexer);
	// create a parser that feeds off the tokens buffer
	TertiaryStructureBondsOptionalSequenceParser sequenceParser = new TertiaryStructureBondsOptionalSequenceParser(
		tokens);
	// remove default error listeners
	sequenceParser.removeErrorListeners();
	// begin parsing at bonds rule
	TertiaryStructureBondsOptionalSequenceParser.SequenceContext tree = sequenceParser
		.sequence();
	// Create a generic parse tree walker that can trigger callbacks
	ParseTreeWalker walker = new ParseTreeWalker();
	// Create the specialised listener for bonds
	TertiaryStructureBondsOptionalSequenceConstructor constructor = new TertiaryStructureBondsOptionalSequenceConstructor();
	// Walk the tree created during the parse, trigger callbacks
	walker.walk(constructor, tree);
	// Get the parsed secondary structure
	return constructor.getSequence();
    }

    /**
     * Use ANTLR 4 and the grammar defined for TertiaryStructure to read a
     * bonds list from a file.
     *
     * @param filename the name of the file to read
     */
    public static ArrayList<Pair<Integer>> readBondsList(String filename)
	    throws IOException {
	CharStream input = CharStreams.fromFileName(filename);
	// create a lexer that feeds off of input CharStream
	TertiaryStructureBondsOptionalSequenceLexer lexer = new TertiaryStructureBondsOptionalSequenceLexer(
		input);
	// create a buffer of tokens pulled from the lexer
	CommonTokenStream tokens = new CommonTokenStream(lexer);
	// create a parser that feeds off the tokens buffer
	TertiaryStructureBondsOptionalSequenceParser bondsParser = new TertiaryStructureBondsOptionalSequenceParser(
		tokens);
	// remove default error listeners
	bondsParser.removeErrorListeners();
	// begin parsing at bonds rule
	TertiaryStructureBondsOptionalSequenceParser.BondsContext tree = bondsParser
		.bonds();
	// Create a generic parse tree walker that can trigger callbacks
	ParseTreeWalker walker = new ParseTreeWalker();
	// Create the specialised listener for bonds
	TertiaryStructureBondsOptionalSequenceConstructor constructor = new TertiaryStructureBondsOptionalSequenceConstructor();
	// Walk the tree created during the parse, trigger callbacks
	walker.walk(constructor, tree);
	// Get the parsed secondary structure
	return constructor.getBondsList();
    }
}