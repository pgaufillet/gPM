/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.user;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessBooleanField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmCheckBox;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmLabel;
import org.topcased.gpm.ui.component.client.container.field.GpmListBox;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultipleMultivaluedField;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.field.multivalued.GpmMultipleMultivaluedElement;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * View for user affectation edition.
 * 
 * @author nveillet
 */
public class UserAffectationView extends PopupView implements
        UserAffectationDisplay {

    private final static double HEIGHT = 0.8;

    private final static double WIDTH = 0.8;

    private GpmMultipleMultivaluedField affectation;

    private VerticalPanel affectationPanel;

    private final Button saveButton;

    private HandlerRegistration saveButtonRegistration;

    private List<String> unusedProductNames;

    private GpmListBox unusedProducts;

    /**
     * Create a select environment(s) view.
     */
    public UserAffectationView() {
        super(CONSTANTS.adminUserPopupAffectationTitle());
        final ScrollPanel lPanel = new ScrollPanel();

        // Build panel
        affectationPanel = new VerticalPanel();
        affectationPanel.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());

        lPanel.addStyleName(ComponentResources.INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(affectationPanel);

        saveButton = addButton(CONSTANTS.save());

        setContent(lPanel);
        setRatioSize(WIDTH, HEIGHT);
    }

    /**
     * Add a new line to the affectation field
     * 
     * @param pProductName the product name
     * @return the new line
     */
    @SuppressWarnings("unchecked")
    private GpmMultipleMultivaluedElement addNewAffectionLine(
            String pProductName, String pDisplayedProductName, String[] pDisabledRoleNames) {
        GpmMultipleMultivaluedElement lLine = affectation.addLine();
        ((BusinessSimpleField<String>) lLine.getField(GpmMenuTitle.EMPTY))
            .set(pDisplayedProductName);
        ((AbstractGpmField<?>) lLine.getField(GpmMenuTitle.EMPTY)).getWidget().addStyleName(
                INSTANCE.css().gpmFieldLabel());

        for (BusinessField lField : lLine) {
        	if (lField instanceof GpmCheckBox) {
          		if (!unusedProductNames.contains(pProductName)) {
        			((GpmCheckBox) lField).getWidget().setEnabled(false);
        		} else {
        			for (String lRegexp : pDisabledRoleNames) {
        				if (((GpmCheckBox) lField).getFieldName().matches(lRegexp)) {
        					((GpmCheckBox) lField).getWidget().setEnabled(false);
        				}
        			}
        		}        		
        	}
        }
 
        unusedProductNames.remove(pProductName);
        return lLine;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.user.UserAffectationDisplay#getProcessAffectations()
     */
    @Override
    public ArrayList<String> getProcessAffectations() {
        ArrayList<String> lRoleNames = new ArrayList<String>();

        for (BusinessField lField : affectation.getFirst()) {
            if (lField instanceof GpmCheckBox) {
                GpmCheckBox lGpmCheckBox = (GpmCheckBox) lField;
                if (/*lGpmCheckBox.getWidget().isEnabled() && */lGpmCheckBox.get()) {
                    lRoleNames.add(lGpmCheckBox.getFieldName());
                }
            }
        }

        return lRoleNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.user.UserAffectationDisplay#getProductAffectations()
     */
    @Override
    public SortedMap<String, List<String>> getProductAffectations() {
        SortedMap<String, List<String>> lAffectations =
                new TreeMap<String, List<String>>();

        for (int i = 1; i < affectation.size(); i++) {
            String lKey = null;
            List<String> lRoleNames = new ArrayList<String>();
            for (BusinessField lField : affectation.get(i)) {
                if (lField instanceof GpmCheckBox) {
                    GpmCheckBox lGpmCheckBox = (GpmCheckBox) lField;
                    if (/*lGpmCheckBox.getWidget().isEnabled() && */lGpmCheckBox.get()) {
                        lRoleNames.add(lGpmCheckBox.getFieldName());
                    }
                }
                else if (lField instanceof BusinessSimpleField<?>) {
                    lKey = lField.getAsString();
                }
            }
            lAffectations.put(lKey, lRoleNames);
        }

        return lAffectations;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.user.UserAffectationDisplay#initAffectation(java.util.List,
     *      java.util.List)
     */
    @Override
    public void initAffectation(List<String> pProductNames,
            List<Translation> pRoles, final String[] pDisabledRoles) {

        affectationPanel.clear();

        List<AbstractGpmField<?>> lFields = new ArrayList<AbstractGpmField<?>>(pRoles.size() + 1);

        GpmLabel<String> lProductField = new GpmLabel<String>(GpmStringFormatter.getInstance());
        lProductField.setFieldName(GpmMenuTitle.EMPTY);
        lFields.add(lProductField);

        for (Translation lRole : pRoles) {
            GpmCheckBox lRoleField = new GpmCheckBox(true);
            lRoleField.setFieldName(lRole.getValue());
            lRoleField.setTranslatedFieldName(lRole.getTranslatedValue());
            lFields.add(lRoleField);
        }

        affectation = new GpmMultipleMultivaluedField(lFields, false, false, false, true);

        // remove first line
        affectation.removeLine();

        affectationPanel.add(affectation.getWidget());

        // Unused product
        unusedProductNames = new ArrayList<String>(pProductNames);
        unusedProducts = new GpmListBox();
        unusedProducts.getWidget().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent pEvent) {
                setProductAffectation(unusedProducts.get(),
                        new ArrayList<String>(), pDisabledRoles);
                refreshUnusedProducts();
            }
        });

        affectationPanel.add(unusedProducts.getWidget());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.user.UserAffectationDisplay#refreshUnusedProducts()
     */
    @Override
    public void refreshUnusedProducts() {
        if (unusedProductNames.isEmpty()) {
            affectationPanel.remove(unusedProducts.getWidget());
        }
        else {
            List<GpmChoiceBoxValue> lPossibleValues =
                    new ArrayList<GpmChoiceBoxValue>();
            lPossibleValues.add(new GpmChoiceBoxValue("", ""));
            for (String lProductName : unusedProductNames) {
                lPossibleValues.add(new GpmChoiceBoxValue(lProductName,
                        lProductName));
            }
            unusedProducts.setPossibleValues(lPossibleValues);
        }
    }

    /**
     * Set affectation
     * 
     * @param pRow
     *            the row
     * @param pRoleNames
     *            the role names
     */
    private void setAffection(GpmMultipleMultivaluedElement pRow,
            List<String> pRoleNames) {
        for (String lRoleName : pRoleNames) {
       		((BusinessBooleanField) pRow.getField(lRoleName)).set(true);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.user.UserAffectationDisplay#setProcessAffectation(java.util.List)
     */
    @Override
    public void setProcessAffectation(List<String> pRoleNames, String[] pDisabledRoleNames) {
        GpmMultipleMultivaluedElement lLine =
                addNewAffectionLine("$PROCESS", CONSTANTS.process(), pDisabledRoleNames);

        setAffection(lLine, pRoleNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.user.UserAffectationDisplay#setProductAffectation(java.lang.String,
     *      java.util.List)
     */
    @Override
    public void setProductAffectation(String pProductName,
            List<String> pRoleNames, String[] pDisabledRoleNames) {
        GpmMultipleMultivaluedElement lLine =
                addNewAffectionLine(pProductName, pProductName, pDisabledRoleNames);

        setAffection(lLine, pRoleNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.user.UserAffectationDisplay#setSaveButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setSaveButtonHandler(ClickHandler pHandler) {
        // Remove old click handler : if exist
        if (saveButtonRegistration != null) {
            saveButtonRegistration.removeHandler();
        }
        saveButtonRegistration = saveButton.addClickHandler(pHandler);
    }
}
