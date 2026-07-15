package sn.l2gl.farimah.daara.model.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "progressions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "talibe")
public class Progression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "talibe_matricule", nullable = false)
    private Talibe talibe;

    @Column(nullable = false, length = 100)
    private String sourate;

    @Column(nullable = false)
    private int nombreVersets;

    @Column(nullable = false)
    private LocalDate dateEvaluation;

    @Column(length = 255)
    private String appreciation;

    public Progression(Talibe talibe, String sourate, int nombreVersets,
                       LocalDate dateEvaluation, String appreciation) {
        this.talibe = talibe;
        this.sourate = sourate;
        this.nombreVersets = nombreVersets;
        this.dateEvaluation = dateEvaluation;
        this.appreciation = appreciation;
    }
}