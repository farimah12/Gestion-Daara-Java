package sn.l2gl.farimah.daara.controller;

import sn.l2gl.farimah.daara.exception.*;
import sn.l2gl.farimah.daara.model.dao.MaitreDao;
import sn.l2gl.farimah.daara.model.models.Maitre;
import sn.l2gl.farimah.daara.util.CsvExporter;
import sn.l2gl.farimah.daara.view.MaitreView;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MaitreController {

    private final MaitreDao dao = new MaitreDao();
    private final MaitreView vue;

    public MaitreController(MaitreView vue) {
        this.vue = vue;
        vue.getBoutonEnregistrer().addActionListener(e -> enregistrer());
        vue.getBoutonModifier().addActionListener(e -> modifier());
        vue.getBoutonSupprimer().addActionListener(e -> supprimer());
        vue.getBoutonChercher().addActionListener(e -> rechercher());
        vue.getBoutonToutAfficher().addActionListener(e -> rafraichir());
        vue.getBoutonExporter().addActionListener(e -> exporter());

        // Clic sur une ligne de la table → remplir le formulaire
        vue.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = vue.getTable().getSelectedRow();
                if (row >= 0) {
                    String matricule = (String) vue.getTableModel().getValueAt(row, 0);
                    dao.trouver(matricule).ifPresent(vue::remplir);
                }
            }
        });

        rafraichir();
    }

    private void rafraichir() {
        vue.afficher(dao.listerTous());
    }

    private void enregistrer() {
        String matricule = vue.getChampMatricule().getText().trim();
        String nom       = vue.getChampNom().getText().trim();
        String tel       = vue.getChampTelephone().getText().trim();

        if (matricule.isEmpty() || nom.isEmpty()) {
            JOptionPane.showMessageDialog(vue, "Matricule et nom sont obligatoires !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            dao.inserer(new Maitre(matricule, nom, tel));
            JOptionPane.showMessageDialog(vue, "Maître ajouté avec succès !");
            vue.reinitialiser();
            rafraichir();
        } catch (MaitreDejaExistantException ex) {
            JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifier() {
        String matricule = vue.getChampMatricule().getText().trim();
        String nom       = vue.getChampNom().getText().trim();
        String tel       = vue.getChampTelephone().getText().trim();

        if (matricule.isEmpty() || nom.isEmpty()) {
            JOptionPane.showMessageDialog(vue, "Matricule et nom sont obligatoires !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            dao.modifier(new Maitre(matricule, nom, tel));
            JOptionPane.showMessageDialog(vue, "Maître modifié avec succès !");
            vue.reinitialiser();
            rafraichir();
        } catch (MaitreIntrouvableException ex) {
            JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimer() {
        String matricule = vue.getChampMatricule().getText().trim();
        if (matricule.isEmpty()) {
            JOptionPane.showMessageDialog(vue, "Sélectionnez un maître à supprimer !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vue,
                "Voulez-vous vraiment supprimer ce maître ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            dao.supprimer(matricule);
            JOptionPane.showMessageDialog(vue, "Maître supprimé avec succès !");
            vue.reinitialiser();
            rafraichir();
        } catch (MaitreIntrouvableException | SuppressionImpossibleException ex) {
            JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rechercher() {
        String txt = vue.getChampRecherche().getText().trim();
        vue.afficher(dao.rechercherParNom(txt));
    }

    private void exporter() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("maitres.csv"));
        if (chooser.showSaveDialog(vue) == JFileChooser.APPROVE_OPTION) {
            try {
                String[] entetes = {"Matricule", "Nom Complet", "Téléphone"};
                List<String[]> lignes = new ArrayList<>();
                for (Maitre m : dao.listerTous()) {
                    lignes.add(new String[]{m.getMatricule(), m.getNomComplet(), m.getTelephone()});
                }
                CsvExporter.exporter(chooser.getSelectedFile().getAbsolutePath(), entetes, lignes);
                JOptionPane.showMessageDialog(vue, "Export CSV réussi !");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur export : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}