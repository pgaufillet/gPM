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

import java.util.List;

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionContainerItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionContainerManager;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionUsableFieldItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionUsableFieldManager;
import org.topcased.gpm.ui.application.client.popup.PopupDisplay;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.field.multivalued.GpmMultipleMultivaluedElement;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterScope;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the FilterEditionView.
 * 
 * @author jlouisy
 */
public interface FilterEditionPopupDisplay extends PopupDisplay {

    /**
     * Add new criteria group in view.
     * 
     * @return index of criteria group.
     */
    int addCriteriaGroup();

    /**
     * Add new criterion to criteria group.
     * 
     * @param pIndex
     *            Index of criteria group.
     * @param pPath
     *            Path to field.
     * @param pTranslatedName
     *            The translated name field .
     * @param pTechnicalName
     *            the non translated name field
     * @param pFieldType
     *            Field type.
     * @param pAvailableOperators
     *            List of available operators to display in list box.
     * @param pOperator
     *            Selected operator.
     * @param pFieldList
     *            fields to compare with.
     * @param pIsCaseSensitive
     *            Case sensitivity.
     * @param pEnableOperators
     *            true if operators are enabled
     */
    void addCriterion(int pIndex, String pPath, String pTranslatedName,
            String pTechnicalName, FieldType pFieldType,
            List<Translation> pAvailableOperators, String pOperator,
            List<AbstractGpmField<?>> pFieldList, Boolean pIsCaseSensitive,
            boolean pEnableOperators);

    /**
     * Add new result field in view.
     * 
     * @param pPath
     *            Path to field.
     * @param pTranslatedName
     *            The translated name field.
     * @param pTechnicalName
     *            The non translated name field
     * @param pLabel
     *            New field label.
     */
    void addResultField(String pPath, String pTranslatedName,
            String pTechnicalName, String pLabel);

    /**
     * Add new sorting field in view.
     * 
     * @param pPath
     *            Path to field.
     * @param pName
     *            Field translated name.
     * @param pTechnicalName
     *            Field technical name
     * @param pAvalaibleOrder
     *            List of available orders to display in list box.
     * @param pOrder
     *            Selected order.
     * @param pVirtual
     *            true if the field is virtual
     */
    void addSortingField(String pPath, String pName, String pTechnicalName,
            List<Translation> pAvalaibleOrder, String pOrder, boolean pVirtual);

    /**
     * Display containers panel.
     */
    void displayContainers();

    /**
     * Display criteria panel.
     */
    public void displayCriterias();

    /**
     * Display result fields panel.
     * 
     * @param pDisplayContainerHierarchy
     *            should container hierarchy be displayed ?
     */
    public void displayResultFields(boolean pDisplayContainerHierarchy);

    /**
     * Display scope panel.
     */
    void displayScopes();

    /**
     * Display sorting fields panel.
     */
    public void displaySortingFields();

    /**
     * Get selected item in containers tree.
     * 
     * @return selected item.
     */
    TreeFilterEditionContainerItem getSelectedContainerItem();

    /**
     * Get selected containers.
     * 
     * @return list of selected containers.
     */
    List<UiFilterContainerType> getSelectedContainers();

    /**
     * Get displayed criteria groups count.
     * 
     * @return number of criteria groups.
     */
    int getSelectedCriteriaGroupsCount();

    /**
     * Get criterion in criteria groups at the given index.
     * 
     * @param pGroupIndex
     *            Index of criteria group.
     * @return all criterion in criteria group.
     */
    BusinessMultivaluedField<GpmMultipleMultivaluedElement> getSelectedCriterias(
            int pGroupIndex);

    /**
     * Get result fields.
     * 
     * @return fields of result field.
     */
    BusinessMultivaluedField<GpmMultipleMultivaluedElement> getSelectedResultFields();

    /**
     * Get selected scopes.
     * 
     * @return List of scopes.
     */
    List<UiFilterScope> getSelectedScopes();

    /**
     * Get sorting fields.
     * 
     * @return fields of each sorting field.
     */
    BusinessMultivaluedField<GpmMultipleMultivaluedElement> getSelectedSortingFields();

    /**
     * Reset View.
     */
    void reset();

    /**
     * Reset criteria panel.
     */
    void resetCriterias();

    /**
     * Reset result fields panel.
     */
    void resetResultFields();

    /**
     * Reset sorting fields panel.
     */
    void resetSortingFields();

    /**
     * Display containers tree panel.
     * 
     * @param pManager
     *            Tree manager.
     * @param pRoots
     *            Root containers (selected in step 1)
     */
    public void setContainerHierarchyPanel(
            TreeFilterEditionContainerManager pManager,
            List<TreeFilterEditionContainerItem> pRoots);

    /**
     * Set click handler on execute button.
     * 
     * @param pClickHandler
     *            The Click handler.
     */
    void setExecuteButtonHandler(ClickHandler pClickHandler);

    /**
     * Set click handler on Next button.
     * 
     * @param pClickHandler
     *            The Click handler.
     * @param pEnabled
     *            Is Next button enabled?
     */
    void setNextButtonHandler(ClickHandler pClickHandler, boolean pEnabled);

    /**
     * Set click handler on Previous button.
     * 
     * @param pClickHandler
     *            The Click handler.
     * @param pEnabled
     *            Is Previous button enabled?
     */
    void setPreviousButtonHandler(ClickHandler pClickHandler, boolean pEnabled);

    /**
     * Set click handler on Save button.
     * 
     * @param pClickHandler
     *            The Click handler.
     * @param pEnabled <tt>true</tt> if the save button is enabled, <tt>false</tt> otherwise.
     */
    void setSaveButtonHandler(ClickHandler pClickHandler, boolean pEnabled);

    /**
     * Populate containers list shifter.
     * 
     * @param pAvailableContainers
     *            Selectable containers.
     * @param pContainers
     *            Selected containers.
     */
    void setSelectContainerPanel(
            List<UiFilterContainerType> pAvailableContainers,
            List<UiFilterContainerType> pContainers);

    /**
     * Populate scopes list shifter.
     * 
     * @param pAvailableScopes
     *            Available scopes.
     * @param pScopes
     *            Selected scopes.
     */
    void setSelectProductPanel(List<UiFilterScope> pAvailableScopes,
            List<UiFilterScope> pScopes);

    /**
     * Display usable fields tree panel.
     * 
     * @param pManager
     *            Tree manager.
     * @param pRoots
     *            Root fields
     */
    void setUsableFieldHierarchy(TreeFilterEditionUsableFieldManager pManager,
            List<TreeFilterEditionUsableFieldItem> pRoots);

    /**
     * Get selected result fields count.
     * 
     * @return selected result fields count.
     */
    int getSelectedResultFieldsCount();

}