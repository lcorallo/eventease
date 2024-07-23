package org.servament.repository;

import java.util.List;

import org.servament.model.Pagination;
import org.servament.model.filter.PaginationFilter;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;

public interface IBaseRepository<K, V, T> extends PanacheRepositoryBase<K, V>{

    /**
     * Get a list of K entity types proper paginated by a paginationFilter and specific filter of K entity
     * 
     * @param paginationFilter  The pagination filter to handle
     * @param filter            the proper filter for the specific K entity type
     * @return                  a paginated list of K entities
     */
    Uni<Pagination<K>> pagination(PaginationFilter paginationFilter, T filter);

    /**
     * Get a list of K entities based on a specific filter of K entity
     * 
     * @param filter    the proper filter of the specific K entity type
     * @return          a list of K entities
     */
    Uni<List<K>> list(T filter);

    /**
     * Implementation of the {@link PanacheRepositoryBase#findById} method
     * It will be implemented in order to be mandatorily defined
     * @param id    the id of the entity K entity to be fetched
     * @return      the entity fetched
     */
    Uni<K> find(V id);

    /**
     * Create a K entity
     * 
     * @param incomingEntity    the entity to be persisted
     * @return                  the entity created
     */
    Uni<K> create(K incomingEntity);

    /**
     * Implementation of the {@link PanacheRepositoryBase#deleteById} method
     * It will be implemented in order to be mandatorily defined
     * @param id    the id of the entity K entity to be deleted
     */
    Uni<Void> remove(V id);
}
