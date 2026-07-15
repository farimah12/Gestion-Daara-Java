package sn.l2gl.farimah.daara.exception;

public class MaitreDejaExistantException extends DaaraException {
    public MaitreDejaExistantException(String matricule) {
        super("Un maître avec le matricule " + matricule + " existe déjà.");
    }
}