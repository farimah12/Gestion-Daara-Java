package sn.l2gl.farimah.daara.controller;

import sn.l2gl.farimah.daara.exception.*;
import sn.l2gl.farimah.daara.model.dao.ProgressionDao;
import sn.l2gl.farimah.daara.model.dao.TalibeDao;
import sn.l2gl.farimah.daara.model.models.Progression;
import sn.l2gl.farimah.daara.model.models.Talibe;
import sn.l2gl.farimah.daara.util.CsvExporter;
import sn.l2gl.farimah.daara.view.ProgressionView;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ProgressionController {

    private final ProgressionDao dao    = new ProgressionDao();
    private final TalibeDao talibeDao   = new TalibeDao();
    private final ProgressionView vue;

    public ProgressionController(ProgressionView vue) {
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
                    Long id = (Long) vue.getTableModel().getValueAt(row, 0);
                    dao.trouver(id).ifPresent(vue::remplir);
                }
            }
        });

        rafraichir();
    }

    private void rafraichir() {
        vue.chargerTalibes(talibeDao.listerTous());
        vue.afficher(dao.listerTous());
    }

    private void enregistrer() {
        try {
            Talibe talibe   = (Talibe) vue.getChampTalibe().getSelectedItem();
            String sourate  = vue.getChampSourate().getText().trim();
            String versetsStr = vue.getChampNombreVersets().getText().trim();
            String dateStr  = vue.getChampDate().getText().trim();
            String appreci  = vue.getChampAppreciation().getText().trim();

            if (talibe == null || sourate.isEmpty() || versetsStr.isEmpty() || dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(vue, "Talibé, sourate, versets et date sont obligatoires !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int nombreVersets = Integer.parseInt(versetsStr);
            LocalDate date    = LocalDate.parse(dateStr);

            dao.inserer(new Progression(talibe, sourate, nombreVersets, date, appreci));
            JOptionPane.showMessageDialog(vue, "Progression ajoutée avec succès !");
            vue.reinitialiser();
            rafraichir();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(vue, "Format de date invalide ! Utilisez yyyy-MM-dd", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vue, "Le nombre de versets doit être un entier !", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (ProgressionInvalideException ex) {
            JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifier() {
        try {
            Long id         = vue.getIdSelectionne();
            Talibe talibe   = (Talibe) vue.getChampTalibe().getSelectedItem();
            String sourate  = vue.getChampSourate().getText().trim();
            String versetsStr = vue.getChampNombreVersets().getText().trim();
            String dateStr  = vue.getChampDate().getText().trim();
            String appreci  = vue.getChampAppreciation().getText().trim();

            if (id == null) {
                JOptionPane.showMessageDialog(vue, "Sélectionnez une progression à modifier !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (talibe == null || sourate.isEmpty() || versetsStr.isEmpty() || dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(vue, "Talibé, sourate, versets et date sont obligatoires !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int nombreVersets = Integer.parseInt(versetsStr);
            LocalDate date    = LocalDate.parse(dateStr);

            Progression p = new Progression(talibe, sourate, nombreVersets, date, appreci);
            p.setId(id);
            dao.modifier(p);
            JOptionPane.showMessageDialog(vue, "Progression modifiée avec succès !");
            vue.reinitialiser();
            rafraichir();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(vue, "Format de date invalide ! Utilisez yyyy-MM-dd", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vue, "Le nombre de versets doit être un entier !", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (ProgressionIntrouvableException | ProgressionInvalideException ex) {
            JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimer() {
        Long id = vue.getIdSelectionne();
        if (id == null) {
            JOptionPane.showMessageDialog(vue, "Sélectionnez une progression à supprimer !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vue,
                "Voulez-vous vraiment supprimer cette progression ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            dao.supprimer(id);
            JOptionPane.showMessageDialog(vue, "Progression supprimée avec succès !");
            vue.reinitialiser();
            rafraichir();
        } catch (ProgressionIntrouvableException ex) {
            JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rechercher() {
        String txt = vue.getChampRecherche().getText().trim();
        vue.afficher(dao.rechercherParSourate(txt));
    }

    private void exporter() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("progressions.csv"));
        if (chooser.showSaveDialog(vue) == JFileChooser.APPROVE_OPTION) {
            try {
                String[] entetes = {"ID", "Talibé", "Sourate", "Versets", "Date", "Appréciation"};
                List<String[]> lignes = new ArrayList<>();
                for (Progression p : dao.listerTous()) {
                    lignes.add(new String[]{
                            p.getId().toString(),
                            p.getTalibe().getPrenom() + " " + p.getTalibe().getNom(),
                            p.getSourate(),
                            String.valueOf(p.getNombreVersets()),
                            p.getDateEvaluation().toString(),
                            p.getAppreciation() != null ? p.getAppreciation() : ""
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