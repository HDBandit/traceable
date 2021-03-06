package com.hdbandit.traceable.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Traceable {
    
    String description() default "Empty description";
    
    String dateFormat() default "dd.MM.yyy HH:mm:ss:SSSSSSS";
    
    String tracerQualifier() default "default tracer";

}
