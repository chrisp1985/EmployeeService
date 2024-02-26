package com.chrisp1985.EmployeeApi.aspects;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CacheAspect {

    @Pointcut("@annotation(org.springframework.cache.annotation.CacheEvict)")
    public void onCacheEviction() {}

    @After("onCacheEviction()")
    public void logCacheEvictions() {
        System.out.println("Cache Eviction happened.");
    }
}
