package com.hdbandit.traceable.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hdbandit.traceable.aspect.TraceableAspect;

@Configuration
public class TraceableConfig {

    @Bean
    public TraceableAspect traceableAspect() {
        return new TraceableAspect();
    }

}
