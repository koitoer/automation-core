package com.staples.sa.automation.commons.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;


/**
 * All the classes for page locator must have this interface
 * @author mauricio.mena
 * @since 28/06/2014
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
public @interface PageLocator {

}
