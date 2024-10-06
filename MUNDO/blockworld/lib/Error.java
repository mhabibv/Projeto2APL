/*
 * Decompiled with CFR 0.152.
 */
package blockworld.lib;

public class Error
extends Exception {
    private static final long serialVersionUID = 1502566209757475201L;

    public Error(String string) {
        super(string);
    }

    public Error(Exception exception) {
        super(exception.getMessage());
    }

    public Error(String string, Exception exception) {
        super(string + ": " + exception.getMessage());
    }
}

