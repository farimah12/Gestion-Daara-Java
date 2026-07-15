package sn.l2gl.farimah.daara.exception;

public class ProgressionInvalideException extends DaaraException {
    public ProgressionInvalideException(String message) {
        super("Progression invalide : " + message);
    }
}