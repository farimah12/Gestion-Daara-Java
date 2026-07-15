package sn.l2gl.farimah.daara.view;

import lombok.Getter;
import sn.l2gl.farimah.daara.model.models.Classe;
import sn.l2gl.farimah.daara.model.models.Talibe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Getter
public class TalibeView extends JPanel {

    private JTextField champMatricule       = new JTextField(15);
    private JTextField champPrenom          = new JTextField(20);
    private JTextField champNom             = new JTextField(20);
    private JTextField champDateNaissance   = new JTextField(15);
    private JTextField champNomTuteur       = new JTextField(20);
    private JTextField champTelTuteur       = new JTextField(15);
    private JComboBox<Classe> champClasse   = new JComboBox<>();
    private JTextField champRecherche       = new JTextField(15);

    private JButton boutonEnregistrer   = new JButton("Enregistrer");
    private JButton boutonSupprimer     = new JButton("Supprimer");
    private JButton boutonModifier      = new JButton("Modifier");
    private JButton boutonChercher      = new JButton("Rechercher");
    private JButton boutonToutAfficher  = new JButton("Tout afficher");
    private JButton boutonExporter      = new JButton("Exporter CSV");

    private JTable table;
    private DefaultTableModel tableModel;

    public TalibeView() {
        setLayout(new BorderLayout(10, 10));

        // Panel formulaire
        JPanel formulaire = new JPanel(new GridBagLayout());
        formulaire.setBorder(BorderFactory.createTitledBorder("Informations Talibé"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; formulaire.add(new JLabel("Matricule :"), gbc);
        gbc.gridx = 1; formulaire.add(champMatricule, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formulaire.add(new JLabel("Prénom :"), gbc);
        gbc.gridx = 1; formulaire.add(champPrenom, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formulaire.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1; formulaire.add(champNom, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formulaire.add(new JLabel("Date naissance (yyyy-MM-dd) :"), gbc);
        gbc.gridx = 1; formulaire.add(champDateNaissance, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formulaire.add(new JLabel("Nom tuteur :"), gbc);
        gbc.gridx = 1; formulaire.add(champNomTuteur, gbc);

        gbc.gridx = 0; gbc.gridy = 5; formulaire.add(new JLabel("Tél tuteur :"), gbc);
        gbc.gridx = 1; formulaire.add(champTelTuteur, gbc);

        gbc.gridx = 0; gbc.gridy = 6; formulaire.add(new JLabel("Classe :"), gbc);
        gbc.gridx = 1; formulaire.add(champClasse, gbc);

        // Panel boutons
        JPanel boutonsForme = new JPanel(new FlowLayout());
        boutonsForme.add(boutonEnregistrer);
        boutonsForme.add(boutonModifier);
        boutonsForme.add(boutonSupprimer);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        formulaire.add(boutonsForme, gbc);

        // Panel recherche
        JPanel recherche = new JPanel(new FlowLayout());
        recherche.setBorder(BorderFactory.createTitledBorder("Recherche"));
        recherche.add(new JLabel("Nom/Prénom :"));
        recherche.add(champRecherche);
        recherche.add(boutonChercher);
        recherche.add(boutonToutAfficher);
        recherche.add(boutonExporter);

        // Panel haut
        JPanel haut = new JPanel(new BorderLayout());
        haut.add(formulaire, BorderLayout.CENTER);
        haut.add(recherche, BorderLayout.SOUTH);

        // Table
        String[] colonnes = {"Matricule", "Prénom", "Nom", "Date Naissance", "Tuteur", "Tél Tuteur", "Classe"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(haut, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void afficher(List<Talibe> talibes) {
        tableModel.setRowCount(0);
        for (Talibe t : talibes) {
            tableModel.addRow(new Object[]{
                    t.getMatricule(),
                    t.getPrenom(),
                    t.getNom(),
                    t.getDateNaissance(),
                    t.getNomTuteur(),
                    t.getTelephoneTuteur(),
                    t.getClasse().getCode()
            });
        }
    }

    public void remplir(Talibe t) {
        champMatricule.setText(t.getMatricule());
        champPrenom.setText(t.getPrenom());
        champNom.setText(t.getNom());
        champDateNaissance.setText(t.getDateNaissance().toString());
        champNomTuteur.setText(t.getNomTuteur());
        champTelTuteur.setText(t.getTelephoneTuteur());
        champClasse.setSelectedItem(t.getClasse());
    }

    public void chargerClasses(List<Classe> classes) {
        champClasse.removeAllItems();
        for (Classe c : classes) {
            champClasse.addItem(c);
        }
    }

    public void reinitialiser() {
        champMatricule.setText("");
        champPrenom.setText("");
        champNom.setText("");
        champDateNaissance.setText("");
        champNomTuteur.setText("");
        champTelTuteur.setText("");
        champRecherche.setText("");
        if (champClasse.getItemCount() > 0) champClasse.setSelectedIndex(0);
    }
}