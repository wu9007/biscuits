package org.hv.biscuits.domain.mediator;

/**
 * @author wujianchuan
 */
public class NoSuchMediatorFunctionException extends Exception {

    private static final long serialVersionUID = -1468492742334846964L;

    public NoSuchMediatorFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchMediatorFunctionException(String message) {
        super(message);
    }
}
