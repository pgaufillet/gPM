/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.topcased.gpm.business.exception.InvalidNameException;

/**
 * Context class used to pass an execution context to the commands object.
 * <p>
 * This context is used to pass parameters to the scripts and java actions
 * registered for an extension point. Each parameter has a name (unique in the
 * context), used to access it, and a value. Parameter names are not case
 * sensitive.
 * <p>
 * It is possible to define new parameters in the context, or set a new value to
 * an existing parameter.
 * 
 * @author llatil
 */
public abstract class Context {
    /**
     * Name of the attribute always present on the root context. This attribute
     * is used to save attributes global to the same action. The type of the
     * attribute is Map<Stirng, Object>
     */
    public static final String GPM_GLOBAL = "gpm.global";

    /**
     * Name of the attribute used to skip the extensions points, the object
     * associated to the attribute can be either:
     * <ul>
     * <li>A Boolean value. This value enable (FALSE) or disable (TRUE) all
     * extension points globally.
     * <li>A list of extension point names (as String values). All extension
     * point names present in the list are disabled.
     * </ul>
     */
    public static final String GPM_SKIP_EXT_PTS = "gpm.SkipExtensionPoints";

    /**
     * Name of the attribute used to skip the extensions point commands.
     * <p>
     * This attribute must contains a list of command names (as String values).
     * All commands present in the list are disabled.
     */
    public static final String GPM_SKIP_COMMANDS = "gpm.SkipCommands";

    public static final String GPM_FILTER_RESULT_DATE_FORMAT =
            "gpm.filterResultDateFormat";

    public static final String GPM_DISABLE_CHECKS = "gpm.DisableChecks";

    public static final String USER_PASSWORD_ENCODING = "passwordEncoding";
    
    /** Original user token */
    public static final String ORIGINAL_USER_TOKEN = "originalusertoken";

    /**
     * Get a parameter value
     * 
     * @param pName
     *            Name of the object in the context
     * @return Value of the object or null if not available
     */
    public abstract Object get(String pName);

    /**
     * Get a parameter value.
     * 
     * @param <T>
     *            Type parameter
     * @param pName
     *            Name of the object in the context. This name is case
     *            insensitive.
     * @param pClass
     *            Expected class of the parameter to retrieve
     * @return Value of the object or null if not available
     */
    public abstract <T> T get(String pName, Class<T> pClass);

    /**
     * Get a parameter value.
     * 
     * @param <T>
     *            The type of the parameter value
     * @param pParameter
     *            The description of the parameter
     * @return The parameter value
     */
    public abstract <T> T get(ContextParameter<T> pParameter);

    /**
     * Check if a parameter is available in the context.
     * 
     * @param pParameterName
     *            Name of the parameter
     * @return true if this parameter is defined in the context.
     * @see Context#get(String)
     * @see Context#get(String, Class)
     */
    public abstract boolean contains(String pParameterName);

    /**
     * Check if a collection of parameters are available in the context.
     * 
     * @param pNames
     *            Collection of parameter names
     * @return true if all parameter are available in context.
     */
    public boolean containsAll(Collection<String> pNames) {
        for (String lName : pNames) {
            if (!contains(lName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Put a new parameter in the context without any defined value.
     * <p>
     * The new parameter is actually defined in the current (toplevel) context
     * in the context hierarchy.
     * <p>
     * This method is used to define a parameter in the context, thus allowing
     * to set a value later.
     * 
     * @param pParameterName
     *            Name of the parameter to define in the context
     * @return This context object, allowing to chain calls on a same instance.
     * @throws InvalidNameException
     *             If the parameter already exists in the context.
     */
    public final Context put(String pParameterName) throws InvalidNameException {
        put(pParameterName.toLowerCase(), null);
        return this;
    }

    /**
     * Put a list of new parameters in the context.
     * <p>
     * Those new parameters are actually defined in the current (toplevel)
     * context in the context hierarchy.
     * 
     * @param pParameterNames
     *            List of parameter names to define in the context
     * @return This context object, allowing to chain calls on a same instance.
     * @throws InvalidNameException
     *             If one of the parameter already exists in the context.
     */
    public final Context put(String[] pParameterNames)
        throws InvalidNameException {
        for (String lParamName : pParameterNames) {
            put(lParamName);
        }
        return this;
    }

    /**
     * Put a new valued parameter in the context.
     * <p>
     * The new parameter is actually defined in the current (top-level) context
     * in the hierarchy.
     * 
     * @param pParameterName
     *            Name of the parameter to define in the context
     * @param pValue
     *            New value of the object
     * @return This context object, allowing to chain calls on a same instance
     * @throws InvalidNameException
     *             If the parameter already exist in the top-level context.
     */
    public abstract Context put(String pParameterName, Object pValue)
        throws InvalidNameException;

    /**
     * Put a new valued parameter in the context.
     * <p>
     * The new parameter is actually defined in the current (top-level) context
     * in the hierarchy.
     * 
     * @param <T>
     *            The type of the parameter value
     * @param pParameter
     *            The description of the parameter
     * @param pValue
     *            The parameter value
     * @return This context object, allowing to chain calls on a same instance
     */
    public abstract <T> Context put(ContextParameter<T> pParameter, T pValue);

    /**
     * Change the value of a parameter stored in context.
     * <p>
     * The parameter must already be defined in the context, as this method does
     * <b>not</b> define it in the context if not present.
     * 
     * @param pParameterName
     *            Name of the object in the context
     * @param pValue
     *            New value of the object
     * @throws InvalidNameException
     *             If the parameter does not exist in the context
     * @see Context#put(String, Object)
     */
    public abstract void set(String pParameterName, Object pValue)
        throws InvalidNameException;

    /**
     * Change the value of a parameter stored in context.
     * <p>
     * The parameter must already be defined in the context, as this method does
     * <b>not</b> define it in the context if not present.
     * 
     * @param <T>
     *            The type of the parameter value
     * @param pParameter
     *            The description of the parameter
     * @param pValue
     *            The parameter value
     */
    public abstract <T> void set(ContextParameter<T> pParameter, T pValue);

    /**
     * Get the names of all parameters stored in this context hierarchy.
     * 
     * @return Array containing all parameters sorted alphabetically
     */
    public String[] getValueNames() {
        SortedSet<String> lNamesSet = new TreeSet<String>();
        lNamesSet.addAll(getNames());
        return lNamesSet.toArray(new String[lNamesSet.size()]);
    }

    /**
     * Get the names of all parameters stored in this context level.
     * <p>
     * This method does not return the parameters available in parent contexts
     * 
     * @return Set of all parameter names for this context level only.
     */
    public abstract Set<String> getLocalNames();

    /**
     * Get the names of all parameters stored in the context hierarchy.
     * 
     * @return Set of all parameter names in the ctx hierarchy
     */
    public Set<String> getNames() {
        Context lCurrentLevel = this;
        Set<String> lNames = new HashSet<String>();

        while (lCurrentLevel != null) {
            lNames.addAll(lCurrentLevel.getLocalNames());
            lCurrentLevel = lCurrentLevel.getParentContext();
        }
        return lNames;
    }

    /**
     * Get the parent context.
     * 
     * @return Parent context.
     */
    public abstract Context getParentContext();

    /**
     * Get a copy of this context.
     * <p>
     * A context and its copy are independent. (a value can be added to the copy
     * without affecting the original context)
     * 
     * @return Copied context.
     */
    public abstract Context getCopy();

    /**
     * Get a new context instance.
     * 
     * @return A new context instance.
     */
    public static Context getContext() {
        return new ContextBase();
    }

    /**
     * Create a new sub-context.
     * 
     * @param pParentContext
     *            Parent context
     * @return New context inheriting from parent context.
     */
    public static Context createContext(Context pParentContext) {
        return new ContextBase(pParentContext);
    }

    /**
     * Get an empty context.
     * 
     * @return A new context instance.
     */
    public static Context getEmptyContext() {
        return new ContextBase();
    }
}
