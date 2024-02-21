package com.leikooo.mallchat.common.common.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @description SpEl表达式解析工具类
 * @date 2024/2/21
 */
public class SpElUtils {
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    public static String parseSpEl(Method method, Object[] args, String spEl) {
        String[] params = Optional.ofNullable(PARAMETER_NAME_DISCOVERER.getParameterNames(method)).orElse(new String[]{});//解析参数名
        // el 解析需要的上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            // 所有参数都作为原材料扔进去
            context.setVariable(params[i], args[i]);
        }
        Expression expression = PARSER.parseExpression(spEl);
        return expression.getValue(context, String.class);
    }

    /**
     * 获取 prefixKey
     *
     * @param method
     * @return
     */
    public static String getMethodKey(Method method) {
        return method.getDeclaringClass() + "#" + method.getName();
    }
}