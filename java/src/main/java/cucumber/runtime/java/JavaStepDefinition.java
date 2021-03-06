package cucumber.runtime.java;

import cucumber.runtime.JdkPatternArgumentMatcher;
import cucumber.runtime.MethodFormat;
import cucumber.runtime.ParameterType;
import cucumber.runtime.StepDefinition;
import cucumber.runtime.Utils;
import gherkin.I18n;
import gherkin.formatter.Argument;
import gherkin.formatter.model.Step;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

public class JavaStepDefinition implements StepDefinition {
    private final Method method;
    private final Pattern pattern;
    private final JdkPatternArgumentMatcher argumentMatcher;
    private final ObjectFactory objectFactory;

    public JavaStepDefinition(Method method, Pattern pattern, ObjectFactory objectFactory) {
        this.method = method;
        this.pattern = pattern;
        this.argumentMatcher = new JdkPatternArgumentMatcher(pattern);
        this.objectFactory = objectFactory;
    }

    public void execute(I18n i18n, Object[] args) throws Throwable {
        Utils.invoke(objectFactory.getInstance(method.getDeclaringClass()), method, args);
    }

    public List<Argument> matchedArguments(Step step) {
        return argumentMatcher.argumentsFrom(step.getName());
    }

    public String getLocation(boolean detail) {
        MethodFormat format = detail ? MethodFormat.FULL : MethodFormat.SHORT;
        return format.format(method);
    }

    public List<ParameterType> getParameterTypes() {
        return ParameterType.fromMethod(method);
    }

    public boolean isDefinedAt(StackTraceElement e) {
        return e.getClassName().equals(method.getDeclaringClass().getName()) && e.getMethodName().equals(method.getName());
    }

    @Override
    public String getPattern() {
        return pattern.pattern();
    }
}
