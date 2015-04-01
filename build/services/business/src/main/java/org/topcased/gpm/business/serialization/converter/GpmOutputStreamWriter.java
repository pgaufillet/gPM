/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.converter;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.CharsetEncoder;

/**
 * GpmOutoutStreamWriter
 * 
 * @author Magali Franchet
 */
public class GpmOutputStreamWriter extends OutputStreamWriter {

    private CharsetEncoder encoder;

    /**
     * Constructor
     * 
     * @param pOut
     *            ouputstream
     * @param pEnc
     *            encoder
     */
    public GpmOutputStreamWriter(OutputStream pOut, CharsetEncoder pEnc) {
        super(pOut, pEnc);
        encoder = pEnc;
    }

    /**
     * Encoding name (Need new method to get encoding name because existing
     * method encodingName() returns historical name (for UTF-8, method returns
     * encodingName() returns UTF8)
     * 
     * @return encoding name
     */
    public String getEncodingName() {
        return encoder.charset().name();
    }

}
