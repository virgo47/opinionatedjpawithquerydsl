package vexpressedmini.core;

import vexpressedmini.grammar.ExprBaseVisitor;
import vexpressedmini.grammar.ExprParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static vexpressedmini.grammar.ExprParser.*;

/** Evaluates the expression - resolver for variables is mandatory, for functions optional. */
public class ExpressionCalculatorVisitor extends ExprBaseVisitor {

	public static final int DEFAULT_MAX_SCALE = 15;
	public static final int DEFAULT_MAX_RESULT_SCALE = 6;

	private final VariableResolver variableResolver;
	private FunctionExecutor functionExecutor;

	private int maxScale = DEFAULT_MAX_SCALE;
	private int maxResultScale = DEFAULT_MAX_RESULT_SCALE;
	private int roundingMode = BigDecimal.ROUND_HALF_UP;

	public ExpressionCalculatorVisitor(VariableResolver variableResolver) {
		if (variableResolver == null) {
			throw new IllegalArgumentException("Variable resolver must be provided");
		}
		this.variableResolver = variableResolver;
	}

	public ExpressionCalculatorVisitor withFunctionExecutor(
		FunctionExecutor functionExecutor)
	{
		this.functionExecutor = functionExecutor;
		return this;
	}

	/** Maximum BigDecimal scale used during computations. */
	public ExpressionCalculatorVisitor maxScale(int maxScale) {
		this.maxScale = maxScale;
		return this;
	}

	/** Maximum BigDecimal scale for result. */
	public ExpressionCalculatorVisitor maxResultScale(int maxResultScale) {
		this.maxResultScale = maxResultScale;
		return this;
	}

	@Override
	public Object visitLogicNot(ExprParser.LogicNotContext ctx) {
		return !(boolean) visitNotNull(ctx.expr());
	}

	@Override
	public Boolean visitLogicOp(LogicOpContext ctx) {
		boolean left = (boolean) visitNotNull(ctx.left);

		switch (ctx.op.getType()) {
			case OP_AND:
				return left && booleanRightSide(ctx);
			case OP_OR:
				return left || booleanRightSide(ctx);
			default:
				throw new ExpressionException("Unknown operator " + ctx.op);
		}
	}

	private boolean booleanRightSide(LogicOpContext ctx) {
		return (boolean) visitNotNull(ctx.right);
	}

	@Override
	public Object visitArithmeticOp(ArithmeticOpContext ctx) {
		Object left = visitNotNull(ctx.left);
		if (left instanceof Temporal) {
			return temporalArithmetic(ctx, (Temporal) left);
		} else if (left instanceof String) {
			return stringArithmetic(ctx, (String) left);
		} else {
			return numberArithmetic(ctx, (Number) left);
		}
	}

	private static final Pattern TEMPORAL_AMOUNT_PATTERN = Pattern.compile("(\\d+)([ymwd])?");

	private Object temporalArithmetic(ArithmeticOpContext ctx, Temporal left) {
		Object right = visitNotNull(ctx.right);
		long amount;
		TemporalUnit unit;

		if (right instanceof Number) {
			amount = ((Number) right).longValue();
			unit = ChronoUnit.DAYS;
		} else {
			Matcher matcher = TEMPORAL_AMOUNT_PATTERN.matcher(((String) right).toLowerCase());
			if (matcher.matches()) {
				amount = Long.parseLong(matcher.group(1));
				unit = parseUnit(matcher.group(2));
			} else {
				throw new ExpressionException("Cannot parse temporal amount: " + right);
			}
		}

		switch (ctx.op.getType()) {
			case OP_ADD:
				return left.plus(amount, unit);
			case OP_SUB:
				return left.minus(amount, unit);
			default:
				throw new ExpressionException(
					"Unknown operator " + ctx.op + ", or not supported for temporal types");
		}
	}

	private Object stringArithmetic(ArithmeticOpContext ctx, String left) {
		Object right = visitNotNull(ctx.right);

		switch (ctx.op.getType()) {
			case OP_ADD:
				return left.concat(right.toString());
			default:
				throw new ExpressionException(
					"Unknown operator " + ctx.op + ", or not supported for temporal types");
		}
	}

	private TemporalUnit parseUnit(String unitGroup) {
		if (unitGroup != null && unitGroup.length() > 0) {
			switch (unitGroup.charAt(0)) {
				case 'y':
					return ChronoUnit.YEARS;
				case 'm':
					return ChronoUnit.MONTHS;
				case 'w':
					return ChronoUnit.WEEKS;
			}
		}
		return ChronoUnit.DAYS;
	}

	private Object numberArithmetic(ArithmeticOpContext ctx, Number left) {
		Number right = (Number) visitNotNull(ctx.right);
		if (left instanceof BigDecimal && right instanceof BigDecimal) {
			return bigDecimalArithmetic(ctx, (BigDecimal) left, (BigDecimal) right);
		} else if (left instanceof BigDecimal) {
			return bigDecimalArithmetic(ctx, (BigDecimal) left, new BigDecimal(right.toString()));
		} else if (right instanceof BigDecimal) {
			return bigDecimalArithmetic(ctx, new BigDecimal(left.toString()), (BigDecimal) right);
		}
		return integerArithmetic(ctx, left.intValue(), right.intValue());
	}

	private Number bigDecimalArithmetic(
    ArithmeticOpContext ctx, BigDecimal left, BigDecimal right)
	{
		switch (ctx.op.getType()) {
			case OP_ADD:
				return left.add(right);
			case OP_SUB:
				return left.subtract(right);
			case OP_MUL:
				return left.multiply(right);
			case OP_DIV:
				return left.divide(right, maxScale, roundingMode).stripTrailingZeros();
			case OP_REMAINDER:
				return left.remainder(right);
			case OP_POW:
				return right.scale() > 0
					? BigDecimal.valueOf(Math.pow(left.doubleValue(), right.doubleValue()))
					: left.pow(right.intValue());
			default:
				throw new ExpressionException("Unknown operator " + ctx.op);
		}
	}

	/** Left side is made long to prevent overflows. */
	private Number integerArithmetic(ArithmeticOpContext ctx, long left, int right) {
		switch (ctx.op.getType()) {
			case OP_ADD:
				return narrowIntegerResult(left + right);
			case OP_SUB:
				return narrowIntegerResult(left - right);
			case OP_MUL:
				return narrowIntegerResult(left * right);
			case OP_DIV:
				return narrowIntegerResult(left / right);
			case OP_REMAINDER:
				return narrowIntegerResult(left % right);
			case OP_POW:
				return narrowIntegerResult(BigInteger.valueOf(left).pow(right));
			default:
				throw new ExpressionException("Unknown operator " + ctx.op);
		}
	}

	private Number narrowIntegerResult(Number result) {
		return (Number) narrowDownNumberTypes(result);
	}

	@Override
	public Boolean visitComparisonOp(ComparisonOpContext ctx) {
		Comparable left = (Comparable) visit(ctx.left);
		Comparable right = (Comparable) visit(ctx.right);
		int operator = ctx.op.getType();
		if (left == null || right == null) {
			// TODO do we want to throw when operator is not EQ/NE?
			return left == null && right == null && operator == OP_EQ
				|| (left != null || right != null) && operator == OP_NE;
		}
		// if one side is integer and the other BigDecimal, we want to unify it to BigDecimal
		if (left instanceof BigDecimal && right instanceof Integer) {
			right = new BigDecimal(right.toString());
		}
		if (right instanceof BigDecimal && left instanceof Integer) {
			left = new BigDecimal(left.toString());
		}

		//noinspection unchecked
		int comp = left.compareTo(right);
		switch (operator) {
			case OP_EQ:
				return comp == 0;
			case OP_NE:
				return comp != 0;
			case OP_GT:
				return comp > 0;
			case OP_LT:
				return comp < 0;
			case OP_GE:
				return comp >= 0;
			case OP_LE:
				return comp <= 0;
			default:
				throw new ExpressionException("Unknown operator " + ctx.op);
		}
	}

	@Override
	public Object visitVariable(VariableContext ctx) {
		Object value = variableResolver.resolve(ctx.ID().getText());
		return narrowDownNumberTypes(value);
	}

	@Override
	public String visitStringLiteral(StringLiteralContext ctx) {
		String text = ctx.STRING_LITERAL().getText();
		text = text.substring(1, text.length() - 1)
			.replaceAll("''", "'");
		return text;
	}

	@Override
	public Boolean visitBooleanLiteral(BooleanLiteralContext ctx) {
		return ctx.BOOLEAN_LITERAL().getText().toLowerCase().charAt(0) == 't';
	}

	@Override
	public Number visitNumericLiteral(NumericLiteralContext ctx) {
		String text = ctx.NUMERIC_LITERAL().getText();
		return stringToNumber(text.replaceAll("_", ""));
	}

	private Number stringToNumber(String text) {
		try {
			if (text.indexOf('.') == -1) {
				return new Integer(text);
			}
		} catch (NumberFormatException e) {
			// ignored, we will just try BigDecimal
		}
		BigDecimal bigDecimal = new BigDecimal(text);

		return bigDecimal.scale() < 0
			? bigDecimal.setScale(0, roundingMode)
			: bigDecimal;
	}

	@Override
	public Object visitNullLiteral(NullLiteralContext ctx) {
		return null;
	}

	@Override
	public Number visitUnarySign(ExprParser.UnarySignContext ctx) {
		Object expr = visitNotNull(ctx.expr());
		if (!(expr instanceof Number)) {
			throw new ExpressionException(
				"Unary sign can be applied only to numbers, not to: " + expr);
		}

		Number result = (Number) expr;
		boolean unaryMinus = ctx.op.getText().equals("-");
		return unaryMinus
			? (result instanceof BigDecimal ? ((BigDecimal) result).negate() : -result.intValue())
			: result;
	}

	@Override
	public Object visitParens(ParensContext ctx) {
		return visit(ctx.expr());
	}

	private final List<FunctionArgument> EMPTY_PARAMS = Collections.emptyList();

	@Override
	public Object visitFunction(ExprParser.FunctionContext ctx) {
		String functionName = ctx.ID().getText();
		List<FunctionArgument> params = ctx.paramlist() != null
			? ctx.paramlist().funarg().stream()
			.map(this::visitFunarg)
			.collect(Collectors.toList())
			: EMPTY_PARAMS;

		return executeFunction(functionName, params);
	}

	@Override
	public FunctionArgument visitFunarg(ExprParser.FunargContext ctx) {
		String argName = ctx.ID() != null ? ctx.ID().getText() : null;
		return new FunctionArgument(argName, visit(ctx.expr()));
	}

	@Override
	public Object visitInfixFunction(ExprParser.InfixFunctionContext ctx) {
		String functionName = ctx.ID().getText();

		return executeFunction(functionName, Arrays.asList(
			new FunctionArgument(visit(ctx.left)),
			new FunctionArgument(visit(ctx.right))));
	}

	@Override
	public Object visitCustomOp(ExprParser.CustomOpContext ctx) {
		String optext = ctx.op.getText();
		System.out.println("\nOPTEXT = " + optext);
		return super.visitCustomOp(ctx);
	}

	private Object executeFunction(String functionName, List<FunctionArgument> params) {
		if (functionExecutor == null) {
			throw new FunctionExecutionFailed("Cannot execute function " +
				functionName + " because no function executor was set.");
		}
		Object result = functionExecutor.execute(functionName, params);
		return narrowDownNumberTypes(result);
	}

	private Object narrowDownNumberTypes(Object value) {
		// directly supported types and null
		if (value == null
			|| value instanceof Integer || value instanceof BigDecimal
			|| value instanceof String || value instanceof Boolean)
		{
			return value;
		}

		if (value instanceof Number) {
			return stringToNumber(value.toString());
		}

		return value;
	}

	@Override
	public Object visitListConstructor(ExprParser.ListConstructorContext ctx) {
		return ctx.listExpr() != null
			? ctx.listExpr().expr().stream()
			.map(this::visit)
			.collect(toCollection(ArrayList::new))
			: Collections.emptyList();
	}

	@Override
	public Object visitResult(ExprParser.ResultContext ctx) {
		Object result = visit(ctx.expr());
		if (result instanceof BigDecimal) {
			BigDecimal bdResult = (BigDecimal) result;
			if (bdResult.scale() > maxResultScale) {
				result = bdResult.setScale(maxResultScale, roundingMode);
			}
		}
		return result;
	}

	private Object visitNotNull(ExprParser.ExprContext expr) {
		Object result = visit(expr);
		if (result == null) {
			throw new ExpressionException(
				"Null value not allowed here: " + expr.toStringTree());
		}

		return result;
	}
}
