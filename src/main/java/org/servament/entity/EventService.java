package org.servament.entity;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.proxy.HibernateProxy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor

@Entity(name = "event_service")
public class EventService extends Event {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<EventOperation> operations;

    @OneToMany(mappedBy = "event", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Booking> bookings;

    @Column(name = "code", unique = true, length = 100)
    private String code;

    @Column(name = "supplier")
    private UUID supplier;

    @Column(name = "num_availability")
    private Integer availability;    

    @Override 
    public final boolean equals(Object o) { 
        if (this == o) return true; 
        if (o == null) return false; 
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass(); 
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass(); 
        if (thisEffectiveClass != oEffectiveClass) return false; 
        EventService eventService = (EventService) o;
        
        return getId() != null && Objects.equals(getId(), eventService.getId()); 
    }

    @Override 
    public final int hashCode() { 
        return this instanceof HibernateProxy 
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() 
            : getClass().hashCode(); 
    }
 
}
