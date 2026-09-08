package br.com.mmtech.equilibra.backend.convite.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "convites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Convite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiraEm;

    @Column(nullable = false)
    @Builder.Default
    private boolean utilizado = false;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime criadoEm = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

}
