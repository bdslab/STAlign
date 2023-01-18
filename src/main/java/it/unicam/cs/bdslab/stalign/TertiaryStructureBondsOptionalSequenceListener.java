// Generated from C:/Users/marco/IdeaProjects/TertiaryStructuresComparator/src/main/resources\TertiaryStructureBondsOptionalSequence.g4 by ANTLR 4.10.1

	package it.unicam.cs.bdslab.stalign;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TertiaryStructureBondsOptionalSequenceParser}.
 */
public interface TertiaryStructureBondsOptionalSequenceListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code AasFormat}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#strucure}.
	 * @param ctx the parse tree
	 */
	void enterAasFormat(TertiaryStructureBondsOptionalSequenceParser.AasFormatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AasFormat}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#strucure}.
	 * @param ctx the parse tree
	 */
	void exitAasFormat(TertiaryStructureBondsOptionalSequenceParser.AasFormatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sequenceContinue}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#sequence}.
	 * @param ctx the parse tree
	 */
	void enterSequenceContinue(TertiaryStructureBondsOptionalSequenceParser.SequenceContinueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sequenceContinue}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#sequence}.
	 * @param ctx the parse tree
	 */
	void exitSequenceContinue(TertiaryStructureBondsOptionalSequenceParser.SequenceContinueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sequenceEnd}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#sequence}.
	 * @param ctx the parse tree
	 */
	void enterSequenceEnd(TertiaryStructureBondsOptionalSequenceParser.SequenceEndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sequenceEnd}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#sequence}.
	 * @param ctx the parse tree
	 */
	void exitSequenceEnd(TertiaryStructureBondsOptionalSequenceParser.SequenceEndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bondsContinue}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#bonds}.
	 * @param ctx the parse tree
	 */
	void enterBondsContinue(TertiaryStructureBondsOptionalSequenceParser.BondsContinueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bondsContinue}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#bonds}.
	 * @param ctx the parse tree
	 */
	void exitBondsContinue(TertiaryStructureBondsOptionalSequenceParser.BondsContinueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bondsEnd}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#bonds}.
	 * @param ctx the parse tree
	 */
	void enterBondsEnd(TertiaryStructureBondsOptionalSequenceParser.BondsEndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bondsEnd}
	 * labeled alternative in {@link TertiaryStructureBondsOptionalSequenceParser#bonds}.
	 * @param ctx the parse tree
	 */
	void exitBondsEnd(TertiaryStructureBondsOptionalSequenceParser.BondsEndContext ctx);
	/**
	 * Enter a parse tree produced by {@link TertiaryStructureBondsOptionalSequenceParser#bond}.
	 * @param ctx the parse tree
	 */
	void enterBond(TertiaryStructureBondsOptionalSequenceParser.BondContext ctx);
	/**
	 * Exit a parse tree produced by {@link TertiaryStructureBondsOptionalSequenceParser#bond}.
	 * @param ctx the parse tree
	 */
	void exitBond(TertiaryStructureBondsOptionalSequenceParser.BondContext ctx);
}