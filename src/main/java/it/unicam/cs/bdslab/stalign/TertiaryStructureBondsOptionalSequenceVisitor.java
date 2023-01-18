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
 * along with STAlign. If not, see <http://www.gnu.org/licenses/>.
 */
// Generated from C:/Users/marco/IdeaProjects/TertiaryStructuresComparator/src/main/resources\TertiaryStructureBondsOptionalSequence.g4 by ANTLR 4.10.1

package it.unicam.cs.bdslab.stalign;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TertiaryStructureBondsOptionalSequenceParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface TertiaryStructureBondsOptionalSequenceVisitor<T>
	extends ParseTreeVisitor<T> {
    /**
     * Visit a parse tree produced by the {@code AasFormat} labeled
     * alternative in
     * {@link TertiaryStructureBondsOptionalSequenceParser#strucure}.
     * 
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAasFormat(
	    TertiaryStructureBondsOptionalSequenceParser.AasFormatContext ctx);

    /**
     * Visit a parse tree produced by the {@code sequenceContinue} labeled
     * alternative in
     * {@link TertiaryStructureBondsOptionalSequenceParser#sequence}.
     * 
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSequenceContinue(
	    TertiaryStructureBondsOptionalSequenceParser.SequenceContinueContext ctx);

    /**
     * Visit a parse tree produced by the {@code sequenceEnd} labeled
     * alternative in
     * {@link TertiaryStructureBondsOptionalSequenceParser#sequence}.
     * 
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSequenceEnd(
	    TertiaryStructureBondsOptionalSequenceParser.SequenceEndContext ctx);

    /**
     * Visit a parse tree produced by the {@code bondsContinue} labeled
     * alternative in
     * {@link TertiaryStructureBondsOptionalSequenceParser#bonds}.
     * 
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBondsContinue(
	    TertiaryStructureBondsOptionalSequenceParser.BondsContinueContext ctx);

    /**
     * Visit a parse tree produced by the {@code bondsEnd} labeled alternative
     * in {@link TertiaryStructureBondsOptionalSequenceParser#bonds}.
     * 
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBondsEnd(
	    TertiaryStructureBondsOptionalSequenceParser.BondsEndContext ctx);

    /**
     * Visit a parse tree produced by
     * {@link TertiaryStructureBondsOptionalSequenceParser#bond}.
     * 
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBond(TertiaryStructureBondsOptionalSequenceParser.BondContext ctx);
}