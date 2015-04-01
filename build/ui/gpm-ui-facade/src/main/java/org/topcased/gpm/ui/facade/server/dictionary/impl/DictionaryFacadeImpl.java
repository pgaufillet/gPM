/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.dictionary.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.dictionary.CategoryData;
import org.topcased.gpm.business.dictionary.CategoryValueData;
import org.topcased.gpm.business.dictionary.EnvironmentData;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.ui.facade.server.AbstractFacade;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.dictionary.DictionaryFacade;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;
import org.topcased.gpm.ui.facade.shared.dictionary.UiCategory;
import org.topcased.gpm.ui.facade.shared.dictionary.UiEnvironment;

/**
 * DictionnaryFacade
 * 
 * @author nveillet
 */
public class DictionaryFacadeImpl extends AbstractFacade implements
        DictionaryFacade {

    /**
     * Create a new Environment.
     * 
     * @param pSession
     *            Current session.
     * @param pEnvironmentName
     *            New environment name.
     * @param pIsPublic
     *            New environment visibility.
     */
    public void createEnvironment(UiSession pSession, String pEnvironmentName,
            boolean pIsPublic) {
        getEnvironmentService().createEnvironment(pSession.getRoleToken(),
                pSession.getParent().getProcessName(), pEnvironmentName,
                pIsPublic);
    }

    /**
     * Delete an environment.
     * 
     * @param pSession
     *            Current session.
     * @param pEnvironmentName
     *            Environment name.
     */
    public void deleteEnvironment(UiSession pSession, String pEnvironmentName) {
        getEnvironmentService().deleteEnvironment(pSession.getRoleToken(),
                pSession.getParent().getProcessName(), pEnvironmentName);
    }

    /**
     * Get all categories.
     * 
     * @param pSession
     *            Current session.
     * @return list of categories.
     */
    public List<UiCategory> getDictionaryCategories(UiSession pSession) {
        Collection<CategoryData> lCategories =
                getEnvironmentService().getModifiableCategories(
                        pSession.getRoleToken(),
                        pSession.getParent().getProcessName());

        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        List<UiCategory> lResultList = new ArrayList<UiCategory>();
        for (CategoryData lCategoryData : lCategories) {
            lResultList.add(new UiCategory(
                    lCategoryData.getId(),
                    lCategoryData.getLabelKey(),
                    lTranslationManager.getTextTranslation(lCategoryData.getLabelKey())));
        }
        Collections.sort(lResultList);
        return lResultList;
    }

    /**
     * Get dictionary category values.
     * 
     * @param pSession
     *            Current session.
     * @param pCategoryName
     *            Category name.
     * @return list of categories.
     */
    public List<String> getDictionaryCategoryValues(UiSession pSession,
            String pCategoryName) {
        List<String> lCategoryName = new ArrayList<String>();
        lCategoryName.add(pCategoryName);
        Map<String, List<CategoryValue>> lCategoryValues =
                getEnvironmentService().getCategoryValues(
                        pSession.getParent().getProcessName(), lCategoryName);
        List<String> lResultList = new ArrayList<String>();
        for (CategoryValue lCategoryValue : lCategoryValues.get(pCategoryName)) {
            lResultList.add(lCategoryValue.getValue());
        }
        //        Collections.sort(lResultList);
        return lResultList;
    }

    /**
     * Get environment.
     * 
     * @param pSession
     *            Current session.
     * @param pEnvironmentName
     *            Environment name.
     * @return the environment.
     */
    public UiEnvironment getEnvironment(UiSession pSession,
            String pEnvironmentName) {
        EnvironmentData lEnvironmentData =
                getEnvironmentService().getEnvironmentByName(
                        pSession.getRoleToken(),
                        pSession.getParent().getProcessName(), pEnvironmentName);
        return new UiEnvironment(lEnvironmentData.getLabelKey(),
                lEnvironmentData.isIsPublic());
    }

    /**
     * Get environment category values.
     * 
     * @param pSession
     *            Current session.
     * @param pEnvironmentName
     *            Environment name.
     * @param pCategoryName
     *            Category name.
     * @return list of categories.
     */
    public List<String> getEnvironmentCategoryValues(UiSession pSession,
            String pEnvironmentName, String pCategoryName) {
        List<String> lCategoryName = new ArrayList<String>();
        lCategoryName.add(pCategoryName);
        Map<String, List<CategoryValue>> lCategoryValues =
                getEnvironmentService().getCategoryValues(
                        pSession.getRoleToken(),
                        pSession.getParent().getProcessName(),
                        pEnvironmentName, lCategoryName);
        List<String> lResultList = new ArrayList<String>();
        if (lCategoryValues.get(pCategoryName) != null) {
            for (CategoryValue lCategoryValue : lCategoryValues.get(pCategoryName)) {
                lResultList.add(lCategoryValue.getValue());
            }
        }
        //        Collections.sort(lResultList);
        return lResultList;
    }

    /**
     * get the list of environments
     * 
     * @param pSession
     *            the session
     * @return the environments names
     */
    public List<String> getEnvironmentNames(UiSession pSession) {
        return new ArrayList<String>(
                getEnvironmentService().getEnvironmentNames(
                        pSession.getRoleToken(),
                        pSession.getParent().getProcessName()));
    }

    /**
     * Update dictionary category values
     * 
     * @param pSession
     *            Current session.
     * @param pCategoryName
     *            Category name.
     * @param pCategoryValues
     *            Category values.
     */
    public void updateDictionaryCategoryValues(UiSession pSession,
            String pCategoryName, List<String> pCategoryValues) {
        Collection<CategoryData> lCategories =
                getEnvironmentService().getCategories(pSession.getRoleToken(),
                        pSession.getParent().getProcessName());
        CategoryData lCategory = null;
        int i = 0;
        boolean lFound = false;
        Iterator<CategoryData> lIterator = lCategories.iterator();
        while (lIterator.hasNext() && !lFound) {
            lCategory = lIterator.next();
            if (pCategoryName.equals(lCategory.getLabelKey())) {
                lFound = true;
            }
            i++;
        }

        if (lCategory != null) {
            CategoryValueData[] lCategoryValueDatas =
                    new CategoryValueData[pCategoryValues.size()];
            i = 0;
            for (String lCategoryValue : pCategoryValues) {
                lCategoryValueDatas[i] = new CategoryValueData(lCategoryValue);
                i++;
            }
            lCategory.setCategoryValueDatas(lCategoryValueDatas);
            getEnvironmentService().setDictionaryCategory(
                    pSession.getRoleToken(), lCategory, true);
        }
    }

    /**
     * Update environment category values
     * 
     * @param pSession
     *            Current session.
     * @param pEnvironmentName
     *            Environment name.
     * @param pCategoryName
     *            Category name.
     * @param pCategoryValues
     *            Category values.
     */
    public void updateEnvironmentCategoryValues(UiSession pSession,
            String pEnvironmentName, String pCategoryName,
            List<String> pCategoryValues) {
        getEnvironmentService().setEnvironmentCategory(pSession.getRoleToken(),
                pSession.getParent().getProcessName(), pEnvironmentName,
                pCategoryName, pCategoryValues);
    }
}
