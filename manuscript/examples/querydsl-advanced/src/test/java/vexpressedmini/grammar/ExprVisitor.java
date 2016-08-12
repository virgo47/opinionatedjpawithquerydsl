// Generated from vexpressed\grammar\Expr.g4 by ANTLR 4.5.3
package vexpressedmini.grammar;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExprParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExprVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ExprParser#result}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult(ExprParser.ResultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(ExprParser.ParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullLiteral}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(ExprParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listConstructor}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListConstructor(ExprParser.ListConstructorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code infixFunction}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfixFunction(ExprParser.InfixFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code comparisonOp}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOp(ExprParser.ComparisonOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unarySign}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnarySign(ExprParser.UnarySignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code customOp}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCustomOp(ExprParser.CustomOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringLiteral}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(ExprParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code function}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(ExprParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variable}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(ExprParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicOp}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicOp(ExprParser.LogicOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arithmeticOp}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticOp(ExprParser.ArithmeticOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleanLiteral}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(ExprParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numericLiteral}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericLiteral(ExprParser.NumericLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicNot}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicNot(ExprParser.LogicNotContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#paramlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamlist(ExprParser.ParamlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#funarg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunarg(ExprParser.FunargContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#listExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListExpr(ExprParser.ListExprContext ctx);
}