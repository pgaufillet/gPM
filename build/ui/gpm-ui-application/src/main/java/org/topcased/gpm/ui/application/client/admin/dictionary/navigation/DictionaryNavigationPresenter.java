/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary.navigation;

import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenDictionaryCommand;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenEnvironmentOnEditionCommand;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.shared.command.authorization.OpenAdminModuleResult;

import com.google.inject.Inject;

/**
 * The presenter for the DictionaryNavigationView.
 * 
 * @author nveillet
 */
public class DictionaryNavigationPresenter extends
        AbstractPresenter<DictionaryNavigationDisplay> {

    private final OpenDictionaryCommand openDictionary;

    private final OpenEnvironmentOnEditionCommand openEnvironment;

    private boolean dictionaryUpdatable;

    /**
     * Create a presenter for the DictionaryNavigationView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pOpenDictionary
     *            The command for open a dictionary
     * @param pOpenEnvironment
     *            The command for open a environment
     */
    @Inject
    public DictionaryNavigationPresenter(DictionaryNavigationDisplay pDisplay,
            EventBus pEventBus, OpenDictionaryCommand pOpenDictionary,
            OpenEnvironmentOnEditionCommand pOpenEnvironment) {
        super(pDisplay, pEventBus);
        openDictionary = pOpenDictionary;
        openEnvironment = pOpenEnvironment;
    }

    /**
     * Clear view
     */
    public void clear() {
        getDisplay().clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        if (dictionaryUpdatable) {
            getDisplay().setDictionaryButtonHandler(openDictionary);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        // Nothing to unbind
    }

    /**
     * Set the list of environments
     * 
     * @param pEnvironments
     *            the environments
     */
    public void setEnvironments(List<String> pEnvironments) {
        for (final String lEnvironment : pEnvironments) {
            getDisplay().addEnvironment(lEnvironment, openEnvironment);
        }
    }

    /**
     * Initialize the view
     * 
     * @param pResult
     *            the result
     */
    public void init(OpenAdminModuleResult pResult) {
        dictionaryUpdatable =
                pResult.getDictionaryActions().containsKey(
                        ActionName.DICTIONARY_EDITION);
    }
}
