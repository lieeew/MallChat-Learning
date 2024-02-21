package com.leikooo.mallchat.common.common.utils;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/2/21
 * @description
 */
public class SpElUtilsTest {

    public static void main(String[] args) {

        List<Integer> primes = new ArrayList<Integer>();
        primes.addAll(Arrays.asList(2,3,5,7,11,13,17));

        // 创建解析器
        ExpressionParser parser = new SpelExpressionParser();
        //构造上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("primes",primes);

        //解析表达式
        Expression exp =parser.parseExpression("#primes.?[#this>10]");
        // 求值
        List<Integer> primesGreaterThanTen = (List<Integer>)exp.getValue(context);
    }
}