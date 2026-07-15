package sn.l2gl.farimah.daara.exception;

public class TalibeIntrouvableException extends DaaraException {
    public TalibeIntrouvableException(String matricule) {
        super("Aucun talibé trouvé avec le matricule : " + matricule);
    }
}
