/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidNameException;

/**
 * Basic implementation of the execution context.
 * 
 * @author llatil
 */
public class ContextBase extends Context implements Cloneable {
    /** Map to store the parameters (parameter name -> value) */
    private Map<String, Object> valuesMap = new HashMap<String, Object>();

    /** Map to store the factories (parameter name -> value factory) */
    private Map<String, ContextValueFactory> factoriesMap =
            Collections.emptyMap();

    /** Parent context */
    private final Context parentCtx;

    /**
     * Constructs an empty context
     */
    public ContextBase() {
        this(null);
    }

    /**
     * Constructs an empty sub-context of the given parent context
     * 
     * @param pParentCtx
     *            Parent context. Values are searched in the parent context when
     *            not available in the local context.
     */
    public ContextBase(Context pParentCtx) {
        parentCtx = pParentCtx;
        if (parentCtx == null) {
            put(Context.GPM_GLOBAL, new HashMap<String, Object>());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.Context#get(java.lang.String)
     */
    public final Object get(String pName) {
        return get(pName, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.Context#get(java.lang.String,
     *      java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String pName, Class<T> pClass) {
        String lLowercasedName = pName.toLowerCase();
        Object lValue = valuesMap.get(lLowercasedName);

        if (null == lValue) {
            // If no object exists in the context, try to get a
            // factory used to create it.
            ContextValueFactory lFactory = factoriesMap.get(lLowercasedName);
            if (null != lFactory) {
                // If a factory exists, use it to create the actual object.
                lValue = lFactory.create(lLowercasedName, pClass);
                if (null != lValue) {
                    // Store the object in the values map (to avoid to re-create it later)
                    valuesMap.put(lLowercasedName, lValue);
                }
            }
            if (null == lValue && parentCtx != null) {
                lValue = parentCtx.get(pName, pClass);
            }
        }
        else if (pClass != null && !pClass.isInstance(lValue)) {
            return null;
        }
        return (T) lValue;
    }

    /**
     * Get the value of a parameter.
     * <p>
     * This method is different from {@link ContextBase#get(String)} as no
     * factory will be used to create the value if it is not available.
     * 
     * @param pName
     *            Parameter name
     * @return Actual value of the parameter
     */
    public final Object getValue(String pName) {
        return valuesMap.get(pName.toLowerCase());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.Context#put(java.lang.String,
     *      java.lang.Object)
     */
    @Override
    public Context put(String pParameterName, Object pValue) {
        String lLowerCasedName = pParameterName.toLowerCase();
        if (valuesMap.containsKey(lLowerCasedName)) {
            throw new InvalidNameException(pParameterName,
                    "Parameter ''{0}'' already defined in this context level.");
        }
        valuesMap.put(lLowerCasedName, pValue);
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.Context#set(java.lang.String,
     *      java.lang.Object)
     */
    @Override
    public void set(String pParameterName, Object pValue)
        throws InvalidNameException {
        String lLowerCasedName = pParameterName.toLowerCase();
        if (valuesMap.containsKey(lLowerCasedName)) {
            valuesMap.put(lLowerCasedName, pValue);
        }
        else if (parentCtx != null) {
            parentCtx.set(lLowerCasedName, pValue);
        }
        else {
            throw new InvalidNameException(pParameterName,
                    "Parameter ''{0}'' cannot be set (not defined in the context)");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.Context#contains(java.lang.String)
     */
    @Override
    public boolean contains(String pParameterName) {
        String lLowerCasedName = pParameterName.toLowerCase();
        if (valuesMap.containsKey(lLowerCasedName)
                || factoriesMap.containsKey(lLowerCasedName)) {
            return true;
        }
        if (parentCtx != null) {
            return parentCtx.contains(lLowerCasedName);
        }
        return false;
    }

    /**
     * Add a factory in the context.
     * <p>
     * Factories are used to create the actual value only when required.
     * 
     * @param pName
     *            Name of the object to be created in the context
     * @param pFactory
     *            Factory implementation used to create the actual object.
     * @return This context object, allowing to chain the 'add' calls on a same
     *         instance.
     */
    public Context addFactory(String pName, ContextValueFactory pFactory) {
        if (factoriesMap.isEmpty()) {
            // If the map is empty (EMPTY_MAP singleton), create an actual map now
            factoriesMap = new HashMap<String, ContextValueFactory>();
        }
        factoriesMap.put(pName.toLowerCase(), pFactory);
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        ContextBase lCopy;
        try {
            lCopy = (ContextBase) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new GDMException("Cannot clone context");
        }
        lCopy.valuesMap = new HashMap<String, Object>(valuesMap);

        if (factoriesMap != Collections.EMPTY_MAP) {
            lCopy.factoriesMap =
                    new HashMap<String, ContextValueFactory>(factoriesMap);
        }
        return lCopy;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.Context#getCopy()
     */
    public Context getCopy() {
        return (Context) clone();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.Context#getLocalNames()
     */
    @Override
    public Set<String> getLocalNames() {
        if (factoriesMap.isEmpty()) {
            return valuesMap.keySet();
        }
        Set<String> lNames = new HashSet<String>(valuesMap.keySet());
        lNames.addAll(factoriesMap.keySet());
        return lNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.Context#getParentContext()
     */
    @Override
    public Context getParentContext() {
        return parentCtx;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.Context#get(org.topcased.gpm.business.extensions.service.ContextParameter)
     */
    @Override
    public <T> T get(ContextParameter<T> pParameter) {
        return get(pParameter.getParameterName(),
                pParameter.getParameterClass());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.Context#put(org.topcased.gpm.business.extensions.service.ContextParameter,
     *      java.lang.Object)
     */
    @Override
    public <T> Context put(ContextParameter<T> pParameter, T pValue) {
        return put(pParameter.getParameterName(), pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.Context#set(org.topcased.gpm.business.extensions.service.ContextParameter,
     *      java.lang.Object)
     */
    @Override
    public <T> void set(ContextParameter<T> pParameter, T pValue) {
        set(pParameter.getParameterName(), pValue);
    }
}
