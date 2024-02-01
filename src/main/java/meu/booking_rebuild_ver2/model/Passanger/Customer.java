package meu.booking_rebuild_ver2.model.Passanger;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import meu.booking_rebuild_ver2.model.Admin.Loyalty;
import meu.booking_rebuild_ver2.model.Status;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, length = 100)
    private String name;
    @Size(max = 10, min = 10)
    @Column(nullable = false, unique = true)
    private String phone;
    @Column(name = "number_of_trip")
    private int numberOfTrips;
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "status", nullable = false)
    @JsonBackReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Ignore Hibernate properties to avoid serialization issues
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "id_loyalty", nullable = false)
    @JsonBackReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Ignore Hibernate properties to avoid serialization issues
    private Loyalty loyalty;
    @CreationTimestamp
    @Column( name = "created_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Instant createdAt;
    @UpdateTimestamp
    @Column( name = "updated_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Instant updatedAt = null;
    @Column(name = "last_updated")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean lastUpdated = false;
    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Customer(UUID id) {
        this.id = id;
    }
}
