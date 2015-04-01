/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien EBALLARD (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.userprofile;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.application.client.util.validation.EmailRule;
import org.topcased.gpm.ui.application.client.util.validation.MandatoryRule;
import org.topcased.gpm.ui.application.client.util.validation.PasswordRule;
import org.topcased.gpm.ui.component.client.container.GpmDisclosurePanel;
import org.topcased.gpm.ui.component.client.container.GpmFieldGridPanel;
import org.topcased.gpm.ui.component.client.container.GpmFormPanel;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmLabel;
import org.topcased.gpm.ui.component.client.container.field.GpmListBox;
import org.topcased.gpm.ui.component.client.container.field.GpmPasswordBox;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.util.validation.IRule;
import org.topcased.gpm.ui.component.client.util.validation.Validator;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * View for user affectation edition.
 * 
 * @author jeballar
 */
public class UserProfileView extends PopupView implements UserProfileDisplay {

    private static final int INFORMATIONS_FIELDS_COUNT = 4;

    private static final int PASSWORD_FIELDS_COUNT = 3;

    private String login;

    private final GpmTextBox<String> name;

    private final GpmLabel<String> nameView;

    private final GpmTextBox<String> firstName;

    private final GpmLabel<String> firstNameView;

    private final GpmTextBox<String> mail;

    private final GpmLabel<String> mailView;

    private final GpmListBox language;

    private final GpmLabel<String> languageView;

    private final GpmPasswordBox currentPassword;

    private final GpmPasswordBox newPassword;

    private final GpmPasswordBox confirmPassword;

    private final GpmDisclosurePanel informationsEditPanel;

    private final GpmDisclosurePanel informationsViewPanel;

    private final GpmDisclosurePanel passwordPanel;

    private final SimplePanel informationsPanel;

    private final Validator validator;

    private FlowPanel profilePanel;

    private final Button validateButton;

    private HandlerRegistration validateButtonRegistration;
    
    private static final int NAME_FIELD_SIZE = 100;
    
    private static final int FIRSTNAME_FIELD_SIZE = 100;
    
    private static final int MAIL_FIELD_SIZE = 100;

    /**
     * Create a select environment(s) view.
     */
    public UserProfileView() {
        super(CONSTANTS.userProfilePopupTitle());
        final ScrollPanel lPanel = new ScrollPanel();

        // Build panel
        profilePanel = new FlowPanel();
        profilePanel.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());

        lPanel.addStyleName(ComponentResources.INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(profilePanel);

        // Init fields
        name = new GpmTextBox<String>(GpmStringFormatter.getInstance());
        firstName = new GpmTextBox<String>(GpmStringFormatter.getInstance());
        mail = new GpmTextBox<String>(GpmStringFormatter.getInstance());
        currentPassword = new GpmPasswordBox();
        newPassword = new GpmPasswordBox();
        confirmPassword = new GpmPasswordBox();
        language = new GpmListBox();
        nameView = new GpmLabel<String>(GpmStringFormatter.getInstance());
        firstNameView = new GpmLabel<String>(GpmStringFormatter.getInstance());
        mailView = new GpmLabel<String>(GpmStringFormatter.getInstance());
        languageView = new GpmLabel<String>(GpmStringFormatter.getInstance());

        // informationPanel can contain informationsViewPanel or informationsEditPanel
        informationsPanel = new SimplePanel();
        profilePanel.add(informationsPanel);

        // Informations Edition Panel 
        informationsEditPanel = new GpmDisclosurePanel();
        GpmFormPanel lForm = new GpmFormPanel(INFORMATIONS_FIELDS_COUNT);
        informationsEditPanel.setContent(lForm);
        lForm.addField(CONSTANTS.name() + GpmFieldGridPanel.MANDATORY_LABEL,
                name.getPanel());
        lForm.addField(CONSTANTS.adminUserFieldFirstName(),
                firstName.getPanel());
        lForm.addField(CONSTANTS.adminUserFieldMail(), mail.getPanel());
        lForm.addField(CONSTANTS.adminUserFieldLanguage(), language.getPanel());
        informationsEditPanel.setButtonText(CONSTANTS.userProfilePopupGroupInformations());
        informationsEditPanel.open();

        // Informations View Panel
        informationsViewPanel = new GpmDisclosurePanel();
        lForm = new GpmFormPanel(INFORMATIONS_FIELDS_COUNT);
        informationsViewPanel.setContent(lForm);
        lForm.addField(CONSTANTS.name(), nameView.getPanel());
        lForm.addField(CONSTANTS.adminUserFieldFirstName(),
                firstNameView.getPanel());
        lForm.addField(CONSTANTS.adminUserFieldMail(), mailView.getPanel());
        lForm.addField(CONSTANTS.adminUserFieldLanguage(),
                languageView.getPanel());
        informationsViewPanel.setButtonText(CONSTANTS.userProfilePopupGroupInformations());
        informationsViewPanel.open();

        // Password panel
        passwordPanel = new GpmDisclosurePanel();
        lForm = new GpmFormPanel(PASSWORD_FIELDS_COUNT);
        passwordPanel.setContent(lForm);
        lForm.addField(CONSTANTS.adminUserFieldPassword(),
                currentPassword.getPanel());
        lForm.addField(CONSTANTS.adminUserFieldNewPassword(),
                newPassword.getPanel());
        lForm.addField(CONSTANTS.adminUserFieldNewPasswordBis(),
                confirmPassword.getPanel());
        passwordPanel.setButtonText(CONSTANTS.userProfileGroupPassword());
        passwordPanel.open();
        profilePanel.add(passwordPanel);

        validateButton = addButton(CONSTANTS.save());

        // Add titles to buttons under validation to be displayed in validation messages
        name.setTranslatedFieldName(CONSTANTS.name());
        mail.setTranslatedFieldName(CONSTANTS.adminUserFieldMail());
        newPassword.setTranslatedFieldName(CONSTANTS.adminUserFieldNewPassword());
        confirmPassword.setTranslatedFieldName(CONSTANTS.adminUserFieldNewPasswordBis());
        
        // Set field size
        name.setSize(NAME_FIELD_SIZE);
        firstName.setSize(FIRSTNAME_FIELD_SIZE);
        mail.setSize(MAIL_FIELD_SIZE);

        // Field Rules
        validator = new Validator();

        validator.addValidation(
                name,
                (List<IRule>) Collections.singletonList((IRule) new MandatoryRule()));
        validator.addValidation(
                mail,
                (List<IRule>) Collections.singletonList((IRule) new EmailRule()));
        validator.addValidation(
                confirmPassword,
                (List<IRule>) Collections.singletonList((IRule) new PasswordRule(
                        newPassword)));
        setContent(lPanel);

        // Add Key Handler on ENTER key : Quick Submit
        KeyPressHandler lEnterKeyPressHandler = new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent pEvent) {
                if (KeyCodes.KEY_ENTER == pEvent.getCharCode()) {
                    ((Button) validateButton).click();
                }
            }
        };

        name.getWidget().addKeyPressHandler(lEnterKeyPressHandler);
        firstName.getWidget().addKeyPressHandler(lEnterKeyPressHandler);
        mail.getWidget().addKeyPressHandler(lEnterKeyPressHandler);
        currentPassword.getWidget().addKeyPressHandler(lEnterKeyPressHandler);
        newPassword.getWidget().addKeyPressHandler(lEnterKeyPressHandler);
        confirmPassword.getWidget().addKeyPressHandler(lEnterKeyPressHandler);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.user.UserAffectationDisplay#setSaveButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setValidateButtonHandler(ClickHandler pHandler) {
        // Remove old click handler : if exist
        if (validateButtonRegistration != null) {
            validateButtonRegistration.removeHandler();
        }
        validateButtonRegistration = validateButton.addClickHandler(pHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.userprofile.UserProfileDisplay#getUser()
     */
    @Override
    public UiUser getUser() {
        UiUser lUser = new UiUser();
        lUser.setLogin(login);
        lUser.setName(name.get());
        lUser.setEmailAdress(mail.get());
        lUser.setForename(firstName.get());
        lUser.setLanguage(language.get());
        lUser.setPassWord(currentPassword.get());
        lUser.setNewPassword(newPassword.get());
        return lUser;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.userprofile.UserProfileDisplay#setUser(org.topcased.gpm.ui.facade.shared.administration.user.UiUser)
     */
    @Override
    public void setUser(UiUser pUser) {
        name.set(pUser.getName());
        firstName.set(pUser.getForename());
        mail.set(pUser.getEmailAdress());
        language.set(pUser.getLanguage());

        nameView.set(pUser.getName());
        firstNameView.set(pUser.getForename());
        mailView.set(pUser.getEmailAdress());
        languageView.set(pUser.getLanguage());

        setLogin(pUser.getLogin());
    }

    /**
     * Set the current login (displayed in popup header text)
     * 
     * @param pLogin
     *            the new login
     */
    private void setLogin(String pLogin) {
        login = pLogin;
        setHeaderText(CONSTANTS.userProfilePopupTitle() + " - " + pLogin);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.userprofile.UserProfileDisplay#setEditable(boolean)
     */
    @Override
    public void setEditable(boolean pEditable) {
        if (pEditable) {
            name.setEnabled(true);
            firstName.setEnabled(true);
            mail.setEnabled(true);
            language.setEnabled(true);
            currentPassword.setEnabled(true);
            newPassword.setEnabled(true);
            confirmPassword.setEnabled(true);
            validateButton.setText(CONSTANTS.save());

            informationsPanel.clear();
            informationsPanel.setWidget(informationsEditPanel);
        }
        else {
            name.setEnabled(false);
            firstName.setEnabled(false);
            mail.setEnabled(false);
            language.setEnabled(false);
            currentPassword.setEnabled(false);
            newPassword.setEnabled(false);
            confirmPassword.setEnabled(false);
            validateButton.setText(CONSTANTS.ok());

            informationsPanel.clear();
            informationsPanel.setWidget(informationsViewPanel);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.userprofile.UserProfileDisplay#setAvailableLanguages(java.util.List)
     */
    @Override
    public void setAvailableLanguages(List<String> pLanguages) {
        language.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(new ArrayList<String>(
                pLanguages)));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.userprofile.UserProfileDisplay#reset()
     */
    @Override
    public void reset() {
        name.clear();
        firstName.clear();
        mail.clear();
        language.clear();
        currentPassword.clear();
        newPassword.clear();
        confirmPassword.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.userprofile.UserProfileDisplay#showPasswordArea(boolean)
     */
    public void showPasswordArea(boolean pEditablePassword) {
        passwordPanel.setVisible(pEditablePassword);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.userprofile.UserProfileDisplay#validate()
     */
    @Override
    public String validate() {
        return validator.validate();
    }
}
