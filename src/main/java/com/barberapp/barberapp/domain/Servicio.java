package com.barberapp.barberapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "servicio")
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer idServicio;

    @Column(unique = true, nullable = false, length = 60)
    @NotBlank(message = "El nombre del servicio no puede estar vacío.")
    @Size(max = 60, message = "El nombre no puede tener más de 60 caracteres.")
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "El precio no puede estar vacío.")
    @DecimalMin(value = "0.01", inclusive = true, message = "El precio debe ser mayor a 0.")
    private BigDecimal precio;

    @Column(name = "ruta_imagen", length = 1024)
    @Size(max = 1024)
    private String rutaImagen;

    @Column(nullable = false)
    private boolean activo = true;
}