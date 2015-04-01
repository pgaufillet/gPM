/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.dictionary;

import java.util.List;

import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.dictionary.UiCategory;
import org.topcased.gpm.ui.facade.shared.dictionary.UiEnvironment;

/**
 * DictionnaryFacade
 * 
 * @author nveillet
 */
public interface DictionaryFacade {

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
            boolean pIsPublic);

    /**
     * Delete an environment.
     * 
     * @param pSession
     *            Current session.
     * @param pEnvironmentName
     *            Environment name.
     */
    public void deleteEnvironment(UiSession pSession, String pEnvironmentName);

    /**
     * Get all categories.
     * 
     * @param pSession
     *            Current session.
     * @return list of categories.
     */
    public List<UiCategory> getDictionaryCategories(UiSession pSession);

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
            String pCategoryName);

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
            String pEnvironmentName);

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
            String pEnvironmentName, String pCategoryName);

    /**
     * get the list of environments
     * 
     * @param pSession
     *            the session
     * @return the environments names
     */
    public List<String> getEnvironmentNames(UiSession pSession);

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
            String pCategoryName, List<String> pCategoryValues);

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
            List<String> pCategoryValues);
}
