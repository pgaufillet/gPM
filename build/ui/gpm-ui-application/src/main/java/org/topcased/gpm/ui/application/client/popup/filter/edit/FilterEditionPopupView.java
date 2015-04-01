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

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.LinkedList;
import java.util.List;

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionContainerItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionContainerManager;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionUsableFieldItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionUsableFieldManager;
import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.button.GpmTextButton;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmCheckBox;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmGenericField;
import org.topcased.gpm.ui.component.client.container.field.GpmLabel;
import org.topcased.gpm.ui.component.client.container.field.GpmListBox;
import org.topcased.gpm.ui.component.client.container.field.GpmListShifterSelector;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.container.field.GpmListShifterSelector.ListShifterWrapper;
import org.topcased.gpm.ui.component.client.container.field.GpmListShifterSelector.ShiftMode;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultipleMultivaluedField;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.field.multivalued.GpmMultipleMultivaluedElement;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.tree.GpmDynamicTree;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterScope;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterOperator;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for editing a filter table on a popup.
 * 
 * @author jlouisy
 */
public class FilterEditionPopupView extends PopupView implements
        FilterEditionPopupDisplay {

    private final class UiFilterContainerTypeListShifterWrapper implements
            ListShifterWrapper<UiFilterContainerType> {

        @Override
        public Widget wrapToSelected(UiFilterContainerType pField) {
            return new GpmTextButton(null,
                    pField.getName().getTranslatedValue());
        }

        @Override
        public Widget wrapToUnSelected(UiFilterContainerType pField) {
            return new GpmTextButton(null,
                    pField.getName().getTranslatedValue());
        }
    }

    private final class UiFilterScopeListShifterWrapper implements
            ListShifterWrapper<UiFilterScope> {

        private boolean isLoaded;

        @Override
        public Widget wrapToSelected(final UiFilterScope pField) {

            FlowPanel lPanel = new FlowPanel();

            GpmTextButton lProductName =
                    new GpmTextButton(null,
                            pField.getProductName().getTranslatedValue());

            CheckBox lIncludeSubProducts = new CheckBox();
            lIncludeSubProducts.setValue(pField.isIncludeSubProduct());
            lIncludeSubProducts.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                @Override
                public void onValueChange(ValueChangeEvent<Boolean> pEvent) {
                    pField.setIncludeSubProduct(pEvent.getValue());
                }
            });
            lIncludeSubProducts.setTitle(TEXT_INCLUDE_SUB_PRODUCTS_TOOLTIP);
            lIncludeSubProducts.getElement().getStyle().setFloat(
                    com.google.gwt.dom.client.Style.Float.RIGHT);
            lIncludeSubProducts.getElement().getStyle().setProperty("clear",
                    "right");

            lPanel.add(lIncludeSubProducts);
            lPanel.add(lProductName);

            return lPanel;
        }

        @Override
        public Widget wrapToUnSelected(UiFilterScope pField) {
            if (isLoaded) {
                pField.setIncludeSubProduct(false);
            }
            return new GpmTextButton(null,
                    pField.getProductName().getTranslatedValue());
        }

        public void setLoaded(boolean pIsLoaded) {
            isLoaded = pIsLoaded;
        }
    }

    private final static double RATIO_WIDTH = 0.8;

    private final static double RATIO_HEIGHT = 0.8;

    private static final int PIXEL_MARGIN = 2;

    public static final String PATH_FIELD = "PATH";

    public static final String NAME_FIELD = "FIELD_NAME";

    /**
     * Technical name of the field, ie the name defined by the instance.
     * This field is used to store the unique name of a field to identify it
     * when two fields have the same translation.
     */
    public static final String TECHNICAL_NAME_FIELD = "TECHNICAL_FIELD_NAME";

    public static final String RESULT_NEW_NAME_FIELD = "FILTER_FIELD_NAME";

    public static final String AVAILABLE_SORTING = "AVAILABLE_SORTING";

    public static final String CRITERION_AVAILABLE_OPERATORS =
            "AVAILABLE_OPERATORS";

    public static final String CRITERION_VALUE_TO_COMPARE_WITH =
            "VALUE_TO_COMPARE_WITH";

    public static final String CRITERION_CASE_SENSITIVE = "CASE_SENSITIVE";

    public static final String SORTING_VIRTUAL_FIELD = "SORTING_VIRTUAL_FIELD";

    private static final String TEXT_AVAILABLE_TYPES_HIERARCHY =
            CONSTANTS.filterEditionAvailableTypesHierarchy();

    private static final String TEXT_INCLUDE_SUB_PRODUCTS_TOOLTIP =
            CONSTANTS.filterEditionIncludeSubProductsTooltip();

    private static final String TEXT_SELECTED_FIELDS =
            CONSTANTS.filterEditionSelectedFields();

    private static final String TEXT_AVAILABLE_CONTAINERS =
            CONSTANTS.filterEditionAvailableContainers();

    private static final String TEXT_SELECTED_CONTAINERS =
            CONSTANTS.filterEditionSelectedContainers();

    private static final String TEXT_AVAILABLE_PRODUCTS =
            CONSTANTS.filterEditionAvailableProducts();

    private static final String TEXT_SELECTED_PRODUCTS_WITH_TICK_INDICATION =
            CONSTANTS.filterEditionSelectedProductsTickToIncSubProducts();

    private GpmListShifterSelector<UiFilterContainerType> containerListShifter;

    private GpmListShifterSelector<UiFilterScope> productListShifter;

    private UiFilterScopeListShifterWrapper filterScopeListShifterWrapper;

    private GpmDynamicTree<TreeFilterEditionContainerItem> containerTree;

    private GpmDynamicTree<TreeFilterEditionUsableFieldItem> fieldTree;

    private GpmMultipleMultivaluedField resultFields;

    private LinkedList<GpmMultipleMultivaluedField> criteriaFields;

    private final ButtonBase previousButton;

    private HandlerRegistration previousButtonHandler;

    private final ButtonBase saveButton;

    private HandlerRegistration saveButtonHandler;

    private final ButtonBase executeButton;

    private HandlerRegistration executeButtonHandler;

    private final ButtonBase nextButton;

    private HandlerRegistration nextButtonHandler;

    private FlowPanel fieldSelectionPanel;

    private SimplePanel detailPanel;

    private GpmMultipleMultivaluedField sortingFields;

    private List<AbstractGpmField<?>> criterionTemplate;

    private FlexTable criteriaPanel;

    private HorizontalPanel criteriaOrPanel;

    private HTML orLabel;

    private TitledPanel rightTitled;

    private TitledPanel leftTitled;
    
    private final static int NEWNAME_FIELD_SIZE = 100;

    /**
     * Create a filter edition view.
     */
    public FilterEditionPopupView() {
        super();

        // Build the firsts panels with ListShifterSelectors
        buildListShifterPanels();

        // Build the lasts panels with docked Panels
        buildDockPanels();

        previousButton = addButton(CONSTANTS.previous());
        saveButton = addButton(CONSTANTS.save());
        executeButton = addButton(CONSTANTS.execute());
        nextButton = addButton(CONSTANTS.next());

        setContent(containerListShifter.getWidget());
        setRatioSize(RATIO_WIDTH, RATIO_HEIGHT);

        LinkedList<AbstractGpmField<?>> lResultFieldsList =
                new LinkedList<AbstractGpmField<?>>();
        GpmLabel<String> lPath =
                new GpmLabel<String>(GpmStringFormatter.getInstance());
        lPath.setFieldName(PATH_FIELD);
        lResultFieldsList.add(lPath);
        GpmLabel<String> lFieldName =
                new GpmLabel<String>(GpmStringFormatter.getInstance());
        lFieldName.setFieldName(NAME_FIELD);
        lResultFieldsList.add(lFieldName);
        GpmTextBox<String> lFieldNewName =
                new GpmTextBox<String>(GpmStringFormatter.getInstance());
        lFieldNewName.setFieldName(RESULT_NEW_NAME_FIELD);
        //set field size
        lFieldNewName.setSize(NEWNAME_FIELD_SIZE);

        lResultFieldsList.add(lFieldNewName);
        GpmLabel<String> lTechnicalNameField =
                new GpmLabel<String>(GpmStringFormatter.getInstance());
        lTechnicalNameField.setFieldName(TECHNICAL_NAME_FIELD);
        lResultFieldsList.add(lTechnicalNameField);
        resultFields =
                new GpmMultipleMultivaluedField(lResultFieldsList, false, true,
                        true, false);

        criterionTemplate = new LinkedList<AbstractGpmField<?>>();
        criterionTemplate.clear();
        criterionTemplate.add(lPath);
        criterionTemplate.add(lFieldName);
        criterionTemplate.add(lTechnicalNameField);
        GpmListBox lOperators = new GpmListBox();
        lOperators.setFieldName(CRITERION_AVAILABLE_OPERATORS);
        criterionTemplate.add(lOperators);
        GpmGenericField lCompareTo = new GpmGenericField();
        lCompareTo.setFieldName(CRITERION_VALUE_TO_COMPARE_WITH);
        criterionTemplate.add(lCompareTo);
        GpmCheckBox lCaseSensitive = new GpmCheckBox(true);
        lCaseSensitive.setFieldName(CRITERION_CASE_SENSITIVE);
        lCaseSensitive.getWidget().setTitle(
                CONSTANTS.filterEditionCaseSensitiveTooltip());
        criterionTemplate.add(lCaseSensitive);
        criteriaFields = new LinkedList<GpmMultipleMultivaluedField>();
        criteriaPanel = new FlexTable();
        criteriaPanel.addStyleName(ComponentResources.INSTANCE.css().gpmFilterEditionCriteria());

        orLabel = new HTML("");
        orLabel.setStylePrimaryName(INSTANCE.css().gpmFieldLabel());
        criteriaOrPanel = new HorizontalPanel();
        criteriaOrPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        criteriaOrPanel.addStyleName(ComponentResources.INSTANCE.css().gpmFilterEditionCriteria());
        criteriaOrPanel.add(orLabel);
        criteriaOrPanel.add(criteriaPanel);

        LinkedList<AbstractGpmField<?>> lSortingFieldsList =
                new LinkedList<AbstractGpmField<?>>();
        lSortingFieldsList.clear();
        lSortingFieldsList.add(lPath);
        lSortingFieldsList.add(lFieldName);
        lSortingFieldsList.add(lTechnicalNameField);
        GpmListBox lSorting = new GpmListBox();
        lSorting.setFieldName(AVAILABLE_SORTING);
        lSortingFieldsList.add(lSorting);

        //Add a field to retrieve a virtual value
        GpmCheckBox lVirtual = new GpmCheckBox(false);
        lVirtual.setFieldName(SORTING_VIRTUAL_FIELD);
        lSortingFieldsList.add(lVirtual);

        sortingFields =
                new GpmMultipleMultivaluedField(lSortingFieldsList, false,
                        true, true, false);
        
        // Add Key Handler on ENTER key : Quick Execute
        final KeyPressHandler lEnterKeyPressHandler = new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent pEvent) {
                if (KeyCodes.KEY_ENTER == pEvent.getCharCode()) {
                    ((Button) executeButton).click();
                }
            }
        };
        
        this.addDomHandler(lEnterKeyPressHandler, KeyPressEvent.getType());
        
    }

    private void buildListShifterPanels() {
        containerListShifter =
                new GpmListShifterSelector<UiFilterContainerType>(true, false,
                        ShiftMode.SHIFT_WITH_BUTTON);
        containerListShifter.setWrapper(new UiFilterContainerTypeListShifterWrapper());

        filterScopeListShifterWrapper = new UiFilterScopeListShifterWrapper();
        productListShifter =
                new GpmListShifterSelector<UiFilterScope>(true, false,
                        ShiftMode.SHIFT_WITH_BUTTON);
        productListShifter.setWrapper(new UiFilterScopeListShifterWrapper());
    }

    private class TitledPanel extends FlowPanel {
        private final Widget content;

        private Label header;

        /**
         * Build a panel width a border, a title and a content
         * 
         * @param pContent
         *            The content of the panel
         * @param pTitle
         *            The title of the panel (displayed on top)
         */
        public TitledPanel(final Widget pContent, String pTitle) {
            header = new Label(pTitle);
            content = pContent;

            addStyleName(ComponentResources.INSTANCE.css().gpmFilterEditionDock());
            header.addStyleName(ComponentResources.INSTANCE.css().gpmFilterEditionDockTitle());
            add(header);
            add(pContent);
        }

        /**
         * Set title
         * 
         * @param pTitle
         *            title to set.
         */
        public void setHeader(String pTitle) {
            header.setText(pTitle);
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.google.gwt.user.client.ui.UIObject#setPixelSize(int, int)
         */
        @Override
        public void setPixelSize(int pWidth, int pHeight) {
            DeferredCommand.addCommand(new Command() {
                @Override
                public void execute() {
                    content.setHeight(getOffsetHeight()
                            - header.getOffsetHeight() - PIXEL_MARGIN + "px");
                    content.setWidth(getOffsetWidth() - PIXEL_MARGIN + "px");
                }
            });
            super.setPixelSize(pWidth, pHeight);
        }
    }

    private void buildDockPanels() {
        containerTree = new GpmDynamicTree<TreeFilterEditionContainerItem>();
        final ScrollPanel lLeftPanel = new ScrollPanel(containerTree);
        fieldTree = new GpmDynamicTree<TreeFilterEditionUsableFieldItem>();
        final ScrollPanel lRightPanel = new ScrollPanel(fieldTree);
        detailPanel = new SimplePanel();
        final ScrollPanel lScrollPanel = new ScrollPanel(detailPanel);

        leftTitled =
                new TitledPanel(lLeftPanel, TEXT_AVAILABLE_TYPES_HIERARCHY);
        rightTitled =
                new TitledPanel(lRightPanel,
                        Ui18n.MESSAGES.filterEditionAvailableFields(""));
        final TitledPanel lDetailTitled =
                new TitledPanel(lScrollPanel, TEXT_SELECTED_FIELDS);

        leftTitled.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        rightTitled.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.RIGHT);
        lDetailTitled.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        lDetailTitled.getElement().getStyle().setProperty("clear", "left");

        fieldSelectionPanel = new FlowPanel() {
            /**
             * {@inheritDoc} Overridden to handle resize depending on the popup
             * content
             * 
             * @see com.google.gwt.user.client.ui.UIObject#setPixelSize(int,
             *      int)
             */
            @Override
            public void setPixelSize(int pWidth, int pHeight) {
                int lMargin = PIXEL_MARGIN * 2;
                if (leftTitled.isVisible()) {
                    leftTitled.setPixelSize(pWidth / 2 - lMargin, pHeight / 2
                            - lMargin);
                    rightTitled.setPixelSize(pWidth / 2 - lMargin, pHeight / 2
                            - lMargin);
                }
                else {
                    rightTitled.setPixelSize(pWidth - lMargin, pHeight / 2
                            - lMargin);
                }
                lDetailTitled.setPixelSize(pWidth - lMargin / 2, pHeight / 2
                        - lMargin);
                super.setPixelSize(pWidth, pHeight);
            }
        };

        // Add the elements
        fieldSelectionPanel.add(leftTitled);
        fieldSelectionPanel.add(rightTitled);
        HTML lSpace = new HTML("&nbsp;");
        lSpace.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        lSpace.getElement().getStyle().setProperty("clear", "left");
        lSpace.setHeight(PIXEL_MARGIN * 2 + "px");
        fieldSelectionPanel.add(lSpace);
        fieldSelectionPanel.add(lDetailTitled);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#addCriteriaGroup()
     */
    @Override
    public int addCriteriaGroup() {
        GpmMultipleMultivaluedField lGroup =
                new GpmMultipleMultivaluedField(criterionTemplate, false, true,
                        true, false);
        lGroup.getWidget().removeAll();
        criteriaFields.add(lGroup);
        return criteriaFields.size() - 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#addCriterion(int,
     *      java.lang.String, java.lang.String, java.util.List,
     *      java.lang.String, java.lang.String, java.lang.Boolean)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addCriterion(int pIndex, String pPath, String pName,
            String pTechnicalName, FieldType pFieldType,
            List<Translation> pAvailableOperators, String pOperator,
            List<AbstractGpmField<?>> pFieldList, Boolean pIsCaseSensitive,
            boolean pEnableOperators) {
        BusinessMultipleField lNewLine = criteriaFields.get(pIndex).addLine();
        // Get various elements
        GpmLabel<String> lFieldName =
                (GpmLabel<String>) lNewLine.getField(NAME_FIELD);
        GpmLabel<String> lFieldPath =
                (GpmLabel<String>) lNewLine.getField(PATH_FIELD);
        GpmLabel<String> lFieldTechnicalName =
                (GpmLabel<String>) lNewLine.getField(TECHNICAL_NAME_FIELD);
        final GpmListBox lOperator =
                (GpmListBox) lNewLine.getField(CRITERION_AVAILABLE_OPERATORS);
        GpmCheckBox lCaseSensitive =
                (GpmCheckBox) lNewLine.getField(CRITERION_CASE_SENSITIVE);
        // Operations
        lFieldPath.set(pPath);
        lFieldName.set(pName);
        lFieldTechnicalName.set(pTechnicalName);
        lOperator.setPossibleValues(GpmChoiceBoxValue.buildFromTranslations(pAvailableOperators));
        lOperator.set(pOperator);

        // Hide elements when unnecessary
        lFieldTechnicalName.getWidget().setVisible(false);
        lOperator.getWidget().setVisible(pEnableOperators); // Hide operator if unnecessary
        final GpmGenericField lField =
                (GpmGenericField) lNewLine.getField(CRITERION_VALUE_TO_COMPARE_WITH);
        lField.getWidget().setTitle(
                CONSTANTS.filterEditionCompareWithCriterionTooltip());
        lField.setFieldList(pFieldList);

        if (pIsCaseSensitive != null) { // Set case sensitiveness and hide it if unnecessary
            lCaseSensitive.set(pIsCaseSensitive);
            lCaseSensitive.getWidget().setTitle(
                    CONSTANTS.filterEditionCaseSensitiveTooltip());
        }
        else {
            lCaseSensitive.getWidget().setVisible(false);
        }

        // Date widget change according to operator value
        if (pFieldType == FieldType.DATE) {
            lOperator.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent pEvent) {
                    updateDateWidget(lOperator, lField);
                }
            });

        }
        updateDateWidget(lOperator, lField);
    }

    private void updateDateWidget(GpmListBox pOperator, GpmGenericField pField) {
        String lOp = pOperator.getAsString();
        if (lOp.equals(UiFilterOperator.SINCE.getKey())) {
            pField.getWidget().showWidget(2);
        }
        else if (lOp.equals(UiFilterOperator.OTHER.getKey())) {
            pField.getWidget().showWidget(1);
        }
        else {
            pField.getWidget().showWidget(0);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#addResultField(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addResultField(String pPath, String pName,
            String pTechnicalName, String pLabel) {
        BusinessMultipleField lNewLine = resultFields.addLine();
        ((GpmLabel<String>) lNewLine.getField(PATH_FIELD)).set(pPath);
        ((GpmLabel<String>) lNewLine.getField(NAME_FIELD)).set(pName);
        ((GpmLabel<String>) lNewLine.getField(TECHNICAL_NAME_FIELD)).set(pTechnicalName);
        ((GpmLabel<String>) lNewLine.getField(TECHNICAL_NAME_FIELD)).getWidget().setVisible(
                false);
        ((GpmTextBox<String>) lNewLine.getField(RESULT_NEW_NAME_FIELD)).set(pLabel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#addSortingField(java.lang.String,
     *      java.lang.String, java.util.List, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addSortingField(String pPath, String pName,
            String pTechnicalName, List<Translation> pAvalaibleOrder,
            String pOrder, boolean pVirtual) {
        BusinessMultipleField lNewLine = sortingFields.addLine();
        ((GpmLabel<String>) lNewLine.getField(PATH_FIELD)).set(pPath);
        ((GpmLabel<String>) lNewLine.getField(NAME_FIELD)).set(pName);
        ((GpmLabel<String>) lNewLine.getField(TECHNICAL_NAME_FIELD)).set(pTechnicalName);
        ((GpmLabel<String>) lNewLine.getField(TECHNICAL_NAME_FIELD)).getWidget().setVisible(
                false);
        ((GpmListBox) lNewLine.getField(AVAILABLE_SORTING)).setPossibleValues(GpmChoiceBoxValue.buildFromTranslations(pAvalaibleOrder));
        ((GpmListBox) lNewLine.getField(AVAILABLE_SORTING)).set(pOrder);

        GpmCheckBox lVirtualValue =
                ((GpmCheckBox) lNewLine.getField(SORTING_VIRTUAL_FIELD));
        //Initialize the Virtual Value
        lVirtualValue.set(pVirtual);
        //Hide Virtual Value field
        lVirtualValue.getWidget().setVisible(false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#displayContainers()
     */
    @Override
    public void displayContainers() {
        setContent(containerListShifter.getWidget());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#displayCriterias()
     */
    @Override
    public void displayCriterias() {
        setContent(fieldSelectionPanel);
        criteriaPanel.removeAllRows();
        detailPanel.clear();

        for (int i = 0; i < criteriaFields.size(); i++) {
            int j = 2 * i;
            GpmMultipleMultivaluedField lGroup = criteriaFields.get(i);
            if (lGroup.size() > 1) {
                criteriaPanel.insertRow(j);

                HTML lAndLabel = new HTML("");
                lAndLabel.setStylePrimaryName(INSTANCE.css().gpmFieldLabel());
                lAndLabel.setHTML(CONSTANTS.filterEditionAnd());

                criteriaPanel.setWidget(j, 1, lAndLabel);
                criteriaPanel.getCellFormatter().setAlignment(j, 1,
                        HasHorizontalAlignment.ALIGN_CENTER,
                        HasVerticalAlignment.ALIGN_MIDDLE);
                criteriaPanel.getCellFormatter().addStyleName(
                        j,
                        1,
                        ComponentResources.INSTANCE.css().gpmFilterEditionCriteriaLeft());
            }
            criteriaPanel.setWidget(j, 2, lGroup.getWidget());
            criteriaPanel.getCellFormatter().addStyleName(
                    j,
                    2,
                    ComponentResources.INSTANCE.css().gpmFilterEditionCriteriaRight());

            criteriaPanel.insertRow(j + 1);
            criteriaPanel.setWidget(j + 1, 0, new HTML(""));
        }

        criteriaPanel.insertRow(0);
        criteriaPanel.setWidget(0, 0, new HTML(""));

        if (getSelectedCriteriaGroupsCount() > 1) {
            orLabel.setHTML(CONSTANTS.filterEditionOr());
        }
        else {
            orLabel.setHTML("");
        }

        criteriaPanel.setCellSpacing(0);
        detailPanel.add(criteriaOrPanel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#displayResultFields(boolean)
     */
    @Override
    public void displayResultFields(boolean pDisplayContainerHierarchy) {
        // TODO : Hack IE
        leftTitled.setVisible(true);
        // leftTitled.setVisible(pDisplayContainerHierarchy);

        detailPanel.clear();
        detailPanel.add(resultFields.getWidget());
        setContent(fieldSelectionPanel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#displayScopes()
     */
    @Override
    public void displayScopes() {
        setContent(productListShifter.getWidget());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#displaySortingFields()
     */
    @Override
    public void displaySortingFields() {
        setContent(fieldSelectionPanel);
        detailPanel.clear();
        detailPanel.add(sortingFields.getWidget());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#getSelectedContainerItem()
     */
    @Override
    public TreeFilterEditionContainerItem getSelectedContainerItem() {
        TreeFilterEditionContainerItem lItem =
                (TreeFilterEditionContainerItem) containerTree.getSelectedItem();
        return lItem;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#getSelectedContainers()
     */
    @Override
    public List<UiFilterContainerType> getSelectedContainers() {
        return containerListShifter.getSelectedValues();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#getSelectedCriteriaGroupsCount()
     */
    @Override
    public int getSelectedCriteriaGroupsCount() {
        return criteriaFields.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#getSelectedCriterias(int)
     */
    @Override
    public BusinessMultivaluedField<GpmMultipleMultivaluedElement> getSelectedCriterias(
            int pGroupIndex) {
        return criteriaFields.get(pGroupIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#getSelectedResultFields()
     */
    @Override
    public BusinessMultivaluedField<GpmMultipleMultivaluedElement> getSelectedResultFields() {
        return resultFields;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#getSelectedScopes()
     */
    @Override
    public List<UiFilterScope> getSelectedScopes() {
        return productListShifter.getSelectedValues();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#getSelectedSortingFields()
     */
    @Override
    public BusinessMultivaluedField<GpmMultipleMultivaluedElement> getSelectedSortingFields() {
        return sortingFields;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#reset()
     */
    @Override
    public void reset() {
        containerListShifter.clear();
        productListShifter.clear();

        containerTree.resetTree();
        fieldTree.resetTree();

        resetResultFields();
        resetSortingFields();
        resetCriterias();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#resetCriterias()
     */
    @Override
    public void resetCriterias() {
        criteriaFields.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#resetResultFields()
     */
    @Override
    public void resetResultFields() {
        resultFields.getWidget().removeAll();
        fieldTree.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#resetSortingFields()
     */
    @Override
    public void resetSortingFields() {
        sortingFields.getWidget().removeAll();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#setContainerHierarchyPanel(org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionContainerManager,
     *      java.util.List)
     */
    @Override
    public void setContainerHierarchyPanel(
            TreeFilterEditionContainerManager pManager,
            List<TreeFilterEditionContainerItem> pRoots) {
        containerTree.resetTree();
        if (pManager != null) {
            containerTree.setDynamicTreeManager(pManager);
        }
        containerTree.setRootItems(pRoots);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#setExecuteButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setExecuteButtonHandler(ClickHandler pClickHandler) {
        if (executeButtonHandler != null) {
            executeButtonHandler.removeHandler();
        }
        executeButtonHandler = executeButton.addClickHandler(pClickHandler);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#setNextButtonHandler(com.google.gwt.event.dom.client.ClickHandler,
     *      boolean)
     */
    @Override
    public void setNextButtonHandler(final ClickHandler pClickHandler,
            final boolean pIsAvailable) {
        if (nextButtonHandler != null) {
            nextButtonHandler.removeHandler();
        }
        nextButtonHandler = nextButton.addClickHandler(pClickHandler);
        nextButton.setEnabled(pIsAvailable);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#setPreviousButtonHandler(com.google.gwt.event.dom.client.ClickHandler,
     *      boolean)
     */
    @Override
    public void setPreviousButtonHandler(final ClickHandler pClickHandler,
            final boolean pIsAvailable) {
        if (previousButtonHandler != null) {
            previousButtonHandler.removeHandler();
        }
        previousButtonHandler = previousButton.addClickHandler(pClickHandler);
        previousButton.setEnabled(pIsAvailable);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#setSaveButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setSaveButtonHandler(ClickHandler pClickHandler, boolean pEnabled) {
        if (saveButtonHandler != null) {
            saveButtonHandler.removeHandler();
        }
        saveButtonHandler = saveButton.addClickHandler(pClickHandler);
        saveButton.setEnabled(pEnabled);
        
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#setSelectContainerPanel(java.util.List,
     *      java.util.List)
     */
    @Override
    public void setSelectContainerPanel(
            List<UiFilterContainerType> pSelectableValues,
            List<UiFilterContainerType> pSelectedValues) {
        containerListShifter.setTitles(TEXT_AVAILABLE_CONTAINERS,
                TEXT_SELECTED_CONTAINERS);
        containerListShifter.setSelectableValues(pSelectableValues);
        containerListShifter.setSelectedValues(pSelectedValues);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#setSelectProductPanel(java.util.List,
     *      java.util.List)
     */
    @Override
    public void setSelectProductPanel(List<UiFilterScope> pSelectableValues,
            List<UiFilterScope> pSelectedValues) {
        //Keep values for include subproduct attribute
        filterScopeListShifterWrapper.setLoaded(false);
        productListShifter.setTitles(TEXT_AVAILABLE_PRODUCTS,
                TEXT_SELECTED_PRODUCTS_WITH_TICK_INDICATION);
        productListShifter.setSelectableValues(pSelectableValues);
        productListShifter.setSelectedValues(pSelectedValues);
        filterScopeListShifterWrapper.setLoaded(true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#setUsableFieldHierarchy(org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionUsableFieldManager,
     *      java.util.List)
     */
    @Override
    public void setUsableFieldHierarchy(
            TreeFilterEditionUsableFieldManager pManager,
            List<TreeFilterEditionUsableFieldItem> pRoots) {
        fieldTree.resetTree();
        fieldTree.setDynamicTreeManager(pManager);
        fieldTree.setRootItems(pRoots);
        if (getSelectedContainerItem() != null) {
            rightTitled.setHeader(Ui18n.MESSAGES.filterEditionAvailableFields(getSelectedContainerItem().getNode().getContainerName()));
        }
        else {
            rightTitled.setHeader(Ui18n.MESSAGES.filterEditionAvailableFields(""));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.filter.edit.FilterEditionPopupDisplay#getSelectedResultFieldsCount()
     */
    @Override
    public int getSelectedResultFieldsCount() {
        return resultFields.size();
    }
}