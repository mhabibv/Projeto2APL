/*
 * Decompiled with CFR 0.152.
 */
package blockworld.lib;

import blockworld.lib.ROIntegerAttr;

public class IntegerAttr
extends ROIntegerAttr {
    public IntegerAttr(String string) {
        this(string, new Integer(0));
    }

    public IntegerAttr(String string, Integer n) {
        super(string, n);
    }

    public IntegerAttr(String string, int n) {
        super(string, new Integer(n));
    }

    public void setValue(Integer n) {
        this._value = n;
        this.setChanged();
        this.notifyObservers(n);
    }

    public void setValue(int n) {
        this.setValue(new Integer(n));
    }

    public void setName(String string) {
        this._name = string;
    }
}

