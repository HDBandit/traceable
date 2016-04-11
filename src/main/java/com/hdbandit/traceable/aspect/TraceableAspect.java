package com.hdbandit.traceable.aspect;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.hdbandit.traceable.Tracer;
import com.hdbandit.traceable.annotation.Traceable;

@Aspect
public class TraceableAspect {
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Pointcut(value="execution(public * *(..))")
    public void anyPublicMethod() { }

    @Around("anyPublicMethod() && @annotation(traceableAnnotation)")
    public Object process(ProceedingJoinPoint jointPoint, Traceable traceableAnnotation) throws Throwable {
        
        MethodSignature signature = (MethodSignature) jointPoint.getSignature();
        
        Method method = signature.getMethod();
        
        Traceable methodAnnotation = method.getAnnotation(Traceable.class);
        
        String joinPointDescription = jointPoint.toString();
               
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(methodAnnotation.dateFormat());
        
        Tracer tracer = (Tracer) applicationContext.getBean(methodAnnotation.tracerQualifier());
        
        String description = String.format("<Tracer: %s, Description: %s , JoinPoint: %s , Arguments: %s>", methodAnnotation.tracerQualifier(), methodAnnotation.description(), joinPointDescription, jointPoint.getArgs());
        
        tracer.trace(String.format("START trace: %s :: %s", simpleDateFormat.format(new Date()), description));
        
        Object result = jointPoint.proceed();
        
        tracer.trace(String.format("END trace: %s :: %s", simpleDateFormat.format(new Date()), description));
        
      return result;
    }

}
