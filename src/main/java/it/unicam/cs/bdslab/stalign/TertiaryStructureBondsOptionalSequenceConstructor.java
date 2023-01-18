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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.biojava.nbio.structure.contact.Pair;

import java.util.ArrayList;
import java.util.Objects;

public class TertiaryStructureBondsOptionalSequenceConstructor
	extends TertiaryStructureBondsOptionalSequenceBaseListener {
    private String sequence;
    private final StringBuffer sequenceBuffer;
    private final ArrayList<Pair<Integer>> bondsList;

    public TertiaryStructureBondsOptionalSequenceConstructor() {
	this.sequence = "";
	this.sequenceBuffer = new StringBuffer();
	this.bondsList = new ArrayList<>();
    }

    public String getSequence() {
	return sequence;
    }

    public ArrayList<Pair<Integer>> getBondsList() {
	return bondsList;
    }

    @Override
    public void enterSequenceContinue(
	    TertiaryStructureBondsOptionalSequenceParser.SequenceContinueContext ctx) {
	this.sequenceBuffer.append(ctx.LETTERS().getText());
    }

    @Override
    public void exitSequenceEnd(
	    TertiaryStructureBondsOptionalSequenceParser.SequenceEndContext ctx) {
	this.sequenceBuffer.append(ctx.LETTERS().getText());
	this.sequence = this.sequenceBuffer.toString();
    }

    @Override
    public void enterBondsContinue(
	    TertiaryStructureBondsOptionalSequenceParser.BondsContinueContext ctx) {
	// add bonds to the list
	int left = Integer.parseInt(ctx.bond().INDEX(0).getText());
	int right = Integer.parseInt(ctx.bond().INDEX(1).getText());
	this.bondsList.add(new Pair<>(left, right));
    }

    @Override
    public void exitBondsContinue(
	    TertiaryStructureBondsOptionalSequenceParser.BondsContinueContext ctx) {
	this.bondsList.sort((o1, o2) -> {
	    // if the first number is equals
	    if (Objects.equals(o1.getFirst(), o2.getFirst())) {
		// we check the second
		if (o1.getSecond() < o2.getSecond()) {
		    // o1 is more in the left
		    return -1;
		} else if (o1.getSecond() > o2.getSecond()) {
		    // o1 is more in the right
		    return 1;
		}
		// error first and second shouldn't both be equals
		throw new IllegalArgumentException(
			"error first and second shouldn't both be equals");
	    } else if (o1.getFirst() < o2.getFirst()) {
		// o1 is more in the left
		return -1;
	    }
	    // o1 is more in the right
	    return 1;
	});
    }

}
