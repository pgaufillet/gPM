/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Sébastien René(Atos Origin), Laurent Latil(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.converter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.SystemException;
import org.topcased.gpm.business.serialization.data.Action;
import org.topcased.gpm.business.serialization.data.ActionAccessCtl;
import org.topcased.gpm.business.serialization.data.AdminAccessCtl;
import org.topcased.gpm.business.serialization.data.AppletParameter;
import org.topcased.gpm.business.serialization.data.AttachedDisplayHint;
import org.topcased.gpm.business.serialization.data.AttachedField;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.Attribute;
import org.topcased.gpm.business.serialization.data.Category;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.ChoiceDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceField;
import org.topcased.gpm.business.serialization.data.ChoiceStringDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceTreeDisplayHint;
import org.topcased.gpm.business.serialization.data.ContainerTypeAccessCtl;
import org.topcased.gpm.business.serialization.data.CriteriaGroup;
import org.topcased.gpm.business.serialization.data.Criterion;
import org.topcased.gpm.business.serialization.data.DateDisplayHint;
import org.topcased.gpm.business.serialization.data.DefaultTranslation;
import org.topcased.gpm.business.serialization.data.Dictionary;
import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.serialization.data.DisplayHint;
import org.topcased.gpm.business.serialization.data.Environment;
import org.topcased.gpm.business.serialization.data.ExtendedAction;
import org.topcased.gpm.business.serialization.data.ExtensionPoint;
import org.topcased.gpm.business.serialization.data.ExternDisplayHint;
import org.topcased.gpm.business.serialization.data.FieldAccessCtl;
import org.topcased.gpm.business.serialization.data.FieldRef;
import org.topcased.gpm.business.serialization.data.FieldResult;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.Filter;
import org.topcased.gpm.business.serialization.data.FilterAccessConstraint;
import org.topcased.gpm.business.serialization.data.FilterAccessCtl;
import org.topcased.gpm.business.serialization.data.Gpm;
import org.topcased.gpm.business.serialization.data.GridColumn;
import org.topcased.gpm.business.serialization.data.GridDisplayHint;
import org.topcased.gpm.business.serialization.data.GuiContext;
import org.topcased.gpm.business.serialization.data.InputData;
import org.topcased.gpm.business.serialization.data.InputDataType;
import org.topcased.gpm.business.serialization.data.JAppletDisplayHint;
import org.topcased.gpm.business.serialization.data.Link;
import org.topcased.gpm.business.serialization.data.LinkFilter;
import org.topcased.gpm.business.serialization.data.LinkType;
import org.topcased.gpm.business.serialization.data.LinkTypeRef;
import org.topcased.gpm.business.serialization.data.LinkTypeSorter;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.business.serialization.data.MenuEntry;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.serialization.data.Option;
import org.topcased.gpm.business.serialization.data.PointerField;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.serialization.data.ProcessDefinition;
import org.topcased.gpm.business.serialization.data.Product;
import org.topcased.gpm.business.serialization.data.ProductFilter;
import org.topcased.gpm.business.serialization.data.ProductLink;
import org.topcased.gpm.business.serialization.data.ProductLinkType;
import org.topcased.gpm.business.serialization.data.ProductScope;
import org.topcased.gpm.business.serialization.data.ProductType;
import org.topcased.gpm.business.serialization.data.ProductTypeRef;
import org.topcased.gpm.business.serialization.data.ReportModel;
import org.topcased.gpm.business.serialization.data.RevisionData;
import org.topcased.gpm.business.serialization.data.Role;
import org.topcased.gpm.business.serialization.data.Script;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.serialization.data.SheetFilter;
import org.topcased.gpm.business.serialization.data.SheetLink;
import org.topcased.gpm.business.serialization.data.SheetLinkType;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.serialization.data.SheetTypeAccessCtl;
import org.topcased.gpm.business.serialization.data.SheetTypeRef;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.serialization.data.State;
import org.topcased.gpm.business.serialization.data.TextDisplayHint;
import org.topcased.gpm.business.serialization.data.Transition;
import org.topcased.gpm.business.serialization.data.TransitionAccessCtl;
import org.topcased.gpm.business.serialization.data.Translation;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.business.serialization.data.UserRole;
import org.w3c.dom.Document;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.EncodedByteArrayConverter;
import com.thoughtworks.xstream.core.ReferenceByXPathMarshallingStrategy;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.DomWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppReader;

/**
 * Main XML file converter. This class parse the XML file and creates the
 * corresponding Java objects
 * 
 * @author llatil
 */
public class XMLConverter {

    private static final int MAGIC_SIZE = 4;

    /** Hexadecimal value of '<' */
    private static final int HEXA_0X3C = 0x3C;

    /** Hexadecimal value of '254' */
    private static final int HEXA_0XFE = 0xFE;

    /** Hexadecimal value of '255' */
    private static final int HEXA_0XFF = 0xFF;

    private static final String UTF_8_ENCODING = "UTF-8";

    private static final String UTF_16BE_ENCODING = "UTF-16BE";

    private static final String UTF_16LE_ENCODING = "UTF-16LE";

    private static final String DEFAULT_ENCODING = UTF_8_ENCODING;

    /** The instance definition format version. */
    public static final String INSTANCE_FMT_VERSION = "1.2";

    private static final String XML_HEADER =
            "<?xml version=\"1.0\" encoding=\"{0}\"?>\n\n";

    private static final String XML_HEADER_REGEXP =
            "<\\?xml version=\"1.0\" encoding=\"(.*)\"\\?>.*";

    private static final int XML_HEADER_MAX_LENGTH = 255;

    public static final String XML_ROOT_NODE = "gpm";

    public static final String XML_NS = "xmlns";

    public static final String XML_NS_VALUE =
            "http://www.airbus.com/topcased/gPM";

    public static final String XML_NS_XSI = "xmlns:xsi";

    public static final String XML_NS_XSI_VALUE =
            "http://www.w3.org/2001/XMLSchema-instance";

    public static final String XSI_SHEMA_LOCATION = "xsi:schemaLocation";

    public static final String XSI_SHEMA_LOCATION_VALUE =
            "http://www.airbus.com/topcased/gPM";

    /** The xstream facade. */
    private XStream xstream;

    /** The object output stream. */
    private ObjectOutputStream objectOutputStream;

    /** The hierarchical stream writer. */
    //do not close this stream - it's managed by another object   
    private HierarchicalStreamWriter hierarchicalStreamWriter;

    /** Hierarchical stream reader. */
    //do not close this stream - it's managed by another object   
    private HierarchicalStreamReader hierarchicalStreamReader;

    /** Output stream to write the serialized XML */
    //do not close this stream - it's managed by another object   
    private GpmOutputStreamWriter contentStreamWriter;

    /** Input stream to read the XML content */
    //do not close this stream - it's managed by another object   
    private InputStreamReader contentStreamReader;

    private boolean isHeaderWritten = false;

    /** The log4j logger object for this class. */
    private static Logger staticLOGGER = Logger.getLogger(XMLConverter.class);

    /**
     * The annotated classes
     */
    private static final Class<?>[] ANNOTATED_CLASSES =
            { Gpm.class, Option.class, Action.class, CriteriaGroup.class,
             Translation.class, DefaultTranslation.class, /* Message.class, */
             DisplayGroup.class, Attribute.class, Lock.class, GridColumn.class,
             DisplayHint.class, GridDisplayHint.class,
             AttachedDisplayHint.class, ExternDisplayHint.class,
             DateDisplayHint.class, ChoiceDisplayHint.class,
             ChoiceTreeDisplayHint.class, ChoiceStringDisplayHint.class,
             TextDisplayHint.class, SimpleField.class, ChoiceField.class,
             AttachedField.class, MultipleField.class, PointerField.class,
             FieldRef.class, ProductType.class, SheetType.class,
             LinkType.class, ProductLinkType.class, SheetLinkType.class,
             InputDataType.class, LinkTypeRef.class, LinkTypeSorter.class,
             SheetTypeRef.class, State.class, Product.class,
             ProductTypeRef.class, SheetData.class, InputData.class,
             Link.class, SheetLink.class, ProductLink.class, Lock.class,
             LinkTypeSorter.class, FieldValueData.class,
             AttachedFieldValueData.class, PointerFieldValueData.class,
             Dictionary.class, Environment.class, Category.class,
             CategoryValue.class, User.class, Role.class, UserRole.class,
             ExtensionPoint.class, Script.class, Action.class,
             ExtendedAction.class, MenuEntry.class, GuiContext.class,
             ContainerTypeAccessCtl.class, SheetTypeAccessCtl.class,
             ActionAccessCtl.class, FieldAccessCtl.class,
             TransitionAccessCtl.class, AdminAccessCtl.class, Filter.class,
             SheetFilter.class, ProductFilter.class, LinkFilter.class,
             CriteriaGroup.class, Criterion.class, FieldResult.class,
             ProductScope.class, ReportModel.class, RevisionData.class,
             Product.class, FilterAccessConstraint.class,
             FilterAccessCtl.class, JAppletDisplayHint.class,
             AppletParameter.class, ProcessDefinition.class, Transition.class };

    /**
     * This constructor makes a new converter and initialize it so it can be
     * used.
     */
    public XMLConverter() {
        init();
    }

    /**
     * This constructor makes a new converter and initializes it so it can be
     * used.
     * <p>
     * Using this ctor, the full header is written in the output stream, and the
     * written objects are nested under the 'gpm' root node.
     * 
     * @param pOS
     *            OutputStream
     */
    public XMLConverter(OutputStream pOS) {
        init();
        setOutputStream(pOS, DEFAULT_ENCODING);
    }

    /**
     * This constructor makes a new converter which can be used to read content
     * from the given input stream.
     * 
     * @param pIS
     *            InputStream
     */
    public XMLConverter(InputStream pIS) {
        init();
        setInputStream(pIS);
    }

    /**
     * XMLConverter This constructor makes a new converter and initialize it so
     * it can be used.
     * <p>
     * Using this constructor the written objects are nested under the specified
     * node name. No headers are written.
     * 
     * @param pNode
     *            Name of the nesting XML node
     * @param pWriter
     *            OutputStream
     */
    public XMLConverter(String pNode, OutputStreamWriter pWriter) {
        init();
        setWriter(pNode, pWriter);
        isHeaderWritten = true;
    }

    /**
     * XMLConverter This constructor makes a new converter and initialize it so
     * it can be used.
     * 
     * @param pDocument
     *            the XML document
     */
    public XMLConverter(Document pDocument) {
        init();
        setDocument(pDocument);
    }

    /**
     * Create a hierarchical stream reader
     * 
     * @return a hierarchical stream reader
     */
    public final HierarchicalStreamReader createHierarchicalStreamReader() {
        if (null == hierarchicalStreamReader) {
            hierarchicalStreamReader = new XppReader(contentStreamReader);

            if (!XML_ROOT_NODE.equals(hierarchicalStreamReader.getNodeName())) {
                throw new GDMException("Invalid XML content");
            }
        }
        return hierarchicalStreamReader;
    }

    private void setDocument(Document pDomDocument) {
        hierarchicalStreamWriter = new DomWriter(pDomDocument);
        try {
            objectOutputStream =
                    xstream.createObjectOutputStream(hierarchicalStreamWriter,
                            XML_ROOT_NODE);
        }
        catch (IOException e) {
            throw new RuntimeException(
                    "Error creating the serialization output stream", e);
        }
    }

    private void setInputStream(InputStream pIs) {
        try {
            BufferedInputStream lBufferedInput = new BufferedInputStream(pIs);

            Charset lEncoding = getXMLEncoding(lBufferedInput);
            if (staticLOGGER.isInfoEnabled()) {
                staticLOGGER.info("Detected encoding: "
                        + lEncoding.displayName());
            }
            contentStreamReader =
                    new InputStreamReader(lBufferedInput, lEncoding);
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot read input", e);
        }
    }

    private Charset getXMLEncoding(BufferedInputStream pIs) throws IOException {
        byte[] lMagic = new byte[MAGIC_SIZE];
        Charset lEncoding = Charset.forName(UTF_8_ENCODING);
        boolean lBomPresent = false;

        pIs.mark(XML_HEADER_MAX_LENGTH);

        pIs.read(lMagic, 0, lMagic.length);

        if ((lMagic[0] & HEXA_0XFF) == HEXA_0XFE
                && (lMagic[1] & HEXA_0XFF) == HEXA_0XFF) {
            lEncoding = Charset.forName(UTF_16BE_ENCODING);
            lBomPresent = true;
        }
        else if ((lMagic[0] & HEXA_0XFF) == 0
                && (lMagic[1] & HEXA_0XFF) == HEXA_0X3C) {
            lEncoding = Charset.forName(UTF_16BE_ENCODING);
        }
        else if ((lMagic[0] & HEXA_0XFF) == HEXA_0XFF
                && (lMagic[1] & HEXA_0XFF) == HEXA_0XFE) {
            lEncoding = Charset.forName(UTF_16LE_ENCODING);
            lBomPresent = true;
        }
        else if ((lMagic[0] & HEXA_0XFF) == HEXA_0X3C
                && (lMagic[1] & HEXA_0XFF) == 0) {
            lEncoding = Charset.forName(UTF_16LE_ENCODING);
        }
        else {
            pIs.reset();
            pIs.mark(XML_HEADER_MAX_LENGTH);

            Reader lReader = new InputStreamReader(pIs, DEFAULT_ENCODING);
            BufferedReader lBufReader =
                    new BufferedReader(lReader, XML_HEADER_MAX_LENGTH);

            String lXmlHeader = lBufReader.readLine();
            Pattern lXmlHeaderPattern = Pattern.compile(XML_HEADER_REGEXP);
            Matcher lRegexpMatcher = lXmlHeaderPattern.matcher(lXmlHeader);

            // Check if the XML header pattern does not match  (not an XML file ?)
            if (lRegexpMatcher.matches()) {
                String lEncodingStr = lRegexpMatcher.group(1);
                lEncoding = Charset.forName(lEncodingStr);
            }
        }
        pIs.reset();
        if (lBomPresent) {
            // Remove the BOM characters
            byte[] lBomHeader = new byte[2];
            pIs.read(lBomHeader);
        }
        return lEncoding;
    }

    private void setOutputStream(OutputStream pOS, String pCharEncoding) {
        try {
            Charset lEncoding = Charset.forName(pCharEncoding);
            CharsetEncoder lCharSetEncoder = lEncoding.newEncoder();

            contentStreamWriter =
                    new GpmOutputStreamWriter(pOS, lCharSetEncoder);
            hierarchicalStreamWriter =
                    new GpmPrettyPrintWriter(contentStreamWriter);

            outputXMLHeader();
            objectOutputStream =
                    xstream.createObjectOutputStream(hierarchicalStreamWriter,
                            XML_ROOT_NODE);

            hierarchicalStreamWriter.addAttribute(XML_NS, XML_NS_VALUE);
            hierarchicalStreamWriter.addAttribute(XML_NS_XSI, XML_NS_XSI_VALUE);
            hierarchicalStreamWriter.addAttribute(XSI_SHEMA_LOCATION,
                    XSI_SHEMA_LOCATION_VALUE);
            hierarchicalStreamWriter.startNode("version");
            hierarchicalStreamWriter.setValue(INSTANCE_FMT_VERSION);
            hierarchicalStreamWriter.endNode();
        }
        catch (IOException e) {
            throw new RuntimeException(
                    "Error creating the serialization output stream", e);
        }
    }

    private void setWriter(String pNode, OutputStreamWriter pWriter) {
        try {
            hierarchicalStreamWriter = new PrettyPrintWriter(pWriter);

            objectOutputStream =
                    xstream.createObjectOutputStream(hierarchicalStreamWriter,
                            pNode);
        }
        catch (IOException e) {
            throw new RuntimeException("Error creating the writer stream", e);
        }
    }

    /**
     * Create an ObjectInputStream
     * 
     * @return an ObjectInputStream
     */
    public ObjectInputStream createObjectInputStream() {
        try {
            return xstream.createObjectInputStream(hierarchicalStreamReader);
        }
        catch (IOException e) {
            throw new RuntimeException(
                    "Error creating the object input stream", e);
        }
    }

    /**
     * The init method configure the XStream parser to use the right converter
     * and the right tags.
     */
    protected void init() {
        xstream = new XStream(new DomDriver());
        xstream.setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(
                ReferenceByXPathMarshallingStrategy.RELATIVE));
        xstream.setMode(XStream.NO_REFERENCES);

        xstream.processAnnotations(ANNOTATED_CLASSES);

        xstream.registerConverter(new MessageConverter());
        xstream.registerConverter(new CodeConverter());
        xstream.registerConverter(new AttributeValueConverter());
        xstream.registerConverter(new LockConverter());
        xstream.registerConverter(new TransitionConverter());

        // Register the byte[] converter.
        xstream.registerConverter(new EncodedByteArrayConverter());
    }

    /**
     * Convert an XML file.
     * 
     * @param pIS
     *            the XML seen as an InputStream
     * @return a Gpm serialization object.
     */
    public Gpm fromXML(InputStream pIS) {
        return (Gpm) xstream.fromXML(pIS);
    }

    /**
     * Convert an XML file.
     * 
     * @return a Gpm serialization object.
     */
    public Gpm fromXML() {
        return (Gpm) xstream.fromXML(contentStreamReader);
    }

    /**
     * Write the gpm serializable content to an output stream as XML format.
     * 
     * @param pOS
     *            Output stream
     * @param pSerializableContent
     *            Content to write in the stream.
     */
    public void toXML(OutputStream pOS, Gpm pSerializableContent) {
        xstream.toXML(pSerializableContent, new OutputStreamWriter(pOS));
    }

    /**
     * To xml.
     * 
     * @param pInstantiate
     *            the instantiate
     * @return the string
     */
    public String toXML(Gpm pInstantiate) {
        return xstream.toXML(pInstantiate);
    }

    /**
     * writeObject.
     * 
     * @param pObj
     *            Object
     * @throws IOException
     *             Error during the object serialization
     */
    public void writeObject(Object pObj) throws IOException {
        objectOutputStream.writeObject(pObj);
    }

    /**
     * Creates the xml header.
     * 
     * @param pOutputStreamWriter
     *            the output stream writer
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void outputXMLHeader() {
        if (contentStreamWriter != null && !isHeaderWritten) {
            try {
                String lHeader =
                        MessageFormat.format(XML_HEADER,
                                contentStreamWriter.getEncodingName());
                contentStreamWriter.write(lHeader);
            }
            catch (IOException e) {
                throw new SystemException("Cannot write to outputstream", e);
            }
        }
        isHeaderWritten = true;
    }

    /**
     * startNode.
     * 
     * @param pNode
     *            the node name
     */
    public void startNode(String pNode) {
        if (!isHeaderWritten) {
            outputXMLHeader();
        }
        hierarchicalStreamWriter.startNode(pNode);
    }

    /**
     * endNode.
     */
    public void endNode() {
        hierarchicalStreamWriter.endNode();
    }

    /**
     * close the objectOutputStream and the root node.
     */
    public void close() {
        if (objectOutputStream != null) {
            try {
                objectOutputStream.close();
                objectOutputStream = null;
            }
            catch (IOException ex) {
                throw new SystemException(
                        "Cannot close the XML output stream.", ex);
            }
        }
        //the other streams shall not be closed because
        //they are managed by other objects        
    }

    /**
     * Flush the underlying object output stream.
     */
    public void flush() {
        if (objectOutputStream != null) {
            try {
                objectOutputStream.flush();
            }
            catch (IOException ex) {
                throw new SystemException(
                        "Cannot flush the XML output stream.", ex);
            }
        }
    }
}
