package org.servament.repository;

import java.util.UUID;

import org.servament.entity.EventService;
import org.servament.model.filter.EventServiceFilter;

public interface IEventServiceRepository extends IBaseRepository<EventService, UUID, EventServiceFilter> {

}
