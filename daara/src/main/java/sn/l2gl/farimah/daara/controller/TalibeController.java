package sn.l2gl.farimah.daara.controller;

import sn.l2gl.farimah.daara.exception.*;
import sn.l2gl.farimah.daara.model.dao.ClasseDao;
import sn.l2gl.farimah.daara.model.dao.TalibeDao;
import sn.l2gl.farimah.daara.model.models.Classe;
import sn.l2gl.farimah.daara.model.models.Talibe;
import sn.l2gl.farimah.daara.util.CsvExporter;
import sn.l2gl.farimah.daara.view.TalibeView;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TalibeController {

    private final TalibeDao dao       = new TalibeDao();
    private final ClasseDao classeDao = new ClasseDao();
    private final TalibeView vue;

    public TalibeController(TalibeView vue) {
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
        vue.chargerClasses(classeDao.listerTous());
        vue.afficher(dao.listerTous());
    }

    private void enregistrer() {
        try {
            String matricule  = vue.getChampMatricule().getText().trim();
            String prenom     = vue.getChampPrenom().getText().trim();
            String nom        = vue.getChampNom().getText().trim();
            String dateStr    = vue.getChampDateNaissance().getText().trim();
            String nomTuteur  = vue.getChampNomTuteur().getText().trim();
            String telTuteur  = vue.getChampTelTuteur().getText().trim();
            Classe classe     = (Classe) vue.getChampClasse().getSelectedItem();

            if (matricule.isEmpty() || prenom.isEmpty() || nom.isEmpty() || dateStr.isEmpty() || nomTuteur.isEmpty() || classe == null) {
                JOptionPane.showMessageDialog(vue, "Tous les champs obligatoires doivent être remplis !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate dateNaissance = LocalDate.parse(dateStr);
            dao.inserer(new Talibe(matricule, prenom, nom, dateNaissance, nomTuteur, telTuteur, classe));
            JOptionPane.showMessageDialog(vue, "Talibé ajouté avec succès !");
            vue.reinitialiser();
            rafraichir();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(vue, "Format de date invalide ! Utilisez yyyy-MM-dd", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (TalibeDejaExistantException ex) {
            JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifier() {
        try {
            String matricule  = vue.getChampMatricule().getText().trim();
            String prenom     = vue.getChampPrenom().getText().trim();
            String nom        = vue.getChampNom().getText().trim();
            String dateStr    = vue.getChampDateNaissance().getText().trim();
            String nomTuteur  = vue.getChampNomTuteur().getText().trim();
            String telTuteur  = vue.getChampTelTuteur().getText().trim();
            Classe classe     = (Classe) vue.getChampClasse().getSelectedItem();

            if (matricule.isEmpty() || prenom.isEmpty() || nom.isEmpty() || dateStr.isEmpty() || nomTuteur.isEmpty() || classe == null) {
                JOptionPane.showMessageDialog(vue, "Tous les champs obligatoires doivent être remplis !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate dateNaissance = LocalDate.parse(dateStr);
            dao.modifier(new Talibe(matricule, prenom, nom, dateNaissance, nomTuteur, telTuteur, classe));
            JOptionPane.showMessageDialog(vue, "Talibé modifié avec succès !");
            vue.reinitialiser();
            rafraichir();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(vue, "Format de date invalide ! Utilisez yyyy-MM-dd", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (TalibeIntrouvableException ex) {
            JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimer() {
        String matricule = vue.getChampMatricule().getText().trim();
        if (matricule.isEmpty()) {
            JOptionPane.showMessageDialog(vue, "Sélectionnez un talibé à supprimer !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vue,
                "Voulez-vous vraiment supprimer ce talibé ? Ses progressions seront aussi supprimées.", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            dao.supprimer(matricule);
            JOptionPane.showMessageDialog(vue, "Talibé supprimé avec succès !");
            vue.reinitialiser();
            rafraichir();
        } catch (TalibeIntrouvableException ex) {
            JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rechercher() {
        String txt = vue.getChampRecherche().getText().trim();
        vue.afficher(dao.rechercherParNom(txt));
    }

    private void exporter() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("talibes.csv"));
        if (chooser.showSaveDialog(vue) == JFileChooser.APPROVE_OPTION) {
            try {
                String[] entetes = {"Matricule", "Prénom", "Nom", "Date Naissance", "Tuteur", "Tél Tuteur", "Classe"};
                List<String[]> lignes = new ArrayList<>();
                for (Talibe t : dao.listerTous()) {
                    lignes.add(new String[]{
                            t.getMatricule(),
                            t.getPrenom(),
                            t.getNom(),
                            t.getDateNaissance().toString(),
                            t.getNomTuteur(),
                            t.getTelephoneTuteur() != null ? t.getTelephoneTuteur() : "",
                            t.getClasse().getCode()
                    });
                }
                CsvExporter.exporter(chooser.getSelectedFile().getAbsolutePath(), entetes, lignes);
                JOptionPane.showMessageDialog(vue, "Export CSV réussi !");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur export : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}