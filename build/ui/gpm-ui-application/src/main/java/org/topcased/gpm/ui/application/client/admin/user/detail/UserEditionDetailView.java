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

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.values.field.simple.BusinessBooleanField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.client.util.validation.EmailRule;
import org.topcased.gpm.ui.application.client.util.validation.MandatoryRule;
import org.topcased.gpm.ui.application.client.util.validation.PasswordRule;
import org.topcased.gpm.ui.component.client.container.GpmDisclosurePanel;
import org.topcased.gpm.ui.component.client.container.GpmFieldGridPanel;
import org.topcased.gpm.ui.component.client.container.GpmFormPanel;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmCheckBox;
import org.topcased.gpm.ui.component.client.container.field.GpmLabel;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultipleMultivaluedField;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.field.multivalued.GpmMultipleMultivaluedElement;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.util.validation.IRule;

import com.google.gwt.user.client.ui.SimplePanel;

/**
 * View for a set of user displayed on the user management space.
 * 
 * @author nveillet
 */
public class UserEditionDetailView extends UserDetailView implements
        UserEditionDetailDisplay {

    private static final int FIELD_COUNT = 4;

    private GpmMultipleMultivaluedField affectation;

    private SimplePanel affectationPanel;

    private GpmDisclosurePanel passwordDisclosurePanel;
    
    private GpmDisclosurePanel affectationDisclosurePanel;

    /**
     * Create a detail view for users.
     */
    public UserEditionDetailView() {
        super();

        /**
         * PROPERTIES
         */
        GpmDisclosurePanel lPropertiesGroup = new GpmDisclosurePanel();
        GpmFormPanel lForm = new GpmFormPanel(FIELD_COUNT);

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
        passwordDisclosurePanel = new GpmDisclosurePanel();
        lForm = new GpmFormPanel(2);

        getPassword().setFieldName(CONSTANTS.adminUserFieldNewPassword());
        lForm.addField(getPassword().getFieldName(), getPassword().getWidget());

        getPasswordBis().setFieldName(CONSTANTS.adminUserFieldNewPasswordBis());
        lForm.addField(getPasswordBis().getFieldName(),
                getPasswordBis().getWidget());
        getValidator().addValidation(
                getPasswordBis(),
                CollectionUtil.singleton((IRule) new PasswordRule(getPassword())));

        // Initialize the group
        passwordDisclosurePanel.setButtonText(CONSTANTS.adminUserGroupPassword());
        passwordDisclosurePanel.setContent(lForm);
        passwordDisclosurePanel.open();

        // Add the group on the values container form
        getGpmValuesContainerPanel().add(passwordDisclosurePanel);

        /**
         * AFFECTATION
         */
        affectationDisclosurePanel = new GpmDisclosurePanel();
        affectationPanel = new SimplePanel();

        // Initialize the group
        affectationDisclosurePanel.setButtonText(CONSTANTS.adminUserGroupAffectation());
        affectationDisclosurePanel.setContent(affectationPanel);
        affectationDisclosurePanel.open();

        // Add the group on the values container form
        getGpmValuesContainerPanel().add(affectationDisclosurePanel);
    }

    /**
     * Add a affectation row
     * 
     * @param pKey
     *            the row key
     * @param pRoleNames
     *            the role names
     */
    @SuppressWarnings("unchecked")
    private void addAffectation(String pKey, List<String> pRoleNames) {
        GpmMultipleMultivaluedElement lLine = affectation.addLine();

        ((BusinessSimpleField<String>) lLine.getField(GpmMenuTitle.EMPTY)).set(pKey);
        ((AbstractGpmField<?>) lLine.getField(GpmMenuTitle.EMPTY)).getWidget().addStyleName(
                INSTANCE.css().gpmFieldLabel());

        for (String lRolename : pRoleNames) {
            ((BusinessBooleanField) lLine.getField(lRolename)).set(true);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailDisplay#addProcessAffectation(java.util.List)
     */
    @Override
    public void addProcessAffectation(List<String> pRoleNames) {
        if (!pRoleNames.isEmpty()) {
            addAffectation(CONSTANTS.process(), pRoleNames);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailDisplay#addProductAffectation(java.lang.String,
     *      java.util.List)
     */
    @Override
    public void addProductAffectation(String pProductName,
            List<String> pRoleNames) {
        addAffectation(pProductName, pRoleNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    @Override
    public void clear() {
        super.clear();
        initAffectation(new ArrayList<Translation>());
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
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserDetailDisplay#getLogin()
     */
    @Override
    public String getLogin() {
        return getTitle();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailDisplay#initAffectation(java.util.List)
     */
    @Override
    public void initAffectation(List<Translation> pRoles) {
        List<AbstractGpmField<?>> lFields =
                new ArrayList<AbstractGpmField<?>>(pRoles.size() + 1);

        GpmLabel<String> lProductField = new GpmLabel<String>(GpmStringFormatter.getInstance());
        lProductField.setFieldName(GpmMenuTitle.EMPTY);
        lFields.add(lProductField);

        for (Translation lRole : pRoles) {
            GpmCheckBox lRoleField = new GpmCheckBox(false);
            lRoleField.setFieldName(lRole.getValue());
            lRoleField.setTranslatedFieldName(lRole.getTranslatedValue());
            lFields.add(lRoleField);
        }

        affectation = new GpmMultipleMultivaluedField(lFields, false, false, false, true);

        // remove first line
        affectation.removeLine();

        affectationPanel.setWidget(affectation.getWidget());

        if (pRoles.isEmpty()) {
            affectationDisclosurePanel.setVisible(false);
        }
        else {
            affectationDisclosurePanel.setVisible(true);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailDisplay#setLogin(java.lang.String)
     */
    @Override
    public void setLogin(String pLogin) {
        setTitle(pLogin);
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

	@Override
	public void enableEdition(boolean pEnable) {
		getName().getWidget().setEnabled(pEnable);
		getFirstName().getWidget().setEnabled(pEnable);
		getMail().getWidget().setEnabled(pEnable);
		getLanguage().getWidget().setEnabled(pEnable);
		passwordDisclosurePanel.setVisible(pEnable);
	}
}