package sn.l2gl.farimah.daara.view;

import lombok.Getter;
import sn.l2gl.farimah.daara.model.models.Classe;
import sn.l2gl.farimah.daara.model.models.Maitre;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Getter
public class ClasseView extends JPanel {

    private JTextField champCode        = new JTextField(15);
    private JTextField champLibelle     = new JTextField(20);
    private JComboBox<Classe.Niveau> champNiveau = new JComboBox<>(Classe.Niveau.values());
    private JComboBox<Maitre> champMaitre        = new JComboBox<>();
    private JTextField champRecherche   = new JTextField(15);

    private JButton boutonEnregistrer   = new JButton("Enregistrer");
    private JButton boutonSupprimer     = new JButton("Supprimer");
    private JButton boutonModifier      = new JButton("Modifier");
    private JButton boutonChercher      = new JButton("Rechercher");
    private JButton boutonToutAfficher  = new JButton("Tout afficher");
    private JButton boutonExporter      = new JButton("Exporter CSV");

    private JTable table;
    private DefaultTableModel tableModel;

    public ClasseView() {
        setLayout(new BorderLayout(10, 10));

        // Panel formulaire
        JPanel formulaire = new JPanel(new GridBagLayout());
        formulaire.setBorder(BorderFactory.createTitledBorder("Informations Classe"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; formulaire.add(new JLabel("Code :"), gbc);
        gbc.gridx = 1; formulaire.add(champCode, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formulaire.add(new JLabel("Libellé :"), gbc);
        gbc.gridx = 1; formulaire.add(champLibelle, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formulaire.add(new JLabel("Niveau :"), gbc);
        gbc.gridx = 1; formulaire.add(champNiveau, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formulaire.add(new JLabel("Maître :"), gbc);
        gbc.gridx = 1; formulaire.add(champMaitre, gbc);

        // Panel boutons
        JPanel boutonsForme = new JPanel(new FlowLayout());
        boutonsForme.add(boutonEnregistrer);
        boutonsForme.add(boutonModifier);
        boutonsForme.add(boutonSupprimer);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formulaire.add(boutonsForme, gbc);

        // Panel recherche
        JPanel recherche = new JPanel(new FlowLayout());
        recherche.setBorder(BorderFactory.createTitledBorder("Recherche"));
        recherche.add(new JLabel("Libellé :"));
        recherche.add(champRecherche);
        recherche.add(boutonChercher);
        recherche.add(boutonToutAfficher);
        recherche.add(boutonExporter);

        // Panel haut
        JPanel haut = new JPanel(new BorderLayout());
        haut.add(formulaire, BorderLayout.CENTER);
        haut.add(recherche, BorderLayout.SOUTH);

        // Table
        String[] colonnes = {"Code", "Libellé", "Niveau", "Maître"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(haut, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void afficher(List<Classe> classes) {
        tableModel.setRowCount(0);
        for (Classe c : classes) {
            tableModel.addRow(new Object[]{
                    c.getCode(),
                    c.getLibelle(),
                    c.getNiveau(),
                    c.getMaitre().getNomComplet()
            });
        }
    }

    public void remplir(Classe c) {
        champCode.setText(c.getCode());
        champLibelle.setText(c.getLibelle());
        champNiveau.setSelectedItem(c.getNiveau());
        champMaitre.setSelectedItem(c.getMaitre());
    }

    public void chargerMaitres(List<Maitre> maitres) {
        champMaitre.removeAllItems();
        for (Maitre m : maitres) {
            champMaitre.addItem(m);
        }
    }

    public void reinitialiser() {
        champCode.setText("");
        champLibelle.setText("");
        champNiveau.setSelectedIndex(0);
        champRecherche.setText("");
        if (champMaitre.getItemCount() > 0) champMaitre.setSelectedIndex(0);
    }
}