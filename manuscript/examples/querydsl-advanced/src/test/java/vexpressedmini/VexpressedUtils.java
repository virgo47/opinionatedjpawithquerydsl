package vexpressedmini;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import vexpressedmini.core.ExpressionCalculatorVisitor;
import vexpressedmini.core.ExpressionException;
import vexpressedmini.core.FunctionExecutor;
import vexpressedmini.core.VariableResolver;
import vexpressedmini.grammar.ExprLexer;
import vexpressedmini.grammar.ExprParser;

public final class VexpressedUtils {

  private static final ANTLRErrorListener ERROR_LISTENER = new ExceptionThrowingErrorListener();

  /**
   * Evaluates the expression using provided {@link VariableResolver} and
   * {@link FunctionExecutor}. This always parses the expression so it is not
   * "production-ready" because it is better to cache the parse trees for the
   * same expression.
   */
  public static Object eval(String expression,
    VariableResolver variableResolver, FunctionExecutor functionExecutor)
  {
    ParseTree parseTree = createParseTree(expression);
    ParseTreeVisitor visitor = new ExpressionCalculatorVisitor(variableResolver)
      .withFunctionExecutor(functionExecutor);
    return visitor.visit(parseTree);
  }

  /**
   * Parses expression and returns the {@link ParseTree} for further usage. This is
   * slow operation compared to evaluation itself, so it is recommended to cache parse
   * trees for repeated evaluations of the same expression.
   */
  public static ParseTree createParseTree(String expression) {
    ANTLRInputStream input = new ANTLRInputStream(expression);
    ExprLexer lexer = new ExprLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    ExprParser parser = new ExprParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(ERROR_LISTENER);
    return parser.result();
  }

  private static class ExceptionThrowingErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
      int line, int col, String msg, RecognitionException e)
    {
      throw new ExpressionException(
        "Expression parse failed at " + line + ':' + col + " - " + msg +
          underlinedError(recognizer, (Token) offendingSymbol, line, col));
    }

    private String underlinedError(
      Recognizer recognizer, Token offendingToken, int line, int col)
    {
      StringBuilder sb = new StringBuilder();
      CommonTokenStream tokens =
        (CommonTokenStream) recognizer.getInputStream();
      String input = tokens.getTokenSource().getInputStream().toString();
      String[] lines = input.split("\n");
      String errorLine = lines[line - 1];
      sb.append('\n').append(errorLine);

      int start = offendingToken.getStartIndex();
      int stop = offendingToken.getStopIndex();
      if (start > stop) {
        return sb.toString();
      }

      sb.append('\n');
      for (int i = 0; i < col; i++) sb.append(' ');
      if (start >= 0 && stop >= 0) {
        for (int i = start; i <= stop; i++) sb.append('^');
      }
      return sb.toString();
    }
  }
}
