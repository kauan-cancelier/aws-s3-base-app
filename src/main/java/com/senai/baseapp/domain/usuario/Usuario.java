package com.senai.baseapp.domain.usuario;

import com.senai.baseapp.domain.permission.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(updatable = false, unique = true)
    private UUID id;

    @NotBlank
    @Size(min = 5, max = 120)
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ]{2,}(?:\\s+[A-Za-zÀ-ÖØ-öø-ÿ]{2,})+$",
            message = "Informe nome completo válido")
    private String nomeCompleto;

    @Email
    private String email;

    @Size(min = 8, max = 100)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Senha deve conter letra maiúscula, minúscula e número")
    private String senha;

    @Column(name = "foto_key")
    private String fotoKey;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


}
