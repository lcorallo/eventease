package repository.servament.repository;

import java.util.UUID;

import org.servament.entity.EventOperation;
import org.servament.model.filter.EventOperationFilter;

public interface IEventOperationRepository extends IBaseRepository<EventOperation, UUID, EventOperationFilter> {

}
