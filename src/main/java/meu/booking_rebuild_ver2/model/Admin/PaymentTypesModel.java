package meu.booking_rebuild_ver2.model.Admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import meu.booking_rebuild_ver2.model.Status;
import meu.booking_rebuild_ver2.model.User;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_types")
public class PaymentTypesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String paymentType;

    @ManyToOne
    @JoinColumn(name = "status" )
    private Status status;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "id_user_config" )
    @JsonIgnore
    private User idUserConfig;
    @JsonIgnore
    private ZonedDateTime createdAt = ZonedDateTime.now();
    @JsonIgnore
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    public PaymentTypesModel(UUID id) {
        this.id = id;
    }
}
