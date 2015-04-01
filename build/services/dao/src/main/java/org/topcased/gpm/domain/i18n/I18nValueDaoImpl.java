/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.i18n;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

/**
 * @see org.topcased.gpm.domain.i18n.I18nValue
 * @author nbousquet
 */
public class I18nValueDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.i18n.I18nValue, java.lang.String>
        implements org.topcased.gpm.domain.i18n.I18nValueDao {
    /**
     * Constructor
     */
    public I18nValueDaoImpl() {
        super(org.topcased.gpm.domain.i18n.I18nValue.class);
    }

    /**
     * @see org.topcased.gpm.domain.i18n.I18nValue#getValue(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public String getTypedValue(final String pLabelKey, final String pLang,
            final String pType) {
        final String lQueryString =
                "select i18nValue.value from I18nValue i18nValue where "
                        + " i18nValue.labelKey = :labelKey"
                        + " and (i18nValue.lang=:lang or i18nValue.lang=' ') "
                        + " and i18nValue.type = :type order by i18nValue.lang desc";

        final Query lQuery = getSession().createQuery(lQueryString);
        lQuery.setParameter("labelKey", pLabelKey);
        lQuery.setParameter("lang", pLang);
        lQuery.setParameter("type", pType);

        // Results can contain (at most) two translations: one for specified
        // language (first) and the default one (second). If only default
        // translation exists, the
        // results list has only one element.
        List<String> lResults = lQuery.list();

        if (lResults.isEmpty()) {
            return null;
        }
        return lResults.iterator().next();
    }

    /**
     * @see org.topcased.gpm.domain.i18n.I18nValue#getValue(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public String getValue(final String pLabelKey, final String pLang) {
        final String lQueryString =
                "select i18nValue.value from I18nValue i18nValue where "
                        + " i18nValue.labelKey = :labelKey and i18nValue.type is null"
                        + " and (i18nValue.lang=:lang or i18nValue.lang=' ') "
                        + " order by i18nValue.lang desc";

        final Query lQuery = getSession().createQuery(lQueryString);

        lQuery.setParameter("labelKey", pLabelKey);
        lQuery.setParameter("lang", pLang);

        List<String> lResults = lQuery.list();
        if (lResults.isEmpty()) {
            return null;
        }
        return lResults.iterator().next();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.i18n.I18nValueDaoBase#getAvailableLanguages()
     */
    @SuppressWarnings("unchecked")
    public String[] getAvailableLanguages() {
        final String lQueryString =
                "select distinct i18nValue.lang from I18nValue i18nValue "
                        + "where i18nValue.lang!=' ' order by i18nValue.lang ";
        final Query lQuery = getSession().createQuery(lQueryString);
        return ((List<String>) lQuery.list()).toArray(new String[0]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.i18n.I18nValueDaoBase#getValues(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getValues(String pLang, String pType) {
        final StringBuilder lQueryString =
                new StringBuilder(
                        "select i18nValue.labelKey, i18nValue.value from I18nValue i18nValue"
                                + " where "
                                + " (i18nValue.lang=:lang or i18nValue.lang=' ') ");
        if (StringUtils.isNotBlank(pType)) {
            lQueryString.append(" and i18nValue.type= :type");
        }
        else {
            lQueryString.append(" and i18nValue.type is null");
        }
        Query lQuery = getSession().createQuery(lQueryString.toString());

        lQuery.setParameter("lang", pLang);
        if (StringUtils.isNotBlank(pType)) {
            lQuery.setParameter("type", pType);
        }

        return lQuery.list();
    }

    /**
     * @see org.topcased.gpm.domain.i18n.I18nValue#get(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.i18n.I18nValue get(
            final java.lang.String pLabelKey, final java.lang.String pLang) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.i18n.I18nValue as i18nValue where i18nValue.labelKey = :labelKey and i18nValue.lang = :lang";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("labelKey", pLabelKey);
            lQueryObject.setParameter("lang", pLang);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.i18n.I18nValue lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.i18n.I18nValue"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.i18n.I18nValue) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.i18n.I18nValue#getTyped(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.i18n.I18nValue getTyped(
            final java.lang.String pLabelKey, final java.lang.String pLang,
            final java.lang.String pType) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.i18n.I18nValue as i18nValue where i18nValue.labelKey = :labelKey and i18nValue.lang = :lang and i18nValue.type = :type";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("labelKey", pLabelKey);
            lQueryObject.setParameter("lang", pLang);
            lQueryObject.setParameter("type", pType);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.i18n.I18nValue lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.i18n.I18nValue"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.i18n.I18nValue) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }
}