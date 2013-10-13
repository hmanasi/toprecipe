package com.toprecipe.configs;

import javax.annotation.PostConstruct;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

@Configuration
@ComponentScan(basePackages = { "com.toprecipe.controllers",
		"com.toprecipe.services", "com.toprecipe.repository" })
@EnableAspectJAutoProxy
public class AppConfig {
	
	@Autowired
	private PlatformTransactionManager txManager;

	@PostConstruct
	public void initAspectTransaction ()
	{
		AnnotationTransactionAspect.aspectOf().setTransactionManager(txManager); 
	}
	
	@Bean
	public CustomizableTraceInterceptor interceptor() {

		CustomizableTraceInterceptor interceptor = new CustomizableTraceInterceptor();
		interceptor.setEnterMessage("Entering $[methodName]($[arguments]).");
		interceptor
				.setExitMessage("Leaving $[methodName](..) with return value $[returnValue], took $[invocationTime]ms.");

		return interceptor;
	}
	
	@Bean
	public Advisor traceAdvisor() {

		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(public * org.springframework.data.repository.Repository+.*(..))");

		return new DefaultPointcutAdvisor(pointcut, interceptor());
	}

	@Bean
	public HibernateExceptionTranslator exceptionTranslator() {
		return new HibernateExceptionTranslator();
	}
}