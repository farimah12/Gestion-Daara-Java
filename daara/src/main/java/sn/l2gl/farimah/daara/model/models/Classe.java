package sn.l2gl.farimah.daara.model.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"code", "libelle"})
public class Classe {

    @Id
    @Column(length = 50, unique = true, nullable = false)
    private String code;

    @Column(nullable = false, length = 100)
    private String libelle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Niveau niveau;

    @ManyToOne(optional = false)
    @JoinColumn(name = "maitre_matricule", nullable = false)
    private Maitre maitre;

    @OneToMany(mappedBy = "classe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Talibe> talibes = new ArrayList<>();

    public enum Niveau {
        DEBUTANT, INTERMEDIAIRE, AVANCE
    }

    public Classe(String code, String libelle, Niveau niveau, Maitre maitre) {
        this.code = code;
        this.libelle = libelle;
        this.niveau = niveau;
        this.maitre = maitre;
    }
}