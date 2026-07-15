package sn.l2gl.farimah.daara.model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sn.l2gl.farimah.daara.exception.*;
import sn.l2gl.farimah.daara.model.models.Classe;
import sn.l2gl.farimah.daara.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class ClasseDao implements Dao<Classe, String> {

    private Session ouvrir() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public Classe inserer(Classe classe) {
        if (trouver(classe.getCode()).isPresent()) {
            throw new ClasseDejaExistanteException(classe.getCode());
        }
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            s.persist(classe);
            tx.commit();
            return classe;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new DaaraException("Erreur lors de l'insertion : " + e.getMessage());
        }
    }

    @Override
    public Classe modifier(Classe classe) {
        trouverObligatoire(classe.getCode());
        Transaction tx = null;
        try (Session s = ouvrir()) {
            tx = s.beginTransaction();
            Classe c = s.merge(classe);
            tx.commit();
            return c;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new DaaraException("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public boolean supprimer(String code) {
        try (Session s = ouvrir()) {
            Classe classe = s.get(Classe.class, code);
            if (classe == null) throw new ClasseIntrouvableException(code);

            // Compter les talibés avec HQL au lieu de LAZY
            Long nbTalibes = s.createQuery(
                            "select count(t) from Talibe t where t.classe.code = :code", Long.class)
                    .setParameter("code", code)
                    .uniqueResult();

            if (nbTalibes > 0) {
                throw new SuppressionImpossibleException(
                        "Impossible de supprimer cette classe : elle a " + nbTalibes + " talibé(s) associé(s)."
                );
            }

            Transaction tx = s.beginTransaction();
            s.remove(classe);
            tx.commit();
            return true;
        } catch (DaaraException e) {
            throw e;
        } catch (Exception e) {
            throw new DaaraException("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public Optional<Classe> trouver(String code) {
        try (Session s = ouvrir()) {
            return Optional.ofNullable(s.get(Classe.class, code));
        }
    }

    public Classe trouverObligatoire(String code) {
        return trouver(code)
                .orElseThrow(() -> new ClasseIntrouvableException(code));
    }

    @Override
    public List<Classe> listerTous() {
        try (Session s = ouvrir()) {
            return s.createQuery("from Classe", Classe.class).list();
        }
    }

    public List<Classe> rechercherParLibelle(String txt) {
        try (Session s = ouvrir()) {
            return s.createQuery(
                            "from Classe c where lower(c.libelle) like lower(:l)", Classe.class)
                    .setParameter("l", "%" + txt + "%")
                    .list();
        }
    }
}