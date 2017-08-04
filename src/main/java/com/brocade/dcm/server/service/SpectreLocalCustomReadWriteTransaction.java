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

import com.brocade.dcm.server.common.CustomApplicationNonRunTimeException;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Transactional(
		isolation = Isolation.READ_COMMITTED,
		propagation = Propagation.REQUIRED, //This is default, just making it explicit
		readOnly = false, //This is the default, just making it explicit 
		timeout = 300, //5 minutes
		rollbackFor = CustomApplicationNonRunTimeException.class)  //Rollback for this application level custom exception aswell in addition to run time exceptions
public @interface SpectreLocalCustomReadWriteTransaction {

}
