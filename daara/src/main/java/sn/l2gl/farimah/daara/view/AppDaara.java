package sn.l2gl.farimah.daara.view;

import sn.l2gl.farimah.daara.controller.*;
import sn.l2gl.farimah.daara.util.HibernateUtil;

import javax.swing.*;
import java.awt.*;

public class AppDaara extends JFrame {

    public AppDaara() {
        setTitle("Gestion Daara - Farimah");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        // Créer les vues
        MaitreView      maitreView      = new MaitreView();
        ClasseView      classeView      = new ClasseView();
        TalibeView      talibeView      = new TalibeView();
        ProgressionView progressionView = new ProgressionView();

        // Créer les contrôleurs
        new MaitreController(maitreView);
        new ClasseController(classeView);
        new TalibeController(talibeView);
        new ProgressionController(progressionView);

        // CardLayout pour basculer entre les vues
        CardLayout cardLayout = new CardLayout();
        JPanel cartes = new JPanel(cardLayout);
        cartes.add(maitreView,      "Maîtres");
        cartes.add(classeView,      "Classes");
        cartes.add(talibeView,      "Talibés");
        cartes.add(progressionView, "Progressions");

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGestion = new JMenu("Gestion");

        JMenuItem itemMaitres      = new JMenuItem("Maîtres");
        JMenuItem itemClasses      = new JMenuItem("Classes");
        JMenuItem itemTalibes      = new JMenuItem("Talibés");
        JMenuItem itemProgressions = new JMenuItem("Progressions");
        JMenuItem itemQuitter      = new JMenuItem("Quitter");

        itemMaitres.addActionListener(e -> cardLayout.show(cartes, "Maîtres"));
        itemClasses.addActionListener(e -> cardLayout.show(cartes, "Classes"));
        itemTalibes.addActionListener(e -> cardLayout.show(cartes, "Talibés"));
        itemProgressions.addActionListener(e -> cardLayout.show(cartes, "Progressions"));
        itemQuitter.addActionListener(e -> {
            HibernateUtil.shutdown();
            System.exit(0);
        });

        menuGestion.add(itemMaitres);
        menuGestion.add(itemClasses);
        menuGestion.add(itemTalibes);
        menuGestion.add(itemProgressions);
        menuGestion.addSeparator();
        menuGestion.add(itemQuitter);
        menuBar.add(menuGestion);
        setJMenuBar(menuBar);

        add(cartes);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AppDaara().setVisible(true);
        });
    }
}