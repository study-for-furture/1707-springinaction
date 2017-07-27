package com.springinaction.knights;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by mitmo on 2017-07-28.
 */
public class KnightMain {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/knights.xml");

        Knight knight = context.getBean(Knight.class);
        knight.embarkOnQuest();

        context.close();
    }
}
