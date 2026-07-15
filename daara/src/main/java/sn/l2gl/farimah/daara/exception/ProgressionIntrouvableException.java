package sn.l2gl.farimah.daara.exception;

public class ProgressionIntrouvableException extends DaaraException {
    public ProgressionIntrouvableException(Long id) {
        super("Aucune progression trouvée avec l'id : " + id);
    }
}
