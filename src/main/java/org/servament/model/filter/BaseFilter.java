package org.servament.model.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class BaseFilter {

    public Integer limit = 100;

    public Integer offset = 0;
}
