/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.domain.util.FieldsUtil;
import org.topcased.gpm.util.lang.CollectionUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Class mapping a revision content
 * 
 * @author mfranche
 */

@XStreamAlias("revision")
public class RevisionData extends ValuesContainerData {

    /** serialVersionUID */
    private static final long serialVersionUID = 7253959991482366226L;

    /** Label of the revision */
    @XStreamAlias("label")
    @XStreamAsAttribute
    private String label;

    /** Author of the revision */
    @XStreamAlias("author")
    @XStreamAsAttribute
    private String author;

    /** Date of the revision creation */
    @XStreamAlias("date")
    @XStreamAsAttribute
    private String date;

    /** Time of the revision creation */
    @XStreamAlias("time")
    @XStreamAsAttribute
    private String time;

    /** List of attributes attached to this revision. */
    @XStreamAlias("revisionAttributes")
    private List<Attribute> revisionAttributes;

    /**
     * get label
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * set label
     * 
     * @param pLabel
     *            the label to set
     */
    public void setLabel(String pLabel) {
        label = pLabel;
    }

    /**
     * get author
     * 
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * set author
     * 
     * @param pAuthor
     *            the author to set
     */
    public void setAuthor(String pAuthor) {
        author = pAuthor;
    }

    /**
     * get creationDate
     * 
     * @return the creationDate
     */
    public Date getDate() {
        Date lResultDate = null;
        try {
            if (time == null) {
                lResultDate = FieldsUtil.parseDate(date);
            }
            else {
                lResultDate = FieldsUtil.parseDate(date + "T" + time);
            }
        }
        catch (ParseException e) {
            // Should not happen here.
        }
        return lResultDate;
    }

    /**
     * set creationDate
     * 
     * @param pDate
     *            the creationDate to set
     */
    public void setDate(Date pDate) {
        String[] lDateTimeStr = FieldsUtil.formatDateTime(pDate);
        date = lDateTimeStr[0];
        time = lDateTimeStr[1];
    }

    /**
     * get revision attributes
     * 
     * @return the revisionAttributes
     */
    public List<Attribute> getRevisionAttributes() {
        return revisionAttributes;
    }

    /**
     * set pRevisionAttributes
     * 
     * @param pRevisionAttributes
     *            the revisionAttributes to set
     */
    public void setRevisionAttributes(List<Attribute> pRevisionAttributes) {
        revisionAttributes = pRevisionAttributes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof RevisionData) {
            RevisionData lOther = (RevisionData) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!StringUtils.equals(label, lOther.label)) {
                return false;
            }
            if (!StringUtils.equals(author, lOther.author)) {
                return false;
            }
            if (!StringUtils.equals(date, lOther.date)) {
                return false;
            }
            if (!StringUtils.equals(time, lOther.time)) {
                return false;
            }
            if (!CollectionUtils.equals(revisionAttributes,
                    lOther.revisionAttributes)) {
                return false;
            }

            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // ValuesContainerData hashcode is sufficient
        return super.hashCode();
    }
}
