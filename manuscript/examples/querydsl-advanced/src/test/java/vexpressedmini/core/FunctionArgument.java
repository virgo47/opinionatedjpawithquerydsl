package vexpressedmini.core;

public class FunctionArgument {

	public final String parameterName;
	public final Object value;

	public FunctionArgument(String parameterName, Object value) {
		this.parameterName = parameterName;
		this.value = value;
	}

	public FunctionArgument(Object value) {
		this(null, value);
	}

	@Override
	public String toString() {
		return (parameterName != null ? parameterName + ':' : "") + value;
	}
}
