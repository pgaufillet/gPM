/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.topcased.gpm.domain.link;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Query;
import org.topcased.gpm.domain.dynamic.container.link.DynamicLinkGeneratorFactory;
import org.topcased.gpm.domain.dynamic.container.sheet.DynamicSheetGeneratorFactory;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.domain.sheet.SheetType;

/**
 * @see org.topcased.gpm.domain.link.Link
 * @author yntsama
 */
public class LinkDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.link.Link, java.lang.String>
        implements org.topcased.gpm.domain.link.LinkDao {
    /**
     * Constructor
     */
    public LinkDaoImpl() {
        super(org.topcased.gpm.domain.link.Link.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#getDestinationLinks(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public String[] getDestinationLinks(final String pSourceId) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select id from org.topcased.gpm.domain.link.Link link "
                                + "where link.origin.id = :sourceId");
        lQuery.setParameter("sourceId", pSourceId);

        final List<String> lLinksId = lQuery.list();
        if (lLinksId.size() == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return lLinksId.toArray(new String[lLinksId.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#getOriginLinks(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public String[] getOriginLinks(final String pDestId) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select id from org.topcased.gpm.domain.link.Link link "
                                + "where link.definition.unidirectionalNavigation = false and "
                                + "link.destination.id = :destId");
        lQuery.setParameter("destId", pDestId);
        final List<String> lLinksId = lQuery.list();
        if (lLinksId.size() == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return lLinksId.toArray(new String[lLinksId.size()]);
    }

    static final String GET_LINKS_QUERY =
            "select link.id from "
                    + Link.class.getName()
                    + " as link where link.origin.id = :containerId"
                    + " or (link.definition.unidirectionalNavigation = false and"
                    + " link.destination.id = :containerId)"
                    + " order by link.definition.name asc";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#getLinks(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> getLinks(final String pContainerId) {
        final Query lQuery = getSession(false).createQuery(GET_LINKS_QUERY);
        lQuery.setParameter("containerId", pContainerId);
        return lQuery.list();
    }

    private static final String GET_LINK =
            "select l.id from " + Link.class.getName() + " l, "
                    + Sheet.class.getName() + " s1, " + Sheet.class.getName()
                    + " s2 " + "where s1.id = l.origin.id "
                    + "and s2.id = l.destination.id "
                    + "and l.definition.name = :typeName "
                    + "and l.definition.businessProcess.name = :processName "
                    + "and s1.product.name = :originProductName "
                    + "and s1.reference = :originSheetReference "
                    + "and s2.product.name = :destinationProductName "
                    + "and s2.reference = :destinationSheetReference";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#getSheetLinkId(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public String getSheetLinkId(final String pProcessName,
            final String pOriginProductName,
            final String pOriginSheetReference,
            final String pDestinationProductName,
            final String pDestinationSheetReference, final String pTypeName) {
        final Query lQuery = createQuery(GET_LINK);

        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("originProductName", pOriginProductName);
        lQuery.setParameter("originSheetReference", pOriginSheetReference);
        lQuery.setParameter("destinationProductName", pDestinationProductName);
        lQuery.setParameter("destinationSheetReference",
                pDestinationSheetReference);
        lQuery.setParameter("typeName", pTypeName);

        return (String) lQuery.uniqueResult();
    }

    private static final String PRODUCT_LINK_ID =
            "select l.id from " + Link.class.getName() + " l, "
                    + Product.class.getName() + " p1, "
                    + Product.class.getName() + " p2 "
                    + "where p1.id = l.origin.id "
                    + "and p2.id = l.destination.id "
                    + "and l.definition.name = :typeName "
                    + "and l.definition.businessProcess.name = :processName "
                    + "and p1.name = :originProductName "
                    + "and p2.name = :destinationProductName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#getProductLinkId(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public String getProductLinkId(final String pProcessName,
            final String pOriginProductName,
            final String pDestinationProductName, final String pTypeName) {
        final Query lQuery = createQuery(PRODUCT_LINK_ID);

        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("originProductName", pOriginProductName);
        lQuery.setParameter("destinationProductName", pDestinationProductName);
        lQuery.setParameter("typeName", pTypeName);

        return (String) lQuery.uniqueResult();
    }

    private static final String GET_LINKINFOS_QUERY =
            "select new " + LinkInfo.class.getName()
                    + " (link.id, link.origin, link.destination) from "
                    + Link.class.getName() + " as link where linkId = :linkId";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#getLinkInfos(java.lang.String)
     */
    public LinkInfo getLinkInfos(final String pLinkId) {
        final Query lQuery = getSession(false).createQuery(GET_LINKINFOS_QUERY);

        return (LinkInfo) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#getSheetLinksByType(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Collection<String> getSheetLinksByType(final String pLinkTypeId,
            final String pElementId) {

        // Get sheet type Id to order by
        Query lQuery =
                getSession(false).createQuery(
                        "SELECT sheetType.id FROM "
                                + SheetType.class.getName()
                                + " AS sheetType, "
                                + Sheet.class.getName()
                                + " AS sheet, "
                                + LinkType.class.getName()
                                + " AS linkType "
                                + "WHERE linkType.id = :linkTypeId AND "
                                + "sheet.id = :elementId AND "
                                + "((linkType.originType.id = sheet.definition.id AND "
                                + "sheetType.id = linkType.destType) OR "
                                + "(linkType.unidirectionalNavigation = false AND "
                                + "linkType.destType.id = sheet.definition.id AND "
                                + "sheetType.id = linkType.originType))");
        lQuery.setParameter("linkTypeId", pLinkTypeId);
        lQuery.setParameter("elementId", pElementId);
        List lSheetType = lQuery.list();
        if (lSheetType.isEmpty()) {
            return lSheetType;
        }
        String lSheetTypeId = (String) lQuery.list().get(0);

        lQuery =
                getSession(false).createQuery(
                        "SELECT field.id FROM "
                                + SheetType.class.getName()
                                + " AS sheetType "
                                + "JOIN sheetType.referenceField.fields AS field "
                                + "WHERE sheetType.id = :sheetTypeId");
        lQuery.setParameter("sheetTypeId", lSheetTypeId);
        List<String> lSheetTypeRef = lQuery.list();

        final String lSheetTableName =
                DynamicSheetGeneratorFactory.getInstance().getDynamicObjectGenerator(
                        lSheetTypeId).getGeneratedClass().getName();
        String lQueryString =
                "SELECT link.id FROM "
                        + Link.class.getName()
                        + " AS link, "
                        + lSheetTableName
                        + " AS sheet "
                        + "WHERE link.definition.id = :linkTypeId AND "
                        + "((link.origin.id = :elementId AND link.destination.id = sheet.id) OR "
                        + "(link.definition.unidirectionalNavigation = false AND "
                        + "link.destination.id = :elementId AND link.origin.id = sheet.id)) "
                        + "ORDER BY ";

        for (int i = 0; i < lSheetTypeRef.size(); i++) {
            lQueryString += "sheet.";
            String lColumnName =
                    DynamicObjectNamesUtils.getInstance().getDynamicColumnName(
                            lSheetTypeRef.get(i));
            lQueryString += lColumnName.substring(0, 1).toLowerCase();
            lQueryString += lColumnName.substring(1);
            if (i < lSheetTypeRef.size() - 1) {
                lQueryString += ", ";
            }
        }
        lQuery = getSession(false).createQuery(lQueryString);
        lQuery.setParameter("linkTypeId", pLinkTypeId);
        lQuery.setParameter("elementId", pElementId);

        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#getNewLink(java.lang.String)
     */
    public Link getNewLink(final String pTypeId) {
        return DynamicLinkGeneratorFactory.getInstance().getDynamicObjectGenerator(
                pTypeId).create();
    }

    /** Query used by isLinkExists and getId methods **/
    private static final String IS_LINK_EXISTS =
            "SELECT link.id FROM " + Link.class.getName() + " as link "
                    + "WHERE link.definition.name = :linkTypeName AND "
                    + "link.destination.id = :destinationId AND "
                    + "link.origin.id = :originId";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#isLinkExists(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public Boolean isLinkExists(String pLinkTypeName, String pOriginId,
            String pDestinationId) {
        final Query lQuery = createQuery(IS_LINK_EXISTS);
        lQuery.setParameter("linkTypeName", pLinkTypeName);
        lQuery.setParameter("originId", pOriginId);
        lQuery.setParameter("destinationId", pDestinationId);
        return hasResult(lQuery);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#getId(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public String getId(String pLinkTypeName, String pOriginId,
            String pDestinationId) {
        final Query lQuery = createQuery(IS_LINK_EXISTS);
        lQuery.setParameter("linkTypeName", pLinkTypeName);
        lQuery.setParameter("originId", pOriginId);
        lQuery.setParameter("destinationId", pDestinationId);
        return (String) lQuery.uniqueResult();
    }

    /**
     * /* (non-Javadoc)
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#isSheetLink(java.lang.String)
     */
    @Override
    public Boolean isSheetLink(final String pId) {
        final Object lLink = getHibernateTemplate().get(Link.class, pId);

        return lLink instanceof Link
                && ((Link) lLink).getOrigin() instanceof Sheet;
    }

    /**
     * @see org.topcased.gpm.domain.link.LinkDaoBase#isProductLink(java.lang.String)
     */
    @Override
    public Boolean isProductLink(final String pId) {
        final Object lLink = getHibernateTemplate().get(Link.class, pId);

        return lLink instanceof Link
                && ((Link) lLink).getOrigin() instanceof Product;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetDaoBase#sheetsIterator(java.lang.String,
     *      java.util.Collection, java.util.Collection)
     */
    @Override
    public Iterator<String> sheetLinksIterator(final String pProcessName,
            final Collection<String> pProductNames,
            final Collection<String> pTypeNames) {
        final StringBuffer lQueryString =
                new StringBuffer(
                        "select l.id from "
                                + Link.class.getName()
                                + " l, "
                                + Sheet.class.getName()
                                + " o, "
                                + Sheet.class.getName()
                                + " d "
                                + "where l.origin.id = o.id and l.destination.id = d.id "
                                + "and l.definition.businessProcess.name = :processName ");

        if (!pProductNames.isEmpty()) {
            lQueryString.append("and (o.product.name in (:productNames) "
                    + "or d.product.name in (:productNames)) ");
        }
        if (!pTypeNames.isEmpty()) {
            lQueryString.append("and l.definition.name in (:typeNames) ");
        }
        lQueryString.append("order by o.reference, d.reference");

        final Query lQuery = createQuery(lQueryString.toString());

        lQuery.setParameter("processName", pProcessName);
        if (!pProductNames.isEmpty()) {
            lQuery.setParameterList("productNames", pProductNames);
        }
        if (!pTypeNames.isEmpty()) {
            lQuery.setParameterList("typeNames", pTypeNames);
        }

        return iterate(lQuery, String.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#sheetLinksIterator(java.lang.String,
     *      java.lang.String, java.lang.String, java.util.Collection)
     */
    @Override
    public Iterator<String> sheetLinksIterator(String pProcessName,
            String pOriginProductName, String pDestinationProductName,
            Collection<String> pTypeNames) {

        final StringBuffer lQueryString =
                new StringBuffer(
                        "select l.id from "
                                + Link.class.getName()
                                + " l, "
                                + Sheet.class.getName()
                                + " o, "
                                + Sheet.class.getName()
                                + " d "
                                + "where l.origin.id = o.id and l.destination.id = d.id "
                                + "and l.definition.businessProcess.name = :processName "
                                + "and o.product.name = (:originProductName)"
                                + "and d.product.name = (:destinationProductName)");

        if (!pTypeNames.isEmpty()) {
            lQueryString.append("and l.definition.name in (:typeNames) ");
        }
        lQueryString.append("order by o.reference, d.reference");

        final Query lQuery = createQuery(lQueryString.toString());

        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("originProductName", pOriginProductName);
        lQuery.setParameter("destinationProductName", pDestinationProductName);

        if (!pTypeNames.isEmpty()) {
            lQuery.setParameterList("typeNames", pTypeNames);
        }

        return iterate(lQuery, String.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDaoBase#productLinksIterator(java.lang.String,
     *      java.util.Collection, java.util.Collection)
     */
    @Override
    public Iterator<String> productLinksIterator(final String pProcessName,
            final Collection<String> pProductNames,
            final Collection<String> pTypeNames) {
        final StringBuffer lQueryString =
                new StringBuffer(
                        "select l.id from "
                                + Link.class.getName()
                                + " l, "
                                + Product.class.getName()
                                + " o, "
                                + Product.class.getName()
                                + " d "
                                + "where l.origin.id = o.id and l.destination.id = d.id "
                                + "and l.definition.businessProcess.name = :processName ");

        if (!pProductNames.isEmpty()) {
            lQueryString.append("and (o.name in (:productNames) "
                    + "or d.name in (:productNames)) ");
        }
        if (!pTypeNames.isEmpty()) {
            lQueryString.append("and l.definition.name in (:typeNames) ");
        }
        lQueryString.append("order by o.name, d.name");

        final Query lQuery = createQuery(lQueryString.toString());

        lQuery.setParameter("processName", pProcessName);
        if (!pProductNames.isEmpty()) {
            lQuery.setParameterList("productNames", pProductNames);
        }
        if (!pTypeNames.isEmpty()) {
            lQuery.setParameterList("typeNames", pTypeNames);
        }

        return iterate(lQuery, String.class);
    }

    private static final String IS_SHEET_LINK_EXISTS =
            "SELECT l.id FROM "
                    + Link.class.getName()
                    + " l, "
                    + Sheet.class.getName()
                    + " o, "
                    + Sheet.class.getName()
                    + " d "
                    + "WHERE l.definition.name = :linkTypeName AND "
                    + "l.destination.id = d.id AND "
                    + "l.origin.id = o.id AND d.reference = :destinationRef AND "
                    + "o.reference = :originRef";

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isSheetLinkExists(String pTypeName, String pOriginRef,
            String pDestinationRef) {
        final Query lQuery = createQuery(IS_SHEET_LINK_EXISTS);
        lQuery.setParameter("linkTypeName", pTypeName);
        lQuery.setParameter("originRef", pOriginRef);
        lQuery.setParameter("destinationRef", pDestinationRef);
        return hasResult(lQuery);
    }

    private static final String IS_PRODUCT_LINK_EXISTS =
            "SELECT l.id FROM "
                    + Link.class.getName()
                    + " l, "
                    + Product.class.getName()
                    + " o, "
                    + Product.class.getName()
                    + " d "
                    + "WHERE l.definition.name = :linkTypeName AND "
                    + "l.destination.id = d.id AND "
                    + "l.origin.id = o.id AND d.name = :destinationProductName AND "
                    + "o.name = :originProductName";

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isProductLinkExists(String pTypeName,
            String pOriginProductName, String pDestinationProductName) {
        final Query lQuery = createQuery(IS_PRODUCT_LINK_EXISTS);
        lQuery.setParameter("linkTypeName", pTypeName);
        lQuery.setParameter("originProductName", pOriginProductName);
        lQuery.setParameter("destinationProductName", pDestinationProductName);
        return hasResult(lQuery);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDao#getSheetLinkDestProducts(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> getSheetLinkDestProducts(String pProcessName,
            String pSheetLinkTypeName, String pOriginProductName) {

        final Query lQuery =
                createQuery("select distinct sd.product.name from "
                        + Link.class.getName()
                        + " l, "
                        + Sheet.class.getName()
                        + " so, "
                        + Sheet.class.getName()
                        + " sd "
                        + "where l.definition.businessProcess.name = :processName "
                        + "and l.definition.name = :linkTypeName "
                        + "and so.id = l.origin.id "
                        + "and sd.id = l.destination.id "
                        + "and so.product.name= :originProductName");

        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("originProductName", pOriginProductName);
        lQuery.setParameter("linkTypeName", pSheetLinkTypeName);

        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.link.LinkDao#getExistingLinkTypes(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getExistingLinkTypes(String pValuesContainerId) {
        final Query lQuery =
                createQuery("SELECT DISTINCT link.definition.id FROM "
                        + Link.class.getName()
                        + " as link "
                        + "WHERE ((link.origin.id=:valuesContainerId "
                        + "OR link.destination.id=:valuesContainerId) "
                        + "AND link.definition.unidirectionalNavigation=:false) "
                        + "OR " + "(link.origin.id=:valuesContainerId "
                        + "AND link.definition.unidirectionalNavigation=:true)");
        lQuery.setParameter("valuesContainerId", pValuesContainerId);
        lQuery.setParameter("false", false);
        lQuery.setParameter("true", true);
        return lQuery.list();
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#load(java.lang.String, boolean)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.ValuesContainer load(
            final java.lang.String pId, final boolean pEagerLoading) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.id = :id and link.eagerLoading = :eagerLoading";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            lQueryObject.setParameter("eagerLoading",
                    java.lang.Boolean.valueOf(pEagerLoading));
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.fields.ValuesContainer lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.fields.ValuesContainer"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.fields.ValuesContainer) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#getVersion(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Integer getVersion(final java.lang.String pId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.id = :id";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            java.util.List lResults = lQueryObject.list();
            java.lang.Integer lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Integer"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Integer) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#getCount(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Long getCount(final java.lang.String pFieldsContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.fieldsContainerId = :fieldsContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("fieldsContainerId", pFieldsContainerId);
            java.util.List lResults = lQueryObject.list();
            java.lang.Long lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Long"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Long) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#getAll(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(final java.lang.String pFieldsContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.fieldsContainerId = :fieldsContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("fieldsContainerId", pFieldsContainerId);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#deleteContainers(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean deleteContainers(
            final java.lang.String pFieldsContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.fieldsContainerId = :fieldsContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("fieldsContainerId", pFieldsContainerId);
            java.util.List lResults = lQueryObject.list();
            java.lang.Boolean lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Boolean"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Boolean) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#getTypeId(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getTypeId(final java.lang.String pValuesContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.ValuesContainerId = :ValuesContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("ValuesContainerId", pValuesContainerId);
            java.util.List lResults = lQueryObject.list();
            java.lang.String lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.String"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.String) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see 
     *      org.topcased.gpm.domain.link.Link#getTypesId(java.util.Collection<String
     *      >)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getTypesId(
            final java.util.Collection<String> pIds) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.Ids = :Ids";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("Ids", pIds);
            java.util.List lResults = lQueryObject.list();
            java.util.List<String> lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List<String>"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (java.util.List<String>) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#getLinkedElements(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getLinkedElements(final java.lang.String pId,
            final java.lang.String pLinkTypeName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.id = :id and link.linkTypeName = :linkTypeName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            lQueryObject.setParameter("linkTypeName", pLinkTypeName);
            java.util.List lResults = lQueryObject.list();
            java.util.List<String> lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List<String>"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (java.util.List<String>) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#removeSubField(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Object removeSubField(final java.lang.Object pSubField) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.subField = :subField";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("subField", pSubField);
            java.util.List lResults = lQueryObject.list();
            java.lang.Object lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Object"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Object) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#getAllId(java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getAllId(final java.lang.String pTypeId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.typeId = :typeId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("typeId", pTypeId);
            java.util.List lResults = lQueryObject.list();
            java.util.List<String> lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List<String>"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (java.util.List<String>) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#getIdByReference(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getIdByReference(final java.lang.String pReference,
            final java.lang.String pTypeName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.reference = :reference and link.typeName = :typeName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("reference", pReference);
            lQueryObject.setParameter("typeName", pTypeName);
            java.util.List lResults = lQueryObject.list();
            java.lang.String lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.String"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.String) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#getTypeName(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getTypeName(
            final java.lang.String pValuesContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.valuesContainerId = :valuesContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("valuesContainerId", pValuesContainerId);
            java.util.List lResults = lQueryObject.list();
            java.lang.String lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.String"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.String) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#getFunctionalReference(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getFunctionalReference(
            final java.lang.String pValuesContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.valuesContainerId = :valuesContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("valuesContainerId", pValuesContainerId);
            java.util.List lResults = lQueryObject.list();
            java.lang.String lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.String"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.String) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.attrContainer.id = :attrContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.attrContainer.id = :attrContainerId and link.attrNames = :attrNames";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameterList("attrNames", pAttrNames);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.link.Link#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.link.Link as link where link.attrContainer.id = :attrContainerId and link.attrName = :attrName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameter("attrName", pAttrName);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.attributes.Attribute lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.attributes.Attribute"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.attributes.Attribute) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }
}