/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.container.product;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.ui.application.client.common.container.ContainerView;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.client.util.validation.MandatoryRule;
import org.topcased.gpm.ui.component.client.container.GpmDisclosurePanel;
import org.topcased.gpm.ui.component.client.container.GpmFieldGridPanel;
import org.topcased.gpm.ui.component.client.container.GpmFormPanel;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmLabel;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedField;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedListBox;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.util.validation.IRule;
import org.topcased.gpm.ui.component.client.util.validation.Validator;

/**
 * View that display a product.
 * 
 * @author tpanuel
 */
public class ProductView extends ContainerView implements ProductDisplay {
    private static final int FORMS_COUNT = 4;

    private BusinessSimpleField<String> productName;

    private BusinessSimpleField<String> description;

    private BusinessMultivaluedField<? extends BusinessField> parents;

    private BusinessMultivaluedField<? extends BusinessField> child;

    private final Validator validator;
    
    private static final int PRODUCTNAME_FIELD_SIZE = 50;
    
    private static final int DESCRIPTION_FIELD_SIZE = 50;

    /**
     * Constructor
     */
    public ProductView() {
        super();
        validator = new Validator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.product.ProductDisplay#initPropertiesGroup(boolean,
     *      boolean, java.util.List, java.util.List, java.util.List,
     *      java.lang.String)
     */
    @Override
    public void initPropertiesGroup(final boolean pProductNameEditable,
            final boolean pPropertiesEditable, final List<String> pProductNames,
            final List<String> pParents, final List<String> pChild,
            String pDescription) {
        final GpmDisclosurePanel lPropertiesGroup = new GpmDisclosurePanel();
        final GpmFormPanel lForm;

        // Initialize the form
        if (pProductNameEditable) {
            lForm = new GpmFormPanel(FORMS_COUNT);
            productName = new GpmTextBox<String>(GpmStringFormatter.getInstance());
            ((GpmTextBox<String>) productName).setFieldName(CONSTANTS.adminProductName());
            ((GpmTextBox<String>) productName).setTranslatedFieldName(CONSTANTS.adminProductName());
            validator.addValidation((GpmTextBox<String>) productName,
                    CollectionUtil.singleton((IRule) new MandatoryRule()));
            lForm.addField(CONSTANTS.adminProductName()
                    + GpmFieldGridPanel.MANDATORY_LABEL,
                    ((AbstractGpmField<?>) productName).getPanel());
            ((GpmTextBox<String>) productName).setSize(PRODUCTNAME_FIELD_SIZE);
        }
        else {
            lForm = new GpmFormPanel(3);
        }

        // Init description field
        if (pPropertiesEditable) {
            description = new GpmTextBox<String>(GpmStringFormatter.getInstance());
            ((GpmTextBox<String>) description).setSize(DESCRIPTION_FIELD_SIZE);
        }
        else {
            description = new GpmLabel<String>(GpmStringFormatter.getInstance());
        }

        ((AbstractGpmField<?>) description).setFieldName(CONSTANTS.adminProductDescription());
        ((AbstractGpmField<?>) description).setTranslatedFieldName(CONSTANTS.adminProductDescription());
        lForm.addField(CONSTANTS.adminProductDescription(), ((AbstractGpmField<?>) description).getPanel());
        description.set(pDescription);
        
        if (!pPropertiesEditable) {
        	final GpmMultivaluedField<GpmLabel<String>> lParentField = new GpmMultivaluedField<GpmLabel<String>>(
        			new GpmLabel<String>(GpmStringFormatter.getInstance()), false, false, false);
        	final GpmMultivaluedField<GpmLabel<String>> lChildField = new GpmMultivaluedField<GpmLabel<String>>(
        			new GpmLabel<String>(GpmStringFormatter.getInstance()), false, false, false);

        	if (pParents != null) {
                for (String lParent : pParents) {
                    lParentField.addLine().set(lParent);
                }
            }
            if (pChild != null) {
                for (String lChild : pChild) {
                    lChildField.addLine().set(lChild);
                }
            }
            parents = lParentField;
            child = lChildField;
        } else {
            final GpmMultivaluedListBox lParentField = new GpmMultivaluedListBox();
            final GpmMultivaluedListBox lChildField = new GpmMultivaluedListBox();

            lParentField.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(pProductNames));
            lChildField.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(pProductNames));
            if (pParents != null) {
                for (String lParent : pParents) {
                    lParentField.setWidgetValueSelected(lParent, true);
                }
            }
            if (pChild != null) {
                for (String lChild : pChild) {
                    lChildField.setWidgetValueSelected(lChild, true);
                }
            }
            parents = lParentField;
            child = lChildField;
        }
        lForm.addField(CONSTANTS.adminProductFieldParent(), ((AbstractGpmField<?>) parents).getWidget());
        lForm.addField(CONSTANTS.adminProductFieldChild(), ((AbstractGpmField<?>) child).getWidget());

        // Initialize the group
        lPropertiesGroup.setButtonText(CONSTANTS.adminProductFieldProperties());
        lPropertiesGroup.setContent(lForm);
        lPropertiesGroup.open();

        // Add the group on the values container form
        getGpmValuesContainerPanel().add(lPropertiesGroup);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.product.ProductDisplay#getProductName()
     */
    @Override
    public String getProductName() {
        if (productName == null) {
            return null;
        }
        else {
            return productName.get();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.product.ProductDisplay#getProductDescription()
     */
    @Override
    public String getProductDescription() {
        return description.get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.product.ProductDisplay#getParentProducts()
     */
    @Override
    public List<String> getParentProducts() {
        final List<String> lParentProducts = new ArrayList<String>();

        for (BusinessField lParent : parents) {
            if (lParent.getAsString() != null
                    && !lParent.getAsString().isEmpty()) {
                lParentProducts.add(lParent.getAsString());
            }
        }

        return lParentProducts;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.product.ProductDisplay#getChildProducts()
     */
    @Override
    public List<String> getChildProducts() {
        final List<String> lChildProducts = new ArrayList<String>();

        for (BusinessField lChild : child) {
            if (lChild.getAsString() != null && !lChild.getAsString().isEmpty()) {
                lChildProducts.add(lChild.getAsString());
            }
        }

        return lChildProducts;
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