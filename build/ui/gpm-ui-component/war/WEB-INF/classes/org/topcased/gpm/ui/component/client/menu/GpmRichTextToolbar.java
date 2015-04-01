/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.menu;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.HashMap;

import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.button.GpmImageToggleButton;
import org.topcased.gpm.ui.component.client.layout.GpmFlowLayoutPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.RichTextArea.Formatter;

/**
 * GpmRichTextToolbar is a static toolbar used for gwt rich text area.
 * <p>
 * Note : The implementation is based on RichtextToolBar code provided from gwt
 * showcase : https://code.google.com/p/richtexttoolbar/
 * </p>
 * 
 * @author frosier
 */
public class GpmRichTextToolbar extends GpmFlowLayoutPanel {
    private final static String HTML_SPACE = "&nbsp;";

    // Images
    private static final ImageResource IMAGE_BOLD =
            INSTANCE.images().richTextBold();

    private static final ImageResource IMAGE_ITALIC =
            INSTANCE.images().richTextItalic();

    private static final ImageResource IMAGE_UNDERLINE =
            INSTANCE.images().richTextUnderline();

    private static final ImageResource IMAGE_ALIGN_LEFT =
            INSTANCE.images().richTextAlignLeft();

    private static final ImageResource IMAGE_ALIGN_MIDDLE =
            INSTANCE.images().richTextAlignMiddle();

    private static final ImageResource IMAGE_ALIGN_RIGHT =
            INSTANCE.images().richTextAlignRight();

    private static final ImageResource IMAGE_ORDERED_LIST =
            INSTANCE.images().richTextOrderList();

    private static final ImageResource IMAGE_UNORDERED_LIST =
            INSTANCE.images().richTextUnorderList();

    private static final ImageResource IMAGE_INDENT_LEFT =
            INSTANCE.images().richTextIndentLeft();

    private static final ImageResource IMAGE_INDENT_RIGHT =
            INSTANCE.images().richTextIndentRight();

    private static final ImageResource IMAGE_INSERT_LINK =
            INSTANCE.images().richTextInsertUrl();

    private static final ImageResource IMAGE_INSERT_IMAGE =
            INSTANCE.images().richTextInsertImage();

    private static final ImageResource IMAGE_REMOVE_FORMATTING =
            INSTANCE.images().richTextRemoveFormatting();

    private static final ImageResource IMAGE_INSERT_LINE =
            INSTANCE.images().richTextInsertLine();

    private static final String CSS_TOOL_LINE_NAME =
            ComponentResources.INSTANCE.css().gpmRichTextToolbarLine();

    //Color and Fontlists - First Value (key) is the Name to display, 
    //Second Value (value) is the HTML-Definition
    public final static HashMap<String, String> GUI_COLORLIST =
            new HashMap<String, String>();
    static {
        GUI_COLORLIST.put("White", "#FFFFFF");
        GUI_COLORLIST.put("Black", "#000000");
        GUI_COLORLIST.put("Red", "red");
        GUI_COLORLIST.put("Green", "green");
        GUI_COLORLIST.put("Yellow", "yellow");
        GUI_COLORLIST.put("Blue", "blue");
    }

    public final static HashMap<String, String> GUI_FONTLIST =
            new HashMap<String, String>();
    static {
        GUI_FONTLIST.put("Times New Roman", "Times New Roman");
        GUI_FONTLIST.put("Arial", "Arial");
        GUI_FONTLIST.put("Courier New", "Courier New");
        GUI_FONTLIST.put("Georgia", "Georgia");
        GUI_FONTLIST.put("Trebuchet", "Trebuchet");
        GUI_FONTLIST.put("Verdana", "Verdana");
    }

    //GUI Related stuff
    private static final String GUI_DIALOG_INSERTURL =
            CONSTANTS.richTextInsertUrl();

    private static final String GUI_DIALOG_IMAGEURL =
            CONSTANTS.richTextInsertImage();

    private static final String GUI_LISTNAME_COLORS =
            CONSTANTS.richTextTooltipColors();

    private static final String GUI_LISTNAME_FONTS =
            CONSTANTS.richTextTooltipFonts();

    private static final String GUI_HOVERTEXT_REMOVEFORMAT =
            CONSTANTS.richTextTooltipClearFormat();

    private static final String GUI_HOVERTEXT_IMAGE =
            CONSTANTS.richTextTooltipImage();

    private static final String GUI_HOVERTEXT_HLINE =
            CONSTANTS.richTextTooltipHorizontalBar();

    private static final String GUI_HOVERTEXT_LINK =
            CONSTANTS.richTextTooltipUrl();

    private static final String GUI_HOVERTEXT_IDENTLEFT =
            CONSTANTS.richTextTooltipIndentLeft();

    private static final String GUI_HOVERTEXT_IDENTRIGHT =
            CONSTANTS.richTextTooltipIndentRight();

    private static final String GUI_HOVERTEXT_UNORDERLIST =
            CONSTANTS.richTextTooltipBullet();

    private static final String GUI_HOVERTEXT_ORDERLIST =
            CONSTANTS.richTextTooltipBulletNumbered();

    private static final String GUI_HOVERTEXT_ALIGNRIGHT =
            CONSTANTS.richTextTooltipAlignRight();

    private static final String GUI_HOVERTEXT_ALIGNCENTER =
            CONSTANTS.richTextTooltipAlignCenter();

    private static final String GUI_HOVERTEXT_ALIGNLEFT =
            CONSTANTS.richTextTooltipAlignLeft();

    private static final String GUI_HOVERTEXT_UNDERLINE =
            CONSTANTS.richTextTooltipUnderline();

    private static final String GUI_HOVERTEXT_ITALIC =
            CONSTANTS.richTextTooltipItalic();

    private static final String GUI_HOVERTEXT_BOLD =
            CONSTANTS.richTextTooltipBold();

    /** Private Variables **/
    //The two inner (Horizontal)-Panels
    private final GpmFlowLayoutPanel topPanel;

    private final GpmFlowLayoutPanel bottomPanel;

    //The RichTextArea this Toolbar referes to and the Interfaces to access the RichTextArea
    private final RichTextArea styleText;

    private final Formatter styleTextFormatter;

    //We use an internal class of the ClickHandler and the KeyUpHandler 
    //to be private to others with these events
    private final EventHandler evHandler;

    //The Buttons of the Menubar
    private GpmImageToggleButton bold;

    private GpmImageToggleButton italic;

    private GpmImageToggleButton underline;

    private GpmImageButton alignleft;

    private GpmImageButton alignmiddle;

    private GpmImageButton alignright;

    private GpmImageButton orderlist;

    private GpmImageButton unorderlist;

    private GpmImageButton indentleft;

    private GpmImageButton indentright;

    private GpmImageButton generatelink;

    private GpmImageButton insertline;

    private GpmImageButton insertimage;

    private GpmImageButton removeformatting;

    private ListBox fontlist;

    private ListBox colorlist;

    /**
     * Constructor of the Toolbar.
     * 
     * @param pRichtext
     *            The associated RichTextArea.
     */
    public GpmRichTextToolbar(final RichTextArea pRichtext) {
        // Initialize the two inner panels
        topPanel = new GpmFlowLayoutPanel();
        bottomPanel = new GpmFlowLayoutPanel();

        topPanel.addStyleName(CSS_TOOL_LINE_NAME);
        bottomPanel.addStyleName(CSS_TOOL_LINE_NAME);

        //Save the reference to the RichText area we refer to and get the interfaces to the stylings
        styleText = pRichtext;
        styleTextFormatter = styleText.getFormatter();

        // Add the two inner panels to the main panel
        add(topPanel);
        add(bottomPanel);

        evHandler = new EventHandler();

        // Add KeyUp and Click-Handler to the RichText, 
        // so that we can actualize the toolbar if neccessary
        styleText.addKeyUpHandler(evHandler);
        styleText.addClickHandler(evHandler);

        // Now lets fill the new toolbar with life
        buildTools();
    }

    /** Click Handler of the Toolbar **/
    private class EventHandler implements ClickHandler, KeyUpHandler,
            ChangeHandler {
        public void onClick(ClickEvent pEvent) {
            if (pEvent.getSource().equals(bold)) {
                styleTextFormatter.toggleBold();
            }
            else if (pEvent.getSource().equals(italic)) {
                styleTextFormatter.toggleItalic();
            }
            else if (pEvent.getSource().equals(underline)) {
                styleTextFormatter.toggleUnderline();
            }
            else if (pEvent.getSource().equals(alignleft)) {
                styleTextFormatter.setJustification(RichTextArea.Justification.LEFT);
            }
            else if (pEvent.getSource().equals(alignmiddle)) {
                styleTextFormatter.setJustification(RichTextArea.Justification.CENTER);
            }
            else if (pEvent.getSource().equals(alignright)) {
                styleTextFormatter.setJustification(RichTextArea.Justification.RIGHT);
            }
            else if (pEvent.getSource().equals(orderlist)) {
                styleTextFormatter.insertOrderedList();
            }
            else if (pEvent.getSource().equals(unorderlist)) {
                styleTextFormatter.insertUnorderedList();
            }
            else if (pEvent.getSource().equals(indentright)) {
                styleTextFormatter.rightIndent();
            }
            else if (pEvent.getSource().equals(indentleft)) {
                styleTextFormatter.leftIndent();
            }
            else if (pEvent.getSource().equals(insertline)) {
                styleTextFormatter.insertHorizontalRule();
            }
            else if (pEvent.getSource().equals(generatelink)) {
                final String lUrl =
                        Window.prompt(GUI_DIALOG_INSERTURL, "http://");
                if (lUrl != null) {
                    styleTextFormatter.createLink(lUrl);
                }
            }
            else if (pEvent.getSource().equals(insertimage)) {
                final String lUrl =
                        Window.prompt(GUI_DIALOG_IMAGEURL, "http://");
                if (lUrl != null) {
                    styleTextFormatter.insertImage(lUrl);
                }
            }
            else if (pEvent.getSource().equals(removeformatting)) {
                styleTextFormatter.removeFormat();
            }
            updateStatus();
        }

        public void onKeyUp(final KeyUpEvent pEvent) {
            updateStatus();
        }

        public void onChange(final ChangeEvent pEvent) {
            if (pEvent.getSource().equals(fontlist)) {
                styleTextFormatter.setFontName(fontlist.getValue(fontlist.getSelectedIndex()));
            }
            else if (pEvent.getSource().equals(colorlist)) {
                styleTextFormatter.setForeColor(colorlist.getValue(colorlist.getSelectedIndex()));
            }
        }
    }

    /**
     * Native JavaScript that returns the selected text and position of the
     * start.
     * 
     * @param pElem
     *            The javascript object element node.
     * @return The selected text and its position.
     */
    public static native JsArrayString getSelection(Element pElem) /*-{
                                                                   var txt = "";
                                                                   var pos = 0;
                                                                   var range;
                                                                   var parentElement;
                                                                   var container;

                                                                   if (pElem.contentWindow.getSelection) {
                                                                   txt = pElem.contentWindow.getSelection();
                                                                   pos = pElem.contentWindow.getSelection().getRangeAt(0).startOffset;
                                                                   } else if (pElem.contentWindow.document.getSelection) {
                                                                   txt = pElem.contentWindow.document.getSelection();
                                                                   pos = pElem.contentWindow.document.getSelection().getRangeAt(0).startOffset;
                                                                   } else if (pElem.contentWindow.document.selection) {
                                                                   range = pElem.contentWindow.document.selection.createRange();
                                                                   txt = range.text;
                                                                   parentElement = range.parentElement();
                                                                   container = range.duplicate();
                                                                   container.moveToElementText(parentElement);
                                                                   container.setEndPoint('EndToEnd', range);
                                                                   pos = container.text.length - range.text.length;
                                                                   }
                                                                   return [""+txt,""+pos];
                                                                   }-*/;

    /** Method called to toggle the style in HTML-Mode **/

    /**
     * Private method to set the toggle buttons and disable/enable buttons which
     * do not work in html-mode
     **/
    private void updateStatus() {
        if (styleTextFormatter != null) {
            bold.setDown(styleTextFormatter.isBold());
            italic.setDown(styleTextFormatter.isItalic());
            underline.setDown(styleTextFormatter.isUnderlined());
        }

        removeformatting.setEnabled(true);
        indentleft.setEnabled(true);
    }

    /** Initialize the options on the toolbar **/
    private void buildTools() {

        //Init the TOP Panel first
        bold = new GpmImageToggleButton(IMAGE_BOLD);
        italic = new GpmImageToggleButton(IMAGE_ITALIC);
        underline = new GpmImageToggleButton(IMAGE_UNDERLINE);
        alignleft = new GpmImageButton(IMAGE_ALIGN_LEFT);
        alignmiddle = new GpmImageButton(IMAGE_ALIGN_MIDDLE);
        alignright = new GpmImageButton(IMAGE_ALIGN_RIGHT);
        orderlist = new GpmImageButton(IMAGE_ORDERED_LIST);
        unorderlist = new GpmImageButton(IMAGE_UNORDERED_LIST);
        indentright = new GpmImageButton(IMAGE_INDENT_RIGHT);
        indentleft = new GpmImageButton(IMAGE_INDENT_LEFT);
        generatelink = new GpmImageButton(IMAGE_INSERT_LINK);
        insertline = new GpmImageButton(IMAGE_INSERT_LINE);
        insertimage = new GpmImageButton(IMAGE_INSERT_IMAGE);
        removeformatting = new GpmImageButton(IMAGE_REMOVE_FORMATTING);

        bold.setTitle(GUI_HOVERTEXT_BOLD);
        italic.setTitle(GUI_HOVERTEXT_ITALIC);
        underline.setTitle(GUI_HOVERTEXT_UNDERLINE);
        alignleft.setTitle(GUI_HOVERTEXT_ALIGNLEFT);
        alignmiddle.setTitle(GUI_HOVERTEXT_ALIGNCENTER);
        alignright.setTitle(GUI_HOVERTEXT_ALIGNRIGHT);
        orderlist.setTitle(GUI_HOVERTEXT_ORDERLIST);
        unorderlist.setTitle(GUI_HOVERTEXT_UNORDERLIST);
        indentleft.setTitle(GUI_HOVERTEXT_IDENTLEFT);
        indentright.setTitle(GUI_HOVERTEXT_IDENTRIGHT);
        generatelink.setTitle(GUI_HOVERTEXT_LINK);
        insertline.setTitle(GUI_HOVERTEXT_HLINE);
        insertimage.setTitle(GUI_HOVERTEXT_IMAGE);
        removeformatting.setTitle(GUI_HOVERTEXT_REMOVEFORMAT);

        bold.addClickHandler(evHandler);
        italic.addClickHandler(evHandler);
        underline.addClickHandler(evHandler);
        alignleft.addClickHandler(evHandler);
        alignright.addClickHandler(evHandler);
        alignmiddle.addClickHandler(evHandler);
        orderlist.addClickHandler(evHandler);
        unorderlist.addClickHandler(evHandler);
        indentleft.addClickHandler(evHandler);
        indentright.addClickHandler(evHandler);
        generatelink.addClickHandler(evHandler);
        insertline.addClickHandler(evHandler);
        insertimage.addClickHandler(evHandler);
        removeformatting.addClickHandler(evHandler);

        addToPanel(topPanel, new HTML(HTML_SPACE));
        addToPanel(topPanel, bold);
        addToPanel(topPanel, italic);
        addToPanel(topPanel, underline);

        addToPanel(topPanel, new HTML(HTML_SPACE));
        addToPanel(topPanel, alignleft);
        addToPanel(topPanel, alignmiddle);
        addToPanel(topPanel, alignright);

        addToPanel(topPanel, new HTML(HTML_SPACE));
        addToPanel(topPanel, orderlist);
        addToPanel(topPanel, unorderlist);
        addToPanel(topPanel, indentright);
        addToPanel(topPanel, indentleft);

        addToPanel(topPanel, new HTML(HTML_SPACE));
        addToPanel(topPanel, generatelink);

        addToPanel(topPanel, new HTML(HTML_SPACE));
        addToPanel(topPanel, insertline);
        addToPanel(topPanel, insertimage);

        addToPanel(topPanel, new HTML(HTML_SPACE));
        addToPanel(topPanel, removeformatting);
        addToPanel(topPanel, new HTML(HTML_SPACE));

        //Init the BOTTOM Panel
        fontlist = createFontList();
        colorlist = createColorList();

        addToPanel(bottomPanel, new HTML(HTML_SPACE));
        addToPanel(bottomPanel, fontlist);
        addToPanel(bottomPanel, new HTML(HTML_SPACE));
        addToPanel(bottomPanel, colorlist);
    }

    /**
     * Add an element to the panel, affecting its property to display on the
     * panel
     * 
     * @param pPanel
     *            the panel into which you want to add an element
     * @param pWidget
     *            the Widget to add
     */
    private void addToPanel(GpmFlowLayoutPanel pPanel, Widget pWidget) {
        pPanel.add(pWidget);
        pWidget.getElement().getStyle().setMarginLeft(1, Unit.PX);
        pWidget.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
    }

    /** Method to create the fontlist for the toolbar **/
    private ListBox createFontList() {
        final ListBox lFontListBox = new ListBox();
        lFontListBox.addChangeHandler(evHandler);
        lFontListBox.setVisibleItemCount(1);

        lFontListBox.addItem(GUI_LISTNAME_FONTS);
        for (String lName : GUI_FONTLIST.keySet()) {
            lFontListBox.addItem(lName, GUI_FONTLIST.get(lName));
        }

        return lFontListBox;
    }

    /** Method to create the colorlist for the toolbar **/
    private ListBox createColorList() {
        final ListBox lColorListBox = new ListBox();
        lColorListBox.addChangeHandler(evHandler);
        lColorListBox.setVisibleItemCount(1);

        lColorListBox.addItem(GUI_LISTNAME_COLORS);
        for (String lName : GUI_COLORLIST.keySet()) {
            lColorListBox.addItem(lName, GUI_COLORLIST.get(lName));
        }

        return lColorListBox;
    }

    public Formatter getStyleTextFormatter() {
        return styleTextFormatter;
    }

}
