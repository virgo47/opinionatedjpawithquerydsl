package tests.exprdemo;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
import org.antlr.v4.runtime.tree.ParseTree;
import vexpressedmini.core.ExpressionException;
import vexpressedmini.core.*;
import vexpressedmini.grammar.ExprBaseVisitor;
import vexpressedmini.grammar.ExprParser;
import vexpressedmini.support.ExpressionFunction;
import vexpressedmini.support.FunctionMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static vexpressedmini.grammar.ExprParser.*;

/** @noinspection unchecked, ConstantConditions */
public class QueryExpressionVisitor extends ExprBaseVisitor {

  private final VariableResolver variableResolver;
  private FunctionExecutor functionExecutor = new FunctionMapper()
    .scanForFunctions(this)
    .executor();

  public QueryExpressionVisitor(VariableResolver variableResolver) {
    if (variableResolver == null) {
      throw new IllegalArgumentException("Variable resolver must be provided");
    }
    this.variableResolver = variableResolver;
  }

  public Predicate predicate(ParseTree tree) {
    return (Predicate) super.visit(tree);
  }

  @Override
  public Expression visitLogicNot(ExprParser.LogicNotContext ctx) {
    return ((Predicate) visitNotNull(ctx.expr())).not();
  }

  @Override
  public Predicate visitLogicOp(ExprParser.LogicOpContext ctx) {
    BooleanExpression left = (BooleanExpression) visitNotNull(ctx.left);
    BooleanExpression right = (BooleanExpression) visitNotNull(ctx.right);

    switch (ctx.op.getType()) {
      case OP_AND:
        return Expressions.booleanOperation(Ops.AND, left, right);
      case OP_OR:
        return Expressions.booleanOperation(Ops.OR, left, right);
      default:
        throw new ExpressionException("Unknown operator " + ctx.op);
    }
  }

  @Override
  public Expression visitArithmeticOp(ExprParser.ArithmeticOpContext ctx) {
    Expression left = visitNotNull(ctx.left);
    // TODO do we want support for temporal arithmetic? (see ExpressionCalculatorVisitor.temporalArithmetic())
//		if (left instanceof Temporal) {
//			return temporalArithmetic(ctx, left);
//		} else
    if (left instanceof StringExpression) {
      return stringArithmetic(ctx, (StringExpression) left);
    } else {
      return numberArithmetic(ctx, left);
    }
  }

  private Expression stringArithmetic(ExprParser.ArithmeticOpContext ctx, StringExpression left) {
    StringExpression right = (StringExpression) visitNotNull(ctx.right);

    switch (ctx.op.getType()) {
      case OP_ADD:
        return left.concat(right);
      default:
        throw new ExpressionException(
          "Unknown operator " + ctx.op + ", or not supported for temporal types");
    }
  }

  private NumberExpression numberArithmetic(ExprParser.ArithmeticOpContext ctx, Expression left) {
    Expression right = visitNotNull(ctx.right);
    switch (ctx.op.getType()) {
      case OP_ADD:
        return Expressions.numberOperation(BigDecimal.class, Ops.ADD, left, right);
      case OP_SUB:
        return Expressions.numberOperation(BigDecimal.class, Ops.SUB, left, right);
      case OP_MUL:
        return Expressions.numberOperation(BigDecimal.class, Ops.MULT, left, right);
      case OP_DIV:
        return Expressions.numberOperation(BigDecimal.class, Ops.DIV, left, right);
      case OP_REMAINDER:
        return Expressions.numberOperation(BigDecimal.class, Ops.MOD, left, right);
      default:
        throw new ExpressionException("Unknown operator " + ctx.op);
    }
  }

  @Override
  public BooleanExpression visitComparisonOp(ExprParser.ComparisonOpContext ctx) {
    Expression left = (Expression) visit(ctx.left);
    Expression right = (Expression) visit(ctx.right);
    if (left.getType().equals(LocalDate.class) && right.getType().equals(String.class)) {
      right = ConstantImpl.create(parseIsoDate(right.toString()));
    } else if (left.getType().equals(LocalDateTime.class)
      && right.getType().equals(String.class))
    {
      right = ConstantImpl.create(parseIsoDateTime(right.toString()));
    } else if (left.getType().equals(Instant.class) && right.getType().equals(String.class)) {
      right = ConstantImpl.create(parseIsoInstant(right.toString()));
    }
    int operator = ctx.op.getType();
    if (right == null) {
      if (operator == OP_EQ) {
        return Expressions.booleanOperation(Ops.IS_NULL, left);
      } else if (operator == OP_NE) {
        return Expressions.booleanOperation(Ops.IS_NOT_NULL, left);
      } else {
        throw new ExpressionException("NULL not supported for operator " + ctx.op);
      }
    }

    switch (operator) {
      case OP_EQ:
        return Expressions.booleanOperation(Ops.EQ, left, right);
      case OP_NE:
        return Expressions.booleanOperation(Ops.NE, left, right);
      case OP_GT:
        return Expressions.booleanOperation(Ops.GT, left, right);
      case OP_LT:
        return Expressions.booleanOperation(Ops.LT, left, right);
      case OP_GE:
        return Expressions.booleanOperation(Ops.GOE, left, right);
      case OP_LE:
        return Expressions.booleanOperation(Ops.LOE, left, right);
      default:
        throw new ExpressionException("Unknown operator " + ctx.op);
    }
  }

  @Override
  public Expression visitVariable(ExprParser.VariableContext ctx) {
    return (Expression) variableResolver.resolve(ctx.ID().getText());
  }

  @Override
  public Expression<String> visitStringLiteral(ExprParser.StringLiteralContext ctx) {
    String text = ctx.STRING_LITERAL().getText();
    text = text.substring(1, text.length() - 1)
      .replaceAll("''", "'");
    return ConstantImpl.create(String.class, text);
  }

  @Override
  public Expression<Boolean> visitBooleanLiteral(ExprParser.BooleanLiteralContext ctx) {
    boolean val = ctx.BOOLEAN_LITERAL().getText().toLowerCase().charAt(0) == 't';
    return ConstantImpl.create(val);
  }

  @Override
  public Constant visitNumericLiteral(ExprParser.NumericLiteralContext ctx) {
    String text = ctx.NUMERIC_LITERAL().getText();
    return ConstantImpl.create(new BigDecimal(text.replaceAll("_", "")));
  }

  @Override
  public Expression visitNullLiteral(ExprParser.NullLiteralContext ctx) {
    return null;
  }

  @Override
  public NumberExpression visitUnarySign(ExprParser.UnarySignContext ctx) {
    Expression expr = visitNotNull(ctx.expr());
    if (!(expr instanceof NumberExpression)) {
      throw new ExpressionException(
        "Unary sign can be applied only to numbers, not to: " + expr);
    }

    NumberExpression result = (NumberExpression) expr;
    boolean unaryMinus = "-".equals(ctx.op.getText());
    return unaryMinus
      ? result.negate()
      : result;
  }

  private final List<FunctionArgument> EMPTY_PARAMS = Collections.emptyList();

  @Override
  public Expression visitFunction(ExprParser.FunctionContext ctx) {
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
  public Expression visitInfixFunction(ExprParser.InfixFunctionContext ctx) {
    String functionName = ctx.ID().getText();

    return executeFunction(functionName, Arrays.asList(
      new FunctionArgument(visit(ctx.left)),
      new FunctionArgument(visit(ctx.right))));
  }

  /** Functions are always converted to uppercase, so define them all with uppercase names. */
  private Expression executeFunction(String functionName, List<FunctionArgument> params) {
    if (functionExecutor == null) {
      throw new FunctionExecutionFailed("Cannot execute function " +
        functionName + " because no function executor was set.");
    }
    return (Expression) functionExecutor.execute(functionName.toUpperCase(), params);
  }

  @Override
  public List visitListConstructor(ExprParser.ListConstructorContext ctx) {
    return ctx.listExpr() != null
      ? ctx.listExpr().expr().stream()
      .map(this::visit)
      .collect(toCollection(ArrayList::new))
      : Collections.emptyList();
  }

  @Override
  public Expression visitParens(ExprParser.ParensContext ctx) {
    return (Expression) visit(ctx.expr());
  }

  @Override
  public Expression visitResult(ExprParser.ResultContext ctx) {
    return (Expression) visit(ctx.expr());
  }

  private Expression visitNotNull(ExprParser.ExprContext expr) {
    Expression result = (Expression) visit(expr);
    if (result == null) {
      throw new ExpressionException(
        "Null value not allowed here: " + expr.toStringTree());
    }

    return result;
  }

  private static LocalDateTime parseIsoDateTime(String dateTime) {
    dateTime = dateTime.replace(' ', 'T');
    return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  private static LocalDate parseIsoDate(String date) {
    return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
  }

  /**
   * Parses ISO extended date time format, optionally with timezone,
   * and returns {@link Instant}.
   */
  private static Instant parseIsoInstant(String instant) {
    instant = fixInstantLooseFormat(instant);
    return Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(instant));
  }

  private static final int TIMEZONE_NOT_BEFORE_INDEX = 15;

  private static String fixInstantLooseFormat(String instant) {
    instant = instant.replace(' ', 'T'); // date and time separator must be T
    if (instant.indexOf('Z', TIMEZONE_NOT_BEFORE_INDEX) != -1
      || instant.indexOf('+', TIMEZONE_NOT_BEFORE_INDEX) != -1
      || instant.indexOf('-', TIMEZONE_NOT_BEFORE_INDEX) != -1)
    {
      return instant;
    }
    return instant + 'Z';
  }

  @ExpressionFunction("IN")
  public BooleanExpression in(SimpleExpression what, Collection collection) {
    return what.in(collection);
  }

  @ExpressionFunction("LIKE")
  public BooleanExpression like(StringExpression left, Expression right) {
    return Expressions.booleanOperation(Ops.LIKE, left, right);
  }
}
