/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.view.client;

/**
 * A list view that displays data in 'pages'.
 * <p>
 * Note: This class is new and its interface subject to change.
 * </p>
 * 
 * @param <T>
 *            the data type of each row
 */
public interface PagingListView<T> extends ListView<T> {

    /**
     * A pager delegate, implemented by classes that depend on the start index,
     * number of visible rows, or data size of a view.
     * 
     * @param <T>
     *            the data type of each row
     */
    public interface Pager<T> {
        void onRangeOrSizeChanged(PagingListView<T> listView);
    }

    /**
     * TODO: doc.
     */
    int getDataSize();

    /**
     * TODO: doc.
     */
    int getPageSize();

    /**
     * TODO: doc.
     */
    int getPageStart();

    /**
     * TODO: doc.
     * 
     * @param pager
     */
    void setPager(Pager<T> pager);

    /**
     * TODO: doc.
     * 
     * @param pageSize
     */
    void setPageSize(int pageSize);

    /**
     * TODO: doc.
     * 
     * @param pageStart
     */
    void setPageStart(int pageStart);
}
