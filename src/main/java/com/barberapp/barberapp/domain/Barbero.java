package com.barberapp.barberapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "barbero")
public class Barbero implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_barbero")
    private Integer idBarbero;

    @Column(nullable = false, length = 60)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 60, message = "El nombre no puede tener más de 60 caracteres.")
    private String nombre;

    @Column(length = 255)
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres.")
    private String descripcion;

    @Column(name = "hora_inicio", nullable = false)
    @NotNull(message = "La hora de inicio es obligatoria.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    @NotNull(message = "La hora de fin es obligatoria.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaFin;

    @Column(nullable = false)
    private boolean activo = true;
}