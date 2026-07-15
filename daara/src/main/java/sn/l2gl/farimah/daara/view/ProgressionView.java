package sn.l2gl.farimah.daara.view;

import lombok.Getter;
import sn.l2gl.farimah.daara.model.models.Progression;
import sn.l2gl.farimah.daara.model.models.Talibe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Getter
public class ProgressionView extends JPanel {

    private JComboBox<Talibe> champTalibe   = new JComboBox<>();
    private JTextField champSourate         = new JTextField(20);
    private JTextField champNombreVersets   = new JTextField(10);
    private JTextField champDate            = new JTextField(15);
    private JTextField champAppreciation    = new JTextField(30);
    private JTextField champRecherche       = new JTextField(15);

    private JButton boutonEnregistrer   = new JButton("Enregistrer");
    private JButton boutonSupprimer     = new JButton("Supprimer");
    private JButton boutonModifier      = new JButton("Modifier");
    private JButton boutonChercher      = new JButton("Rechercher");
    private JButton boutonToutAfficher  = new JButton("Tout afficher");
    private JButton boutonExporter      = new JButton("Exporter CSV");

    private JTable table;
    private DefaultTableModel tableModel;

    // Pour stocker l'id de la progression sélectionnée
    private Long idSelectionne = null;

    public ProgressionView() {
        setLayout(new BorderLayout(10, 10));

        // Panel formulaire
        JPanel formulaire = new JPanel(new GridBagLayout());
        formulaire.setBorder(BorderFactory.createTitledBorder("Informations Progression"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; formulaire.add(new JLabel("Talibé :"), gbc);
        gbc.gridx = 1; formulaire.add(champTalibe, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formulaire.add(new JLabel("Sourate :"), gbc);
        gbc.gridx = 1; formulaire.add(champSourate, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formulaire.add(new JLabel("Nombre de versets :"), gbc);
        gbc.gridx = 1; formulaire.add(champNombreVersets, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formulaire.add(new JLabel("Date (yyyy-MM-dd) :"), gbc);
        gbc.gridx = 1; formulaire.add(champDate, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formulaire.add(new JLabel("Appréciation :"), gbc);
        gbc.gridx = 1; formulaire.add(champAppreciation, gbc);

        // Panel boutons
        JPanel boutonsForme = new JPanel(new FlowLayout());
        boutonsForme.add(boutonEnregistrer);
        boutonsForme.add(boutonModifier);
        boutonsForme.add(boutonSupprimer);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formulaire.add(boutonsForme, gbc);

        // Panel recherche
        JPanel recherche = new JPanel(new FlowLayout());
        recherche.setBorder(BorderFactory.createTitledBorder("Recherche"));
        recherche.add(new JLabel("Sourate :"));
        recherche.add(champRecherche);
        recherche.add(boutonChercher);
        recherche.add(boutonToutAfficher);
        recherche.add(boutonExporter);

        // Panel haut
        JPanel haut = new JPanel(new BorderLayout());
        haut.add(formulaire, BorderLayout.CENTER);
        haut.add(recherche, BorderLayout.SOUTH);

        // Table
        String[] colonnes = {"ID", "Talibé", "Sourate", "Versets", "Date", "Appréciation"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(haut, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void afficher(List<Progression> progressions) {
        tableModel.setRowCount(0);
        for (Progression p : progressions) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getTalibe().getPrenom() + " " + p.getTalibe().getNom(),
                    p.getSourate(),
                    p.getNombreVersets(),
                    p.getDateEvaluation(),
                    p.getAppreciation()
            });
        }
    }

    public void remplir(Progression p) {
        idSelectionne = p.getId();
        champTalibe.setSelectedItem(p.getTalibe());
        champSourate.setText(p.getSourate());
        champNombreVersets.setText(String.valueOf(p.getNombreVersets()));
        champDate.setText(p.getDateEvaluation().toString());
        champAppreciation.setText(p.getAppreciation());
    }

    public void chargerTalibes(List<Talibe> talibes) {
        champTalibe.removeAllItems();
        for (Talibe t : talibes) {
            champTalibe.addItem(t);
        }
    }

    public void reinitialiser() {
        idSelectionne = null;
        champSourate.setText("");
        champNombreVersets.setText("");
        champDate.setText("");
        champAppreciation.setText("");
        champRecherche.setText("");
        if (champTalibe.getItemCount() > 0) champTalibe.setSelectedIndex(0);
    }
}