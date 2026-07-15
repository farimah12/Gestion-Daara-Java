package sn.l2gl.farimah.daara.model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sn.l2gl.farimah.daara.exception.*;
import sn.l2gl.farimah.daara.model.models.Talibe;
import sn.l2gl.farimah.daara.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class TalibeDao implements Dao<Talibe, String> {

    private Session ouvrir() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public Talibe inserer(Talibe talibe) {
        if (trouver(talibe.getMatricule()).isPresent()) {
            throw new TalibeDejaExistantException(talibe.getMatricule());
        }
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            s.persist(talibe);
            tx.commit();
            return talibe;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new DaaraException("Erreur lors de l'insertion : " + e.getMessage());
        }
    }

    @Override
    public Talibe modifier(Talibe talibe) {
        trouverObligatoire(talibe.getMatricule());
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            Talibe t = s.merge(talibe);
            tx.commit();
            return t;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new DaaraException("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public boolean supprimer(String matricule) {
        trouverObligatoire(matricule);
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            Talibe t = s.get(Talibe.class, matricule);
            s.remove(t);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new DaaraException("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public Optional<Talibe> trouver(String matricule) {
        try (Session s = ouvrir()) {
            return Optional.ofNullable(s.get(Talibe.class, matricule));
        }
    }

    public Talibe trouverObligatoire(String matricule) {
        return trouver(matricule)
                .orElseThrow(() -> new TalibeIntrouvableException(matricule));
    }

    @Override
    public List<Talibe> listerTous() {
        try (Session s = ouvrir()) {
            return s.createQuery("from Talibe", Talibe.class).list();
        }
    }

    public List<Talibe> rechercherParNom(String txt) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "from Talibe t where lower(t.nom) like lower(:n) or lower(t.prenom) like lower(:n)", Talibe.class)
                    .setParameter("n", "%" + txt + "%")
                    .list();
        }
    }

    public List<Talibe> listerParClasse(String classeCode) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "from Talibe t where t.classe.code = :c", Talibe.class)
                    .setParameter("c", classeCode)
                    .list();
        }
    }
}