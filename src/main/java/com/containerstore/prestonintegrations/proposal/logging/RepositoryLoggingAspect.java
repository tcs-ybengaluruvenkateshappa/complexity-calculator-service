package com.containerstore.prestonintegrations.proposal.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryLoggingAspect.class);

    @Before("execution(* com.containerstore.prestonintegrations.proposal.repository..*(..))")
    public void logAfterRepositoryCall(JoinPoint joinPoint) {
        logger.info("Fetching data from db: {}", joinPoint.getSignature().toShortString());
    }
}
