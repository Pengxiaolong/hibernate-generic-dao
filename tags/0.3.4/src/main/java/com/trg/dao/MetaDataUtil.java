package com.trg.dao;

import java.io.Serializable;

public interface MetaDataUtil {
	/**
	 * Get the type of a property of a bean class. The property can be simple
	 * ("name") or nested ("organization.name").
	 * 
	 * @throws PropertyNotFoundException
	 *             if the class does not have the given bean property.
	 */
	public Class<?> getExpectedClass(Class<?> rootClass, String propertyPath);
	
	/**
	 * Get the value of the ID property of an entity.
	 */
	public Serializable getId (Object object);
}
