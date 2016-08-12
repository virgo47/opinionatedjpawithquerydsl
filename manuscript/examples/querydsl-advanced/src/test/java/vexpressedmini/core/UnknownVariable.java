package vexpressedmini.core;

public class UnknownVariable extends ExpressionException {

	public final String variableName;

	public UnknownVariable(String variableName) {
		super("Unknown variable " + variableName);
		this.variableName = variableName;
	}
}
