package vexpressedmini.core;

import java.util.List;

public interface FunctionExecutor {

	/**
	 * Returns value for the function call, possibly with parameters.
	 * Some parameters can be named, but this is not guaranteed. Order and name interpretation
	 * is fully deferred to the implementation.
	 */
	Object execute(String functionName, List<FunctionArgument> params)
		throws FunctionExecutionFailed;
}
