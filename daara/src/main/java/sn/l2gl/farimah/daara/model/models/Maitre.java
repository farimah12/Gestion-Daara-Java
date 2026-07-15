package sn.l2gl.farimah.daara.model.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "maitres")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"matricule", "nomComplet"})
public class Maitre {

    @Id
    @Column(length = 50, unique = true, nullable = false)
    private String matricule;

    @Column(nullable = false, length = 100)
    private String nomComplet;

    @Column(length = 20)
    private String telephone;

    @OneToMany(mappedBy = "maitre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Classe> classes = new ArrayList<>();

    public Maitre(String matricule, String nomComplet, String telephone) {
        this.matricule = matricule;
        this.nomComplet = nomComplet;
        this.telephone = telephone;
    }
}