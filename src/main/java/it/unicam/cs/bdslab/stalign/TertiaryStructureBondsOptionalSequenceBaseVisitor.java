// Generated from C:/Users/marco/IdeaProjects/TertiaryStructuresComparator/src/main/resources\TertiaryStructureBondsOptionalSequence.g4 by ANTLR 4.10.1

	package it.unicam.cs.bdslab.stalign;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link TertiaryStructureBondsOptionalSequenceVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class TertiaryStructureBondsOptionalSequenceBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements TertiaryStructureBondsOptionalSequenceVisitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAasFormat(TertiaryStructureBondsOptionalSequenceParser.AasFormatContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSequenceContinue(TertiaryStructureBondsOptionalSequenceParser.SequenceContinueContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSequenceEnd(TertiaryStructureBondsOptionalSequenceParser.SequenceEndContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBondsContinue(TertiaryStructureBondsOptionalSequenceParser.BondsContinueContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBondsEnd(TertiaryStructureBondsOptionalSequenceParser.BondsEndContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBond(TertiaryStructureBondsOptionalSequenceParser.BondContext ctx) { return visitChildren(ctx); }
}