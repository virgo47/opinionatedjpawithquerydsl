package vexpressedmini.support;


import vexpressedmini.core.FunctionArgument;
import vexpressedmini.core.FunctionExecutionFailed;
import vexpressedmini.core.FunctionExecutor;
import vexpressedmini.core.VariableResolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class FunctionMapper {

  private final Map<String, MethodInfo> methodMap = new HashMap<>();

  /**
   * Finds all public methods annotated by {@link ExpressionFunction} and populates
   * method map from these. Can be called repeatedly. Can be also called with a class
   * object, in which case delegate object is null, but can be used for functions
   * implemented by static methods.
   */
  public FunctionMapper scanForFunctions(Object delegate) {
    Class delegateClass = delegate.getClass();
    if (delegate instanceof Class) {
      delegateClass = (Class) delegate;
      delegate = null;
    }
    return scanForFunctions(delegateClass, delegate);
  }

  private FunctionMapper scanForFunctions(Class delegateClass, Object delegate) {
    Method[] methods = delegateClass.getDeclaredMethods();
    for (Method method : methods) {
      ExpressionFunction annotation = method.getAnnotation(ExpressionFunction.class);
      if (annotation != null) {
        String functionName = annotation.value();
        if (functionName.isEmpty()) {
          functionName = method.getName();
        }

        methodMap.put(functionName, createMethodInfo(delegate, method));
      }
    }

    return this;
  }

  private MethodInfo createMethodInfo(Object delegate, Method method) {
    Parameter[] methodParameters = method.getParameters();
    ParameterInfo[] paramListInfo = new ParameterInfo[methodParameters.length];

    for (int i = 0; i < paramListInfo.length; i++) {
      Parameter methodParameter = methodParameters[i];
      FunctionParam paramAnnotation = methodParameter.getAnnotation(FunctionParam.class);
      String paramName = paramAnnotation != null && !paramAnnotation.name().isEmpty()
        ? paramAnnotation.name()
        : methodParameter.getName();
      Object defaultValue =
        paramAnnotation != null && !paramAnnotation.defaultValue().isEmpty()
          ? paramAnnotation.defaultValue()
          : null;
      paramListInfo[i] = new ParameterInfo(
        paramName, methodParameter.getType(), defaultValue);
    }

    return new MethodInfo(delegate, method,
      method.getReturnType(), paramListInfo);
  }

  /**
   * Explicitly adds method from delegate object to the function to method map. Can be also
   * called with a class object, in which case delegate object is null, but can be used for
   * functions implemented by static methods.
   */
  public FunctionMapper registerFunction(
    String functionName, Object delegate, String methodName, Class<?>... parameterTypes)
  {
    try {
      Class<?> delegateClass = delegate.getClass();
      if (delegate instanceof Class) {
        delegateClass = (Class) delegate;
        delegate = null;
      }
      Method method = delegateClass.getDeclaredMethod(methodName, parameterTypes);
      methodMap.put(functionName, createMethodInfo(delegate, method));
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(
        "Invalid method specified for function " + functionName, e);
    }
    return this;
  }

  private MethodInfo getMethodInfo(String functionName) {
    MethodInfo methodInfo = methodMap.get(functionName);
    if (methodInfo == null) {
      throw new FunctionExecutionFailed("Function '" + functionName + "' was not registered");
    }
    return methodInfo;
  }

  /**
   * Creates {@link FunctionExecutor} based on these function definitions (this object)
   * with {@link VariableResolver}. This is required way for calling functions that require
   * variable resolver parameter.
   */
  public FunctionExecutor executor(VariableResolver variableResolver) {
    return new Executor(variableResolver);
  }

  /** Creates {@link FunctionExecutor} based on these function definitions (this object). */
  public FunctionExecutor executor() {
    return new Executor(null);
  }

  private static class MethodInfo {

    private final Object object;
    private final Method method;
    private final ExpressionType returnExpressionType;
    // contains also special params, like variable resolver (which is not function parameter)
    private final ParameterInfo[] paramsInfo;

    private MethodInfo(Object object, Method method,
      Class returnType, ParameterInfo[] paramDefinitions)
    {
      this.object = object;
      this.method = method;
      returnExpressionType = ExpressionType.fromClass(returnType);
      this.paramsInfo = paramDefinitions;
    }
  }

  private static class ParameterInfo {

    public final String name;
    public final Class type;
    public final Object defaultValue;

    private ParameterInfo(String name, Class type, Object defaultValue) {
      this.name = name;
      this.type = type;
      this.defaultValue = defaultValue;
    }
  }

  private class Executor implements FunctionExecutor {

    private final VariableResolver variableResolver;

    private Executor(VariableResolver variableResolver) {
      this.variableResolver = variableResolver;
    }

    @Override
    public Object execute(String functionName, List<FunctionArgument> params) {
      MethodInfo methodInfo = getMethodInfo(functionName);

      Object[] args = prepareArguments(params, methodInfo, variableResolver);
      try {
        return methodInfo.method.invoke(methodInfo.object, args);
      } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
        String reason = e.getCause() != null
          ? e.getCause().getMessage()
          : e.getMessage();
        throw new FunctionExecutionFailed(reason, e);
      }
    }

    private Object[] prepareArguments(
      List<FunctionArgument> params, MethodInfo methodInfo, VariableResolver variableResolver)
    {
      List<FunctionArgument> unresolvedParams = new ArrayList<>(params);
      ParameterInfo[] paramsInfo = methodInfo.paramsInfo;
      Object[] args = new Object[paramsInfo.length];
      int paramIndex = 0;
      for (ParameterInfo parameterInfo : paramsInfo) {
        Object arg;
        if (parameterInfo.type == VariableResolver.class) {
          arg = variableResolver;
        } else {
          arg = getArgumentValue(unresolvedParams, parameterInfo);
        }
        args[paramIndex] = arg;
        paramIndex += 1;
      }
      return args;
    }

    private Object getArgumentValue(
      List<FunctionArgument> params, ParameterInfo parameterInfo)
    {
      Object arg = resolveParam(params, parameterInfo);
      arg = coerceArgument(parameterInfo, arg);
      return arg;
    }

    /** Finds named argument or uses the first unnamed - <b>mutates the params list</b>. */
    private Object resolveParam(List<FunctionArgument> params, ParameterInfo parameterInfo) {
      for (Iterator<FunctionArgument> iterator = params.iterator(); iterator.hasNext(); ) {
        FunctionArgument param = iterator.next();
        if (parameterInfo.name.equals(param.parameterName)) {
          iterator.remove();
          return param.value;
        }
      }

      // find first unnamed then
      for (Iterator<FunctionArgument> iterator = params.iterator(); iterator.hasNext(); ) {
        FunctionArgument param = iterator.next();
        if (param.parameterName == null) {
          iterator.remove();
          return param.value;
        }
      }
      return parameterInfo.defaultValue;
    }

    /** Converts between similar types, also manages conversion from default value string. */
    private Object coerceArgument(ParameterInfo parameterInfo, Object arg) {
      return ExpressionType.fromClass(parameterInfo.type).promote(arg);
    }
  }
}
