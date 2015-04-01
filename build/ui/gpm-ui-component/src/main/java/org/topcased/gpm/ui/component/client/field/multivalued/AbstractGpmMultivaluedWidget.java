/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field.multivalued;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.component.client.button.GpmDoubleImageButton;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * An abstract class for multivalued widget.
 * 
 * @author tpanuel
 * @param <E>
 *            The type of element.
 */
public abstract class AbstractGpmMultivaluedWidget<E extends AbstractGpmField<?>>
        extends FlowPanel implements HasBlurHandlers, HasChangeHandlers {
    protected final List<GpmMultivaluedElement<E>> elements;

    private final E template;

    private ClickHandler removeClickHandler;

    private ClickHandler undoClickHandler;

    private ClickHandler upClickHandler;

    private ClickHandler downClickHandler;

    private GpmDoubleImageButton addButton;

    private boolean refreshLaunch;

    protected BlurHandler blurHandler;

    protected ChangeHandler changeHandler;

    private boolean enabled = true;

    /**
     * Create a AbstractGpmMultivaluedWidget.
     * 
     * @param pTemplate
     *            the template.
     * @param pAddButtonAvailable
     *            If the add button is available.
     * @param pRemoveButtonAvailable
     *            If the remove button is available.
     * @param pMoveButtonsAvailable
     *            If the move buttons are available.
     */
    public AbstractGpmMultivaluedWidget(final E pTemplate,
            final boolean pAddButtonAvailable,
            final boolean pRemoveButtonAvailable,
            final boolean pMoveButtonsAvailable) {
        elements = new ArrayList<GpmMultivaluedElement<E>>();
        template = pTemplate;
        refreshLaunch = false;
        final BlurEvent lBlurEvent = new BlurEvent() {

        };

        // Buttons to remove a line
        if (pRemoveButtonAvailable) {
            removeClickHandler = new ClickHandler() {
                @Override
                public void onClick(final ClickEvent pEvent) {
                    elements.get(getId(pEvent)).setEnabled(false);
                    if (blurHandler != null) {
                        blurHandler.onBlur(lBlurEvent);
                    }
                }
            };
            undoClickHandler = new ClickHandler() {
                @Override
                public void onClick(final ClickEvent pEvent) {
                    elements.get(getId(pEvent)).setEnabled(true);
                    if (blurHandler != null) {
                        blurHandler.onBlur(lBlurEvent);
                    }
                }
            };
        }
        // Button to switch line order
        if (pMoveButtonsAvailable) {
            upClickHandler = new ClickHandler() {
                @Override
                public void onClick(final ClickEvent pEvent) {
                    upLine(getId(pEvent) - 1);
                }
            };
            downClickHandler = new ClickHandler() {
                @Override
                public void onClick(final ClickEvent pEvent) {
                    upLine(getId(pEvent));
                }
            };
        }
        // Button to add a line
        if (pAddButtonAvailable) {
            addButton =
                    new GpmDoubleImageButton(INSTANCE.images().add(),
                            INSTANCE.images().addHover());
            addButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(final ClickEvent pEvent) {
                    addLine();
                    if (blurHandler != null) {
                        blurHandler.onBlur(lBlurEvent);
                    }
                }
            });
        }
        // Set style
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmMultivaluedWidget());
        setSubFieldTemplateField();
        // Add a first line
        addLine();
    }

    private void upLine(final int pPosition) {
        final List<GpmMultivaluedElement<E>> lNew =
                new ArrayList<GpmMultivaluedElement<E>>();

        // Add the first elements
        for (int i = 0; i < pPosition; i++) {
            lNew.add(elements.get(i));
        }
        // Switch the two elements
        lNew.add(elements.get(pPosition + 1));
        lNew.add(elements.get(pPosition));
        // Add the last elements
        for (int i = pPosition + 2; i < elements.size(); i++) {
            lNew.add(elements.get(i));
        }
        // Set the list of elements
        elements.clear();
        elements.addAll(lNew);
        launchRefresh();
    }

    private int getId(final ClickEvent pEvent) {
        return Integer.parseInt(((GpmDoubleImageButton) pEvent.getSource()).getElement().getId());
    }

    /**
     * Indicates to the validator of the template field that it is a template
     * field. If template is a multiple multivalued element, iterates on its
     * fields.
     */
    private void setSubFieldTemplateField() {
        // Also indicates the template field validator that it is a template field
        // Not used in mandatory rules for example
        if (template.getFieldValidator() != null) {
            template.getFieldValidator().setTemplateField(true);
        }
        // If multiple multivalued, recurse to subfields
        if (template instanceof GpmMultipleMultivaluedElement) {
            GpmMultipleMultivaluedElement lField =
                    (GpmMultipleMultivaluedElement) template;
            for (AbstractGpmField<?> lElement : lField.getFields()) {
                if (lElement.getFieldValidator() != null) {
                    lElement.getFieldValidator().setTemplateField(true);
                }
            }
        }
    }

    private void launchRefresh() {
        // Refresh launch only one time
        if (!refreshLaunch) {
            DeferredCommand.addCommand(new Command() {
                @Override
                public void execute() {
                    final int lNbElements = elements.size();

                    // Clear panel
                    clear();
                    // Give its position at each element
                    for (int i = 0; i < lNbElements; i++) {
                        elements.get(i).setPosition(i, lNbElements);
                    }
                    // Build panel
                    doRefreshDisplay();
                    // Add button is need
                    if (addButton != null) {
                        add(addButton);
                    }
                    refreshLaunch = false;
                }
            });
            refreshLaunch = true;
        }
    }

    /**
     * Refresh the content and display.
     */
    abstract protected void doRefreshDisplay();

    /**
     * Get the template field.
     * 
     * @return The template field.
     */
    public E getTemplateField() {
        return template;
    }

    /**
     * Get the elements.
     * 
     * @return The elements.
     */
    public List<GpmMultivaluedElement<E>> getElements() {
        return elements;
    }

    /**
     * If the button is available.
     * 
     * @return the addButtonAvailable
     */
    public boolean isAddButtonAvailable() {
        return addButton != null;
    }

    /**
     * If the button is available.
     * 
     * @return the removeButtonAvailable
     */
    public boolean isRemoveButtonAvailable() {
        return removeClickHandler != null;
    }

    /**
     * If the button is available.
     * 
     * @return the moveButtonsAvailable
     */
    public boolean isMoveButtonsAvailable() {
        return upClickHandler != null;
    }

    /**
     * Add a new line.
     * 
     * @return The new element of the new line.
     */
    @SuppressWarnings("unchecked")
    public E addLine() {
        final GpmMultivaluedElement<E> lNewElement;

        // Use the template as the first element
        lNewElement =
                new GpmMultivaluedElement<E>((E) template.getEmptyClone(),
                        removeClickHandler, undoClickHandler, upClickHandler,
                        downClickHandler);
        elements.add(lNewElement);
        // Defer refresh
        launchRefresh();

        if (blurHandler != null) {
            addBlurHandler(blurHandler);
        }
        if (changeHandler != null) {
            addChangeHandler(changeHandler);
        }

        return lNewElement.getElement();
    }

    /**
     * Get the available lines.
     * 
     * @return The available lines.
     */
    public List<E> getAvailableLines() {
        final List<E> lAvailableLines = new ArrayList<E>();

        for (final GpmMultivaluedElement<E> lElement : elements) {
            if (lElement.isEnabled()) {
                lAvailableLines.add(lElement.getElement());
            }
        }

        return lAvailableLines;
    }

    /**
     * Set if the widget is enabled.
     * 
     * @param pEnabled
     *            If the widget is enabled.
     */
    public void setEnabled(boolean pEnabled) {
        enabled = pEnabled;
        for (final GpmMultivaluedElement<E> lElement : elements) {
            lElement.setEnabled(pEnabled);
        }
    }

    /**
     * Remove all the lines.
     */
    public void removeAll() {
        elements.clear();
        launchRefresh();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.ComplexPanel#remove(int)
     */
    @Override
    public boolean remove(final int pIndex) {
        elements.remove(pIndex);
        launchRefresh();
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.HasBlurHandlers#addBlurHandler(com.google.gwt.event.dom.client.BlurHandler)
     */
    @Override
    public HandlerRegistration addBlurHandler(BlurHandler pHandler) {
        blurHandler = pHandler;
        HandlerRegistration lHandlerRegistration = null;
        for (GpmMultivaluedElement<?> lElement : elements) {
            Widget lWidget =
                    ((AbstractGpmField<?>) lElement.getElement()).getWidget();
            if (lWidget instanceof HasBlurHandlers) {
                lHandlerRegistration =
                        ((HasBlurHandlers) lWidget).addBlurHandler(pHandler);
            }
        }
        return lHandlerRegistration;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.HasChangeHandlers#addChangeHandler(com.google.gwt.event.dom.client.ChangeHandler)
     */
    public HandlerRegistration addChangeHandler(ChangeHandler pHandler) {
        changeHandler = pHandler;
        HandlerRegistration lHandlerRegistration = null;
        for (GpmMultivaluedElement<?> lElement : elements) {
            Widget lWidget =
                    ((AbstractGpmField<?>) lElement.getElement()).getWidget();
            if (lWidget instanceof HasChangeHandlers) {
                lHandlerRegistration =
                        ((HasChangeHandlers) lWidget).addChangeHandler(pHandler);
            }
        }
        return lHandlerRegistration;
    }

    /**
     * Indicate if the widget is enabled
     * 
     * @return <CODE>true</CODE> if widget is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
}