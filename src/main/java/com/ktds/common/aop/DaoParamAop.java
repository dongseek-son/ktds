package com.ktds.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoParamAop {

	private Logger logger = LoggerFactory.getLogger(DaoParamAop.class);
	
	// around
	public Object getParam(ProceedingJoinPoint pjp) {
		
		String classAndMethod = pjp.getSignature().toShortString();
		
		// before;
		// parameter 가져오기
		Object[] paramArray = pjp.getArgs();
		for (Object param : paramArray) {
			logger.debug(classAndMethod + " = " + param.toString() + " : " + param.getClass().toString());
		}
		
		// 기존의 메소드 실행
		Object result = null;
		try {
//			logger.debug("Before");
			result = pjp.proceed();
			logger.debug(classAndMethod + " = " + "Result : " + result.toString());
		} catch (Throwable e) {
			// after-throwing
			logger.debug(classAndMethod + " = " + "Exception : " + e.getCause().toString() + " , " + e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		}
//		finally {
//			// after
//			logger.debug("After");
//		}
		
//		// after-returning
//		logger.debug("After-returning");
		return result;
	}
	
}
