package com.brocade.dcm.server.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Transactional(
		isolation = Isolation.READ_COMMITTED,
		propagation = Propagation.REQUIRED, //This is default though, just making it explicit
		readOnly = true,
		timeout = 300) //5 minutes
public @interface SpectreLocalReadOnlyTransaction {
	
}
