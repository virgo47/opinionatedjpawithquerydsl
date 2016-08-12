package vexpressedmini.core;

public interface VariableResolver {

	VariableResolver NULL_VARIABLE_RESOLVER = var -> null;

	/** Returns value for the variable name. */
	Object resolve(String variableName) throws UnknownVariable;
}
