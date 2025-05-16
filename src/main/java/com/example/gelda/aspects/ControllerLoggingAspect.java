package com.example.gelda.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class ControllerLoggingAspect {

    // Log BEFORE method execution in controller package
    @Before("execution(* com.example.gelda.user.controller..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();

        // Mask password-like strings in arguments
        String args = Arrays.stream(joinPoint.getArgs())
                .map(arg -> {
                    if (arg instanceof String && ((String) arg).toLowerCase().contains("pass")) {
                        return "*";
                    }
                    return arg != null ? arg.toString() : "null";
                })
                .collect(Collectors.joining(", "));
        System.out.println("[CONTROLLER BEFORE] " + methodName + " | Args: [" + args + "]");
    }

    // Log AFTER returning with the response (especially ResponseEntity bodies)
    @AfterReturning(pointcut = "execution(* com.example.gelda.user.controller..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().toShortString();

        if (result instanceof ResponseEntity) {
            Object body = ((ResponseEntity<?>) result).getBody();
            System.out.println("[CONTROLLER AFTER RETURNING] " + methodName + " | Response Body: " + body);
        } else {
            System.out.println("[CONTROLLER AFTER RETURNING] " + methodName + " | Response: " + result);
        }
    }

    // Log exceptions thrown in controller methods
    @AfterThrowing(pointcut = "execution(* com.example.gelda.user.controller..*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        System.out.println("[CONTROLLER EXCEPTION] " + joinPoint.getSignature().toShortString() + " | Error: " + ex.getMessage());
    }
}
