package com.github.budgerigar;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class TestMain2 {

    public static void main(String[] args) {
        SpelExpressionParser parser = new SpelExpressionParser();

        // 直接解析常量
        Expression expression = parser.parseExpression("'Hello Spring Expression!'.toUpperCase()");
        String result = expression.getValue(String.class);
        System.out.println(result); // 输出：HELLO SPRING EXPRESSION!

    }

}
