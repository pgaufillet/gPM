/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary.detail;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.client.util.validation.MandatoryRule;
import org.topcased.gpm.ui.component.client.container.GpmFieldGridPanel;
import org.topcased.gpm.ui.component.client.container.field.GpmCheckBox;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.util.validation.IRule;
import org.topcased.gpm.ui.component.client.util.validation.Validator;

/**
 * View for a set of environment displayed on the dictionary management space.
 * 
 * @author nveillet
 */
public class EnvironmentCreationDetailView extends AbstractDictionaryDetailView
        implements EnvironmentCreationDetailDisplay {

    private GpmTextBox<String> name;

    private GpmCheckBox publicField;

    private final Validator validator;
    
    private static final int NAME_FIELD_SIZE = 60;

    /**
     * Create a detail view for users.
     */
    public EnvironmentCreationDetailView() {
        super();

        setTitle("* " + CONSTANTS.environment());
        validator = new Validator();

        // Name field
        name = new GpmTextBox<String>(GpmStringFormatter.getInstance());
        name.setFieldName(Ui18n.CONSTANTS.name());
        name.setTranslatedFieldName(Ui18n.CONSTANTS.name());
        name.setSize(NAME_FIELD_SIZE);
        validator.addValidation(name,
                CollectionUtil.singleton((IRule) new MandatoryRule()));
        getFormPanel().addField(
                Ui18n.CONSTANTS.name() + GpmFieldGridPanel.MANDATORY_LABEL,
                name.getPanel());

        // Access field
        publicField = new GpmCheckBox(true);
        publicField.set(true);
        publicField.setTranslatedFieldName(CONSTANTS.adminDictionaryEnvironmentPublic());
        getFormPanel().addField(publicField.getTranslatedFieldName(),
                publicField.getWidget());
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    @Override
    public void clear() {
        super.clear();
        name.clear();
        publicField.set(true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentCreationDetailDisplay#getName()
     */
    @Override
    public GpmTextBox<String> getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentCreationDetailDisplay#getPublic()
     */
    @Override
    public GpmCheckBox getPublic() {
        return publicField;
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