package com.chap04.concert;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by mitmo on 2017-08-26.
 */
@Aspect
public class Audience {
    @Before("execution(** com.chap04.concert.Performance.perform(..))")
    public void silenceCellPhone() {
        System.out.println("Silencing cell phones");
    }

    @Before("execution(** com.chap04.concert.Performance.perform(..))")
    public void takeSeats() {
        System.out.println("Taking seats");
    }

    @AfterReturning("execution(** com.chap04.concert.Performance.perform(..))")
    public void applause() {
        System.out.println("CLAP CLAP CLAP!!!");
    }

    @AfterThrowing("execution(** com.chap04.concert.Performance.perform(..))")
    public void demandRefund() {
        System.out.println("Demanding a refund");
    }
}
