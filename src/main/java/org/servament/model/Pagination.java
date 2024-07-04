package org.servament.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class Pagination<T> {

   private List<T> data;
   private Integer page;
   private Integer size;
   private Integer totalPages;
   private Integer totalSize;
   
}
