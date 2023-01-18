// Generated from C:/Users/marco/IdeaProjects/TertiaryStructuresComparator/src/main/resources\TertiaryStructureBondsOptionalSequence.g4 by ANTLR 4.10.1

	package it.unicam.cs.bdslab.tersaling;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TertiaryStructureBondsOptionalSequenceParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TertiaryStructureBondsOptionalSequenceVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code AasFormat}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#strucure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAasFormat(TertiaryStructureBondsOptionalSequenceParser.AasFormatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sequenceContinue}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceContinue(TertiaryStructureBondsOptionalSequenceParser.SequenceContinueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sequenceEnd}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceEnd(TertiaryStructureBondsOptionalSequenceParser.SequenceEndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bondsContinue}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#bonds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBondsContinue(TertiaryStructureBondsOptionalSequenceParser.BondsContinueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bondsEnd}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#bonds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBondsEnd(TertiaryStructureBondsOptionalSequenceParser.BondsEndContext ctx);
	/**
	 * Visit a parse tree produced by {@link TertiaryStructureBondsOptionalSequenceParser#bond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBond(TertiaryStructureBondsOptionalSequenceParser.BondContext ctx);
}