// Generated from C:/Users/marco/IdeaProjects/TertiaryStructuresComparator/src/main/resources\TertiaryStructureBondsOptionalSequence.g4 by ANTLR 4.10.1

	package it.unicam.cs.bdslab.tersaling;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TertiaryStructureBondsOptionalSequenceParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, INDEX=5, LETTERS=6, LINE_COMMENT=7, WS=8;
	public static final int
		RULE_strucure = 0, RULE_sequence = 1, RULE_bonds = 2, RULE_bond = 3;
	private static String[] makeRuleNames() {
		return new String[] {
			"strucure", "sequence", "bonds", "bond"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'('", "','", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, "INDEX", "LETTERS", "LINE_COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "TertiaryStructureBondsOptionalSequence.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TertiaryStructureBondsOptionalSequenceParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class StrucureContext extends ParserRuleContext {
		public StrucureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_strucure; }
	 
		public StrucureContext() { }
		public void copyFrom(StrucureContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AasFormatContext extends StrucureContext {
		public BondsContext bonds() {
			return getRuleContext(BondsContext.class,0);
		}
		public SequenceContext sequence() {
			return getRuleContext(SequenceContext.class,0);
		}
		public AasFormatContext(StrucureContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TertiaryStructureBondsOptionalSequenceListener ) ((TertiaryStructureBondsOptionalSequenceListener)listener).enterAasFormat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TertiaryStructureBondsOptionalSequenceListener ) ((TertiaryStructureBondsOptionalSequenceListener)listener).exitAasFormat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TertiaryStructureBondsOptionalSequenceVisitor ) return ((TertiaryStructureBondsOptionalSequenceVisitor<? extends T>)visitor).visitAasFormat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StrucureContext strucure() throws RecognitionException {
		StrucureContext _localctx = new StrucureContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_strucure);
		int _la;
		try {
			_localctx = new AasFormatContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(9);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LETTERS) {
				{
				setState(8);
				sequence();
				}
			}

			setState(11);
			bonds();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SequenceContext extends ParserRuleContext {
		public SequenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequence; }
	 
		public SequenceContext() { }
		public void copyFrom(SequenceContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SequenceContinueContext extends SequenceContext {
		public TerminalNode LETTERS() { return getToken(TertiaryStructureBondsOptionalSequenceParser.LETTERS, 0); }
		public SequenceContext sequence() {
			return getRuleContext(SequenceContext.class,0);
		}
		public SequenceContinueContext(SequenceContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TertiaryStructureBondsOptionalSequenceListener ) ((TertiaryStructureBondsOptionalSequenceListener)listener).enterSequenceContinue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TertiaryStructureBondsOptionalSequenceListener ) ((TertiaryStructureBondsOptionalSequenceListener)listener).exitSequenceContinue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TertiaryStructureBondsOptionalSequenceVisitor ) return ((TertiaryStructureBondsOptionalSequenceVisitor<? extends T>)visitor).visitSequenceContinue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SequenceEndContext extends SequenceContext {
		public TerminalNode LETTERS() { return getToken(TertiaryStructureBondsOptionalSequenceParser.LETTERS, 0); }
		public SequenceEndContext(SequenceContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TertiaryStructureBondsOptionalSequenceListener ) ((TertiaryStructureBondsOptionalSequenceListener)listener).enterSequenceEnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TertiaryStructureBondsOptionalSequenceListener ) ((TertiaryStructureBondsOptionalSequenceListener)listener).exitSequenceEnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TertiaryStructureBondsOptionalSequenceVisitor ) return ((TertiaryStructureBondsOptionalSequenceVisitor<? extends T>)visitor).visitSequenceEnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceContext sequence() throws RecognitionException {
		SequenceContext _localctx = new SequenceContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_sequence);
		try {
			setState(16);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new SequenceContinueContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(13);
				match(LETTERS);
				setState(14);
				sequence();
				}
				break;
			case 2:
				_localctx = new SequenceEndContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(15);
				match(LETTERS);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BondsContext extends ParserRuleContext {
		public BondsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bonds; }
	 
		public BondsContext() { }
		public void copyFrom(BondsContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BondsEndContext extends BondsContext {
		public BondContext bond() {
			return getRuleContext(BondContext.class,0);
		}
		public BondsEndContext(BondsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TertiaryStructureBondsOptionalSequenceListener ) ((TertiaryStructureBondsOptionalSequenceListener)listener).enterBondsEnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TertiaryStructureBondsOptionalSequenceListener ) ((TertiaryStructureBondsOptionalSequenceListener)listener).exitBondsEnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TertiaryStructureBondsOptionalSequenceVisitor ) return ((TertiaryStructureBondsOptionalSequenceVisitor<? extends T>)visitor).visitBondsEnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BondsContinueContext extends BondsContext {
		public BondContext bond() {
			return getRuleContext(BondContext.class,0);
		}
		public BondsContext bonds() {
			return getRuleContext(BondsContext.class,0);
		}
		public BondsContinueContext(BondsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TertiaryStructureBondsOptionalSequenceListener ) ((TertiaryStructureBondsOptionalSequenceListener)listener).enterBondsContinue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TertiaryStructureBondsOptionalSequenceListener ) ((TertiaryStructureBondsOptionalSequenceListener)listener).exitBondsContinue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TertiaryStructureBondsOptionalSequenceVisitor ) return ((TertiaryStructureBondsOptionalSequenceVisitor<? extends T>)visitor).visitBondsContinue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BondsContext bonds() throws RecognitionException {
		BondsContext _localctx = new BondsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_bonds);
		try {
			setState(23);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				_localctx = new BondsContinueContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(18);
				bond();
				setState(19);
				match(T__0);
				setState(20);
				bonds();
				}
				break;
			case 2:
				_localctx = new BondsEndContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(22);
				bond();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BondContext extends ParserRuleContext {
		public List<TerminalNode> INDEX() { return getTokens(TertiaryStructureBondsOptionalSequenceParser.INDEX); }
		public TerminalNode INDEX(int i) {
			return getToken(TertiaryStructureBondsOptionalSequenceParser.INDEX, i);
		}
		public BondContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TertiaryStructureBondsOptionalSequenceListener ) ((TertiaryStructureBondsOptionalSequenceListener)listener).enterBond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TertiaryStructureBondsOptionalSequenceListener ) ((TertiaryStructureBondsOptionalSequenceListener)listener).exitBond(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TertiaryStructureBondsOptionalSequenceVisitor ) return ((TertiaryStructureBondsOptionalSequenceVisitor<? extends T>)visitor).visitBond(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BondContext bond() throws RecognitionException {
		BondContext _localctx = new BondContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_bond);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			match(T__1);
			setState(26);
			match(INDEX);
			setState(27);
			match(T__2);
			setState(28);
			match(INDEX);
			setState(29);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\b \u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0001\u0000\u0003\u0000\n\b"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0003"+
		"\u0001\u0011\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0003\u0002\u0018\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0000\u0000\u0004\u0000\u0002"+
		"\u0004\u0006\u0000\u0000\u001e\u0000\t\u0001\u0000\u0000\u0000\u0002\u0010"+
		"\u0001\u0000\u0000\u0000\u0004\u0017\u0001\u0000\u0000\u0000\u0006\u0019"+
		"\u0001\u0000\u0000\u0000\b\n\u0003\u0002\u0001\u0000\t\b\u0001\u0000\u0000"+
		"\u0000\t\n\u0001\u0000\u0000\u0000\n\u000b\u0001\u0000\u0000\u0000\u000b"+
		"\f\u0003\u0004\u0002\u0000\f\u0001\u0001\u0000\u0000\u0000\r\u000e\u0005"+
		"\u0006\u0000\u0000\u000e\u0011\u0003\u0002\u0001\u0000\u000f\u0011\u0005"+
		"\u0006\u0000\u0000\u0010\r\u0001\u0000\u0000\u0000\u0010\u000f\u0001\u0000"+
		"\u0000\u0000\u0011\u0003\u0001\u0000\u0000\u0000\u0012\u0013\u0003\u0006"+
		"\u0003\u0000\u0013\u0014\u0005\u0001\u0000\u0000\u0014\u0015\u0003\u0004"+
		"\u0002\u0000\u0015\u0018\u0001\u0000\u0000\u0000\u0016\u0018\u0003\u0006"+
		"\u0003\u0000\u0017\u0012\u0001\u0000\u0000\u0000\u0017\u0016\u0001\u0000"+
		"\u0000\u0000\u0018\u0005\u0001\u0000\u0000\u0000\u0019\u001a\u0005\u0002"+
		"\u0000\u0000\u001a\u001b\u0005\u0005\u0000\u0000\u001b\u001c\u0005\u0003"+
		"\u0000\u0000\u001c\u001d\u0005\u0005\u0000\u0000\u001d\u001e\u0005\u0004"+
		"\u0000\u0000\u001e\u0007\u0001\u0000\u0000\u0000\u0003\t\u0010\u0017";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}