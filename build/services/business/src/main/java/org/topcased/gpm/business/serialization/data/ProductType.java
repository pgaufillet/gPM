/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * A product maps to a ProductType in gPM and is used for XML
 * marshalling/unmarshalling. Here a productType is just a list of Fields with a
 * name.
 * 
 * @author sidjelli
 */
@XStreamAlias("productType")
public class ProductType extends FieldsContainer {

    /** Generated UID */
    private static final long serialVersionUID = 7344319833360282920L;

}
