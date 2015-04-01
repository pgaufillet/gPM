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
package org.topcased.gpm.business.environment.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.dictionary.CategoryAccessData;
import org.topcased.gpm.business.dictionary.CategoryData;
import org.topcased.gpm.business.dictionary.CategoryValueData;
import org.topcased.gpm.business.dictionary.EnvironmentData;
import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.DuplicateValueException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.UndeletableElementException;
import org.topcased.gpm.business.exception.UndeletableValuesException;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.product.service.ProductSummaryData;
import org.topcased.gpm.business.search.impl.fields.UsableFieldsManager;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.dictionary.Category;
import org.topcased.gpm.domain.dictionary.CategoryAccess;
import org.topcased.gpm.domain.dictionary.CategoryDao;
import org.topcased.gpm.domain.dictionary.CategoryValue;
import org.topcased.gpm.domain.dictionary.CategoryValueDao;
import org.topcased.gpm.domain.dictionary.CategoryValueInfo;
import org.topcased.gpm.domain.dictionary.Dictionary;
import org.topcased.gpm.domain.dictionary.DictionaryDao;
import org.topcased.gpm.domain.dictionary.Environment;
import org.topcased.gpm.domain.product.Product;

/**
 * Implementation of Environment Service.
 * 
 * @author tszadel
 * @author llatil
 */
public class EnvironmentServiceImpl extends ServiceImplBase implements
        EnvironmentService {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.environment.service.EnvironmentService#getEnvironmentByName(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public EnvironmentData getEnvironmentByName(String pRoleToken,
            String pBusinessProcessName, String pEnvironmentName) {

        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);
        Dictionary lDictionary = lBusinessProcess.getDictionary();

        if (null == lDictionary) {
            return null;
        }

        Collection<Environment> lEnvironments = lDictionary.getEnvironments();
        EnvironmentData lEnvironmentData = null;

        boolean lNameFound = false;

        for (Environment lEnv : lEnvironments) {
            if (pEnvironmentName.equals(lEnv.getName())) {
                lNameFound = true;

                lEnvironmentData = new EnvironmentData();

                lEnvironmentData.setId(lEnv.getId());
                lEnvironmentData.setBusinessProcess(pBusinessProcessName);
                lEnvironmentData.setLabelKey(lEnv.getName());
                lEnvironmentData.setIsPublic(lEnv.isIsPublic());

                Collection<CategoryValue> lCategoryValues =
                        lEnv.getCategoryValues();
                CategoryValueData[] lCategoryValueDataArray;
                lCategoryValueDataArray =
                        new CategoryValueData[lCategoryValues.size()];

                int i = 0;
                CategoryValueData lCategoryValueData = new CategoryValueData();

                for (CategoryValue lCatValue : lCategoryValues) {
                    if (null != lCatValue) {
                        lCategoryValueData.setValue(lCatValue.getValue());
                    }
                    lCategoryValueDataArray[i] = lCategoryValueData;
                    i++;
                }
                lEnvironmentData.setCategoryValueDatas(lCategoryValueDataArray);
            }
        }

        if (!lNameFound) {
            return null;
        }
        return lEnvironmentData;
    }

    /**
     * Check if the category can be update.
     * <p>
     * Global admin can update all category.
     * </p>
     * <p>
     * User can only update 'PRODUCT' and 'USER' category and he must have the
     * admin access 'dictinary.modify' on instance or on product.
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pCategoryAccess
     *            Access of the category
     * @return True if the category can be updated, false otherwise.
     */
    public boolean isDictionaryCategoryUpdatable(final String pRoleToken,
            final CategoryAccessData pCategoryAccess) {
        boolean lHasGlobalAdminRole =
                getAuthService().hasGlobalAdminRole(pRoleToken);
        if (lHasGlobalAdminRole) {
            return true;
        }

        if (CategoryAccessData.USER.equals(pCategoryAccess)
                || CategoryAccessData.PRODUCT.equals(pCategoryAccess)) {
            boolean lAdminAccessOnInstance =
                    getAuthService().isAdminAccessDefinedOnInstance(pRoleToken,
                            AdministrationAction.DICT_MODIFY.getActionKey());
            if (lAdminAccessOnInstance) {
                return true;
            }

            String lCurrentProductName =
                    getAuthService().getProductNameFromSessionToken(pRoleToken);
            boolean lAdminAccessOnProduct =
                    getAuthService().isAdminAccessDefinedOnProduct(pRoleToken,
                            AdministrationAction.DICT_MODIFY.getActionKey(),
                            lCurrentProductName);
            if (lAdminAccessOnProduct) {
                return true;
            }
        }

        return false;
    }

    /**
     * Define a new category in the dictionary (or update an existing one).
     * 
     * @param pRoleToken
     *            Role session token
     * @param pCategory
     *            Category to define, with its values list.
     */
    public void setDictionaryCategory(String pRoleToken, CategoryData pCategory) {
        if (!isDictionaryCategoryUpdatable(pRoleToken,
                pCategory.getAccessLevel())) {
            throw new AuthorizationException(
                    "Unsufficient rights to modify dictionary.");
        }

        BusinessProcess lBusinessProcess =
                getBusinessProcess(pCategory.getBusinessProcess());
        Dictionary lDictionary = getDictionary(lBusinessProcess);

        Category lCategory =
                categoryDao.getCategory(lDictionary, pCategory.getLabelKey());

        CategoryAccess lCatAccess =
                getCategoryAccessValue(pCategory, lCategory);

        if (lCategory == null) {
            // Category must be created if required
            lCategory = Category.newInstance();
            lCategory.setName(pCategory.getLabelKey());
            lCategory.setDictionary(lDictionary);
            lCategory.setAccessLevel(lCatAccess);
            getCategoryDao().create(lCategory);
            lDictionary.addToCategoryList(lCategory);
        }
        else {
            // Update access level if needed.
            if (lCatAccess != null) {
                lCategory.setAccessLevel(lCatAccess);
            }

            // If category already exists, we must update it:
            // First, compare new value list with the one stored in DB to
            // get the value to delete.
            // Values present in the DB, and not in the new value list must
            // be deleted.
            Set<String> lValuesToBeRemoved = new HashSet<String>();

            // Initialize the list with all actual values
            for (CategoryValue lCatValue : lCategory.getCategoryValues()) {
                lValuesToBeRemoved.add(lCatValue.getValue());
            }

            // And remove the ones present in input data (not to be deleted)
            for (CategoryValueData lValueData : pCategory.getCategoryValueDatas()) {
                lValuesToBeRemoved.remove(lValueData.getValue());
            }

            for (String lValueName : lValuesToBeRemoved) {
                CategoryValue lCatValue =
                        getCategoryValueDao().get(lCategory, lValueName);

                // Unknown category name
                if (lCatValue == null) {
                    throw new InvalidNameException(lValueName,
                            "Invalid value {0}");
                }
                else {
                    lCategory.removeFromCategoryValueList(lCatValue);
                    try {
                        // A DataIntegrityViolationException is launch is the
                        // value is used
                        getCategoryValueDao().remove(lCatValue);
                        // Flush the session to throw the
                        // DataIntegrityViolationException
                        getCategoryValueDao().flush();
                    }
                    catch (DataIntegrityViolationException e) {
                        throw new UndeletableValuesException(
                                Collections.singletonList(lValueName));
                    }
                }
            }
        }
        CategoryValueData[] lCatValueDatas = pCategory.getCategoryValueDatas();

        for (int i = 0; i < lCatValueDatas.length; i++) {

            CategoryValueData lCatValueData = lCatValueDatas[i];
            boolean isFound = false;
            // if the value exist, remove then add to the correct position
            List<CategoryValue> lCategoryValues = lCategory.getCategoryValues();
            for (int k = 0; k < lCategoryValues.size() && !isFound; k++) {
                CategoryValue lCatValue = lCategoryValues.get(k);
                if (lCatValueData.getValue().equals(lCatValue.getValue())) {
                    lCategory.removeFromCategoryValueList(lCatValue);
                    getCategoryDao().addValueAtPosition(lCategory, lCatValue, i);
                    isFound = true;
                }
            }
            // do not save empty category values.
            if (!isFound && StringUtils.isNotEmpty(lCatValueData.getValue())) {
                // Create a new category value because it does not exist yet
                CategoryValue lCatValue = CategoryValue.newInstance();
                lCatValue.setValue(lCatValueData.getValue());
                getCategoryValueDao().create(lCatValue);
                if (lCategory.getCategoryValues().isEmpty()) {
                    lCategory.addToCategoryValueList(lCatValue);
                }
                else {
                    getCategoryDao().addValueAtPosition(lCategory, lCatValue, i);
                }
            }
        }
    }

    /**
     * Get the new value of the access attribute of a category.
     * <ol>
     * <li>If the attribute is not define in the category data and the category
     * doesn't exists. The default value is PROCESS.
     * <li>If the attribute is define, get the CategoryAccess object
     * <li>If the attribute is not define and the category exists, do nothing.
     * 
     * @param pCategoryData
     *            Category to create
     * @param pCategory
     *            Category in database (can be null if not exists)
     * @return The category access if define, null otherwise.
     * @throws IllegalArgumentException
     *             If the access object cannot be create.
     */
    private CategoryAccess getCategoryAccessValue(CategoryData pCategoryData,
            Category pCategory) throws IllegalArgumentException {

        CategoryAccess lCatAccess = null;
        if ((null == pCategoryData.getAccessLevel()) && (pCategory == null)) {
            lCatAccess = CategoryAccess.PROCESS;
        }
        else if (null != pCategoryData.getAccessLevel()) {
            lCatAccess =
                    CategoryAccess.fromString(pCategoryData.getAccessLevel().toString());
            if (null == lCatAccess) {
                throw new IllegalArgumentException("Invalid access level:"
                        + pCategoryData.getAccessLevel().toString());
            }
        }
        return lCatAccess;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.environment.service.EnvironmentService#deleteCategory(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void deleteCategory(String pRoleToken, String pBusinessProcessName,
            String pCategoryKey) {

        Category lCategory = null;
        BusinessProcess lProcess = getBusinessProcess(pBusinessProcessName);
        Dictionary lDictionary = lProcess.getDictionary();

        assert (lDictionary != null);

        // Get the category object.
        lCategory = getCategoryDao().getCategory(lDictionary, pCategoryKey);
        if (null == lCategory) {
            throw new InvalidNameException(pCategoryKey, "Invalid category {0}");
        }

        if (!isDictionaryCategoryUpdatable(
                pRoleToken,
                CategoryAccessData.fromString(lCategory.getAccessLevel().getValue()))) {
            throw new AuthorizationException(
                    "Unsufficient rights to modify dictionary.");
        }

        // Check if category is used.
        if (getCategoryDao().isCategoryUsed(lCategory)) {
            throw new UndeletableElementException(lCategory.getName());
        }

        // Remove category.
        // We need to remove the category from ALL associations !
        lDictionary.removeFromCategoryList(lCategory);
        getCategoryDao().remove(lCategory);
    }

    /**
     * Check if an environment is creatable by current role token. An
     * environment is creatable only for global admin role and for an admin
     * access define on instance.
     * 
     * @param pRoleToken
     *            Role session token
     * @return true if the environment is creatable.
     */
    public boolean isEnvironmentCreatable(String pRoleToken) {
        boolean lHasGlobalAdminRole =
                getAuthService().hasGlobalAdminRole(pRoleToken);
        if (lHasGlobalAdminRole) {
            return true;
        }

        boolean lAdminAccessOnInstance =
                getAuthService().isAdminAccessDefinedOnInstance(pRoleToken,
                        AdministrationAction.ENV_CREATE.getActionKey());

        return lAdminAccessOnInstance;
    }

    /**
     * Create a new environment.
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
            boolean pIsPublic) {

        if (!isEnvironmentCreatable(pRoleToken)) {
            throw new AuthorizationException(
                    "Unsufficient rights to create environment.");
        }

        BusinessProcess lProcess = getBusinessProcess(pBusinessProcessName);
        Dictionary lDictionary = getDictionary(lProcess);
        Environment lEnv =
                getEnvironmentDao().getEnvironment(pBusinessProcessName,
                        pEnvironmentName);

        if (null != lEnv) {
            // This environment exists already.
            throw new InvalidNameException(pEnvironmentName);
        }

        lEnv = Environment.newInstance();
        lEnv.setName(pEnvironmentName);
        lEnv.setDictionary(lDictionary);
        lEnv.setIsPublic(pIsPublic);
        getEnvironmentDao().create(lEnv);

        lDictionary.addToEnvironmentList(lEnv);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.environment.service.EnvironmentService#deleteEnvironment(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void deleteEnvironment(String pRoleToken,
            String pBusinessProcessName, String pEnvironmentKey) {
        getAuthService().assertGlobalAdminRole(pRoleToken);

        Environment lEnv = null;
        BusinessProcess lProcess = getBusinessProcess(pBusinessProcessName);
        Dictionary lDictionary = lProcess.getDictionary();

        assert (lDictionary != null);

        // Check if environment is used.
        if (isEnvironmentUsed(pRoleToken, pEnvironmentKey)) {
            throw new UndeletableElementException(pEnvironmentKey);
        }

        lEnv =
                getEnvironmentDao().getEnvironment(pBusinessProcessName,
                        pEnvironmentKey);

        if (null == lEnv) {
            throw new InvalidNameException(pEnvironmentKey,
                    "Invalid environment {0}");
        }

        // Get the category object.
        // update these category values (removed from the environment)
        for (CategoryValue lCatValue : lEnv.getCategoryValues()) {
            lCatValue.removeFromEnvironmentList(lEnv);
        }

        // Remove environment
        // We need to remove the category from ALL associations !
        lDictionary.removeFromEnvironmentList(lEnv);
        getEnvironmentDao().remove(lEnv);
    }

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
            String pBusinessProcessName) {
        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);
        Dictionary lDictionary = lBusinessProcess.getDictionary();

        if (null == lDictionary) {
            return new ArrayList<String>(0);
        }

        Collection<Category> lCategories = lDictionary.getCategories();
        Collection<String> lNames = new ArrayList<String>(lCategories.size());

        for (Category lCat : lCategories) {
            lNames.add(lCat.getName());
        }
        return lNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.environment.service.EnvironmentService#getCategories(java.lang.String,
     *      java.lang.String)
     */
    public Collection<CategoryData> getCategories(String pRoleToken,
            String pBusinessProcessName) {
        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);
        Dictionary lDictionary = lBusinessProcess.getDictionary();

        if (null == lDictionary) {
            return Collections.emptyList();
        }

        Collection<Category> lCategories = lDictionary.getCategories();
        return createCategoryDataList(pRoleToken, pBusinessProcessName,
                lCategories);
    }

    public CategoryData getCategory(final String pRoleToken,
            final String pCategoryName) {
        final BusinessProcess lBusinessProcess =
                getBusinessProcess(authorizationService.getProcessName(pRoleToken));
        final Dictionary lDictionary = lBusinessProcess.getDictionary();

        if (lDictionary == null) {
            return null;
        }
        else {
            final Category lCat =
                    categoryDao.getCategory(lDictionary, pCategoryName);

            if (lCat == null) {
                return null;
            }
            else {
                return createCategoryData(pRoleToken,
                        lBusinessProcess.getName(), lCat);
            }
        }
    }

    /**
     * Retrieve the category using its identifier.
     * 
     * @param pRoleToken
     *            Role token
     * @param pId
     *            Identifier of the category.
     * @return The category if found, null object otherwise
     */
    public CategoryData getCategoryById(final String pRoleToken,
            final String pId) {
        final BusinessProcess lBusinessProcess =
                getBusinessProcess(authorizationService.getProcessName(pRoleToken));
        final Dictionary lDictionary = lBusinessProcess.getDictionary();

        if (lDictionary == null) {
            return null;
        }
        else {
            try {
                final Category lCat = categoryDao.load(pId);
                return createCategoryData(pRoleToken,
                        lBusinessProcess.getName(), lCat);
            }
            catch (HibernateObjectRetrievalFailureException e) {
                return null;
            }
        }
    }

    /**
     * Get available categories access level, function of role token. A global
     * admin role has access to USER, PRODUCT, and PROCESS whereas an
     * adminaccess on instance or on product has access to USER, PRODUCT. All
     * other roles has access to USER.
     * 
     * @param pRoleToken
     *            role session token
     * @return Associated access level
     */
    protected String[] getAccessLevels(String pRoleToken) {

        if (getAuthService().hasGlobalAdminRole(pRoleToken)) {
            return new String[] { "USER", "PRODUCT", "PROCESS" };
        }

        boolean lAdminAccessOnInstance =
                getAuthService().isAdminAccessDefinedOnInstance(pRoleToken,
                        AdministrationAction.DICT_MODIFY.getActionKey());
        String lCurrentProductName =
                getAuthService().getProductNameFromSessionToken(pRoleToken);
        boolean lAdminAccessOnProduct =
                getAuthService().isAdminAccessDefinedOnProduct(pRoleToken,
                        AdministrationAction.DICT_MODIFY.getActionKey(),
                        lCurrentProductName);

        if (getAuthService().hasAdminAccess(pRoleToken)
                || lAdminAccessOnInstance || lAdminAccessOnProduct) {
            return new String[] { "USER", "PRODUCT" };
        }

        return new String[] { "USER" };
    }

    /**
     * Get the list of categories allowed to be modified by the user..
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pBusinessProcessName
     *            Business process name.
     * @return The list of Category.
     */
    public Collection<CategoryData> getModifiableCategories(String pRoleToken,
            String pBusinessProcessName) {
        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);
        Dictionary lDictionary = lBusinessProcess.getDictionary();

        if (null == lDictionary) {
            return Collections.emptyList();
        }

        String[] lAccessLevels = getAccessLevels(pRoleToken);

        List<Category> lCategories =
                getCategoryDao().getModifiableCategories(lDictionary,
                        lAccessLevels);
        return createCategoryDataList(pRoleToken, pBusinessProcessName,
                lCategories);
    }

    /**
     * Creates a category data from a category entity.
     * 
     * @param pBusinessProcessName
     *            The business process name.
     * @param pCategory
     *            The category.
     * @return The category data.
     */
    private CategoryData createCategoryData(final String pRoleToken,
            final String pBusinessProcessName, final Category pCategory) {
        final CategoryData lCatData = new CategoryData();

        lCatData.setId(pCategory.getId());
        lCatData.setLabelKey(pCategory.getName());
        lCatData.setI18nName(getI18nService().getValueForUser(pRoleToken,
                pCategory.getName()));
        lCatData.setBusinessProcess(pBusinessProcessName);
        lCatData.setAccessLevel(CategoryAccessData.fromString(pCategory.getAccessLevel().getValue()));
        lCatData.setCategoryValueDatas(createCategoryValueDataArray(pCategory.getCategoryValues()));

        return lCatData;
    }

    /**
     * Creates a category data list from a category entities list.
     * 
     * @param pBusinessProcessName
     *            the business process name
     * @param pCategories
     *            the categories
     * @return the category data list
     */
    private List<CategoryData> createCategoryDataList(String pRoleToken,
            String pBusinessProcessName, Collection<Category> pCategories) {
        final List<CategoryData> lList = new ArrayList<CategoryData>();

        for (Category lCat : pCategories) {
            lList.add(createCategoryData(pRoleToken, pBusinessProcessName, lCat));
        }

        return lList;
    }

    /**
     * Create an array of CategoryValueData objects from a list of
     * CategoryValue.
     * 
     * @param pCatValues
     *            the cat values
     * @return the category value data[]
     */
    private CategoryValueData[] createCategoryValueDataArray(
            Collection<CategoryValue> pCatValues) {

        List<CategoryValueData> lCatValuesData =
                new ArrayList<CategoryValueData>(pCatValues.size());

        for (CategoryValue lCatValue : pCatValues) {
            CategoryValueData lCatValueData = new CategoryValueData();
            lCatValueData.setValue(lCatValue.getValue());
            lCatValuesData.add(lCatValueData);
        }
        return lCatValuesData.toArray(new CategoryValueData[pCatValues.size()]);
    }

    /**
     * Get a list of environment names defined for a process. For the global
     * admin role, all environments (public and private) are returned. For an
     * admin access on instance, only public environments are returned. For an
     * admin access on current product : only public environments on current
     * product are returned.
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pBusinessProcessName
     *            Business process name.
     * @return The list of environment names.
     */
    public Collection<String> getEnvironmentNames(String pRoleToken,
            String pBusinessProcessName) {
        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);
        AuthorizationService lAuthService = getAuthService();

        Dictionary lDictionary = lBusinessProcess.getDictionary();

        if (null == lDictionary) {
            return new ArrayList<String>(0);
        }

        Collection<Environment> lEnvironments = lDictionary.getEnvironments();
        Collection<String> lNames = new ArrayList<String>(lEnvironments.size());

        String lCurrentProductName =
                getAuthService().getProductNameFromSessionToken(pRoleToken);
        List<String> lAvailableEnvNames = null;

        if (lAuthService.hasGlobalAdminRole(pRoleToken)
                || lAuthService.isAdminAccessDefinedOnInstance(pRoleToken,
                        AdministrationAction.ENV_MODIFY.getActionKey())) {
            lAvailableEnvNames = new ArrayList<String>();
        }
        // Fill the env name list for an admin access define on product.
        else if (getAuthService().isAdminAccessDefinedOnProduct(pRoleToken,
                AdministrationAction.ENV_MODIFY.getActionKey(),
                lCurrentProductName)) {
            lAvailableEnvNames = new ArrayList<String>();
            ProductSummaryData lCurrentProductSummaryData =
                    getProductService().getCurrentProductSummary(pRoleToken);
            for (String lEnv : lCurrentProductSummaryData.getEnvironments()) {
                lAvailableEnvNames.add(lEnv);
            }
        }

        if (lAvailableEnvNames == null) {
            return lNames;
        }

        for (Environment lEnv : lEnvironments) {
            // Public environments are automatically added
            if (lEnv.isIsPublic()) {
                if (lAvailableEnvNames.isEmpty()
                        || lAvailableEnvNames.contains(lEnv.getName())) {
                    lNames.add(lEnv.getName());
                }
            }
            // Private environments are added only for admin role.
            else if (lAuthService.hasGlobalAdminRole(pRoleToken)) {
                lNames.add(lEnv.getName());
            }
        }

        return lNames;
    }

    /**
     * Get a list of the environment names modifiable by the current user /
     * role. The modification access is controlled by the given role token:
     * <ul>
     * <li>Global admin can modify all environments
     * <li>Admin on some products
     * <li>Other users can
     * 
     * @param pRoleToken
     *            Role session token.
     * @param pBusinessProcessName
     *            Business process name.
     * @return The list of environment names.
     */
    public Collection<String> getModifiableEnvironmentNames(String pRoleToken,
            String pBusinessProcessName) {
        // TODO Implements access level on environments.
        //
        //
        return getEnvironmentNames(pRoleToken, pBusinessProcessName);
    }

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
            String pCategoryName) {
        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);
        Dictionary lDictionary = lBusinessProcess.getDictionary();
        Category lCat;

        if (null == lDictionary) {
            throw new InvalidNameException(pEnvironmentName);
        }

        Environment lEnv =
                getEnvironmentDao().getEnvironment(pBusinessProcessName,
                        pEnvironmentName);
        if (null == lEnv) {
            throw new InvalidNameException(pEnvironmentName);
        }

        lCat = getCategoryDao().getCategory(lDictionary, pCategoryName);
        if (null == lCat) {
            throw new InvalidNameException(pCategoryName);
        }

        EnvironmentData lEnvData = new EnvironmentData();
        lEnvData.setLabelKey(pEnvironmentName);
        lEnvData.setCategoryKey(pCategoryName);
        lEnvData.setBusinessProcess(pBusinessProcessName);
        lEnvData.setIsPublic(lEnv.isIsPublic());

        final Collection<String> lCatValues =
                getEnvironmentDao().getEnvironmentCategoryValues(
                        pBusinessProcessName, pEnvironmentName, pCategoryName);
        final List<CategoryValueData> lCatValuesData =
                new LinkedList<CategoryValueData>();

        for (final String lCatValue : lCatValues) {
            lCatValuesData.add(new CategoryValueData(lCatValue));
        }
        lEnvData.setCategoryValueDatas(lCatValuesData.toArray(new CategoryValueData[0]));

        return lEnvData;
    }

    /**
     * Check if the environment is updatable (public and private environments
     * are updatable for global admin role, public environments are updatable
     * for admin access on instance, only current product environments are
     * updatable for admin access on product
     * 
     * @param pRoleToken
     *            Role Session token
     * @param pEnvironmentName
     *            Name of the environment to test.
     * @param pIsPublic
     *            True if the environment to test is public
     * @return true if the category is updatable
     */
    public boolean isEnvironmentUpdatable(String pRoleToken,
            final String pEnvironmentName, final boolean pIsPublic) {

        boolean lHasGlobalAdminRole =
                getAuthService().hasGlobalAdminRole(pRoleToken);

        // A private environment is only updatable by the global admin role
        if (!pIsPublic) {
            return lHasGlobalAdminRole;
        }
        // Else, public environments case
        // Global admin can update
        if (lHasGlobalAdminRole) {
            return true;
        }

        // Admin access on instance can update
        boolean lAdminAccessOnInstance =
                getAuthService().isAdminAccessDefinedOnInstance(pRoleToken,
                        AdministrationAction.ENV_MODIFY.getActionKey());
        if (lAdminAccessOnInstance) {
            return true;
        }

        // Admin access on product can update only the current product
        // environments
        if (authorizationService.getProductFromSessionToken(pRoleToken) != null) {
            ProductSummaryData lProductSummaryDara =
                    getProductService().getCurrentProductSummary(pRoleToken);
            boolean lAdminAccessOnProduct =
                    getAuthService().isAdminAccessDefinedOnProduct(pRoleToken,
                            AdministrationAction.ENV_MODIFY.getActionKey(),
                            lProductSummaryDara.getName());
            boolean lEnvInProduct = false;
            for (String lEnvName : lProductSummaryDara.getEnvironments()) {
                if (pEnvironmentName.equals(lEnvName)) {
                    lEnvInProduct = true;
                }
            }
            if (lAdminAccessOnProduct) {
                return lEnvInProduct;
            }
        }
        return false;
    }

    /**
     * Define a new category for an environment (or update an existing one).
     * 
     * @param pRoleToken
     *            Role session token
     * @param pEnvironment
     *            Category to define, with its values list.
     */
    public void setEnvironmentCategory(String pRoleToken,
            EnvironmentData pEnvironment) {
        if (!isEnvironmentUpdatable(pRoleToken, pEnvironment.getLabelKey(),
                pEnvironment.isIsPublic())) {
            throw new AuthorizationException(
                    "Unsufficient rights to modify environment. ");
        }

        setEnvironmentCategories(pRoleToken, pEnvironment);
    }

    /**
     * Define a new category for an environment (or update an existing one).
     * 
     * @param pRoleToken
     *            Role session token
     * @param pEnvironment
     *            Category to define, with its values list.
     * @throws InvalidNameException
     *             Cannot find categories values.
     */
    public void setEnvironmentCategories(String pRoleToken,
            EnvironmentData pEnvironment) throws InvalidNameException {
        BusinessProcess lBusinessProcess =
                getBusinessProcess(pEnvironment.getBusinessProcess());
        Dictionary lDictionary = lBusinessProcess.getDictionary();
        Category lCat;

        if (null == lDictionary) {
            throw new InvalidNameException(pEnvironment.getBusinessProcess());
        }

        Environment lEnv =
                getEnvironmentDao().getEnvironment(
                        pEnvironment.getBusinessProcess(),
                        pEnvironment.getLabelKey());
        if (null == lEnv) {
            throw new InvalidNameException(pEnvironment.getLabelKey());
        }

        lCat =
                getCategoryDao().getCategory(lDictionary,
                        pEnvironment.getCategoryKey());
        if (null == lCat) {
            throw new InvalidNameException(pEnvironment.getCategoryKey());
        }

        if (lCat.getAccessLevel() == CategoryAccess.PROCESS) {
            getAuthService().assertGlobalAdminRole(pRoleToken);
        }
        if (lCat.getAccessLevel() == CategoryAccess.PRODUCT) {
            getAuthService().assertAdminRole(pRoleToken);
        }

        // Get only the category values from the good category
        Collection<CategoryValue> lCatValuesInCategory =
                lCat.getCategoryValues();

        // get all category values present in the environment at the beginning
        Collection<CategoryValue> lOldCategoryValues =
                new Vector<CategoryValue>();
        lOldCategoryValues.addAll(lEnv.getCategoryValues());

        // Remove all values from the category cat in the environment
        lEnv.getCategoryValues().removeAll(lCatValuesInCategory);

        // Reference all category values in this environment.
        // The category values must be present in the dictionary category

        // Update all category values that are in the new environment
        for (CategoryValueData lValueData : pEnvironment.getCategoryValueDatas()) {
            CategoryValue lCatValue;
            lCatValue = getCategoryValueDao().get(lCat, lValueData.getValue());
            if (null == lCatValue) {
                throw new InvalidNameException(lValueData.getValue(),
                        "Value {0} not in dictionary");
            }
            lEnv.addToCategoryValueList(lCatValue);
            if (!lCatValue.getEnvironments().contains(lEnv)) {
                lCatValue.addToEnvironmentList(lEnv);
            }
        }

        // Get all category values not in this environment anymore
        lOldCategoryValues.removeAll(lEnv.getCategoryValues());

        // update these category values (removed from the environment)
        for (CategoryValue lCatValue : lOldCategoryValues) {
            lCatValue.removeFromEnvironmentList(lEnv);
        }
    }

    /**
     * Get the dictionary of a business process. The dictionary is created if
     * needed.
     * 
     * @param pBusinessProc
     *            the business process
     * @return the dictionary
     */
    private Dictionary getDictionary(BusinessProcess pBusinessProc) {
        Dictionary lDictionary = pBusinessProc.getDictionary();

        if (null == lDictionary) {
            lDictionary = Dictionary.newInstance();
            getDictionaryDao().create(lDictionary);
            pBusinessProc.setDictionary(lDictionary);
        }
        return lDictionary;
    }

    /** The category DAO. */
    private CategoryDao categoryDao;

    /**
     * Returns the category Dao.
     * 
     * @return Returns the categoryDao.
     */
    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    /**
     * Sets the category Dao.
     * 
     * @param pCategoryDao
     *            The categoryDao to set.
     */
    public void setCategoryDao(CategoryDao pCategoryDao) {
        categoryDao = pCategoryDao;
    }

    /** The dictionary dao. */
    private DictionaryDao dictionaryDao;

    /**
     * Get the dictionary DAO.
     * 
     * @return Returns the dictionnaryDao.
     */
    public DictionaryDao getDictionaryDao() {
        return dictionaryDao;
    }

    /**
     * Set the dictionary DAO.
     * 
     * @param pDictionaryDao
     *            The dictionaryDao to set.
     */
    public void setDictionaryDao(DictionaryDao pDictionaryDao) {
        dictionaryDao = pDictionaryDao;
    }

    /** The category value dao. */
    private CategoryValueDao categoryValueDao;

    /**
     * Get the CategoryValue DAO.
     * 
     * @return Returns the categoryValueDao.
     */
    public CategoryValueDao getCategoryValueDao() {
        return categoryValueDao;
    }

    /**
     * Set the CategoryValue DAO.
     * 
     * @param pCategoryValueDao
     *            The categoryValueDao to set.
     */
    public void setCategoryValueDao(CategoryValueDao pCategoryValueDao) {
        categoryValueDao = pCategoryValueDao;
    }

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
            String pCategoryName, List<String> pCategoryValues) {

        BusinessProcess lBusinessProcess =
                getBusinessProcess(pBusinessProcessName);
        Dictionary lDictionary = lBusinessProcess.getDictionary();
        Category lCat;

        if (null == lDictionary) {
            throw new InvalidNameException(pEnvironmentName);
        }

        Environment lEnv =
                getEnvironmentDao().getEnvironment(pBusinessProcessName,
                        pEnvironmentName);

        if (null == lEnv) {
            throw new InvalidNameException(pEnvironmentName);
        }

        if (!isEnvironmentUpdatable(pRoleToken, lEnv.getName(),
                lEnv.isIsPublic())) {
            throw new AuthorizationException(
                    "Unsufficient rights to modify environment. ");
        }

        lCat = getCategoryDao().getCategory(lDictionary, pCategoryName);
        if (null == lCat) {
            throw new InvalidNameException(pCategoryName);
        }

        // Get category values from update list
        List<CategoryValue> lDictionaryCategoryValues =
                lCat.getCategoryValues();
        Set<CategoryValue> lEnvironmentCategoryValues =
                lEnv.getCategoryValues();

        // iterate on dictionary category values
        for (CategoryValue lCategoryValue : lDictionaryCategoryValues) {
            Set<Environment> lEnvironmentList =
                    lCategoryValue.getEnvironments();
            // if category value has to be added (i.e. is in values to add and
            // not already in environment category values)
            if (pCategoryValues.contains(lCategoryValue.getValue())
                    && !lEnvironmentList.contains(lEnv)) {
                lEnvironmentList.add(lEnv);
                lEnvironmentCategoryValues.add(lCategoryValue);
            }
            // if category value has to be removed (i.e. is not in values to add
            // and already in environment category values)
            else if (!pCategoryValues.contains(lCategoryValue.getValue())
                    && lEnvironmentList.contains(lEnv)) {
                lCategoryValue.getEnvironments().remove(lEnv);
                lEnvironmentCategoryValues.add(lCategoryValue);
            }
        }
    }

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
     *            Array of fields container ids (containing the usable fields).
     */
    public void updateUsableFieldDatas(String pRoleToken,
            String pBusinessProcessName, String[] pProductNames,
            Map<String, UsableFieldData> pUsableFieldData,
            String[] pFieldsContainerIds) {

        boolean lHasNotSpecified = false;
        for (String lCurrentProductName : pProductNames) {
            if (lCurrentProductName.equals(UsableFieldsManager.NOT_SPECIFIED)) {
                lHasNotSpecified = true;
                break;
            }
        }

        if (pProductNames.length > 0 && !lHasNotSpecified) {
            // We get all environments concerned by these products
            List<String> lEnvironmentNames = new ArrayList<String>();

            for (String lProductName : pProductNames) {
                Product lProduct =
                        getProduct(pBusinessProcessName, lProductName);

                for (Environment lEnv : lProduct.getEnvironments()) {
                    if (!lEnvironmentNames.contains(lEnv.getName())) {
                        lEnvironmentNames.add(lEnv.getName());
                    }
                }
            }
            if (pUsableFieldData != null) {
                for (UsableFieldData lUsableFieldData : pUsableFieldData.values()) {
                    if (FieldType.CHOICE_FIELD.equals(lUsableFieldData.getFieldType())) {

                        List<CategoryValue> lCategoryValues;

                        lCategoryValues =
                                getCategoryValueDao().getPossibleValues(
                                        pBusinessProcessName,
                                        lUsableFieldData.getCategoryName(),
                                        lEnvironmentNames);

                        List<String> lPossibleValues =
                                new ArrayList<String>(
                                        lCategoryValues.size() + 1);
                        lPossibleValues.add(UsableFieldsManager.NOT_SPECIFIED);
                        for (CategoryValue lValue : lCategoryValues) {
                            lPossibleValues.add(lValue.getValue());
                        }
                        lUsableFieldData.setPossibleValues(lPossibleValues);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.environment.service.EnvironmentService#getCategoryValues(java.lang.String,
     *      java.lang.String, java.lang.String, java.util.List)
     */
    public Map<String, List<org.topcased.gpm.business.serialization.data.CategoryValue>> getCategoryValues(
            String pRoleToken, String pProcessName, String pEnvironmentName,
            List<String> pCategoryNames) {

        // TODO check roleToken
        return getCategoryValues(pProcessName, pEnvironmentName, pCategoryNames);
    }

    /**
     * Gets the values defined in some categories for a specific environment.
     * 
     * @param pProcessName
     *            the process name
     * @param pEnvironmentName
     *            the environment name
     * @param pCategoryNames
     *            the category names
     * @return A map associating the category values to each category name
     */
    public Map<String, List<org.topcased.gpm.business.serialization.data.CategoryValue>> getCategoryValues(
            String pProcessName, String pEnvironmentName,
            List<String> pCategoryNames) {

        // Get the list of (categoryName, categoryValues) ordered by
        // categoryName
        List<CategoryValueInfo> lCategoryValueInfos =
                getEnvironmentDao().getCategoryValues(pProcessName,
                        pEnvironmentName, pCategoryNames);

        // Create the map of categoryName -> list of category values
        Map<String, List<org.topcased.gpm.business.serialization.data.CategoryValue>> lValues =
                new HashMap<String, List<org.topcased.gpm.business.serialization.data.CategoryValue>>();

        String lCurrentCategoryName = null;
        List<org.topcased.gpm.business.serialization.data.CategoryValue> lCurrentList =
                null;
        for (CategoryValueInfo lCategoryValueInfo : lCategoryValueInfos) {
            if (!lCategoryValueInfo.getCategoryName().equals(
                    lCurrentCategoryName)) {
                if (null != lCurrentList) {
                    lValues.put(lCurrentCategoryName, lCurrentList);
                }
                lCurrentCategoryName = lCategoryValueInfo.getCategoryName();
                lCurrentList =
                        new ArrayList<org.topcased.gpm.business.serialization.data.CategoryValue>();
            }
            lCurrentList.add(new org.topcased.gpm.business.serialization.data.CategoryValue(
                    lCategoryValueInfo.getCategoryValue()));
        }
        if (lCurrentCategoryName != null && lCurrentList != null) {
            lValues.put(lCurrentCategoryName, lCurrentList);
        }
        return lValues;
    }

    /**
     * Gets all values defined in some categories. Values are retrieved from the
     * dictionary.
     * 
     * @param pProcessName
     *            the business process name
     * @param pCategoryNames
     *            the category names
     * @return A map associating the category values to each category name
     */
    public Map<String, List<org.topcased.gpm.business.serialization.data.CategoryValue>> getCategoryValues(
            String pProcessName, List<String> pCategoryNames) {
        // Create the map of categoryName -> list of category values
        Map<String, List<org.topcased.gpm.business.serialization.data.CategoryValue>> lValues =
                new HashMap<String, List<org.topcased.gpm.business.serialization.data.CategoryValue>>();

        for (String lCategoryName : pCategoryNames) {
            // Get the category
            Category lCategory =
                    getCategoryDao().getCategory(pProcessName, lCategoryName);
            List<org.topcased.gpm.business.serialization.data.CategoryValue> lCurrentList =
                    new ArrayList<org.topcased.gpm.business.serialization.data.CategoryValue>(
                            lCategory.getCategoryValues().size());

            // Transforms categoryValue domain to serialization object.
            for (CategoryValue lCategoryValue : lCategory.getCategoryValues()) {
                lCurrentList.add(new org.topcased.gpm.business.serialization.data.CategoryValue(
                        lCategoryValue.getValue()));
            }
            lValues.put(lCategory.getName(), lCurrentList);
        }
        return lValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.environment.service.EnvironmentService#getCategoryValues(java.lang.String,
     *      java.lang.String, java.util.Collection, java.util.List)
     */
    public Map<String, List<org.topcased.gpm.business.serialization.data.CategoryValue>> getCategoryValues(
            String pRoleToken, String pProcessName,
            Collection<String> pEnvironmentNames, List<String> pCategoryNames) {
        Map<String, List<org.topcased.gpm.business.serialization.data.CategoryValue>> lValues =
                null;

        // Merge the categories of all environments
        // but don't merge the content of the category
        for (String lEnv : pEnvironmentNames) {
            Map<String, List<org.topcased.gpm.business.serialization.data.CategoryValue>> lValuesTmp =
                    getCategoryValues(pRoleToken, pProcessName, lEnv,
                            pCategoryNames);

            if (lValues == null) {
                lValues = lValuesTmp;
            }
            else {
                for (Entry<String, List<org.topcased.gpm.business.serialization.data.CategoryValue>> lEntry : lValuesTmp.entrySet()) {
                    // Priority on the categories of the previous environment
                    if (lValues.get(lEntry.getKey()) == null) {
                        lValues.put(lEntry.getKey(), lEntry.getValue());
                    }
                }
            }
        }
        return lValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.environment.service.EnvironmentService#setDictionaryCategory(java.lang.String,
     *      org.topcased.gpm.business.dictionary.CategoryData, boolean)
     */
    public void setDictionaryCategory(String pRoleToken,
            CategoryData pCategory, boolean pErase) {

        // throw Duplicate value exception if some values are duplicated
        checkDuplicateCategoryValue(pCategory);
        if (pErase) {
            setDictionaryCategory(pRoleToken, pCategory);
        }
        else {
            updateDictionaryCategory(pRoleToken, pCategory);
        }
    }

    /**
     * Set category for a dictionary.<br />
     * Add new values if the category exists: category values that no redefined
     * in the new category are not removed.
     * 
     * @param pRoleToken
     *            Role token
     * @param pCategory
     *            Category to update
     * @throws IllegalArgumentException
     *             If the level access is invalid.
     */
    public void updateDictionaryCategory(String pRoleToken,
            CategoryData pCategory) {
        if (!isDictionaryCategoryUpdatable(pRoleToken,
                pCategory.getAccessLevel())) {
            throw new AuthorizationException(
                    "Unsufficient rights to modify dictionary.");
        }

        BusinessProcess lBusinessProcess =
                getBusinessProcess(pCategory.getBusinessProcess());
        Dictionary lDictionary = getDictionary(lBusinessProcess);

        Category lCategory =
                categoryDao.getCategory(lDictionary, pCategory.getLabelKey());

        CategoryAccess lCatAccess =
                getCategoryAccessValue(pCategory, lCategory);

        if (lCategory == null) {
            // Category must be created if required
            lCategory = Category.newInstance();
            lCategory.setName(pCategory.getLabelKey());
            lCategory.setDictionary(lDictionary);
            lCategory.setAccessLevel(lCatAccess);
            getCategoryDao().create(lCategory);
            lDictionary.addToCategoryList(lCategory);
        }
        else {
            // Update access level if needed.
            if (lCatAccess != null) {
                lCategory.setAccessLevel(lCatAccess);
            }
        }

        // Finally, let's create or update the category values
        for (CategoryValueData lCatValueData : pCategory.getCategoryValueDatas()) {

            // Search for the category value
            CategoryValue lCatValue =
                    getCategoryValueDao().get(lCategory,
                            lCatValueData.getValue());
            // If not exists creates it.
            if (null == lCatValue) {
                lCatValue = CategoryValue.newInstance();
                lCatValue.setValue(lCatValueData.getValue());
                getCategoryValueDao().create(lCatValue);
                lCategory.addToCategoryValueList(lCatValue);
            }
        }
    }

    /**
     * Get a category business object from the category serialize object.
     * 
     * @param pRoleToken
     *            Role token
     * @param pSerializableCategory
     *            Serialize object.
     * @return Business object.
     */
    public CategoryData getCategory(
            String pRoleToken,
            org.topcased.gpm.business.serialization.data.Category pSerializableCategory) {

        // Initialize category
        CategoryData lCategory = new CategoryData();
        lCategory.setLabelKey(pSerializableCategory.getName());
        lCategory.setBusinessProcess(authorizationService.getProcessNameFromToken(pRoleToken));

        // Set category access
        if (pSerializableCategory.getAccess() != null) {
            CategoryAccessData lAccess =
                    CategoryAccessData.fromString(pSerializableCategory.getAccess().toUpperCase());
            lCategory.setAccessLevel(lAccess);
        }

        // set category values
        List<org.topcased.gpm.business.serialization.data.CategoryValue> lValues =
                pSerializableCategory.getValues();
        CategoryValueData[] lCategoryValueDatas =
                new CategoryValueData[lValues.size()];
        for (int i = 0; i < lValues.size(); i++) {
            lCategoryValueDatas[i] =
                    new CategoryValueData(lValues.get(i).getValue());
        }
        lCategory.setCategoryValueDatas(lCategoryValueDatas);

        return lCategory;
    }

    /**
     * Get the identifier of the category from its name.
     * 
     * @param pName
     *            Name of the category.
     * @return Identifier of the category, Blank string if not found (or name is
     *         blank)
     */
    public String getCategoryId(final String pName) {
        final String lId;
        if (StringUtils.isBlank(pName)) {
            lId = StringUtils.EMPTY;
        }
        else {
            lId = categoryDao.getId(pName);
        }
        return lId;
    }

    /**
     * Test the category existence.
     * 
     * @param pName
     *            Name of the category.
     * @return True if the category exists, false otherwise (name is blank).
     */
    public Boolean isCategoryExists(final String pName) {
        final Boolean lIsExists;
        if (StringUtils.isBlank(pName)) {
            lIsExists = Boolean.FALSE;
        }
        else {
            lIsExists = categoryDao.isExists(pName);
        }
        return lIsExists;
    }

    /**
     * Test if the category is used by a choice field.
     * 
     * @param pName
     *            Name of the category.
     * @return True if the category is used, false otherwise (name is blank)
     */
    public Boolean isCategoryUsed(final String pName) {
        final Boolean lIsUsed;
        if (StringUtils.isBlank(pName)) {
            lIsUsed = Boolean.FALSE;
        }
        else {
            lIsUsed = categoryDao.isUsed(pName);
        }
        return lIsUsed;
    }

    /**
     * Test if the environment is used by a product.
     * 
     * @param pRoleToken
     *            Role token
     * @param pName
     *            Name of the environment.
     * @return True if the environment is used, false otherwise (name is blank)
     */
    public Boolean isEnvironmentUsed(final String pRoleToken, final String pName) {
        final Boolean lIsUsed;
        if (StringUtils.isBlank(pName)) {
            lIsUsed = Boolean.FALSE;
        }
        else {
            String lProcessName =
                    authorizationService.getProcessNameFromToken(pRoleToken);
            lIsUsed = getEnvironmentDao().isUsed(lProcessName, pName);
        }
        return lIsUsed;
    }

    /**
     * Get the identifier of the environment from its name.
     * 
     * @param pRoleToken
     *            Role token
     * @param pName
     *            Name of the environment.
     * @return Identifier of the environment, Blank string if not found (or name
     *         is blank)
     */
    public String getEnvironmentId(final String pRoleToken, final String pName) {
        final String lId;
        if (StringUtils.isBlank(pName)) {
            lId = StringUtils.EMPTY;
        }
        else {
            String lProcessName =
                    authorizationService.getProcessNameFromToken(pRoleToken);
            lId = getEnvironmentDao().getId(lProcessName, pName);
        }
        return lId;
    }

    /**
     * Test the environment existence.
     * 
     * @param pRoleToken
     *            Role token
     * @param pName
     *            Name of the environment.
     * @return True if the environment exists, false otherwise (name is blank).
     */
    public Boolean isEnvironmentExists(final String pRoleToken,
            final String pName) {
        final Boolean lIsExists;
        if (StringUtils.isBlank(pName)) {
            lIsExists = Boolean.FALSE;
        }
        else {
            String lProcessName =
                    authorizationService.getProcessNameFromToken(pRoleToken);
            lIsExists = getEnvironmentDao().isExists(lProcessName, pName);
        }
        return lIsExists;
    }

    /**
     * Update the environment dao object.
     * <p>
     * Use the technical identifier to retrieve the old environment.
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pEnvironment
     *            Environment to update.
     * @throws InvalidIdentifierException
     *             If the old environment cannot be loaded.
     * @throws UndeletableElementException
     *             The environment is used by a product.
     * @throws AuthorizationException
     *             No right to update the environment.
     */
    public void updateEnvironment(final String pRoleToken,
            final EnvironmentData pEnvironment) {
        if (!isEnvironmentUpdatable(pRoleToken, pEnvironment.getLabelKey(),
                pEnvironment.isIsPublic())) {
            throw new AuthorizationException(
                    "Unsufficient rights to modify environment.");
        }

        final Environment lDaoEnvironment;
        try {
            lDaoEnvironment = getEnvironmentDao().load(pEnvironment.getId());
            if (isEnvironmentUsed(pRoleToken, pEnvironment.getLabelKey())) {
                throw new UndeletableElementException(
                        pEnvironment.getLabelKey());
            }
            lDaoEnvironment.setName(pEnvironment.getLabelKey());
            lDaoEnvironment.setIsPublic(pEnvironment.isIsPublic());
        }
        catch (HibernateObjectRetrievalFailureException e) {
            throw new InvalidIdentifierException(pEnvironment.getId());
        }
    }

    /**
     * Get the environment using its technical identifier.
     * 
     * @param pRoleToken
     *            Role token
     * @param pId
     *            Technical identifier of the environment.
     * @return Environment with its categories values or null if not found.
     */
    public EnvironmentData getEnvironmentById(final String pRoleToken,
            final String pId) {
        final String lProcessName =
                authorizationService.getProcessName(pRoleToken);

        try {
            final Environment lEnv = getEnvironmentDao().load(pId);
            EnvironmentData lEnvironmentData = new EnvironmentData();

            lEnvironmentData.setId(lEnv.getId());
            lEnvironmentData.setBusinessProcess(lProcessName);
            lEnvironmentData.setLabelKey(lEnv.getName());

            Collection<CategoryValue> lCategoryValues =
                    lEnv.getCategoryValues();
            CategoryValueData[] lCategoryValueDataArray;
            lCategoryValueDataArray =
                    new CategoryValueData[lCategoryValues.size()];

            int i = 0;
            CategoryValueData lCategoryValueData = new CategoryValueData();

            for (CategoryValue lCatValue : lCategoryValues) {
                if (null != lCatValue) {
                    lCategoryValueData.setValue(lCatValue.getValue());
                }
                lCategoryValueDataArray[i] = lCategoryValueData;
                i++;
            }
            lEnvironmentData.setCategoryValueDatas(lCategoryValueDataArray);
            return lEnvironmentData;
        }
        catch (HibernateObjectRetrievalFailureException e) {
            return null;
        }
    }

    /**
     * Check category values.
     * 
     * @param CategoryData
     * @return throw DuplicateValueException
     */
    private void checkDuplicateCategoryValue(CategoryData pCategoryData) {
        // Temporally java Set use to check duplicated values
        Set<String> lValues = new HashSet<String>();
        int lIter;
        CategoryValueData[] lCategoryValueDatas =
                pCategoryData.getCategoryValueDatas();
        for (lIter = 0; lIter < lCategoryValueDatas.length; lIter++) {
            if (!lValues.add(lCategoryValueDatas[lIter].getValue())) {
                throw new DuplicateValueException(
                        lCategoryValueDatas[lIter].getValue());
            }
        }
    }
}
