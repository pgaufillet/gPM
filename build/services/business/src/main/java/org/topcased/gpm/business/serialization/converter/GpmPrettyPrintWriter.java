/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Vincent Hemery (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.converter;

import java.io.Writer;

import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

/**
 * <p>
 * A simple writer that outputs XML in a pretty-printed indented stream. This
 * class has the same behavior as PrettyPrintWriter, except that the char
 * <code><xmp>\n</xmp></code> is also escaped.
 * </p>
 * <p>
 * This means that the chars <code><xmp>& < > " ' \r\n</xmp></code> are escaped and replaced with a
 * suitable XML entity.
 * </p>
 * 
 * @author vhemery
 */
public class GpmPrettyPrintWriter extends PrettyPrintWriter {

    private static final char[] NULL = "&#x0;".toCharArray();

    private static final char[] AMP = "&amp;".toCharArray();

    private static final char[] LT = "&lt;".toCharArray();

    private static final char[] GT = "&gt;".toCharArray();

    private static final char[] SLASH_R = "&#x0D;".toCharArray();

    private static final char[] QUOT = "&quot;".toCharArray();

    private static final char[] APOS = "&apos;".toCharArray();

    private static final char[] SLASH_N = "&#x0A;".toCharArray();

    private static final char[] SLASH_T = "&#x09;".toCharArray();

    /**
     * Construct a new simple writerto output XML
     * 
     * @param pWriter
     *            the writer
     * @see PrettyPrintWriter#PrettyPrintWriter(Writer)
     */
    public GpmPrettyPrintWriter(Writer pWriter) {
        super(pWriter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeAttributeValue(QuickWriter pWriter, String pText) {
        gpmWriteText(pWriter, pText);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeText(QuickWriter pWriter, String pText) {
        gpmWriteText(pWriter, pText);
    }

    /**
     * Write the text, escaping chars <code><xmp>& < > " ' \r\n</xmp></code>. As advised in
     * {@link PrettyPrintWriter}, calling methods are overridden to call this
     * private method.
     * 
     * @param pWriter
     *            the writer
     * @param pText
     *            the text to write
     */
    private void gpmWriteText(QuickWriter pWriter, String pText) {
        int lLength = pText.length();
        for (int i = 0; i < lLength; i++) {
            char lChar = pText.charAt(i);
            switch (lChar) {
                case '\0':
                    pWriter.write(NULL);
                    break;
                case '&':
                    pWriter.write(AMP);
                    break;
                case '<':
                    pWriter.write(LT);
                    break;
                case '>':
                    pWriter.write(GT);
                    break;
                case '"':
                    pWriter.write(QUOT);
                    break;
                case '\'':
                    pWriter.write(APOS);
                    break;
                case '\r':
                    pWriter.write(SLASH_R);
                    break;
                case '\n':
                    pWriter.write(SLASH_N);
                    break;
                case '\t':
                    pWriter.write(SLASH_T);
                    break;
                default:
                    pWriter.write(lChar);
            }
        }
    }

}
