package sn.l2gl.farimah.daara.model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sn.l2gl.farimah.daara.exception.*;
import sn.l2gl.farimah.daara.model.models.Progression;
import sn.l2gl.farimah.daara.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class ProgressionDao implements Dao<Progression, Long> {

    private Session ouvrir() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public Progression inserer(Progression progression) {
        if (progression.getSourate() == null || progression.getSourate().isBlank()) {
            throw new ProgressionInvalideException("La sourate ne peut pas être vide.");
        }
        if (progression.getNombreVersets() < 0) {
            throw new ProgressionInvalideException("Le nombre de versets ne peut pas être négatif.");
        }
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            s.persist(progression);
            tx.commit();
            return progression;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new DaaraException("Erreur lors de l'insertion : " + e.getMessage());
        }
    }

    @Override
    public Progression modifier(Progression progression) {
        trouverObligatoire(progression.getId());
        if (progression.getSourate() == null || progression.getSourate().isBlank()) {
            throw new ProgressionInvalideException("La sourate ne peut pas être vide.");
        }
        if (progression.getNombreVersets() < 0) {
            throw new ProgressionInvalideException("Le nombre de versets ne peut pas être négatif.");
        }
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            Progression p = s.merge(progression);
            tx.commit();
            return p;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new DaaraException("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public boolean supprimer(Long id) {
        trouverObligatoire(id);
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            Progression p = s.get(Progression.class, id);
            s.remove(p);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new DaaraException("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public Optional<Progression> trouver(Long id) {
        try (Session s = ouvrir()) {
            return Optional.ofNullable(s.get(Progression.class, id));
        }
    }

    public Progression trouverObligatoire(Long id) {
        return trouver(id)
                .orElseThrow(() -> new ProgressionIntrouvableException(id));
    }

    @Override
    public List<Progression> listerTous() {
        try (Session s = ouvrir()) {
            return s.createQuery("from Progression", Progression.class).list();
        }
    }

    public List<Progression> listerParTalibe(String talibeMatricule) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "from Progression p where p.talibe.matricule = :m", Progression.class)
                    .setParameter("m", talibeMatricule)
                    .list();
        }
    }

    public List<Progression> rechercherParSourate(String sourate) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "from Progression p where lower(p.sourate) like lower(:s)", Progression.class)
                    .setParameter("s", "%" + sourate + "%")
                    .list();
        }
    }
}