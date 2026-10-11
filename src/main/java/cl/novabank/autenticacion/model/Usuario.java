package cl.novabank.autenticacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El RUT es obligatorio")
    @Column(unique = true, nullable = false)
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es valido")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "La password es obligatoria")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "CLIENTE|OPERADOR|AUDITOR", message = "Rol debe ser CLIENTE, OPERADOR o AUDITOR")
    @Column(nullable = false)
    private String rol;

    @Column(nullable = false)
    private Boolean activo;
}