package org.servament.entity;

import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.proxy.HibernateProxy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity(name = "event_operation")
public class EventOperation extends Event {
    
    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "event_service_id", referencedColumnName = "id", nullable = false)
    private EventService eventService;

    @Column(name = "operator")
    private UUID operatorId;

    @Column(name = "num_partecipants")
    private Integer partecipants;
    
    @Override 
    public final boolean equals(Object o) { 
        if (this == o) return true; 
        if (o == null) return false; 
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass(); 
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass(); 
        if (thisEffectiveClass != oEffectiveClass) return false; 
        EventOperation eventOperation = (EventOperation) o;
        
        return getId() != null && Objects.equals(getId(), eventOperation.getId()); 
    }

    @Override 
    public final int hashCode() { 
        return this instanceof HibernateProxy 
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() 
            : getClass().hashCode(); 
    }
}
