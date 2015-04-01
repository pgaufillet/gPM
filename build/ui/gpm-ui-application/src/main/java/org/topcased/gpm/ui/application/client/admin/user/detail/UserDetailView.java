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

import org.topcased.gpm.ui.component.client.container.GpmValuesContainerPanel;
import org.topcased.gpm.ui.component.client.container.field.GpmListBox;
import org.topcased.gpm.ui.component.client.container.field.GpmPasswordBox;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.layout.GpmLayoutPanelWithMenu;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.util.validation.Validator;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for a set of user displayed on the user management space.
 * 
 * @author nveillet
 */
public abstract class UserDetailView extends GpmLayoutPanelWithMenu implements
        UserDetailDisplay {

    private final GpmTextBox<String> firstName;

    private final GpmListBox language;

    private final GpmTextBox<String> mail;

    private final GpmTextBox<String> name;

    private final GpmPasswordBox password;

    private final GpmPasswordBox passwordBis;

    private String title;

    private GpmMenuTitle userTitle;

    private final Validator validator;

    private GpmValuesContainerPanel valuesContainerPanel;
    
    private static final int NAME_FIELD_SIZE = 100;
    
    private static final int FIRSTNAME_FIELD_SIZE = 100;
    
    private static final int MAIL_FIELD_SIZE = 100;

    /**
     * Create a detail view for users.
     */
    public UserDetailView() {
        super(true);
        userTitle = new GpmMenuTitle(true);
        getMenu().addTitle(userTitle);

        valuesContainerPanel = new GpmValuesContainerPanel();
        setContent(new ScrollPanel(valuesContainerPanel));

        // Validator is initialized here and filled in subclasses
        validator = new Validator();

        name = new GpmTextBox<String>(GpmStringFormatter.getInstance());
        name.setFieldName(CONSTANTS.adminUserFieldName());
        name.setTranslatedFieldName(CONSTANTS.adminUserFieldName());
        name.setSize(NAME_FIELD_SIZE);

        firstName = new GpmTextBox<String>(GpmStringFormatter.getInstance());
        firstName.setFieldName(CONSTANTS.adminUserFieldFirstName());
        firstName.setTranslatedFieldName(CONSTANTS.adminUserFieldFirstName());
        firstName.setSize(FIRSTNAME_FIELD_SIZE);

        mail = new GpmTextBox<String>(GpmStringFormatter.getInstance());
        mail.setFieldName(CONSTANTS.adminUserFieldMail());
        mail.setTranslatedFieldName(CONSTANTS.adminUserFieldMail());
        mail.setSize(MAIL_FIELD_SIZE);

        language = new GpmListBox();
        language.setFieldName(CONSTANTS.adminUserFieldLanguage());
        language.setTranslatedFieldName(CONSTANTS.adminUserFieldLanguage());

        password = new GpmPasswordBox();
        password.setFieldName(CONSTANTS.adminUserFieldPassword());
        password.setTranslatedFieldName(CONSTANTS.adminUserFieldPassword());

        passwordBis = new GpmPasswordBox();
        passwordBis.setFieldName(CONSTANTS.adminUserFieldPasswordBis());
        passwordBis.setTranslatedFieldName(CONSTANTS.adminUserFieldPasswordBis());
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    @Override
    public void clear() {
        setTitle(GpmMenuTitle.EMPTY);
        name.set(null);
        firstName.set(null);
        mail.set(null);
        language.setCategoryValue(null);
        password.set(null);
        passwordBis.set(null);
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
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserDetailDisplay#getFirstName()
     */
    @Override
    public GpmTextBox<String> getFirstName() {
        return firstName;
    }

    /**
     * Get the values container panel.
     * 
     * @return The values container panel.
     */
    protected GpmValuesContainerPanel getGpmValuesContainerPanel() {
        return valuesContainerPanel;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserDetailDisplay#getLanguage()
     */
    @Override
    public GpmListBox getLanguage() {
        return language;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserCreationDetailDisplay#getMail()
     */
    @Override
    public GpmTextBox<String> getMail() {
        return mail;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserCreationDetailDisplay#getName()
     */
    @Override
    public GpmTextBox<String> getName() {
        return name;
    }

    /**
     * get password
     * 
     * @return the password
     */
    public GpmPasswordBox getPassword() {
        return password;
    }

    /**
     * get passwordBis
     * 
     * @return the passwordBis
     */
    protected GpmPasswordBox getPasswordBis() {
        return passwordBis;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.UIObject#getTitle()
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * get validator
     * 
     * @return the validator
     */
    protected Validator getValidator() {
        return validator;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.GpmLayoutPanelWithMenu#setContent(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public void setContent(Widget pPanel) {
        super.setContent(pPanel);
        pPanel.setStylePrimaryName(ComponentResources.INSTANCE.css().gpmTabLayoutPanelContent());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailDisplay#setLogin(java.lang.String)
     */
    @Override
    public void setTitle(String pTitle) {
        title = pTitle;
        userTitle.setHTML(title);
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

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserDetailDisplay#validate()
     */
    @Override
    public String validate() {
        return validator.validate();
    }
}