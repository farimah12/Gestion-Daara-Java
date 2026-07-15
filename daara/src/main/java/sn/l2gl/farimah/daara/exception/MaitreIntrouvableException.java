package sn.l2gl.farimah.daara.exception;

public class MaitreIntrouvableException extends DaaraException {
    public MaitreIntrouvableException(String matricule) {
        super("Aucun maître trouvé avec le matricule : " + matricule);
    }
}