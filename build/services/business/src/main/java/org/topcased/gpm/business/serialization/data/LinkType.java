/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * A SheetLinkType maps a Sheet in gPM and is used for XML
 * marshalling/unmarshalling of a SheetLinkType. Here a sheet link type is just
 * a list of fields.
 * 
 * @author sidjelli
 */
@XStreamAlias("linkType")
public class LinkType extends FieldsContainer {
    /**
     * The name of the option on the instanciation file and the name of the
     * associated extended attribute
     */
    public final static String FILTER_FOR_DESTINATION_SHEET = "Destination";

    /**
     * The name of the option on the instanciation file and the name of the
     * associated extended attribute
     */
    public final static String FILTER_FOR_ORIGIN_SHEET = "Origin";

    /** Generated UID */
    private static final long serialVersionUID = 1525943338064203306L;

    /** The low bound. */
    @XStreamAsAttribute
    private Integer lowBound;

    /** The high bound. */
    @XStreamAsAttribute
    private Integer highBound;

    /** The origin type. */
    @XStreamAsAttribute
    private String originType;

    /** The destination type. */
    @XStreamAsAttribute
    private String destinationType;

    /** The unidirectional. */
    @XStreamAsAttribute
    private String unidirectional;

    /** The filters for the sort */
    @XStreamAlias(value = "sorts")
    private List<LinkTypeSorter> sorts;

    /**
     * Checks if is unidirectional creation.
     * 
     * @return true if link is unidirectional for creation.
     */
    public Boolean isUnidirectionalCreation() {
        if (null == unidirectional) {
            return Boolean.FALSE;
        }
        return "true".equals(unidirectional)
                || "creation".equals(unidirectional);
    }

    /**
     * Checks if is unidirectional navigation.
     * 
     * @return the boolean
     */
    public Boolean isUnidirectionalNavigation() {
        if (null == unidirectional) {
            return Boolean.FALSE;
        }
        return "true".equals(unidirectional)
                || "navigation".equals(unidirectional);
    }

    /**
     * Gets the destination type.
     * 
     * @return the destination type
     */
    public String getDestinationType() {
        return destinationType;
    }

    /**
     * Sets the destination type.
     * 
     * @param pDestType
     *            the dest type
     */
    public void setDestinationType(String pDestType) {
        this.destinationType = pDestType;
    }

    /**
     * Gets the high bound.
     * 
     * @return the high bound
     */
    public int getHighBound() {
        if (highBound != null) {
            return highBound;
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Sets the high bound.
     * 
     * @param pHighBound
     *            the high bound
     */
    public void setHighBound(int pHighBound) {
        this.highBound = pHighBound;
    }

    /**
     * Gets the low bound.
     * 
     * @return the low bound
     */
    public int getLowBound() {
        if (lowBound != null) {
            return lowBound;
        }
        return 0;
    }

    /**
     * Sets the low bound.
     * 
     * @param pLowBound
     *            the low bound
     */
    public void setLowBound(int pLowBound) {
        this.lowBound = pLowBound;
    }

    /**
     * Gets the origin type.
     * 
     * @return the origin type
     */
    public String getOriginType() {
        return originType;
    }

    /**
     * Sets the origin type.
     * 
     * @param pOriginType
     *            the origin type
     */
    public void setOriginType(String pOriginType) {
        this.originType = pOriginType;
    }

    /**
     * Gets the unidirectional.
     * 
     * @return the unidirectional
     */
    public String getUnidirectional() {
        return unidirectional;
    }

    /**
     * Sets the unidirectional
     * 
     * @param pUnidirectional
     *            unidirectional value.
     */
    public void setUnidirectional(String pUnidirectional) {
        unidirectional = pUnidirectional;
    }

    /**
     * Getter on the sorts
     * 
     * @return The sorts
     */
    public List<LinkTypeSorter> getSorts() {
        return sorts;
    }

    /**
     * Setter on the sorts
     * 
     * @param pSorts
     *            The new sorts
     */
    public void setSorts(List<LinkTypeSorter> pSorts) {
        sorts = pSorts;
    }
}
