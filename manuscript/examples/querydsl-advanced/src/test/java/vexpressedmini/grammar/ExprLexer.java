// Generated from vexpressed\grammar\Expr.g4 by ANTLR 4.5.3
package vexpressedmini.grammar;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExprLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, OP_LT=7, OP_GT=8, OP_LE=9,
		OP_GE=10, OP_EQ=11, OP_NE=12, OP_AND=13, OP_OR=14, OP_NOT=15, OP_ADD=16,
		OP_SUB=17, OP_MUL=18, OP_DIV=19, OP_REMAINDER=20, OP_POW=21, K_NULL=22,
		BOOLEAN_LITERAL=23, ID=24, NUMERIC_LITERAL=25, DIGITS=26, STRING_LITERAL=27,
		SPACES=28, COMMENT=29, LINE_COMMENT=30, CUSTOM_OP=31, UNEXPECTED_CHAR=32;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "OP_LT", "OP_GT", "OP_LE",
		"OP_GE", "OP_EQ", "OP_NE", "OP_AND", "OP_OR", "OP_NOT", "OP_ADD", "OP_SUB",
		"OP_MUL", "OP_DIV", "OP_REMAINDER", "OP_POW", "K_NULL", "BOOLEAN_LITERAL",
		"ID", "NUMERIC_LITERAL", "DIGITS", "STRING_LITERAL", "SPACES", "COMMENT",
		"LINE_COMMENT", "CUSTOM_OP", "UNEXPECTED_CHAR", "A", "B", "C", "D", "E",
		"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
		"T", "U", "V", "W", "X", "Y", "Z"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "'['", "']'", "','", "':'", null, null, null, null,
		null, null, null, null, null, "'+'", "'-'", "'*'", "'/'", "'%'", "'^'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, "OP_LT", "OP_GT", "OP_LE", "OP_GE",
		"OP_EQ", "OP_NE", "OP_AND", "OP_OR", "OP_NOT", "OP_ADD", "OP_SUB", "OP_MUL",
		"OP_DIV", "OP_REMAINDER", "OP_POW", "K_NULL", "BOOLEAN_LITERAL", "ID",
		"NUMERIC_LITERAL", "DIGITS", "STRING_LITERAL", "SPACES", "COMMENT", "LINE_COMMENT",
		"CUSTOM_OP", "UNEXPECTED_CHAR"
	};
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


	public ExprLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Expr.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\"\u0173\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\3\2\3\2\3\3"+
		"\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\5\b\u0088\n\b\3\t"+
		"\3\t\3\t\3\t\5\t\u008e\n\t\3\n\3\n\3\n\3\n\3\n\5\n\u0095\n\n\3\13\3\13"+
		"\3\13\3\13\3\13\5\13\u009c\n\13\3\f\3\f\3\f\3\f\3\f\5\f\u00a3\n\f\5\f"+
		"\u00a5\n\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u00b2\n\r\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\5\16\u00ba\n\16\3\17\3\17\3\17\3\17\3\17"+
		"\5\17\u00c1\n\17\3\20\3\20\3\20\3\20\3\20\5\20\u00c8\n\20\3\21\3\21\3"+
		"\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\27\3\27\3"+
		"\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\5\30\u00e6"+
		"\n\30\3\31\3\31\7\31\u00ea\n\31\f\31\16\31\u00ed\13\31\3\32\3\32\3\32"+
		"\5\32\u00f2\n\32\5\32\u00f4\n\32\3\32\3\32\5\32\u00f8\n\32\3\32\3\32\5"+
		"\32\u00fc\n\32\3\32\3\32\3\32\3\32\5\32\u0102\n\32\3\32\3\32\5\32\u0106"+
		"\n\32\5\32\u0108\n\32\3\33\3\33\7\33\u010c\n\33\f\33\16\33\u010f\13\33"+
		"\3\34\3\34\3\34\3\34\7\34\u0115\n\34\f\34\16\34\u0118\13\34\3\34\3\34"+
		"\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\7\36\u0124\n\36\f\36\16\36\u0127"+
		"\13\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\7\37\u0132\n\37\f"+
		"\37\16\37\u0135\13\37\3\37\3\37\3 \6 \u013a\n \r \16 \u013b\3!\3!\3\""+
		"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3-"+
		"\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3\64\3\65"+
		"\3\65\3\66\3\66\3\67\3\67\38\38\39\39\3:\3:\3;\3;\3\u0125\2<\3\3\5\4\7"+
		"\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C"+
		"\2E\2G\2I\2K\2M\2O\2Q\2S\2U\2W\2Y\2[\2]\2_\2a\2c\2e\2g\2i\2k\2m\2o\2q"+
		"\2s\2u\2\3\2%\6\2&&C\\aac|\b\2&&\60\60\62;C\\aac|\4\2--//\3\2\62;\4\2"+
		"\62;aa\3\2))\5\2\13\r\17\17\"\"\4\2\f\f\17\17\n\2##%(,-/\61<B`a~~\u0080"+
		"\u0080\4\2CCcc\4\2DDdd\4\2EEee\4\2FFff\4\2GGgg\4\2HHhh\4\2IIii\4\2JJj"+
		"j\4\2KKkk\4\2LLll\4\2MMmm\4\2NNnn\4\2OOoo\4\2PPpp\4\2QQqq\4\2RRrr\4\2"+
		"SSss\4\2TTtt\4\2UUuu\4\2VVvv\4\2WWww\4\2XXxx\4\2YYyy\4\2ZZzz\4\2[[{{\4"+
		"\2\\\\||\u0173\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3"+
		"\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2"+
		"\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3"+
		"\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2"+
		"\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\2"+
		"9\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\3w\3\2\2\2\5y\3"+
		"\2\2\2\7{\3\2\2\2\t}\3\2\2\2\13\177\3\2\2\2\r\u0081\3\2\2\2\17\u0087\3"+
		"\2\2\2\21\u008d\3\2\2\2\23\u0094\3\2\2\2\25\u009b\3\2\2\2\27\u00a4\3\2"+
		"\2\2\31\u00b1\3\2\2\2\33\u00b9\3\2\2\2\35\u00c0\3\2\2\2\37\u00c7\3\2\2"+
		"\2!\u00c9\3\2\2\2#\u00cb\3\2\2\2%\u00cd\3\2\2\2\'\u00cf\3\2\2\2)\u00d1"+
		"\3\2\2\2+\u00d3\3\2\2\2-\u00d5\3\2\2\2/\u00e5\3\2\2\2\61\u00e7\3\2\2\2"+
		"\63\u0107\3\2\2\2\65\u0109\3\2\2\2\67\u0110\3\2\2\29\u011b\3\2\2\2;\u011f"+
		"\3\2\2\2=\u012d\3\2\2\2?\u0139\3\2\2\2A\u013d\3\2\2\2C\u013f\3\2\2\2E"+
		"\u0141\3\2\2\2G\u0143\3\2\2\2I\u0145\3\2\2\2K\u0147\3\2\2\2M\u0149\3\2"+
		"\2\2O\u014b\3\2\2\2Q\u014d\3\2\2\2S\u014f\3\2\2\2U\u0151\3\2\2\2W\u0153"+
		"\3\2\2\2Y\u0155\3\2\2\2[\u0157\3\2\2\2]\u0159\3\2\2\2_\u015b\3\2\2\2a"+
		"\u015d\3\2\2\2c\u015f\3\2\2\2e\u0161\3\2\2\2g\u0163\3\2\2\2i\u0165\3\2"+
		"\2\2k\u0167\3\2\2\2m\u0169\3\2\2\2o\u016b\3\2\2\2q\u016d\3\2\2\2s\u016f"+
		"\3\2\2\2u\u0171\3\2\2\2wx\7*\2\2x\4\3\2\2\2yz\7+\2\2z\6\3\2\2\2{|\7]\2"+
		"\2|\b\3\2\2\2}~\7_\2\2~\n\3\2\2\2\177\u0080\7.\2\2\u0080\f\3\2\2\2\u0081"+
		"\u0082\7<\2\2\u0082\16\3\2\2\2\u0083\u0084\5Y-\2\u0084\u0085\5i\65\2\u0085"+
		"\u0088\3\2\2\2\u0086\u0088\7>\2\2\u0087\u0083\3\2\2\2\u0087\u0086\3\2"+
		"\2\2\u0088\20\3\2\2\2\u0089\u008a\5O(\2\u008a\u008b\5i\65\2\u008b\u008e"+
		"\3\2\2\2\u008c\u008e\7@\2\2\u008d\u0089\3\2\2\2\u008d\u008c\3\2\2\2\u008e"+
		"\22\3\2\2\2\u008f\u0090\5Y-\2\u0090\u0091\5K&\2\u0091\u0095\3\2\2\2\u0092"+
		"\u0093\7>\2\2\u0093\u0095\7?\2\2\u0094\u008f\3\2\2\2\u0094\u0092\3\2\2"+
		"\2\u0095\24\3\2\2\2\u0096\u0097\5O(\2\u0097\u0098\5K&\2\u0098\u009c\3"+
		"\2\2\2\u0099\u009a\7@\2\2\u009a\u009c\7?\2\2\u009b\u0096\3\2\2\2\u009b"+
		"\u0099\3\2\2\2\u009c\26\3\2\2\2\u009d\u009e\5K&\2\u009e\u009f\5c\62\2"+
		"\u009f\u00a5\3\2\2\2\u00a0\u00a2\7?\2\2\u00a1\u00a3\7?\2\2\u00a2\u00a1"+
		"\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a5\3\2\2\2\u00a4\u009d\3\2\2\2\u00a4"+
		"\u00a0\3\2\2\2\u00a5\30\3\2\2\2\u00a6\u00a7\5]/\2\u00a7\u00a8\5K&\2\u00a8"+
		"\u00b2\3\2\2\2\u00a9\u00aa\5]/\2\u00aa\u00ab\5K&\2\u00ab\u00ac\5c\62\2"+
		"\u00ac\u00b2\3\2\2\2\u00ad\u00ae\7#\2\2\u00ae\u00b2\7?\2\2\u00af\u00b0"+
		"\7>\2\2\u00b0\u00b2\7@\2\2\u00b1\u00a6\3\2\2\2\u00b1\u00a9\3\2\2\2\u00b1"+
		"\u00ad\3\2\2\2\u00b1\u00af\3\2\2\2\u00b2\32\3\2\2\2\u00b3\u00b4\5C\"\2"+
		"\u00b4\u00b5\5]/\2\u00b5\u00b6\5I%\2\u00b6\u00ba\3\2\2\2\u00b7\u00b8\7"+
		"(\2\2\u00b8\u00ba\7(\2\2\u00b9\u00b3\3\2\2\2\u00b9\u00b7\3\2\2\2\u00ba"+
		"\34\3\2\2\2\u00bb\u00bc\5_\60\2\u00bc\u00bd\5e\63\2\u00bd\u00c1\3\2\2"+
		"\2\u00be\u00bf\7~\2\2\u00bf\u00c1\7~\2\2\u00c0\u00bb\3\2\2\2\u00c0\u00be"+
		"\3\2\2\2\u00c1\36\3\2\2\2\u00c2\u00c3\5]/\2\u00c3\u00c4\5_\60\2\u00c4"+
		"\u00c5\5i\65\2\u00c5\u00c8\3\2\2\2\u00c6\u00c8\7#\2\2\u00c7\u00c2\3\2"+
		"\2\2\u00c7\u00c6\3\2\2\2\u00c8 \3\2\2\2\u00c9\u00ca\7-\2\2\u00ca\"\3\2"+
		"\2\2\u00cb\u00cc\7/\2\2\u00cc$\3\2\2\2\u00cd\u00ce\7,\2\2\u00ce&\3\2\2"+
		"\2\u00cf\u00d0\7\61\2\2\u00d0(\3\2\2\2\u00d1\u00d2\7\'\2\2\u00d2*\3\2"+
		"\2\2\u00d3\u00d4\7`\2\2\u00d4,\3\2\2\2\u00d5\u00d6\5]/\2\u00d6\u00d7\5"+
		"k\66\2\u00d7\u00d8\5Y-\2\u00d8\u00d9\5Y-\2\u00d9.\3\2\2\2\u00da\u00db"+
		"\5i\65\2\u00db\u00dc\5e\63\2\u00dc\u00dd\5k\66\2\u00dd\u00de\5K&\2\u00de"+
		"\u00e6\3\2\2\2\u00df\u00e0\5M\'\2\u00e0\u00e1\5C\"\2\u00e1\u00e2\5Y-\2"+
		"\u00e2\u00e3\5g\64\2\u00e3\u00e4\5K&\2\u00e4\u00e6\3\2\2\2\u00e5\u00da"+
		"\3\2\2\2\u00e5\u00df\3\2\2\2\u00e6\60\3\2\2\2\u00e7\u00eb\t\2\2\2\u00e8"+
		"\u00ea\t\3\2\2\u00e9\u00e8\3\2\2\2\u00ea\u00ed\3\2\2\2\u00eb\u00e9\3\2"+
		"\2\2\u00eb\u00ec\3\2\2\2\u00ec\62\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ee\u00f3"+
		"\5\65\33\2\u00ef\u00f1\7\60\2\2\u00f0\u00f2\5\65\33\2\u00f1\u00f0\3\2"+
		"\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f4\3\2\2\2\u00f3\u00ef\3\2\2\2\u00f3"+
		"\u00f4\3\2\2\2\u00f4\u00fb\3\2\2\2\u00f5\u00f7\5K&\2\u00f6\u00f8\t\4\2"+
		"\2\u00f7\u00f6\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u00fa"+
		"\5\65\33\2\u00fa\u00fc\3\2\2\2\u00fb\u00f5\3\2\2\2\u00fb\u00fc\3\2\2\2"+
		"\u00fc\u0108\3\2\2\2\u00fd\u00fe\7\60\2\2\u00fe\u0105\5\65\33\2\u00ff"+
		"\u0101\5K&\2\u0100\u0102\t\4\2\2\u0101\u0100\3\2\2\2\u0101\u0102\3\2\2"+
		"\2\u0102\u0103\3\2\2\2\u0103\u0104\5\65\33\2\u0104\u0106\3\2\2\2\u0105"+
		"\u00ff\3\2\2\2\u0105\u0106\3\2\2\2\u0106\u0108\3\2\2\2\u0107\u00ee\3\2"+
		"\2\2\u0107\u00fd\3\2\2\2\u0108\64\3\2\2\2\u0109\u010d\t\5\2\2\u010a\u010c"+
		"\t\6\2\2\u010b\u010a\3\2\2\2\u010c\u010f\3\2\2\2\u010d\u010b\3\2\2\2\u010d"+
		"\u010e\3\2\2\2\u010e\66\3\2\2\2\u010f\u010d\3\2\2\2\u0110\u0116\7)\2\2"+
		"\u0111\u0115\n\7\2\2\u0112\u0113\7)\2\2\u0113\u0115\7)\2\2\u0114\u0111"+
		"\3\2\2\2\u0114\u0112\3\2\2\2\u0115\u0118\3\2\2\2\u0116\u0114\3\2\2\2\u0116"+
		"\u0117\3\2\2\2\u0117\u0119\3\2\2\2\u0118\u0116\3\2\2\2\u0119\u011a\7)"+
		"\2\2\u011a8\3\2\2\2\u011b\u011c\t\b\2\2\u011c\u011d\3\2\2\2\u011d\u011e"+
		"\b\35\2\2\u011e:\3\2\2\2\u011f\u0120\7\61\2\2\u0120\u0121\7,\2\2\u0121"+
		"\u0125\3\2\2\2\u0122\u0124\13\2\2\2\u0123\u0122\3\2\2\2\u0124\u0127\3"+
		"\2\2\2\u0125\u0126\3\2\2\2\u0125\u0123\3\2\2\2\u0126\u0128\3\2\2\2\u0127"+
		"\u0125\3\2\2\2\u0128\u0129\7,\2\2\u0129\u012a\7\61\2\2\u012a\u012b\3\2"+
		"\2\2\u012b\u012c\b\36\3\2\u012c<\3\2\2\2\u012d\u012e\7\61\2\2\u012e\u012f"+
		"\7\61\2\2\u012f\u0133\3\2\2\2\u0130\u0132\n\t\2\2\u0131\u0130\3\2\2\2"+
		"\u0132\u0135\3\2\2\2\u0133\u0131\3\2\2\2\u0133\u0134\3\2\2\2\u0134\u0136"+
		"\3\2\2\2\u0135\u0133\3\2\2\2\u0136\u0137\b\37\3\2\u0137>\3\2\2\2\u0138"+
		"\u013a\t\n\2\2\u0139\u0138\3\2\2\2\u013a\u013b\3\2\2\2\u013b\u0139\3\2"+
		"\2\2\u013b\u013c\3\2\2\2\u013c@\3\2\2\2\u013d\u013e\13\2\2\2\u013eB\3"+
		"\2\2\2\u013f\u0140\t\13\2\2\u0140D\3\2\2\2\u0141\u0142\t\f\2\2\u0142F"+
		"\3\2\2\2\u0143\u0144\t\r\2\2\u0144H\3\2\2\2\u0145\u0146\t\16\2\2\u0146"+
		"J\3\2\2\2\u0147\u0148\t\17\2\2\u0148L\3\2\2\2\u0149\u014a\t\20\2\2\u014a"+
		"N\3\2\2\2\u014b\u014c\t\21\2\2\u014cP\3\2\2\2\u014d\u014e\t\22\2\2\u014e"+
		"R\3\2\2\2\u014f\u0150\t\23\2\2\u0150T\3\2\2\2\u0151\u0152\t\24\2\2\u0152"+
		"V\3\2\2\2\u0153\u0154\t\25\2\2\u0154X\3\2\2\2\u0155\u0156\t\26\2\2\u0156"+
		"Z\3\2\2\2\u0157\u0158\t\27\2\2\u0158\\\3\2\2\2\u0159\u015a\t\30\2\2\u015a"+
		"^\3\2\2\2\u015b\u015c\t\31\2\2\u015c`\3\2\2\2\u015d\u015e\t\32\2\2\u015e"+
		"b\3\2\2\2\u015f\u0160\t\33\2\2\u0160d\3\2\2\2\u0161\u0162\t\34\2\2\u0162"+
		"f\3\2\2\2\u0163\u0164\t\35\2\2\u0164h\3\2\2\2\u0165\u0166\t\36\2\2\u0166"+
		"j\3\2\2\2\u0167\u0168\t\37\2\2\u0168l\3\2\2\2\u0169\u016a\t \2\2\u016a"+
		"n\3\2\2\2\u016b\u016c\t!\2\2\u016cp\3\2\2\2\u016d\u016e\t\"\2\2\u016e"+
		"r\3\2\2\2\u016f\u0170\t#\2\2\u0170t\3\2\2\2\u0171\u0172\t$\2\2\u0172v"+
		"\3\2\2\2\34\2\u0087\u008d\u0094\u009b\u00a2\u00a4\u00b1\u00b9\u00c0\u00c7"+
		"\u00e5\u00eb\u00f1\u00f3\u00f7\u00fb\u0101\u0105\u0107\u010d\u0114\u0116"+
		"\u0125\u0133\u013b\4\2\3\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}