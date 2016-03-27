package com.hdbandit.traceable.aspect;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.hdbandit.traceable.Tracer;
import com.hdbandit.traceable.annotation.Traceable;

@Aspect
public class TraceableAspect {

    private static final String EMPTY = "";
    
    @Autowired
    private ApplicationContext applicationContext;

    @Around("anyPublicMethod() && @annotation(traceableAnnotation)")
    public Object process(ProceedingJoinPoint jointPoint, Traceable traceableAnnotation) throws Throwable {
        
        String dateFormat = traceableAnnotation.dateFormat();
        
        String joinPointDescription = jointPoint.toString();
        
        String description = traceableAnnotation.description() == EMPTY ? joinPointDescription : traceableAnnotation.description();
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        
        String beanNameToGet = traceableAnnotation.tracerQualifier() != EMPTY ? traceableAnnotation.tracerQualifier() : EMPTY;
        
        Tracer tracer = beanNameToGet == EMPTY ? applicationContext.getBean(Tracer.class) : (Tracer) applicationContext.getBean(beanNameToGet);
        
        tracer.trace(String.format("START trace: %s > %s", simpleDateFormat.format(new Date()), description));
        
        Object result = jointPoint.proceed();
        
        tracer.trace(String.format("END trace: %s > %s", simpleDateFormat.format(new Date()), description));
        
      return result;
    }

}
