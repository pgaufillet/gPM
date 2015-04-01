/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.user.detail;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.client.util.validation.EmailRule;
import org.topcased.gpm.ui.application.client.util.validation.MandatoryRule;
import org.topcased.gpm.ui.application.client.util.validation.PasswordRule;
import org.topcased.gpm.ui.component.client.container.GpmDisclosurePanel;
import org.topcased.gpm.ui.component.client.container.GpmFieldGridPanel;
import org.topcased.gpm.ui.component.client.container.GpmFormPanel;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.util.validation.IRule;

/**
 * View for a set of user displayed on the user management space.
 * 
 * @author nveillet
 */
public class UserCreationDetailView extends UserDetailView implements
        UserCreationDetailDisplay {

    private static final int FIELD_COUNT = 5;

    private final GpmTextBox<String> login;
    
    private static final int LOGIN_FIELD_SIZE = 100;

    /**
     * Create a detail view for users.
     */
    public UserCreationDetailView() {
        super();

        setTitle("* " + CONSTANTS.user());

        /**
         * PROPERTIES
         */
        GpmDisclosurePanel lPropertiesGroup = new GpmDisclosurePanel();
        GpmFormPanel lForm = new GpmFormPanel(FIELD_COUNT);

        login = new GpmTextBox<String>(GpmStringFormatter.getInstance());
        login.setFieldName(CONSTANTS.adminUserFieldLogin());
        lForm.addField(
                login.getFieldName() + GpmFieldGridPanel.MANDATORY_LABEL,
                login.getWidget());
        getValidator().addValidation(login,
                CollectionUtil.singleton((IRule) new MandatoryRule()));
        login.setSize(LOGIN_FIELD_SIZE);

        lForm.addField(getName().getFieldName()
                + GpmFieldGridPanel.MANDATORY_LABEL, getName().getWidget());
        getValidator().addValidation(getName(),
                CollectionUtil.singleton((IRule) new MandatoryRule()));

        lForm.addField(getFirstName().getFieldName(),
                getFirstName().getWidget());

        lForm.addField(getMail().getFieldName(), getMail().getWidget());
        getValidator().addValidation(getMail(),
                CollectionUtil.singleton((IRule) new EmailRule()));

        lForm.addField(getLanguage().getFieldName(), getLanguage().getWidget());

        // Initialize the group
        lPropertiesGroup.setButtonText(CONSTANTS.adminUserGroupProperties());
        lPropertiesGroup.setContent(lForm);
        lPropertiesGroup.open();

        // Add the group on the values container form
        getGpmValuesContainerPanel().add(lPropertiesGroup);

        /**
         * PASSWORD
         */
        lPropertiesGroup = new GpmDisclosurePanel();
        lForm = new GpmFormPanel(2);

        getPassword().setFieldName(CONSTANTS.adminUserFieldPassword());
        lForm.addField(getPassword().getFieldName()
                + GpmFieldGridPanel.MANDATORY_LABEL, getPassword().getWidget());
        getValidator().addValidation(getPassword(),
                CollectionUtil.singleton((IRule) new MandatoryRule()));

        getPasswordBis().setFieldName(CONSTANTS.adminUserFieldPasswordBis());
        lForm.addField(getPasswordBis().getFieldName(),
                getPasswordBis().getWidget());
        List<IRule> lRules = new ArrayList<IRule>();
        lRules.add((IRule) new MandatoryRule());
        lRules.add((IRule) new PasswordRule(getPassword()));
        getValidator().addValidation(getPasswordBis(), lRules);

        // Initialize the group
        lPropertiesGroup.setButtonText(CONSTANTS.adminUserGroupPassword());
        lPropertiesGroup.setContent(lForm);
        lPropertiesGroup.open();

        // Add the group on the values container form
        getGpmValuesContainerPanel().add(lPropertiesGroup);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    @Override
    public void clear() {
        super.clear();
        setTitle("* " + CONSTANTS.user());
        login.set(null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doMaximize()
     */
    @Override
    public void doMaximize() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doMinimize()
     */
    @Override
    public void doMinimize() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doRestore()
     */
    @Override
    public void doRestore() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserCreationDetailDisplay#getLogin()
     */
    @Override
    public String getLogin() {
        return login.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailDisplay#setToolBar(org.topcased.gpm.ui.component.client.menu.GpmToolBar)
     */
    @Override
    public void setToolBar(GpmToolBar pToolBar) {
        getMenu().setToolBar(pToolBar);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#startProcessing()
     */
    @Override
    public void startProcessing() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#stopProcessing()
     */
    @Override
    public void stopProcessing() {
        // Nothing to do
    }
}