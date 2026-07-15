package sn.l2gl.farimah.daara.model.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T, ID> {
    T inserer(T t);
    T modifier(T t);
    boolean supprimer(ID id);
    Optional<T> trouver(ID id);
    List<T> listerTous();
}