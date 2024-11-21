package org.broz.arrivalai.aspects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LoggerAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Around("execution(* org.broz.arrivalai.controllers.*.*(..))" )
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        HttpServletResponse response = attributes != null ? attributes.getResponse() : null;

        Object result = joinPoint.proceed();

        if (request != null && response != null) {
            logger.info("New Request: Method - {}, Status - {}, URI - {}, Response - {}",
                    joinPoint.getSignature().getName(),
                    response.getStatus(),
                    request.getRequestURI(),
                    result != null ? result.toString() : "null");
        }

        return result;
    }
}
