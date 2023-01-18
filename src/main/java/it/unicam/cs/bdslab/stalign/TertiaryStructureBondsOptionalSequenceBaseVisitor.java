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

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of
 * {@link TertiaryStructureBondsOptionalSequenceVisitor}, which can be
 * extended to create a visitor which only needs to handle a subset of the
 * available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public class TertiaryStructureBondsOptionalSequenceBaseVisitor<T>
	extends AbstractParseTreeVisitor<T>
	implements TertiaryStructureBondsOptionalSequenceVisitor<T> {
    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public T visitAasFormat(
	    TertiaryStructureBondsOptionalSequenceParser.AasFormatContext ctx) {
	return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public T visitSequenceContinue(
	    TertiaryStructureBondsOptionalSequenceParser.SequenceContinueContext ctx) {
	return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public T visitSequenceEnd(
	    TertiaryStructureBondsOptionalSequenceParser.SequenceEndContext ctx) {
	return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public T visitBondsContinue(
	    TertiaryStructureBondsOptionalSequenceParser.BondsContinueContext ctx) {
	return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public T visitBondsEnd(
	    TertiaryStructureBondsOptionalSequenceParser.BondsEndContext ctx) {
	return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public T visitBond(
	    TertiaryStructureBondsOptionalSequenceParser.BondContext ctx) {
	return visitChildren(ctx);
    }
}