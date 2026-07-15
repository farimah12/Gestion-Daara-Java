package sn.l2gl.farimah.daara.view;

import lombok.Getter;
import sn.l2gl.farimah.daara.model.models.Maitre;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Getter
public class MaitreView extends JPanel {

    private JTextField champMatricule   = new JTextField(15);
    private JTextField champNom         = new JTextField(20);
    private JTextField champTelephone   = new JTextField(15);
    private JTextField champRecherche   = new JTextField(15);

    private JButton boutonEnregistrer   = new JButton("Enregistrer");
    private JButton boutonSupprimer     = new JButton("Supprimer");
    private JButton boutonModifier      = new JButton("Modifier");
    private JButton boutonChercher      = new JButton("Rechercher");
    private JButton boutonToutAfficher  = new JButton("Tout afficher");
    private JButton boutonExporter      = new JButton("Exporter CSV");

    private JTable table;
    private DefaultTableModel tableModel;

    public MaitreView() {
        setLayout(new BorderLayout(10, 10));

        // Panel formulaire
        JPanel formulaire = new JPanel(new GridBagLayout());
        formulaire.setBorder(BorderFactory.createTitledBorder("Informations Maître"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; formulaire.add(new JLabel("Matricule :"), gbc);
        gbc.gridx = 1; formulaire.add(champMatricule, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formulaire.add(new JLabel("Nom complet :"), gbc);
        gbc.gridx = 1; formulaire.add(champNom, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formulaire.add(new JLabel("Téléphone :"), gbc);
        gbc.gridx = 1; formulaire.add(champTelephone, gbc);

        // Panel boutons formulaire
        JPanel boutonsForme = new JPanel(new FlowLayout());
        boutonsForme.add(boutonEnregistrer);
        boutonsForme.add(boutonModifier);
        boutonsForme.add(boutonSupprimer);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formulaire.add(boutonsForme, gbc);

        // Panel recherche
        JPanel recherche = new JPanel(new FlowLayout());
        recherche.setBorder(BorderFactory.createTitledBorder("Recherche"));
        recherche.add(new JLabel("Nom :"));
        recherche.add(champRecherche);
        recherche.add(boutonChercher);
        recherche.add(boutonToutAfficher);
        recherche.add(boutonExporter);

        // Panel haut
        JPanel haut = new JPanel(new BorderLayout());
        haut.add(formulaire, BorderLayout.CENTER);
        haut.add(recherche, BorderLayout.SOUTH);

        // Table
        String[] colonnes = {"Matricule", "Nom Complet", "Téléphone"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(haut, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void afficher(List<Maitre> maitres) {
        tableModel.setRowCount(0);
        for (Maitre m : maitres) {
            tableModel.addRow(new Object[]{
                    m.getMatricule(),
                    m.getNomComplet(),
                    m.getTelephone()
            });
        }
    }

    public void remplir(Maitre m) {
        champMatricule.setText(m.getMatricule());
        champNom.setText(m.getNomComplet());
        champTelephone.setText(m.getTelephone());
    }

    public void reinitialiser() {
        champMatricule.setText("");
        champNom.setText("");
        champTelephone.setText("");
        champRecherche.setText("");
    }
}