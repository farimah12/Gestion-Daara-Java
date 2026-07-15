package sn.l2gl.farimah.daara.controller;

import sn.l2gl.farimah.daara.exception.*;
import sn.l2gl.farimah.daara.model.dao.ClasseDao;
import sn.l2gl.farimah.daara.model.dao.MaitreDao;
import sn.l2gl.farimah.daara.model.models.Classe;
import sn.l2gl.farimah.daara.model.models.Maitre;
import sn.l2gl.farimah.daara.util.CsvExporter;
import sn.l2gl.farimah.daara.view.ClasseView;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClasseController {

    private final ClasseDao dao      = new ClasseDao();
    private final MaitreDao maitreDao = new MaitreDao();
    private final ClasseView vue;

    public ClasseController(ClasseView vue) {
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
                    String code = (String) vue.getTableModel().getValueAt(row, 0);
                    dao.trouver(code).ifPresent(vue::remplir);
                }
            }
        });

        rafraichir();
    }

    private void rafraichir() {
        vue.chargerMaitres(maitreDao.listerTous());
        vue.afficher(dao.listerTous());
    }

    private void enregistrer() {
        String code    = vue.getChampCode().getText().trim();
        String libelle = vue.getChampLibelle().getText().trim();
        Classe.Niveau niveau = (Classe.Niveau) vue.getChampNiveau().getSelectedItem();
        Maitre maitre  = (Maitre) vue.getChampMaitre().getSelectedItem();

        if (code.isEmpty() || libelle.isEmpty() || maitre == null) {
            JOptionPane.showMessageDialog(vue, "Code, libellé et maître sont obligatoires !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            dao.inserer(new Classe(code, libelle, niveau, maitre));
            JOptionPane.showMessageDialog(vue, "Classe ajoutée avec succès !");
            vue.reinitialiser();
            rafraichir();
        } catch (ClasseDejaExistanteException ex) {
            JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifier() {
        String code    = vue.getChampCode().getText().trim();
        String libelle = vue.getChampLibelle().getText().trim();
        Classe.Niveau niveau = (Classe.Niveau) vue.getChampNiveau().getSelectedItem();
        Maitre maitre  = (Maitre) vue.getChampMaitre().getSelectedItem();

        if (code.isEmpty() || libelle.isEmpty() || maitre == null) {
            JOptionPane.showMessageDialog(vue, "Code, libellé et maître sont obligatoires !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            dao.modifier(new Classe(code, libelle, niveau, maitre));
            JOptionPane.showMessageDialog(vue, "Classe modifiée avec succès !");
            vue.reinitialiser();
            rafraichir();
        } catch (ClasseIntrouvableException ex) {
            JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimer() {
        String code = vue.getChampCode().getText().trim();
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(vue, "Sélectionnez une classe à supprimer !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vue,
                "Voulez-vous vraiment supprimer cette classe ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            dao.supprimer(code);
            JOptionPane.showMessageDialog(vue, "Classe supprimée avec succès !");
            vue.reinitialiser();
            rafraichir();
        } catch (ClasseIntrouvableException | SuppressionImpossibleException ex) {
            JOptionPane.showMessageDialog(vue, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rechercher() {
        String txt = vue.getChampRecherche().getText().trim();
        vue.afficher(dao.rechercherParLibelle(txt));
    }

    private void exporter() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("classes.csv"));
        if (chooser.showSaveDialog(vue) == JFileChooser.APPROVE_OPTION) {
            try {
                String[] entetes = {"Code", "Libellé", "Niveau", "Maître"};
                List<String[]> lignes = new ArrayList<>();
                for (Classe c : dao.listerTous()) {
                    lignes.add(new String[]{
                            c.getCode(),
                            c.getLibelle(),
                            c.getNiveau().toString(),
                            c.getMaitre().getNomComplet()
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