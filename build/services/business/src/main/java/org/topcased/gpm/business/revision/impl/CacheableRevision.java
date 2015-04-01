/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.revision.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.topcased.gpm.business.attributes.impl.CacheableAttributesContainer;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.serialization.data.Attribute;
import org.topcased.gpm.business.serialization.data.RevisionData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.revision.Revision;
import org.topcased.gpm.domain.util.FieldsUtil;

/**
 * CacheableRevision.
 * 
 * @author mfranche
 */
public class CacheableRevision extends CacheableValuesContainer {

    /** serialVersionUID. */
    private static final long serialVersionUID = -3079600907543655070L;

    /** Label of the revision. */
    private String label;

    /** Author of the revision. */
    private String author;

    /** Date of the revision creation. */
    private Date creationDate;

    /** List of attributes attached to this revision. */
    private CacheableAttributesContainer revisionAttributes;

    /**
     * Constructor for mutable / immutable switch
     */
    public CacheableRevision() {
        super();
    }

    /**
     * Create a new cacheable revision data.
     * 
     * @param pRevision
     *            Revision entity
     * @param pSheetType
     *            Sheet type definition
     */
    public CacheableRevision(Revision pRevision, CacheableSheetType pSheetType) {
        super(pRevision, pSheetType);

        label = pRevision.getLabel();
        author = pRevision.getAuthor();
        creationDate = pRevision.getCreationDate();

        revisionAttributes =
                new CacheableAttributesContainer(
                        pRevision.getRevisionAttrs().getId());
    }

    /**
     * Create a new cacheable revision data from a serialization object.
     * 
     * @param pRevision
     *            Serializable revision object
     * @param pType
     *            Type definition
     */
    public CacheableRevision(RevisionData pRevision,
            CacheableFieldsContainer pType) {
        super(pRevision, pType);

        label = pRevision.getLabel();
        author = pRevision.getAuthor();
        creationDate = pRevision.getDate();
        revisionAttributes =
                new CacheableAttributesContainer(
                        pRevision.getRevisionAttributes());
    }

    /**
     * Gets the label.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     * 
     * @param pLabel
     *            the new label
     */
    public void setLabel(String pLabel) {
        label = pLabel;
    }

    /**
     * Gets the author.
     * 
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author.
     * 
     * @param pAuthor
     *            the new author
     */
    public void setAuthor(String pAuthor) {
        author = pAuthor;
    }

    /**
     * Gets the creation date.
     * 
     * @return the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date.
     * 
     * @param pDate
     *            the new creation date
     */
    public void setCreationDate(Date pDate) {
        creationDate = pDate;
    }

    /**
     * Sets the creation date.
     * 
     * @param pDate
     *            the new creation date
     */
    public void setCreationDate(String pDate) {
        try {
            creationDate = FieldsUtil.parseDate(pDate);
        }
        catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format : " + pDate);
        }
    }

    /**
     * Method overridden to remove the 'reference' field from the list
     * 
     * @return List of field labels contained in this revision.
     */
    @Override
    public Collection<String> getFieldLabels() {
        Set<String> lLabels = new HashSet<String>(super.getFieldLabels());
        lLabels.remove(FieldsService.REFERENCE_FIELD_NAME);

        return lLabels;
    }

    /**
     * Gets the revision attributes.
     * 
     * @return the revision attributes
     */
    public CacheableAttributesContainer getRevisionAttributes() {
        return revisionAttributes;
    }

    /**
     * Set the revisionAttributes
     * 
     * @param pRevisionAttributes
     *            the new revisionAttributes
     */
    public void setRevisionAttributes(
            CacheableAttributesContainer pRevisionAttributes) {
        revisionAttributes = pRevisionAttributes;
    }

    /**
     * Marshal this object into the given Revision object.
     * 
     * @param pObject
     *            the serialized revision
     */
    @Override
    public void marshal(Object pObject) {
        super.marshal(pObject);

        RevisionData lSerializedRevision = (RevisionData) pObject;
        lSerializedRevision.setAuthor(getAuthor());
        lSerializedRevision.setLabel(getLabel());
        lSerializedRevision.setDate(getCreationDate());

        lSerializedRevision.setType(null);
        lSerializedRevision.setVersion(null);

        if (CollectionUtils.isNotEmpty(getRevisionAttributes().getAllAttributes())) {
            final List<Attribute> lAttributes;
            lAttributes =
                    new ArrayList<Attribute>(
                            getRevisionAttributes().getAllAttributes());
            lSerializedRevision.setRevisionAttributes(lAttributes);
        }
    }

}
