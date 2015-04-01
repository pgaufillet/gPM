/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.product;

import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.client.util.validation.MandatoryRule;
import org.topcased.gpm.ui.component.client.container.GpmFieldGridPanel;
import org.topcased.gpm.ui.component.client.container.GpmFormPanel;
import org.topcased.gpm.ui.component.client.container.field.GpmUploadFile;
import org.topcased.gpm.ui.component.client.container.field.GpmUploadFileRegister;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.util.validation.IRule;
import org.topcased.gpm.ui.component.client.util.validation.Validator;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for the file to import product(s).
 * 
 * @author tpanuel
 */
public class ImportProductsView extends PopupView implements
        ImportProductsDisplay {
    private final static int HEIGHT = 120;

    private final static int NB_FIELDS = 1;

    private final static int WIDTH = 450;

    private final GpmUploadFile fileUpload;

    private final Button importButton;

    private final Validator validator;

    private HandlerRegistration importButtonRegistration;

    /**
     * Create a view for the file to import product(s).
     */
    public ImportProductsView() {
        super(Ui18n.CONSTANTS.productImportTitle());
        final ScrollPanel lPanel = new ScrollPanel();
        // Build form
        final GpmFormPanel lForm = new GpmFormPanel(NB_FIELDS);

        lForm.addStyleName(ComponentResources.INSTANCE.css().gpmBigBorder());

        // Import file field
        validator = new Validator();
        fileUpload = new GpmUploadFile(null, null, null);
        fileUpload.setFieldName(Ui18n.CONSTANTS.productImportField());
        fileUpload.setTranslatedFieldName(Ui18n.CONSTANTS.productImportField());
        validator.addValidation(fileUpload,
                CollectionUtil.singleton((IRule) new MandatoryRule()));
        lForm.addField(Ui18n.CONSTANTS.productImportField()
                + GpmFieldGridPanel.MANDATORY_LABEL, fileUpload.getPanel());

        lPanel.addStyleName(ComponentResources.INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(lForm);

        importButton = addButton(Ui18n.CONSTANTS.productImportButton());

        setContent(lPanel);
        setPixelSize(WIDTH, HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    @Override
    public void clear() {
        fileUpload.getWidget().setFileName(null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.product.ImportProductsDisplay#setImportButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setImportButtonHandler(ClickHandler pHandler) {
        // Remove old click handler : if exist
        if (importButtonRegistration != null) {
            importButtonRegistration.removeHandler();
        }
        importButtonRegistration = importButton.addClickHandler(pHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.product.ImportProductsDisplay#getFileName()
     */
    @Override
    public String getFileName() {
        return fileUpload.getFileName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.product.ImportProductsDisplay#getUploadFileRegister()
     */
    @Override
    public GpmUploadFileRegister getUploadFileRegister() {
        final GpmUploadFileRegister lUploadFileRegister =
                new GpmUploadFileRegister();

        lUploadFileRegister.registerUploadFile(fileUpload);

        return lUploadFileRegister;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.product.ImportProductsDisplay#getValidator()
     */
    @Override
    public Validator getValidator() {
        return validator;
    }
}