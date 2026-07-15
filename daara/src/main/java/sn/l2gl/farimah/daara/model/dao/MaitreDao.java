package sn.l2gl.farimah.daara.model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sn.l2gl.farimah.daara.exception.*;
import sn.l2gl.farimah.daara.model.models.Maitre;
import sn.l2gl.farimah.daara.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class MaitreDao implements Dao<Maitre, String> {

    private Session ouvrir() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public Maitre inserer(Maitre maitre) {
        if (trouver(maitre.getMatricule()).isPresent()) {
            throw new MaitreDejaExistantException(maitre.getMatricule());
        }
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            s.persist(maitre);
            tx.commit();
            return maitre;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new DaaraException("Erreur lors de l'insertion : " + e.getMessage());
        }
    }

    @Override
    public Maitre modifier(Maitre maitre) {
        trouverObligatoire(maitre.getMatricule());
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            Maitre m = s.merge(maitre);
            tx.commit();
            return m;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new DaaraException("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public boolean supprimer(String matricule) {
        try (Session s = ouvrir()) {
            Maitre maitre = s.get(Maitre.class, matricule);
            if (maitre == null) throw new MaitreIntrouvableException(matricule);

            // Compter les classes avec HQL au lieu de LAZY
            Long nbClasses = s.createQuery(
                            "select count(c) from Classe c where c.maitre.matricule = :matricule", Long.class)
                    .setParameter("matricule", matricule)
                    .uniqueResult();

            if (nbClasses > 0) {
                throw new SuppressionImpossibleException(
                        "Impossible de supprimer ce maître : il a " + nbClasses + " classe(s) associée(s)."
                );
            }

            Transaction tx = s.beginTransaction();
            s.remove(maitre);
            tx.commit();
            return true;
        } catch (DaaraException e) {
            throw e;
        } catch (Exception e) {
            throw new DaaraException("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public Optional<Maitre> trouver(String matricule) {
        try (Session s = ouvrir()) {
            return Optional.ofNullable(s.get(Maitre.class, matricule));
        }
    }

    public Maitre trouverObligatoire(String matricule) {
        return trouver(matricule)
                .orElseThrow(() -> new MaitreIntrouvableException(matricule));
    }

    @Override
    public List<Maitre> listerTous() {
        try (Session s = ouvrir()) {
            return s.createQuery("from Maitre", Maitre.class).list();
        }
    }

    public List<Maitre> rechercherParNom(String txt) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "from Maitre m where lower(m.nomComplet) like lower(:n)", Maitre.class)
                    .setParameter("n", "%" + txt + "%")
                    .list();
        }
    }
}