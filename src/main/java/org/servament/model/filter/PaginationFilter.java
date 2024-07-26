package org.servament.model.filter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter

@NoArgsConstructor
@AllArgsConstructor

public class PaginationFilter {

    private static final Integer DEFAULT_PAGE_SIZE = 10;

    private static final Integer DEFAULT_NUM_PAGE = 0;

    private Integer pageSize;

    private Integer numPage;

    public Integer getPageSize() {
        if (pageSize == null) return PaginationFilter.DEFAULT_PAGE_SIZE;
        return pageSize;
    }

    public Integer getNumPage() {
        if (numPage == null) return PaginationFilter.DEFAULT_NUM_PAGE;
        return numPage;
    }

}
