package vexpressedmini.support;

import vexpressedmini.core.UnknownVariable;
import vexpressedmini.core.VariableResolver;

import java.util.HashMap;
import java.util.Map;

/** Map-like variable binding with static values. */
public class VariableBinding implements VariableResolver {

	private final Map<String, Object> binding = new HashMap<>();

	public VariableBinding add(String variableName, Object value) {
		binding.put(variableName, value);
		return this;
	}

	public VariableBinding addAll(Map<String, Object> vars) {
		binding.putAll(vars);
		return this;
	}

	@Override
	public Object resolve(String variableName) throws UnknownVariable {
		Object value = binding.get(variableName);
		if (value == null && !binding.containsKey(variableName)) {
			throw new UnknownVariable(variableName);
		}

		return value;
	}
}
