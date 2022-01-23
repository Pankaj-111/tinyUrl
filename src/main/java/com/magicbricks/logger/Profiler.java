package com.magicbricks.logger;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Configuration
public class Profiler {

	@Value("${enable.profiling:N}")
	private String profiling;

	@Around("execution(* com.magicbricks.*.*.*(..))")
	public Object logExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
		if (profiling.equalsIgnoreCase("Y")) {
			log.info("EXecuting {}", joinPoint.getSignature());
		}
		final StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		final Object proceed = joinPoint.proceed();
		stopWatch.stop();
		if (profiling.equalsIgnoreCase("Y")) {
			log.info("{} has taken {} ms", joinPoint.getSignature(), stopWatch.getTotalTimeMillis());
		}
		return proceed;
	}

	@PostConstruct
	public void checkLogging() {
		log.info("********* Profiling enabled :{} *********", profiling);
	}
}
