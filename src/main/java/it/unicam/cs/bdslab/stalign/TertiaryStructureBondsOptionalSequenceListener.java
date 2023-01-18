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
// Generated from C:/Users/marco/IdeaProjects/TertiaryStructuresComparator/src/main/resources\TertiaryStructureBondsOptionalSequence.g4 by ANTLR 4.10.1

package it.unicam.cs.bdslab.stalign;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TertiaryStructureBondsOptionalSequenceParser}.
 */
public interface TertiaryStructureBondsOptionalSequenceListener
	extends ParseTreeListener {
    /**
     * Enter a parse tree produced by the {@code AasFormat} labeled
     * alternative in
     * {@link TertiaryStructureBondsOptionalSequenceParser#strucure}.
     * 
     * @param ctx the parse tree
     */
    void enterAasFormat(
	    TertiaryStructureBondsOptionalSequenceParser.AasFormatContext ctx);

    /**
     * Exit a parse tree produced by the {@code AasFormat} labeled alternative
     * in {@link TertiaryStructureBondsOptionalSequenceParser#strucure}.
     * 
     * @param ctx the parse tree
     */
    void exitAasFormat(
	    TertiaryStructureBondsOptionalSequenceParser.AasFormatContext ctx);

    /**
     * Enter a parse tree produced by the {@code sequenceContinue} labeled
     * alternative in
     * {@link TertiaryStructureBondsOptionalSequenceParser#sequence}.
     * 
     * @param ctx the parse tree
     */
    void enterSequenceContinue(
	    TertiaryStructureBondsOptionalSequenceParser.SequenceContinueContext ctx);

    /**
     * Exit a parse tree produced by the {@code sequenceContinue} labeled
     * alternative in
     * {@link TertiaryStructureBondsOptionalSequenceParser#sequence}.
     * 
     * @param ctx the parse tree
     */
    void exitSequenceContinue(
	    TertiaryStructureBondsOptionalSequenceParser.SequenceContinueContext ctx);

    /**
     * Enter a parse tree produced by the {@code sequenceEnd} labeled
     * alternative in
     * {@link TertiaryStructureBondsOptionalSequenceParser#sequence}.
     * 
     * @param ctx the parse tree
     */
    void enterSequenceEnd(
	    TertiaryStructureBondsOptionalSequenceParser.SequenceEndContext ctx);

    /**
     * Exit a parse tree produced by the {@code sequenceEnd} labeled
     * alternative in
     * {@link TertiaryStructureBondsOptionalSequenceParser#sequence}.
     * 
     * @param ctx the parse tree
     */
    void exitSequenceEnd(
	    TertiaryStructureBondsOptionalSequenceParser.SequenceEndContext ctx);

    /**
     * Enter a parse tree produced by the {@code bondsContinue} labeled
     * alternative in
     * {@link TertiaryStructureBondsOptionalSequenceParser#bonds}.
     * 
     * @param ctx the parse tree
     */
    void enterBondsContinue(
	    TertiaryStructureBondsOptionalSequenceParser.BondsContinueContext ctx);

    /**
     * Exit a parse tree produced by the {@code bondsContinue} labeled
     * alternative in
     * {@link TertiaryStructureBondsOptionalSequenceParser#bonds}.
     * 
     * @param ctx the parse tree
     */
    void exitBondsContinue(
	    TertiaryStructureBondsOptionalSequenceParser.BondsContinueContext ctx);

    /**
     * Enter a parse tree produced by the {@code bondsEnd} labeled alternative
     * in {@link TertiaryStructureBondsOptionalSequenceParser#bonds}.
     * 
     * @param ctx the parse tree
     */
    void enterBondsEnd(
	    TertiaryStructureBondsOptionalSequenceParser.BondsEndContext ctx);

    /**
     * Exit a parse tree produced by the {@code bondsEnd} labeled alternative
     * in {@link TertiaryStructureBondsOptionalSequenceParser#bonds}.
     * 
     * @param ctx the parse tree
     */
    void exitBondsEnd(
	    TertiaryStructureBondsOptionalSequenceParser.BondsEndContext ctx);

    /**
     * Enter a parse tree produced by
     * {@link TertiaryStructureBondsOptionalSequenceParser#bond}.
     * 
     * @param ctx the parse tree
     */
    void enterBond(
	    TertiaryStructureBondsOptionalSequenceParser.BondContext ctx);

    /**
     * Exit a parse tree produced by
     * {@link TertiaryStructureBondsOptionalSequenceParser#bond}.
     * 
     * @param ctx the parse tree
     */
    void exitBond(
	    TertiaryStructureBondsOptionalSequenceParser.BondContext ctx);
}