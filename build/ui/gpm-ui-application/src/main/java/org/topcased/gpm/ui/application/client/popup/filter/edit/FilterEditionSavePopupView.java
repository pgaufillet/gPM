/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.filter.edit;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.LinkedList;
import java.util.List;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.FieldCreationHandler;
import org.topcased.gpm.ui.component.client.container.GpmFieldGridPanel;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmCheckBox;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmListBox;
import org.topcased.gpm.ui.component.client.container.field.GpmMultilineBox;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * View for saving a filter on a popup.
 * 
 * @author jlouisy
 */
public class FilterEditionSavePopupView extends PopupView implements
        FilterEditionSavePopupDisplay {

    private GpmFieldGridPanel savePanel;

    public static final String SAVE_NAME_FIELD =
            CONSTANTS.filterEditionFilterName();

    public static final String SAVE_DESCRIPTION_FIELD =
            CONSTANTS.filterEditionFilterDescription();

    public static final String SAVE_USAGE_FIELD =
            CONSTANTS.filterEditionFilterUsage();

    public static final String SAVE_VISIBILITY_FIELD =
            CONSTANTS.filterEditionFilterVisibility();

    public static final String SAVE_HIDDEN_FIELD =
            CONSTANTS.filterEditionFilterHidden();;

    public static final int DESCRIPTION_LINES = 6;

    private Button saveButton;

    private HandlerRegistration saveButtonHandler;

    private GpmTextBox<String> nameField;

    private GpmMultilineBox descriptionField;

    private GpmListBox usageField;

    private GpmListBox visibilityField;

    private GpmCheckBox hiddenField;

    private LinkedList<AbstractGpmField<?>> fieldList;
    
    private static final int NAME_FIELD_SIZE = 255;
    
    private static final String DESCRIPTION_FIELD_SIZE = "300";

    /**
     * Create a product selection view.
     */
    public FilterEditionSavePopupView() {
        super();
        setHeaderText(CONSTANTS.filterEditionSaveTitle());

        fieldList = new LinkedList<AbstractGpmField<?>>();

        nameField = new GpmTextBox<String>(GpmStringFormatter.getInstance());
        nameField.setTranslatedFieldName(SAVE_NAME_FIELD);
        nameField.setFieldDescription(CONSTANTS.filterEditionFilterNameTooltip());
        nameField.setSize(NAME_FIELD_SIZE);

        descriptionField = new GpmMultilineBox(DESCRIPTION_FIELD_SIZE);
        descriptionField.setTranslatedFieldName(SAVE_DESCRIPTION_FIELD);
        descriptionField.setFieldDescription(CONSTANTS.filterEditionFilterDescriptionTooltip());
        descriptionField.getWidget().setVisibleLines(DESCRIPTION_LINES);
               
        usageField = new GpmListBox();
        usageField.setTranslatedFieldName(SAVE_USAGE_FIELD);
        usageField.setFieldDescription(CONSTANTS.filterEditionFilterUsageTooltip());

        visibilityField = new GpmListBox();
        visibilityField.setTranslatedFieldName(SAVE_VISIBILITY_FIELD);
        visibilityField.setFieldDescription(CONSTANTS.filterEditionFilterVisibilityTooltip());

        hiddenField = new GpmCheckBox(true);
        hiddenField.setTranslatedFieldName(SAVE_HIDDEN_FIELD);
        hiddenField.setFieldDescription(CONSTANTS.filterEditionFilterHiddenTooltip());
        
        fieldList.add(nameField);
        fieldList.add(descriptionField);
        fieldList.add(usageField);
        fieldList.add(visibilityField);

        savePanel = new GpmFieldGridPanel();
        savePanel.setFieldCreationHandler(new FieldCreationHandler() {

            @Override
            public List<AbstractGpmField<?>> createFields() {
                return fieldList;
            }

        });
        saveButton = addButton(CONSTANTS.save());

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#display()
     */
    @Override
    public void display() {
        SimplePanel lPanel = new SimplePanel();
        lPanel.setStyleName(ComponentResources.INSTANCE.css().gpmSmallBorder());
        savePanel.ensureWidget();
        lPanel.add(savePanel.getWidget());
        setContent(lPanel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#getDescription()
     */
    @Override
    public String getDescription() {
        return descriptionField.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#getHidden()
     */
    @Override
    public Boolean getHidden() {
        if (fieldList.contains(hiddenField)) {
            return hiddenField.get();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#getName()
     */
    @Override
    public String getName() {
        return nameField.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#getNameField()
     */
    @Override
    public GpmTextBox<String> getNameField() {
        return nameField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#getUsage()
     */
    @Override
    public String getUsage() {
        return usageField.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#getVisibility()
     */
    @Override
    public String getVisibility() {
        return visibilityField.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String pDescription) {
        descriptionField.set(pDescription);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#setHidden(java.lang.Boolean)
     */
    @Override
    public void setHidden(Boolean pIsHidden) {
        if (pIsHidden == null) {
            fieldList.remove(hiddenField);
        }
        else {
            hiddenField.set(pIsHidden);
            if (!fieldList.contains(hiddenField)) {
                fieldList.add(hiddenField);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#setName(java.lang.String)
     */
    @Override
    public void setName(String pName) {
        nameField.set(pName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#setSaveButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setSaveButtonHandler(ClickHandler pClickHandler, boolean pIsEnabled) {
        if (saveButtonHandler != null) {
            saveButtonHandler.removeHandler();
        }
        saveButtonHandler = saveButton.addClickHandler(pClickHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#setUsage(java.lang.String,
     *      java.util.List)
     */
    @Override
    public void setUsage(String pUsage, List<Translation> pUsageList) {
        usageField.setPossibleValues(GpmChoiceBoxValue.buildFromTranslations(pUsageList));
        usageField.set(pUsage);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionSavePopupDisplay#setVisibility(java.lang.String,
     *      java.util.List)
     */
    @Override
    public void setVisibility(String pVisibility,
            List<Translation> pVisibilityList) {
        visibilityField.setPossibleValues(GpmChoiceBoxValue.buildFromTranslations(pVisibilityList));
        visibilityField.set(pVisibility);
    }

}