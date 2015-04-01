/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary.detail;

import java.util.HashMap;
import java.util.Map;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.client.menu.admin.dictionary.EnvironmentCreationMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.authorization.OpenAdminModuleResult;
import org.topcased.gpm.ui.facade.shared.action.UiAction;

import com.google.inject.Inject;

/**
 * The presenter for the EnvironmentCreationDetailView.
 * 
 * @author nveillet
 */
public class EnvironmentCreationDetailPresenter extends
        AbstractPresenter<EnvironmentCreationDetailDisplay> {

    private final EnvironmentCreationMenuBuilder menuBuilder;

    /**
     * Create a presenter for the EnvironmentCreationDetailView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pMenuBuilder
     *            The menu builder.
     */
    @Inject
    public EnvironmentCreationDetailPresenter(
            EnvironmentCreationDetailDisplay pDisplay, EventBus pEventBus,
            final EnvironmentCreationMenuBuilder pMenuBuilder) {
        super(pDisplay, pEventBus);
        menuBuilder = pMenuBuilder;
    }

    /**
     * Clear view
     */
    public void clear() {
        getDisplay().clear();
    }

    /**
     * get the environment name
     * 
     * @return the name
     */
    public String getEnvironmentName() {
        return getDisplay().getName().get();
    }

    /**
     * get the environment public access
     * 
     * @return the environment public access
     */
    public boolean getEnvironmentPublic() {
        return getDisplay().getPublic().get();
    }

    /**
     * Initialize the details.
     * 
     * @param pResult
     *            the result
     */
    public void init(OpenAdminModuleResult pResult) {
        getDisplay().getPublic().setEnabled(
                pResult.isPrivateEnvironmentCreationAccess());
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        Map<String, UiAction> lActions = new HashMap<String, UiAction>();
        lActions.put(ActionName.ENVIRONMENT_SAVE, new UiAction(
                ActionName.ENVIRONMENT_SAVE));
        getDisplay().setToolBar(menuBuilder.buildMenu(lActions, null));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        // Do nothing
    }

    /**
     * Validate view
     * 
     * @return the validation message
     */
    public String validate() {
        return getDisplay().validate();
    }
}
