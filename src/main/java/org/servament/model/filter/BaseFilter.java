package org.servament.model.filter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter

@NoArgsConstructor
@AllArgsConstructor
public class BaseFilter {

    private static final Integer DEFAULT_LIMIT = 100;

    private static final Integer DEFAULT_OFFSET = 0;

    private Integer limit;

    private Integer offset;

    public Integer getLimit() {
        if (limit == null) return BaseFilter.DEFAULT_LIMIT;
        return limit;
    }

    public Integer getOffset() {
        if (offset == null) return BaseFilter.DEFAULT_OFFSET;
        return offset;
    }
}
