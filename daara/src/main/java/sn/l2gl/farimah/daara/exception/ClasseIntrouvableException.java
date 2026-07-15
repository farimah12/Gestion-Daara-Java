package sn.l2gl.farimah.daara.exception;

public class ClasseIntrouvableException extends DaaraException {
    public ClasseIntrouvableException(String code) {
        super("Aucune classe trouvée avec le code : " + code);
    }
}