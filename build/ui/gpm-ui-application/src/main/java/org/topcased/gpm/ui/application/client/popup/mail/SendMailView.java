/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.mail;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.util.ChoiceDisplayHintType;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.client.util.validation.EmailRule;
import org.topcased.gpm.ui.application.client.util.validation.MandatoryRule;
import org.topcased.gpm.ui.component.client.container.GpmFieldGridPanel;
import org.topcased.gpm.ui.component.client.container.GpmFormPanel;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmDropDownListBox;
import org.topcased.gpm.ui.component.client.container.field.GpmLabel;
import org.topcased.gpm.ui.component.client.container.field.GpmMultilineBox;
import org.topcased.gpm.ui.component.client.container.field.GpmRadioBox;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedField;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.util.validation.IRule;
import org.topcased.gpm.ui.component.client.util.validation.Validator;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for send sheet(s) by mail.
 * 
 * @author tpanuel
 */
public class SendMailView extends PopupView implements SendMailDisplay {

    private final static double RATIO_WIDTH = 0.5;

    private final static double RATIO_HEIGHT = 0.6;

    private final static int NB_FIELDS = 5;

    private final GpmFormPanel form;

    private final GpmMultivaluedField<GpmDropDownListBox> to;

    private final GpmTextBox<String> subject;

    private final GpmMultilineBox body;

    private final GpmMultivaluedField<GpmLabel<String>> attached;

    private final GpmRadioBox reportModel;

    private final Button sendButton;

    private final Validator validator;

    private HandlerRegistration sendButtonRegistration;

    /**
     * Create a send sheet(s) by mail view.
     */
    public SendMailView() {
        super("");
        validator = new Validator();
        final ScrollPanel lPanel = new ScrollPanel();

        // Build form
        form = new GpmFormPanel(NB_FIELDS);
        form.addStyleName(INSTANCE.css().gpmBigBorder());

        // Destination field
        to =
                new GpmMultivaluedField<GpmDropDownListBox>(
                        new GpmDropDownListBox(), true, true, true);
        to.setTranslatedFieldName(CONSTANTS.emailTo());
        validator.addValidation(to, Arrays.asList(new EmailRule(),
                new MandatoryRule()));
        form.addField(CONSTANTS.emailTo() + GpmFieldGridPanel.MANDATORY_LABEL,
                to.getPanel());

        // Subject field
        subject = new GpmTextBox<String>(GpmStringFormatter.getInstance());
        subject.setFieldName(CONSTANTS.emailSubject());
        subject.setTranslatedFieldName(CONSTANTS.emailSubject());
        validator.addValidation(subject,
                CollectionUtil.singleton((IRule) new MandatoryRule()));
        form.addField(CONSTANTS.emailSubject()
                + GpmFieldGridPanel.MANDATORY_LABEL, subject.getPanel());

        // Body field
        body = new GpmMultilineBox();
        body.setFieldName(CONSTANTS.emailBody());
        body.setTranslatedFieldName(CONSTANTS.emailBody());
        validator.addValidation(body,
                CollectionUtil.singleton((IRule) new MandatoryRule()));
        form.addField(
                CONSTANTS.emailBody() + GpmFieldGridPanel.MANDATORY_LABEL,
                body.getPanel());

        // Attached fields
        attached =
                new GpmMultivaluedField<GpmLabel<String>>(new GpmLabel<String>(
                        GpmStringFormatter.getInstance()), false, false, false);
        form.addField(MESSAGES.emailAttachedSheets(), attached.getWidget());

        // Report model
        reportModel = new GpmRadioBox(ChoiceDisplayHintType.LIST);
        form.addField(CONSTANTS.emailReportModel(), reportModel.getWidget());

        lPanel.addStyleName(INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(form);

        sendButton = addButton(CONSTANTS.emailSendButton());

        setContent(lPanel);

        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.mail.SendMailDisplay#setSendButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setSendButtonHandler(final ClickHandler pHandler) {
        // Remove old click handler : if exist
        if (sendButtonRegistration != null) {
            sendButtonRegistration.removeHandler();
        }
        sendButtonRegistration = sendButton.addClickHandler(pHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    @Override
    public void clear() {
        to.clear();
        subject.clear();
        body.clear();
        attached.clear();
        reportModel.getWidget().clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.mail.SendMailDisplay#setAvailableMailAddress(java.util.List)
     */
    @Override
    public void setAvailableMailAddress(final List<String> pAvailableMailAddress) {
        to.getTemplateField().setPossibleValues(
                GpmChoiceBoxValue.buildFromStrings(pAvailableMailAddress));
        // Recreate the first line
        to.removeLine();
        to.addLine();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.mail.SendMailDisplay#setAttachedSheetReferences(java.util.List)
     */
    @Override
    public void setAttachedSheetReferences(
            final List<String> pAttachedSheetReferences) {
        final int lSheetCount = pAttachedSheetReferences.size();
        for (int i = 0; i < lSheetCount; i++) {
            attached.addLine().set(pAttachedSheetReferences.get(i));
        }
        setHeaderText(MESSAGES.emailTitle(lSheetCount));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.mail.SendMailDisplay#setReportModel(java.util.List)
     */
    @Override
    public void setReportModel(final List<Translation> pReportModel) {
        reportModel.setPossibleValues(GpmChoiceBoxValue.buildFromTranslations(pReportModel));
        // Initialize the selection to the fist element
        if (!pReportModel.isEmpty()) {
            reportModel.setCategoryValue(pReportModel.get(0).getValue());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.mail.SendMailDisplay#getDestination()
     */
    @Override
    public List<String> getDestination() {
        final List<String> lDestinations = new ArrayList<String>();

        for (BusinessSimpleField<?> lDestination : to) {
            String lDestinationValue = lDestination.getAsString();
            if (lDestinationValue != null) {
                lDestinations.add(lDestination.getAsString());
            }
        }

        return lDestinations;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.mail.SendMailDisplay#getSubject()
     */
    @Override
    public String getSubject() {
        return subject.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.mail.SendMailDisplay#getBody()
     */
    @Override
    public String getBody() {
        return body.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.mail.SendMailDisplay#getReportModel()
     */
    @Override
    public String getReportModel() {
        return reportModel.getCategoryValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentCreationDetailDisplay#validate()
     */
    @Override
    public String validate() {
        return validator.validate();
    }
}