/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.filter.FilterFacade;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterScope;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterUsage;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterVisibility;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterFieldName;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterFieldNameType;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterCriteriaGroup;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterCriterion;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterOperator;
import org.topcased.gpm.ui.facade.shared.filter.field.result.UiFilterResultField;
import org.topcased.gpm.ui.facade.shared.filter.field.sort.UiFilterSorting;
import org.topcased.gpm.ui.facade.shared.filter.field.sort.UiFilterSortingField;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummary;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestSaveFilterFacade
 * 
 * @author nveillet
 */
public class TestSaveFilterFacade extends AbstractFacadeTestCase {

    private static final String FILTER_NAME = "SHEET_1 LIST TABLE";

    private static final String SHEET_REFERENCE = "REF_Origin_Sheet";

    private void compare(List<UiFilterFieldName> pExpectedName,
            List<UiFilterFieldName> pSavedName) {
        assertEquals(pExpectedName.size(), pSavedName.size());
        for (int i = 0; i < pExpectedName.size(); i++) {
            UiFilterFieldName lExpectedFilterFieldName = pExpectedName.get(i);
            UiFilterFieldName lSavedFilterFieldName = pSavedName.get(i);
            assertEquals(lExpectedFilterFieldName.getName(),
                    lSavedFilterFieldName.getName());
            assertEquals(lExpectedFilterFieldName.getType(),
                    lSavedFilterFieldName.getType());
        }
    }

    private void compare(UiFilter pExpectedFilter, UiFilter pSavedFilter) {

        assertEquals(pExpectedFilter.getDescription(),
                pSavedFilter.getDescription());
        assertEquals(pExpectedFilter.getId(), pSavedFilter.getId());
        assertEquals(pExpectedFilter.getName(), pSavedFilter.getName());
        assertEquals(pExpectedFilter.getHidden(), pSavedFilter.getHidden());
        assertEquals(pExpectedFilter.getFilterType(),
                pSavedFilter.getFilterType());
        assertEquals(pExpectedFilter.getUsage(), pSavedFilter.getUsage());
        assertEquals(pExpectedFilter.getVisibility(),
                pSavedFilter.getVisibility());

        assertEquals(pExpectedFilter.getContainerTypes().size(),
                pSavedFilter.getContainerTypes().size());
        for (int i = 0; i < pExpectedFilter.getContainerTypes().size(); i++) {
            UiFilterContainerType lExpectedContainerType =
                    pExpectedFilter.getContainerTypes().get(i);
            UiFilterContainerType lSavedContainerType =
                    pSavedFilter.getContainerTypes().get(i);
            assertEquals(lExpectedContainerType.getId(),
                    lSavedContainerType.getId());
            assertEquals(lExpectedContainerType.getName().getValue(),
                    lSavedContainerType.getName().getValue());
        }

        assertEquals(pExpectedFilter.getCriteriaGroups().size(),
                pSavedFilter.getCriteriaGroups().size());
        for (int i = 0; i < pExpectedFilter.getCriteriaGroups().size(); i++) {
            UiFilterCriteriaGroup lExpectedCriteriaGroup =
                    pExpectedFilter.getCriteriaGroups().get(i);
            UiFilterCriteriaGroup lSavedCriteriaGroup =
                    pSavedFilter.getCriteriaGroups().get(i);

            assertEquals(lExpectedCriteriaGroup.getCriteria().size(),
                    lSavedCriteriaGroup.getCriteria().size());
            for (int j = 0; j < lExpectedCriteriaGroup.getCriteria().size(); j++) {
                UiFilterCriterion lExpectedCriterion =
                        lExpectedCriteriaGroup.getCriteria().get(j);
                UiFilterCriterion lSavedCriterion =
                        lSavedCriteriaGroup.getCriteria().get(j);
                assertEquals(lExpectedCriterion.getId(),
                        lSavedCriterion.getId());
                assertEquals(lExpectedCriterion.getValue(),
                        lSavedCriterion.getValue());
                assertEquals(lExpectedCriterion.getFieldType(),
                        lSavedCriterion.getFieldType());
                compare(lExpectedCriterion.getName(), lSavedCriterion.getName());
                assertEquals(lExpectedCriterion.getOperator(),
                        lSavedCriterion.getOperator());
            }
        }

        assertEquals(pExpectedFilter.getResultFields().size(),
                pSavedFilter.getResultFields().size());
        for (int i = 0; i < pExpectedFilter.getResultFields().size(); i++) {
            UiFilterResultField lExpectedResultField =
                    pExpectedFilter.getResultFields().get(i);
            UiFilterResultField lSavedResultField =
                    pSavedFilter.getResultFields().get(i);
            assertEquals(lExpectedResultField.getLabel(),
                    lSavedResultField.getLabel());
            compare(lExpectedResultField.getName(), lSavedResultField.getName());
        }

        assertEquals(pExpectedFilter.getScopes().size(),
                pSavedFilter.getScopes().size());
        //Make product name list
        Map<String, UiFilterScope> lExpectedProductNameList =
                new HashMap<String, UiFilterScope>();
        for (int i = 0; i < pExpectedFilter.getScopes().size(); i++) {
            UiFilterScope lExpectedScope = pExpectedFilter.getScopes().get(i);
            lExpectedProductNameList.put(
                    lExpectedScope.getProductName().getValue(), lExpectedScope);
        }

        for (int i = 0; i < pExpectedFilter.getScopes().size(); i++) {
            UiFilterScope lSavedScope = pSavedFilter.getScopes().get(i);
            assertTrue(lExpectedProductNameList.containsKey(lSavedScope.getProductName().getValue()));
            assertEquals(
                    lExpectedProductNameList.get(
                            lSavedScope.getProductName().getValue()).getProductName().getValue(),
                    lSavedScope.getProductName().getValue());
            assertEquals(
                    lExpectedProductNameList.get(
                            lSavedScope.getProductName().getValue()).isIncludeSubProduct(),
                    lSavedScope.isIncludeSubProduct());
        }

        assertEquals(pExpectedFilter.getSortingFields().size(),
                pSavedFilter.getSortingFields().size());
        for (int i = 0; i < pExpectedFilter.getSortingFields().size(); i++) {
            UiFilterSortingField lExpectedSortingField =
                    pExpectedFilter.getSortingFields().get(i);
            UiFilterSortingField lSavedSortingField =
                    pSavedFilter.getSortingFields().get(i);
            assertEquals(lExpectedSortingField.getId(),
                    lSavedSortingField.getId());
            compare(lExpectedSortingField.getName(),
                    lSavedSortingField.getName());
            assertEquals(lExpectedSortingField.getOrder(),
                    lSavedSortingField.getOrder());
        }
    }

    /**
     * create filter
     */
    public void testCreateCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lSession = getAdminUserSession().getSession(getProductName());

        SheetService lSheetService = getSheetService();

        String lSheetTypeId =
                lSheetService.getCacheableSheet(
                        lSession.getRoleToken(),
                        lSheetService.getSheetIdByReference(
                                lSession.getRoleToken(), SHEET_REFERENCE),
                        CacheProperties.IMMUTABLE).getTypeId();

        List<String> lContainerList =
                Arrays.asList(new String[] { lSheetTypeId });

        UsableFieldData lBooleanField =
                getSearchService().getUsableField(
                        lSession.getParent().getProcessName(),
                        "BOOLEAN_SIMPLE_FIELD", lContainerList);
        UsableFieldData lDateField =
                getSearchService().getUsableField(
                        lSession.getParent().getProcessName(),
                        "DATE_SIMPLE_FIELD", lContainerList);
        UsableFieldData lIntegerField =
                getSearchService().getUsableField(
                        lSession.getParent().getProcessName(),
                        "INTEGER_SIMPLE_FIELD", lContainerList);
        UsableFieldData lRealField =
                getSearchService().getUsableField(
                        lSession.getParent().getProcessName(),
                        "REAL_SIMPLE_FIELD", lContainerList);
        UsableFieldData lStringField =
                getSearchService().getUsableField(
                        lSession.getParent().getProcessName(),
                        "STRING_SIMPLE_FIELD", lContainerList);
        UsableFieldData lMultipleField =
                getSearchService().getUsableField(
                        lSession.getParent().getProcessName(),
                        "MULTIPLE_FIELD", lContainerList);
        UsableFieldData lSubMultipleIntegerField =
                getSearchService().getUsableField(
                        lSession.getParent().getProcessName(),
                        "SUB_INTEGER_SIMPLE_FIELD", lContainerList);
        UsableFieldData lMultivaluedField =
                getSearchService().getUsableField(
                        lSession.getParent().getProcessName(),
                        "MULTIVALUED_INTEGER_SIMPLE_FIELD", lContainerList);
        UsableFieldData lAttachedField =
                getSearchService().getUsableField(
                        lSession.getParent().getProcessName(),
                        "ATTACHED_FIELD", lContainerList);
        UsableFieldData lChoiceField =
                getSearchService().getUsableField(
                        lSession.getParent().getProcessName(), "CHOICE_FIELD",
                        lContainerList);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Create UiFilter.");
        }

        // Choose containers
        List<UiFilterContainerType> lFilterContainerTypes =
                new ArrayList<UiFilterContainerType>();
        lFilterContainerTypes.add(new UiFilterContainerType(lSheetTypeId,
                new Translation("SHEET_1", "SHEET_1")));

        // Choose products
        ArrayList<UiFilterScope> lFilterScopes = new ArrayList<UiFilterScope>();
        lFilterScopes.add(new UiFilterScope(new Translation("ROOT_PRODUCT",
                "ROOT_PRODUCT"), true));

        // Select result fields
        ArrayList<UiFilterResultField> lFilterResultFields =
                new ArrayList<UiFilterResultField>();
        UiFilterFieldName lBooleanFieldName =
                new UiFilterFieldName("BOOLEAN_SIMPLE_FIELD",
                        "BOOLEAN_SIMPLE_FIELD", UiFilterFieldNameType.FIELD);
        UiFilterFieldName lDateFieldName =
                new UiFilterFieldName("DATE_SIMPLE_FIELD", "DATE_SIMPLE_FIELD",
                        UiFilterFieldNameType.FIELD);
        UiFilterFieldName lIntegerFieldName =
                new UiFilterFieldName("INTEGER_SIMPLE_FIELD",
                        "INTEGER_SIMPLE_FIELD", UiFilterFieldNameType.FIELD);
        UiFilterFieldName lRealFieldName =
                new UiFilterFieldName("REAL_SIMPLE_FIELD", "REAL_SIMPLE_FIELD",
                        UiFilterFieldNameType.FIELD);
        UiFilterFieldName lStringFieldName =
                new UiFilterFieldName("STRING_SIMPLE_FIELD",
                        "STRING_SIMPLE_FIELD", UiFilterFieldNameType.FIELD);
        UiFilterFieldName lMultipleFieldName =
                new UiFilterFieldName("MULTIPLE_FIELD", "MULTIPLE_FIELD",
                        UiFilterFieldNameType.FIELD);
        UiFilterFieldName lSubMultipleIntegerFieldName =
                new UiFilterFieldName("SUB_INTEGER_SIMPLE_FIELD",
                        "SUB_INTEGER_SIMPLE_FIELD", UiFilterFieldNameType.FIELD);
        UiFilterFieldName lMultivaluedFieldName =
                new UiFilterFieldName("MULTIVALUED_INTEGER_SIMPLE_FIELD",
                        "MULTIVALUED_INTEGER_SIMPLE_FIELD",
                        UiFilterFieldNameType.FIELD);
        UiFilterFieldName lAttachedFieldName =
                new UiFilterFieldName("ATTACHED_FIELD", "ATTACHED_FIELD",
                        UiFilterFieldNameType.FIELD);
        UiFilterFieldName lChoiceFieldName =
                new UiFilterFieldName("CHOICE_FIELD", "CHOICE_FIELD",
                        UiFilterFieldNameType.FIELD);

        LinkedList<UiFilterFieldName> lBooleanFieldCompleteName =
                new LinkedList<UiFilterFieldName>();
        lBooleanFieldCompleteName.add(lBooleanFieldName);
        lFilterResultFields.add(new UiFilterResultField(
                lBooleanField.getLabel(), lBooleanFieldCompleteName));

        LinkedList<UiFilterFieldName> lDateFieldCompleteName =
                new LinkedList<UiFilterFieldName>();
        lDateFieldCompleteName.add(lDateFieldName);
        lFilterResultFields.add(new UiFilterResultField(lDateField.getLabel(),
                lDateFieldCompleteName));

        LinkedList<UiFilterFieldName> lIntegerFieldCompleteName =
                new LinkedList<UiFilterFieldName>();
        lIntegerFieldCompleteName.add(lIntegerFieldName);
        lFilterResultFields.add(new UiFilterResultField(
                lIntegerField.getLabel(), lIntegerFieldCompleteName));

        LinkedList<UiFilterFieldName> lRealFieldCompleteName =
                new LinkedList<UiFilterFieldName>();
        lRealFieldCompleteName.add(lRealFieldName);
        lFilterResultFields.add(new UiFilterResultField(lRealField.getLabel(),
                lRealFieldCompleteName));

        LinkedList<UiFilterFieldName> lStringFieldCompleteName =
                new LinkedList<UiFilterFieldName>();
        lStringFieldCompleteName.add(lStringFieldName);
        lFilterResultFields.add(new UiFilterResultField(
                lStringField.getLabel(), lStringFieldCompleteName));

        LinkedList<UiFilterFieldName> lMultipleFieldCompleteName =
                new LinkedList<UiFilterFieldName>();
        lMultipleFieldCompleteName.add(lMultipleFieldName);
        lFilterResultFields.add(new UiFilterResultField(
                lMultipleField.getLabel(), lMultipleFieldCompleteName));

        LinkedList<UiFilterFieldName> lSubMultipleIntegerFieldCompleteName =
                new LinkedList<UiFilterFieldName>();
        lSubMultipleIntegerFieldCompleteName.add(new UiFilterFieldName(
                "MULTIPLE_FIELD", "MULTIPLE_FIELD",
                UiFilterFieldNameType.MULTIPLE));
        lSubMultipleIntegerFieldCompleteName.add(lSubMultipleIntegerFieldName);
        lFilterResultFields.add(new UiFilterResultField(
                lSubMultipleIntegerField.getLabel(),
                lSubMultipleIntegerFieldCompleteName));

        LinkedList<UiFilterFieldName> lMultivaluedFieldCompleteName =
                new LinkedList<UiFilterFieldName>();
        lMultivaluedFieldCompleteName.add(lMultivaluedFieldName);
        lFilterResultFields.add(new UiFilterResultField(
                lMultivaluedField.getLabel(), lMultivaluedFieldCompleteName));

        LinkedList<UiFilterFieldName> lAttachedFieldCompleteName =
                new LinkedList<UiFilterFieldName>();
        lAttachedFieldCompleteName.add(lAttachedFieldName);
        lFilterResultFields.add(new UiFilterResultField(
                lAttachedField.getLabel(), lAttachedFieldCompleteName));

        LinkedList<UiFilterFieldName> lChoiceFieldCompleteName =
                new LinkedList<UiFilterFieldName>();
        lChoiceFieldCompleteName.add(lChoiceFieldName);
        lFilterResultFields.add(new UiFilterResultField(
                lChoiceField.getLabel(), lChoiceFieldCompleteName));

        // Select criterias
        List<UiFilterCriteriaGroup> lCriteriaGroups =
                new ArrayList<UiFilterCriteriaGroup>();
        UiFilterCriteriaGroup lCriteriaGroup1 = new UiFilterCriteriaGroup();
        UiFilterCriteriaGroup lCriteriaGroup2 = new UiFilterCriteriaGroup();
        List<UiFilterCriterion> lCriterions1 =
                new ArrayList<UiFilterCriterion>();
        lCriterions1.add(new UiFilterCriterion(lBooleanField.getFieldId(),
                lBooleanFieldCompleteName, UiFilterOperator.EQUAL,
                String.valueOf(true), false, FieldType.BOOLEAN, null));
        List<UiFilterCriterion> lCriterions2 =
                new ArrayList<UiFilterCriterion>();
        lCriterions2.add(new UiFilterCriterion(lDateField.getFieldId(),
                lDateFieldCompleteName, UiFilterOperator.GREATER,
                DateFormatUtils.ISO_DATE_FORMAT.format(new Date()), false,
                FieldType.DATE, null));
        lCriterions2.add(new UiFilterCriterion(lIntegerField.getFieldId(),
                lIntegerFieldCompleteName, UiFilterOperator.NOT_EQUAL,
                String.valueOf(89), false, FieldType.INTEGER, null));
        lCriterions2.add(new UiFilterCriterion(lRealField.getFieldId(),
                lRealFieldCompleteName, UiFilterOperator.LESS_OR_EQUALS,
                String.valueOf(87), false, FieldType.REAL, null));
        lCriterions2.add(new UiFilterCriterion(lStringField.getFieldId(),
                lStringFieldCompleteName, UiFilterOperator.LIKE, "plop*rEPlop",
                true, FieldType.STRING, null));
        lCriterions2.add(new UiFilterCriterion(
                lSubMultipleIntegerField.getFieldId(),
                lSubMultipleIntegerFieldCompleteName,
                UiFilterOperator.LESS_OR_EQUALS, String.valueOf(135), false,
                FieldType.INTEGER, null));
        lCriterions2.add(new UiFilterCriterion(lMultivaluedField.getFieldId(),
                lMultivaluedFieldCompleteName,
                UiFilterOperator.GREATER_OR_EQUALS, String.valueOf(789), false,
                FieldType.INTEGER, null));
        lCriterions2.add(new UiFilterCriterion(lAttachedField.getFieldId(),
                lAttachedFieldCompleteName, UiFilterOperator.EQUAL, "new file",
                true, FieldType.ATTACHED, null));
        lCriterions2.add(new UiFilterCriterion(lChoiceField.getFieldId(),
                lChoiceFieldCompleteName, UiFilterOperator.EQUAL, "CHOICE 4",
                true, FieldType.CHOICE, null));
        lCriteriaGroup1.setCriteria(lCriterions1);
        lCriteriaGroup2.setCriteria(lCriterions2);
        lCriteriaGroups.add(lCriteriaGroup1);
        lCriteriaGroups.add(lCriteriaGroup2);

        // Choose sorting fields
        List<UiFilterSortingField> lSortingFields =
                new ArrayList<UiFilterSortingField>();
        lSortingFields.add(new UiFilterSortingField(lStringField.getFieldId(),
                lStringFieldCompleteName, UiFilterSorting.ASCENDANT,
                FieldType.STRING, false));
        lSortingFields.add(new UiFilterSortingField(lIntegerField.getFieldId(),
                lIntegerFieldCompleteName, UiFilterSorting.ASCENDANT_DEFINED,
                FieldType.INTEGER, false));
        lSortingFields.add(new UiFilterSortingField(
                lSubMultipleIntegerField.getFieldId(),
                lSubMultipleIntegerFieldCompleteName,
                UiFilterSorting.DESCENDANT, FieldType.MULTIPLE, false));
        lSortingFields.add(new UiFilterSortingField(
                lMultivaluedField.getFieldId(), lMultivaluedFieldCompleteName,
                UiFilterSorting.DESCENDANT_DEFINED, FieldType.MULTIVALUED,
                false));
        lSortingFields.add(new UiFilterSortingField(
                lAttachedField.getFieldId(), lAttachedFieldCompleteName,
                UiFilterSorting.DESCENDANT_DEFINED, FieldType.ATTACHED, false));
        lSortingFields.add(new UiFilterSortingField(lChoiceField.getFieldId(),
                lChoiceFieldCompleteName, UiFilterSorting.ASCENDANT,
                FieldType.CHOICE, false));

        UiFilter lExpectedFilter = new UiFilter();
        lExpectedFilter.setContainerTypes(lFilterContainerTypes);
        lExpectedFilter.setCriteriaGroups(lCriteriaGroups);
        lExpectedFilter.setDescription("new desc");
        lExpectedFilter.setFilterType(FilterType.SHEET);
        lExpectedFilter.setHidden(false);
        lExpectedFilter.setName("NEW_FILTER");
        lExpectedFilter.setResultFields(lFilterResultFields);
        lExpectedFilter.setScopes(lFilterScopes);
        lExpectedFilter.setSortingFields(lSortingFields);
        lExpectedFilter.setUsage(UiFilterUsage.BOTH_VIEWS);
        lExpectedFilter.setVisibility(UiFilterVisibility.PRODUCT);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Save Filter.");
        }
        String lFilterId = lFilterFacade.saveFilter(lSession, lExpectedFilter);
        lExpectedFilter.setId(lFilterId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Filter.");
        }
        UiFilter lSavedFilter = lFilterFacade.getFilter(lSession, lFilterId);

        compare(lExpectedFilter, lSavedFilter);
    }

    /**
     * update existing filter
     */
    public void testUpdateCase() {

        FilterFacade lFilterFacade = getFacadeLocator().getFilterFacade();
        UiSession lSession = getAdminUserSession().getSession(getProductName());

        UiFilterSummaries lFilterSummaries =
                lFilterFacade.getFilters(lSession, FilterType.SHEET);

        List<UiFilterSummary> lFilterSummariesList =
                getFilterSummariesAsList(lFilterSummaries);

        String lFilterId = null;

        for (UiFilterSummary lFilterSummary : lFilterSummariesList) {
            if (FILTER_NAME.equals(lFilterSummary.getName())) {
                lFilterId = lFilterSummary.getId();
                break;
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Filter.");
        }
        UiFilter lExpectedFilter = lFilterFacade.getFilter(lSession, lFilterId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Modify Filter.");
        }
        lExpectedFilter.setDescription("changed desc");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Save Filter.");
        }
        lFilterFacade.saveFilter(lSession, lExpectedFilter);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get Filter.");
        }
        UiFilter lSavedFilter = lFilterFacade.getFilter(lSession, lFilterId);

        compare(lExpectedFilter, lSavedFilter);
    }
}
