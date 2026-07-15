package sn.l2gl.farimah.daara.model.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "talibes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"matricule", "prenom", "nom"})
public class Talibe {

    @Id
    @Column(length = 50, unique = true, nullable = false)
    private String matricule;

    @Column(nullable = false, length = 50)
    private String prenom;

    @Column(nullable = false, length = 50)
    private String nom;

    @Column(nullable = false)
    private LocalDate dateNaissance;

    @Column(nullable = false, length = 100)
    private String nomTuteur;

    @Column(length = 20)
    private String telephoneTuteur;

    @ManyToOne(optional = false)
    @JoinColumn(name = "classe_code", nullable = false)
    private Classe classe;

    @OneToMany(mappedBy = "talibe", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Progression> progressions = new ArrayList<>();

    public Talibe(String matricule, String prenom, String nom, LocalDate dateNaissance,
                  String nomTuteur, String telephoneTuteur, Classe classe) {
        this.matricule = matricule;
        this.prenom = prenom;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.nomTuteur = nomTuteur;
        this.telephoneTuteur = telephoneTuteur;
        this.classe = classe;
    }
}