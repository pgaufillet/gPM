/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.container;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.user.ProductWorkspacePresenter;
import org.topcased.gpm.ui.application.client.user.detail.SheetEditionJsAccessor;
import org.topcased.gpm.ui.application.client.util.validation.RuleManager;
import org.topcased.gpm.ui.application.client.util.validation.UrlRule;
import org.topcased.gpm.ui.application.shared.exception.UIAttachmentException;
import org.topcased.gpm.ui.application.shared.exception.UIAttachmentTotalSizeException;
import org.topcased.gpm.ui.component.client.container.FieldCreationHandler;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmAnchor;
import org.topcased.gpm.ui.component.client.container.field.GpmApplet;
import org.topcased.gpm.ui.component.client.container.field.GpmCheckBox;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmDateBox;
import org.topcased.gpm.ui.component.client.container.field.GpmDateTimeBox;
import org.topcased.gpm.ui.component.client.container.field.GpmDropDownListBox;
import org.topcased.gpm.ui.component.client.container.field.GpmImageAndText;
import org.topcased.gpm.ui.component.client.container.field.GpmLabel;
import org.topcased.gpm.ui.component.client.container.field.GpmListBox;
import org.topcased.gpm.ui.component.client.container.field.GpmMultilineBox;
import org.topcased.gpm.ui.component.client.container.field.GpmMultiple;
import org.topcased.gpm.ui.component.client.container.field.GpmRadioBox;
import org.topcased.gpm.ui.component.client.container.field.GpmRichTextBox;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.container.field.GpmUploadFile;
import org.topcased.gpm.ui.component.client.container.field.GpmUploadFileRegister;
import org.topcased.gpm.ui.component.client.container.field.IGpmChoiceBox;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultipleMultivaluedField;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedCheckBox;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedField;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedListBox;
import org.topcased.gpm.ui.component.client.field.GpmExternalContent;
import org.topcased.gpm.ui.component.client.field.GpmMultivaluedTreeChoiceField;
import org.topcased.gpm.ui.component.client.field.GpmRichTextLabel;
import org.topcased.gpm.ui.component.client.field.GpmTreeChoiceField;
import org.topcased.gpm.ui.component.client.field.formater.GpmDoubleFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmFullDateFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmFullDateTimeFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmIntegerFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmLongDateFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmLongDateTimeFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmMediumDateFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmMediumDateTimeFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmShortDateFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmShortDateTimeFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.field.tree.GpmTreeChoiceFieldItem;
import org.topcased.gpm.ui.component.client.util.GpmAnchorTarget;
import org.topcased.gpm.ui.component.client.util.GpmAppletBuilder;
import org.topcased.gpm.ui.component.client.util.GpmUrlBuilder;
import org.topcased.gpm.ui.component.client.util.validation.Validator;
import org.topcased.gpm.ui.component.shared.util.DownloadParameter;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.field.multiple.UiMultipleField;
import org.topcased.gpm.ui.facade.shared.container.field.multivalued.UiMultivaluedField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiAppletField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiChoiceField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiDateField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringChoiceField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;
import org.topcased.gpm.ui.facade.shared.container.field.value.UiChoiceFieldValue;
import org.topcased.gpm.ui.facade.shared.container.field.value.UiChoiceTreeFieldValue;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * An handler for field creation.
 * 
 * @author tpanuel
 */
public class UiFieldCreationHandler implements FieldCreationHandler {
    private final List<UiField> uiFields;

    private List<UIAttachmentException> attachedFieldsInError;

    private final Validator validator;

    private final GpmUploadFileRegister uploadFileRegister;

    /**
     * Constructor
     */
    public UiFieldCreationHandler() {
        uiFields = new LinkedList<UiField>();
        validator = new Validator();
        uploadFileRegister = new GpmUploadFileRegister();
    }

    /**
     * Add an UiField.
     * 
     * @param pField
     *            The UiField.
     */
    public void addUiField(final UiField pField) {
        uiFields.add(pField);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.FieldCreationHandler#createFields()
     */
    @Override
    public List<AbstractGpmField<?>> createFields() {
        final List<AbstractGpmField<?>> lGpmFields =
                new LinkedList<AbstractGpmField<?>>();

        // Convert UiFields to GpmFields
        for (final UiField lUiField : uiFields) {
            final AbstractGpmField<?> lGpmField;

            lGpmField = createField(lUiField, true);
            lGpmFields.add(lGpmField);

        }
        return lGpmFields;
    }

    /**
     * Create field widget from UiField
     * 
     * @param pUiField
     *            the UiField
     * @return the field widget
     */
    private AbstractGpmField<?> createField(final UiField pUiField,
            boolean pAddValidation) {
        final AbstractGpmField<?> lGpmField;

        if (pUiField.isUpdatable()) {
            lGpmField = createEditionField(pUiField);
        }
        else {
            lGpmField = createVisualizationField(pUiField);
        }
        lGpmField.setFieldName(pUiField.getFieldName());
        lGpmField.setTranslatedFieldName(pUiField.getTranslatedFieldName());
        lGpmField.setFieldDescription(pUiField.getFieldDescription());
        lGpmField.setMandatory(pUiField.isMandatory());
        lGpmField.copy(pUiField);

        if (pUiField.isUpdatable() && pAddValidation) {
            // Only add validator on updatable fields
            validator.addValidation(lGpmField, RuleManager.getRules(pUiField));
        }

        return lGpmField;
    }

    private AbstractGpmField<?> createVisualizationField(final UiField pField) {
        final AbstractGpmField<?> lGpmField;

        switch (pField.getFieldType()) {
            case STRING:
                switch (((UiStringField) pField).getStringDisplayHintType()) {
                    case INTERNAL_URL:
                        lGpmField =
                                new GpmAnchor(
                                        Application.INJECTOR.getSheetVisualizationPresenter().getInternalUrlClickHandler());
                        ((GpmAnchor) lGpmField).setInternalUrlSheet(
                                pField.getAsString(),
                                ((UiStringField) pField).getInternalUrlSheetReference());
                        break;
                    case EXTERNAL_WEB_CONTENT:
                        final GpmExternalContent lFrame =
                                new GpmExternalContent();

                        lFrame.setExternalContent(((UiStringField) pField).get());
                        lFrame.setWidth(((UiStringField) pField).getWidth());
                        lFrame.setHeight(((UiStringField) pField).getHeight());
                        lGpmField = lFrame;
                        break;
                    case GRID:
                        throw new IllegalStateException(
                                "Not implemented method");
                    case MULTI_LINE:
                        lGpmField =
                                new GpmRichTextLabel(buildNewLineFormatter());
                        break;
                    case RICH_TEXT:
                        lGpmField = new GpmRichTextLabel();
                        break;
                    case URL:
                        GpmUrlBuilder.setIUnsupportedProtocol(new UrlRule());
                        lGpmField =
                                new GpmAnchor(new GpmUrlBuilder(
                                        pField.getAsString()),
                                        GpmAnchorTarget.BLANK);
                        ((GpmAnchor) lGpmField).setAsString(pField.getAsString());

                        break;
                    case APPLET:
                        SheetEditionJsAccessor.defineBridge();
                        UiAppletField lUiAppletField = (UiAppletField) pField;

                        lGpmField =
                                new GpmApplet(new GpmAppletBuilder(
                                        lUiAppletField.getAppletName(),
                                        lUiAppletField.getAppletAlter(),
                                        lUiAppletField.getAppletArchive(),
                                        lUiAppletField.getAppletCode(),
                                        lUiAppletField.getAppletCodeBase()),
                                        lUiAppletField.getSheetId(),
                                        new SheetEditionJsAccessor(),
                                        lUiAppletField.getAppletParameterList());

                        ((GpmApplet) lGpmField).setAsString(pField.getAsString());
                        ((GpmApplet) lGpmField).setEdited(false);
                        break;

                    default:
                        lGpmField =
                                new GpmLabel<String>(
                                        GpmStringFormatter.getInstance());
                        break;

                }
                break;
            case INTEGER:
                lGpmField =
                        new GpmLabel<Integer>(GpmIntegerFormatter.getInstance());
                break;
            case REAL:
                lGpmField =
                        new GpmLabel<Double>(GpmDoubleFormatter.getInstance());
                break;
            case BOOLEAN:
                lGpmField = new GpmCheckBox(false);
                break;
            case DATE:
                switch (((UiDateField) pField).getDateDisplayHintType()) {
                    case DATE_SHORT:
                        lGpmField =
                                new GpmLabel<Date>(
                                        GpmShortDateFormatter.getInstance());
                        break;
                    case DATE_MEDIUM:
                        lGpmField =
                                new GpmLabel<Date>(
                                        GpmMediumDateFormatter.getInstance());
                        break;
                    case DATE_LONG:
                        lGpmField =
                                new GpmLabel<Date>(
                                        GpmLongDateFormatter.getInstance());
                        break;
                    case DATE_FULL:
                        lGpmField =
                                new GpmLabel<Date>(
                                        GpmFullDateFormatter.getInstance());
                        break;
                    case DATE_TIME_SHORT:
                        lGpmField =
                                new GpmLabel<Date>(
                                        GpmShortDateTimeFormatter.getInstance());
                        break;
                    case DATE_TIME_MEDIUM:
                        lGpmField =
                                new GpmLabel<Date>(
                                        GpmMediumDateTimeFormatter.getInstance());
                        break;
                    case DATE_TIME_LONG:
                        lGpmField =
                                new GpmLabel<Date>(
                                        GpmLongDateTimeFormatter.getInstance());
                        break;
                    case DATE_TIME_FULL:
                        lGpmField =
                                new GpmLabel<Date>(
                                        GpmFullDateTimeFormatter.getInstance());
                        break;
                    default:
                        lGpmField = null;
                }
                break;
            case CHOICE:
                final UiChoiceField lChoice = (UiChoiceField) pField;

                List<Translation> lTextTranslations =
                        new ArrayList<Translation>();
                List<Translation> lImageTranslations =
                        new ArrayList<Translation>();
                for (Translation lPossibleValue : lChoice.getPossibleValues()) {
                    lTextTranslations.add(new Translation(
                            lPossibleValue.getValue(),
                            lPossibleValue.getTranslatedValue()));
                    if (lPossibleValue instanceof UiChoiceFieldValue) {
                        lImageTranslations.add(new Translation(
                                lPossibleValue.getValue(),
                                ((UiChoiceFieldValue) lPossibleValue).getTranslatedImageValue()));
                    }
                }

                switch (lChoice.getChoiceDisplayHintType()) {
                    case LIST:
                    case NOT_LIST:
                        lGpmField =
                                new GpmLabel<String>(
                                        GpmStringFormatter.getInstance());
                        ((GpmLabel<?>) lGpmField).setPossibleValues(lTextTranslations);
                        break;
                    case NOT_LIST_IMAGE:
                        lGpmField = new GpmImageAndText(lImageTranslations);
                        break;
                    case NOT_LIST_IMAGE_TEXT:
                        lGpmField = new GpmImageAndText(lImageTranslations);
                        ((GpmImageAndText) lGpmField).setPossibleTextValues(lTextTranslations);
                        break;
                    case TREE:
                        lGpmField = new GpmTreeChoiceField(false);
                        ((GpmTreeChoiceField) lGpmField).setEnabled(false);
                        ((GpmTreeChoiceField) lGpmField).addTreeRootValues(getTreeChoiceRootValues(lChoice.getPossibleValues()));
                        break;
                    default:
                        lGpmField =
                                new GpmLabel<String>(
                                        GpmStringFormatter.getInstance());
                        break;
                }
                break;
            case MULTIPLE:
                lGpmField =
                        new GpmMultiple(createFields((UiMultipleField) pField));
                break;
            case MULTIVALUED:
                final UiMultivaluedField lMultivaluedField =
                        (UiMultivaluedField) pField;

                switch (lMultivaluedField.getTemplateField().getFieldType()) {
                    case MULTIPLE:
                        lGpmField =
                                new GpmMultipleMultivaluedField(
                                        createFields((BusinessMultipleField) lMultivaluedField.getTemplateField()),
                                        false, false, false, true);
                        break;
                    default:
                        lGpmField =
                                new GpmMultivaluedField<AbstractGpmField<?>>(
                                        createField(
                                                lMultivaluedField.getTemplateField(),
                                                false), false, false, false);
                        break;
                }

                break;
            case POINTER:
                throw new IllegalStateException(
                        "Pointer field cannot be displayed.");
            case ATTACHED:
                lGpmField =
                        new GpmAnchor(createAttachedFieldUrlBuilder(),
                                GpmAnchorTarget.BLANK);
                break;
            case VIRTUAL:
                // Virtual field is display like a string label
            default:
                lGpmField =
                        new GpmLabel<String>(GpmStringFormatter.getInstance());
        }

        return lGpmField;
    }

    private GpmFormatter<String> buildNewLineFormatter() {
        return new GpmFormatter<String>() {
            @Override
            public String format(String pValue) {
                if (pValue == null) {
                    return null;
                }
                else {
                    // Avoid HTML code into label
                    // Replace system new lines with HTML break lines
                    return pValue.replaceAll("<", "&lt;").replaceAll(">",
                            "&gt;").replaceAll("\r\n", "<br/>").replaceAll(
                            "\n", "<br/>").replaceAll("\r", "<br/>");
                }
            }

            @Override
            public String parse(String pValue) {
                // Not used in visualisation
                return null;
            }
        };
    }

    private AbstractGpmField<?> createEditionField(final UiField pField) {
        final AbstractGpmField<?> lGpmField;

        switch (pField.getFieldType()) {
            case STRING:
                final UiStringField lUiStringField = (UiStringField) pField;

                switch (lUiStringField.getStringDisplayHintType()) {
                    case MULTI_LINE:
                        final GpmMultilineBox lGpmMultilineBox =
                                new GpmMultilineBox();

                        lGpmMultilineBox.getWidget().setCharacterWidth(
                                lUiStringField.getWidth());
                        lGpmMultilineBox.getWidget().setVisibleLines(
                                lUiStringField.getHeight());
                        lGpmField = lGpmMultilineBox;
                        break;
                    case GRID:
                        throw new IllegalStateException(
                                "Not implemented field.");
                    case CHOICE_STRING:
                        final UiStringChoiceField lChoiceField =
                                (UiStringChoiceField) pField;
                        final IGpmChoiceBox lGpmChoiceBox;

                        if (lChoiceField.isStrict()) {
                            lGpmChoiceBox = new GpmListBox();
                        }
                        else {
                            lGpmChoiceBox = new GpmDropDownListBox();
                            GpmDropDownListBox lBox =
                                    (GpmDropDownListBox) lGpmChoiceBox;
                            lBox.setPixelWidth(lUiStringField.getWidth());
                        }
                        List<GpmChoiceBoxValue> lPossibleValues =
                                new ArrayList<GpmChoiceBoxValue>();
                        for (String lPossibleValue : lChoiceField.getPossibleValues()) {
                            lPossibleValues.add(new GpmChoiceBoxValue(
                                    lPossibleValue, lPossibleValue));
                        }
                        lGpmChoiceBox.setPossibleValues(lPossibleValues);
                        lGpmField = (AbstractGpmField<?>) lGpmChoiceBox;
                        break;
                    case INTERNAL_URL:
                        lGpmField =
                                new GpmLabel<String>(
                                        GpmStringFormatter.getInstance());
                        break;
                    case RICH_TEXT:
                        final GpmRichTextBox lRichTextBox =
                                new GpmRichTextBox();

                        lRichTextBox.setHeight(Integer.toString(lUiStringField.getHeight()));
                        lRichTextBox.setWidth(Integer.toString(lUiStringField.getWidth()));
                        lGpmField = lRichTextBox;
                        break;
                    case APPLET:
                        SheetEditionJsAccessor.defineBridge();
                        UiAppletField lUiAppletField = (UiAppletField) pField;
                        lGpmField =
                                new GpmApplet(new GpmAppletBuilder(
                                        lUiAppletField.getAppletName(),
                                        lUiAppletField.getAppletAlter(),
                                        lUiAppletField.getAppletArchive(),
                                        lUiAppletField.getAppletCode(),
                                        lUiAppletField.getAppletCodeBase()),
                                        lUiAppletField.getSheetId(),
                                        new SheetEditionJsAccessor(),
                                        lUiAppletField.getAppletParameterList());

                        ((GpmApplet) lGpmField).setAsString(pField.getAsString());
                        ((GpmApplet) lGpmField).setEdited(true);
                        break;
                    case URL: // default                   
                    case EXTERNAL_WEB_CONTENT:
                    default:
                        final GpmTextBox<String> lTextBox =
                                new GpmTextBox<String>(
                                        GpmStringFormatter.getInstance());

                        lTextBox.setPixelWidth(lUiStringField.getWidth());
                        lTextBox.setSize(lUiStringField.getSize());
                        lGpmField = lTextBox;
                        break;
                }

                break;

            case INTEGER:
                lGpmField =
                        new GpmTextBox<Integer>(
                                GpmIntegerFormatter.getInstance());
                break;
            case REAL:
                lGpmField =
                        new GpmTextBox<Double>(GpmDoubleFormatter.getInstance());
                break;
            case BOOLEAN:
                lGpmField = new GpmCheckBox(true);
                break;
            case DATE:
                switch (((UiDateField) pField).getDateDisplayHintType()) {
                    case DATE_SHORT:
                        lGpmField =
                                new GpmDateBox(
                                        DateTimeFormat.getShortDateFormat());
                        break;
                    case DATE_MEDIUM:
                        lGpmField =
                                new GpmDateBox(
                                        DateTimeFormat.getMediumDateFormat());
                        break;
                    case DATE_LONG:
                        lGpmField =
                                new GpmDateBox(
                                        DateTimeFormat.getLongDateFormat());
                        break;
                    case DATE_FULL:
                        lGpmField =
                                new GpmDateBox(
                                        DateTimeFormat.getFullDateFormat());
                        break;
                    case DATE_TIME_SHORT:
                        lGpmField =
                                new GpmDateTimeBox(
                                        DateTimeFormat.getShortDateFormat());
                        break;
                    case DATE_TIME_MEDIUM:
                        lGpmField =
                                new GpmDateTimeBox(
                                        DateTimeFormat.getMediumDateFormat());
                        break;
                    case DATE_TIME_LONG:
                        lGpmField =
                                new GpmDateTimeBox(
                                        DateTimeFormat.getLongDateFormat());
                        break;
                    case DATE_TIME_FULL:
                        lGpmField =
                                new GpmDateTimeBox(
                                        DateTimeFormat.getFullDateFormat());
                        break;
                    default:
                        lGpmField = null;
                }
                break;
            case CHOICE:
                final UiChoiceField lChoiceField = (UiChoiceField) pField;
                final IGpmChoiceBox lChoiceFieldBox;

                switch (lChoiceField.getChoiceDisplayHintType()) {
                    case TREE:
                        final GpmTreeChoiceField lTreeChoice =
                                new GpmTreeChoiceField(true);
                        lTreeChoice.addTreeRootValues(getTreeChoiceRootValues(lChoiceField.getPossibleValues()));
                        lGpmField = lTreeChoice;
                        break;
                    case LIST:
                        lChoiceFieldBox = new GpmListBox();
                        lChoiceFieldBox.setPossibleValues(getChoicePossibleValues(lChoiceField.getPossibleValues()));
                        lGpmField = (AbstractGpmField<?>) lChoiceFieldBox;
                        break;
                    default:
                        lChoiceFieldBox =
                                new GpmRadioBox(
                                        ((UiChoiceField) pField).getChoiceDisplayHintType());
                        lChoiceFieldBox.setPossibleValues(getChoicePossibleValues(lChoiceField.getPossibleValues()));
                        lGpmField = (AbstractGpmField<?>) lChoiceFieldBox;
                        break;
                }
                break;
            case MULTIPLE:
                lGpmField =
                        new GpmMultiple(createFields((UiMultipleField) pField));
                break;
            case MULTIVALUED:
                final UiMultivaluedField lMultivaluedField =
                        (UiMultivaluedField) pField;

                switch (lMultivaluedField.getTemplateField().getFieldType()) {
                    case MULTIPLE:
                        lGpmField =
                                new GpmMultipleMultivaluedField(
                                        createFields((BusinessMultipleField) lMultivaluedField.getTemplateField()),
                                        pField.isUpdatable(),
                                        pField.isUpdatable(),
                                        pField.isUpdatable(), true);
                        break;
                    case CHOICE:
                        final UiChoiceField lMultivaluedChoiceField =
                                (UiChoiceField) lMultivaluedField.getTemplateField();

                        switch (lMultivaluedChoiceField.getChoiceDisplayHintType()) {
                            case LIST:
                                lGpmField = new GpmMultivaluedListBox();
                                ((IGpmChoiceBox) lGpmField).setPossibleValues(getChoicePossibleValues(lMultivaluedChoiceField.getPossibleValues()));
                                break;
                            case TREE:
                                lGpmField = new GpmMultivaluedTreeChoiceField();
                                ((GpmMultivaluedTreeChoiceField) lGpmField).addTreeRootValues(getTreeChoiceRootValues(lMultivaluedChoiceField.getPossibleValues()));
                                break;
                            default:
                                lGpmField = new GpmMultivaluedCheckBox();
                                ((IGpmChoiceBox) lGpmField).setPossibleValues(getChoicePossibleValues(lMultivaluedChoiceField.getPossibleValues()));
                                break;
                        }
                        break;
                    default:
                        lGpmField =
                                new GpmMultivaluedField<AbstractGpmField<?>>(
                                        createField(
                                                lMultivaluedField.getTemplateField(),
                                                false), true, true, true);
                        break;
                }
                break;
            case POINTER:
                throw new IllegalStateException(
                        "Pointer field cannot be displayed.");
            case ATTACHED:
                lGpmField =
                        new GpmUploadFile(uploadFileRegister,
                                createAttachedFieldUrlBuilder(),
                                GpmAnchorTarget.BLANK);
                List<String> lAttachedFilenamesInError = new ArrayList<String>();
                if (attachedFieldsInError != null) {
	                for (UIAttachmentException ex : attachedFieldsInError) {
	                	if (!(ex instanceof UIAttachmentTotalSizeException)) {
	                		lAttachedFilenamesInError.add(ex.getItem());
	                	}
	                }
                }
                ((GpmUploadFile) lGpmField).setAttachedFilenamesInError(lAttachedFilenamesInError);
                break;
            case VIRTUAL:
                // Virtual field is display like a string label
            default:
                lGpmField =
                        new GpmLabel<String>(GpmStringFormatter.getInstance());
                break;
        }
        return lGpmField;
    }

    private GpmUrlBuilder createAttachedFieldUrlBuilder() {
        final GpmUrlBuilder lUrlBuilder =
                new GpmUrlBuilder(Application.DOWNLOAD_URL);
        final ProductWorkspacePresenter lCurrentProductWorkspace =
                Application.INJECTOR.getUserSpacePresenter().getCurrentProductWorkspace();
        final String lProductName = lCurrentProductWorkspace.getTabId();

        // TODO : if container is a product, don't send the product name
        lUrlBuilder.setParameter(DownloadParameter.PRODUCT_NAME, lProductName);
        lUrlBuilder.setParameter(DownloadParameter.TYPE,
                DownloadParameter.TYPE_ATTACHED_FILE.name().toLowerCase());

        return lUrlBuilder;
    }

    private List<AbstractGpmField<?>> createFields(
            final BusinessMultipleField pMultiple) {
        final List<AbstractGpmField<?>> lFields =
                new LinkedList<AbstractGpmField<?>>();

        for (BusinessField lSubField : pMultiple) {
            lFields.add(createField((UiField) lSubField, true));
        }

        return lFields;
    }

    /**
     * Get the validator.
     * 
     * @return The validator.
     */
    public Validator getValidator() {
        return validator;
    }

    /**
     * Get the upload register.
     * 
     * @return The upload register.
     */
    public GpmUploadFileRegister getUploadRegister() {
        return uploadFileRegister;
    }

    /**
     * Convert possible values of tree choice fields
     * 
     * @param pList
     *            the facade possible values
     * @return the component possible values
     */
    private List<GpmTreeChoiceFieldItem> getTreeChoiceRootValues(
            List<Translation> pList) {
        List<GpmTreeChoiceFieldItem> lRootValues =
                new ArrayList<GpmTreeChoiceFieldItem>(pList.size());

        for (Translation lPossibleValue : pList) {
            lRootValues.add(createTreeItemsRecursive((UiChoiceTreeFieldValue) lPossibleValue));
        }
        return lRootValues;
    }

    /**
     * Create recursively the Item and its sub items
     * 
     * @param pValue
     *            the root UI item
     * @return the transformed item for the field
     */
    private GpmTreeChoiceFieldItem createTreeItemsRecursive(
            UiChoiceTreeFieldValue pValue) {
        GpmTreeChoiceFieldItem lItem =
                new GpmTreeChoiceFieldItem(pValue.getTranslatedValue(),
                        pValue.getValue(), pValue.isSelectable());
        if (pValue.getChildren() != null) {
            for (UiChoiceTreeFieldValue lValue : pValue.getChildren()) {
                lItem.addSubItem(createTreeItemsRecursive(lValue));
            }
        }
        return lItem;
    }

    /**
     * Convert possible value of choice fields
     * 
     * @param pList
     *            the facade possible values
     * @return the component possible values
     */
    private List<GpmChoiceBoxValue> getChoicePossibleValues(
            List<Translation> pList) {
        List<GpmChoiceBoxValue> lPossibleValues =
                new ArrayList<GpmChoiceBoxValue>(pList.size());

        for (Translation lPossibleValue : pList) {
            lPossibleValues.add(new GpmChoiceBoxValue(
                    lPossibleValue.getValue(),
                    lPossibleValue.getTranslatedValue(),
                    ((UiChoiceFieldValue) lPossibleValue).getTranslatedImageValue()));
        }
        return lPossibleValues;
    }

    /**
     * Get the error attached field list
     * 
     * @return the error attached field list
     */
    public List<UIAttachmentException> getAttachedFieldsInError() {
        return attachedFieldsInError;
    }

    /**
     * Set the error attached field List
     * 
     * @param pAttachedFieldsInError
     *            The list to set
     */
    public void setAttachedFieldsInError(
    		List<UIAttachmentException> pAttachedFieldsInError) {
        this.attachedFieldsInError = pAttachedFieldsInError;
    }
}
