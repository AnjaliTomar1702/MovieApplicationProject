package com.niit.userauthentication.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LoggerAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Pointcut("execution (* com.niit.userauthentication.repository.*.*(..)) || "
    + "execution (* com.niit.userauthentication.controller.*.*(..)) || "
    + "execution (* com.niit.userauthentication.service.*.*(..))")
    public void allMethods(){}

    @Before(value = "allMethods()")
    public void beforeAdvice(JoinPoint joinPoint){
        logger.info("**********@Before**********");
        logger.debug("Method name : {}", joinPoint.getSignature().getName());
        logger.debug("Method arguments : {}", Arrays.toString(joinPoint.getArgs()));
        logger.info("**********@BeforeEnds**********");
    }

    @After("allMethods()")
    public  void afterAdvice(JoinPoint joinPoint){
        logger.info("**********@After**********");
        logger.debug("Method name : {}", joinPoint.getSignature().getName());
        logger.debug("Method argumentss : {}", Arrays.toString(joinPoint.getArgs()));
        logger.info("**********@AfterEnds**********");
    }

    @AfterReturning(value = "allMethods()", returning = "returnedValue")
    public void AfterReturning(JoinPoint joinPoint, Object returnedValue){
        logger.info("**********@AfterReturning**********");
        logger.debug("Method name : {}", joinPoint.getSignature().getName());
        logger.debug("Method arguments : {}", Arrays.toString(joinPoint.getArgs()));
        logger.debug("Result : {}", returnedValue);
        logger.info("**********@AfterReturningEnds**********");
    }

    @AfterThrowing(value = "allMethods()", throwing = "error")
    public void AfterThrowing(JoinPoint joinPoint, Throwable error){
        logger.info("**********@AfterThrowing**********");
        logger.debug("Method name : {}", joinPoint.getSignature().getName());
        logger.debug("Method arguments : {}", Arrays.toString(joinPoint.getArgs()));
        logger.debug("Exception : {}", error);
        logger.info("**********@AfterThrowingEnds**********");
    }

//Around is giving error for async methods

//    @Around("execution (* com.niit.userauthentication.controller.*.*(..))")
//    @Async
//    public void executingAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
//        logger.info("**********@Around**********");
//        logger.debug("Method name : {}", proceedingJoinPoint.getSignature().getName());
//        logger.debug("Method arguments : {}", Arrays.toString(proceedingJoinPoint.getArgs()));
//        proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
//        logger.info("**********@AfterAround**********");
//    }

}
