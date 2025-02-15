package org.servament.model.filter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.servament.model.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class BookingFilter extends BaseFilter {

    private Set<BookingStatus> statuses;

    private List<UUID> events;

}
