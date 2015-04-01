/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.environment.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.dictionary.CategoryData;
import org.topcased.gpm.business.dictionary.EnvironmentData;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.serialization.data.CategoryValue;

/**
 * The Environment service
 * 
 * @author tszadel
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface EnvironmentService {

    /**
     * Get a list of category names defined for a process..
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pBusinessProcessName
     *            Business process name.
     * @return The list of category names.
     */
    public Collection<String> getCategoryNames(String pRoleToken,
            String pBusinessProcessName);

    /**
     * Get the list of categories.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pBusinessProcessName
     *            Business process name.
     * @return The list of Category.
     */
    public Collection<CategoryData> getCategories(String pRoleToken,
            String pBusinessProcessName);

    /**
     * Get the list of categories allowed to be modified by the user.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pBusinessProcessName
     *            Business process name.
     * @return The list of Category.
     */
    public Collection<CategoryData> getModifiableCategories(String pRoleToken,
            String pBusinessProcessName);

    /**
     * Get a list of environment names defined for a process.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pBusinessProcessName
     *            Business process name.
     * @return The list of environment names. This list contains all public
     *         environment names. If you have admin rights, this list contains
     *         too private environment names.
     */
    public Collection<String> getEnvironmentNames(String pRoleToken,
            String pBusinessProcessName);

    /**
     * Get the list of environment names allowed to be modified by the user.
     * <p>
     * The environment returned depends on the given role token
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pBusinessProcessName
     *            Business process name.
     * @return The list of environment names.
     */
    public Collection<String> getModifiableEnvironmentNames(String pRoleToken,
            String pBusinessProcessName);

    /**
     * Create a new environment
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcessName
     *            Name of the business process
     * @param pEnvironmentName
     *            Name of the new environment
     * @param pIsPublic
     *            Is this environment public
     */
    public void createEnvironment(String pRoleToken,
            String pBusinessProcessName, String pEnvironmentName,
            boolean pIsPublic);

    /**
     * Get the list of values of a category defined for an environment.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcessName
     *            Business process name.
     * @param pEnvironmentName
     *            Name of the environment.
     * @param pCategoryName
     *            Name of the category.
     * @return The list of environment data.
     */
    public EnvironmentData getEnvironmentCategory(String pRoleToken,
            String pBusinessProcessName, String pEnvironmentName,
            String pCategoryName);

    /**
     * Set the list of values of a category defined for an environment.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pBusinessProcessName
     *            Business process name.
     * @param pEnvironmentName
     *            Name of the environment.
     * @param pCategoryName
     *            Name of the category.
     * @param pCategoryValues
     *            Values to set.
     */
    public void setEnvironmentCategory(String pRoleToken,
            String pBusinessProcessName, String pEnvironmentName,
            String pCategoryName, List<String> pCategoryValues);

    /**
     * Define a new category for an environment (or update an existing one).
     * 
     * @param pRoleToken
     *            Role session token
     * @param pEnvironment
     *            Category to define in the environment, with its values list.
     */
    public void setEnvironmentCategory(String pRoleToken,
            EnvironmentData pEnvironment);

    /**
     * Define a new category in the dictionary (or update an existing one).
     * 
     * @param pRoleToken
     *            Role session token
     * @param pCategory
     *            Category to define, with its values list.
     * @deprecated Since 1.7 use
     *             {@link EnvironmentService#setDictionaryCategory(String, CategoryData, boolean)}
     *             with boolean setting at true
     */
    public void setDictionaryCategory(String pRoleToken, CategoryData pCategory);

    /**
     * Define a new category in the dictionary (or update an existing one).<br />
     * Category values of the existing category can be remove if they are not
     * redefined.
     * <p>
     * e.g 1: Erase old values
     * <ul>
     * <li>Existing: Cat1 {val1, val2, val3}
     * <li>Redefinition: Cat1 {val1, val3, val4, val5}
     * <li>Cat1 {val1, val3, val4, val5}
     * </ul>
     * <p>
     * </ul> e.g 2: Update category
     * <ul>
     * <li>Existing: Cat1 {val1, val2, val3}
     * <li>Redefinition: Cat1 {val1, val3, val4, val5}
     * <li>Cat1 {val1, val2, val3, val4, val5}
     * </ul>
     * 
     * @param pRoleToken
     *            Role session token
     * @param pCategory
     *            Category to define, with its values list.
     * @param pErase
     *            If true the category values that already exist will be
     *            removed.
     */
    public void setDictionaryCategory(String pRoleToken,
            CategoryData pCategory, boolean pErase);

    /**
     * Remove a category from a process dictionary.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pBusinessProcessName
     *            Business process name.
     * @param pCategoryKey
     *            Label key of the category to remove.
     */
    public void deleteCategory(String pRoleToken, String pBusinessProcessName,
            String pCategoryKey);

    /**
     * Remove an environment from a process dictionary.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pBusinessProcessName
     *            Business process name.
     * @param pEnvironmentKey
     *            Label key of the env. to remove.
     */
    public void deleteEnvironment(String pRoleToken,
            String pBusinessProcessName, String pEnvironmentKey);

    /**
     * Get info on a sheet type from a sheet identifier.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pBusinessProcessName
     *            Business process name
     * @param pEnvironmentName
     *            Name of environment.
     * @return Information on the environment, or NULL is the environment does
     *         not exist
     */
    public EnvironmentData getEnvironmentByName(String pRoleToken,
            String pBusinessProcessName, String pEnvironmentName);

    /**
     * Update the usable field data for a given list of products.
     * 
     * @param pRoleToken
     *            user role token.
     * @param pBusinessProcessName
     *            Business process name.
     * @param pProductNames
     *            List of product names.
     * @param pUsableFieldData
     *            List of usable field data to update.
     * @param pFieldsContainerIds
     *            Array of fields container identifiers (containing the usable
     *            fields).
     */
    public void updateUsableFieldDatas(String pRoleToken,
            String pBusinessProcessName, String[] pProductNames,
            Map<String, UsableFieldData> pUsableFieldData,
            String[] pFieldsContainerIds);

    /**
     * Get the category values as a map : <categoryName, List of category
     * values> available for the current role in the given environment
     * 
     * @param pRoleToken
     *            the role token
     * @param pProcessName
     *            the name of the process
     * @param pEnvironmentName
     *            the environment name
     * @param pCategoryNames
     *            the category names
     * @return A map associating the category values to each category name
     */
    public Map<String, List<CategoryValue>> getCategoryValues(
            String pRoleToken, String pProcessName, String pEnvironmentName,
            List<String> pCategoryNames);

    /**
     * Get the category values as a map <categoryName, List of category values>
     * available for the current role in the given environments
     * 
     * @param pRoleToken
     *            the role token
     * @param pProcessName
     *            the name of the process
     * @param pEnvironmentNames
     *            the environment names
     * @param pCategoryNames
     *            the category names
     * @return A map associating the category values to each category name
     */
    public Map<String, List<CategoryValue>> getCategoryValues(
            String pRoleToken, String pProcessName,
            Collection<String> pEnvironmentNames, List<String> pCategoryNames);

    /**
     * Gets all values defined in some categories.
     * <p>
     * Values are retrieved from the dictionary.
     * 
     * @param pProcessName
     *            the business process name
     * @param pCategoryNames
     *            the category names
     * @return A map associating the category values to each category name
     */
    public Map<String, List<CategoryValue>> getCategoryValues(
            String pProcessName, List<String> pCategoryNames);
}
