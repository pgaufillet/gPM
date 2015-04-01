/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.topcased.gpm.domain.export;

import java.util.List;

import org.hibernate.Query;

/**
 * @see org.topcased.gpm.domain.export.ReportModel
 * @author ahaugommard
 */
public class ReportModelDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.export.ReportModel, java.lang.String>
        implements org.topcased.gpm.domain.export.ReportModelDao {
    /**
     * Constructor
     */
    public ReportModelDaoImpl() {
        super(org.topcased.gpm.domain.export.ReportModel.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.export.ReportModelDaoBase#getCompatibleReportModels(java.lang.String[],
     *      org.topcased.gpm.domain.export.ExportType)
     */
    @SuppressWarnings("unchecked")
    public List<ReportModel> getCompatibleReportModels(
            final String[] pValuesContainersIds, final ExportType pExportType) {
        final String lHqlQuery =
                "from ReportModel reportModel "
                        + "where "
                        + "( "
                        + "(:type in elements (reportModel.exportTypes )) "
                        + "and "
                        + "( "
                        + "not exists ( "
                        + "from ValuesContainer valuesContainer "
                        + "where "
                        + " ("
                        + " valuesContainer.id in (:containersIds) "
                        + "and "
                        + " valuesContainer.definition.id not in elements(reportModel.fieldsContainers) "
                        + ")" + ")" + ")" + ")";

        final Query lQuery = getSession().createQuery(lHqlQuery);
        lQuery.setParameter("type", pExportType);
        lQuery.setParameterList("containersIds", pValuesContainersIds);
        lQuery.setCacheable(true);
        return (List<ReportModel>) lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.export.ReportModelDaoBase#getReportModel(java.lang.String,
     *      java.lang.String)
     */
    public ReportModel getReportModel(final String pReportName,
            final String pProcessName) {
        final String lHqlQuery =
                "from ReportModel reportModel "
                        + "where "
                        + "( "
                        + "reportModel.name = :reportName "
                        + "and "
                        + "not exists ("
                        + "   from FieldsContainer container where "
                        + "   container in elements (reportModel.fieldsContainers)"
                        + "   and not (container.businessProcess.name = :processName)"
                        + ")" + ")";

        final Query lQuery = getSession().createQuery(lHqlQuery);
        lQuery.setParameter("reportName", pReportName);
        lQuery.setParameter("processName", pProcessName);
        lQuery.setCacheable(true);
        return (ReportModel) lQuery.uniqueResult();
    }
}