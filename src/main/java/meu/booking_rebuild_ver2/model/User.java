package meu.booking_rebuild_ver2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import meu.booking_rebuild_ver2.config.Constants;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "admin")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @Column(nullable = false, length = 100)
    @Size(max = 100)
    @Pattern(regexp = "^[ a-zA-Z0-9_.'\\-]+?", message = "Invalid characters in name")
    @NotNull
    private String fullname;
    @Column(nullable = false, unique = true)
    @Email
    @Pattern(regexp = "(^[0-9A-Za-z][\\w.\\-]+@[\\w]+\\.[\\w]\\S+\\w)$", message = Constants.MESSAGE_INVALID_USERNAME)
    @NotBlank
    @NotNull
    private String username;
    @Column(length = 100)
    @Size(min = 8)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(min = 8)
    @NotNull
    private String confirmPass;
    @CreationTimestamp
    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;
    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private AuthProvider authProvider = AuthProvider.LOCAL ;
    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserRole userRole = UserRole.ROLE_SUPER_ADMIN ;
    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "status")
    private Status status;
    public String getUserRole() {
        return String.valueOf(userRole);
    }

    public User(UUID id, String fullname, String username, Instant createdAt, UserRole userRole, Status status) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.createdAt = createdAt;
        this.userRole = userRole;
        this.status = status;
    }

    public User(String fullname, String username, String password, String confirmPass, Status status) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.confirmPass = confirmPass;
        this.status = status;
    }

    public User(UUID id) {
        this.id = id;
    }
}
