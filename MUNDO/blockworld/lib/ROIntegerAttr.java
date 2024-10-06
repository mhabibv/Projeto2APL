/*
 * Decompiled with CFR 0.152.
 */
package blockworld.lib;

import blockworld.lib.IntegerAttrAdapter;
import blockworld.lib.IntegerAttrListener;
import java.util.Observable;

public class ROIntegerAttr
extends Observable
implements Cloneable {
    String _name;
    Integer _value;

    public ROIntegerAttr(String string, Integer n) {
        this._name = string;
        this._value = n;
    }

    public void addAttributeListener(IntegerAttrListener integerAttrListener) {
        this.addObserver(new IntegerAttrAdapter(integerAttrListener));
    }

    public void removeAttributeListener(IntegerAttrListener integerAttrListener) {
        this.deleteObserver(new IntegerAttrAdapter(integerAttrListener));
    }

    public int getValue() {
        return this._value;
    }

    public Integer getIntegerValue() {
        return this._value;
    }

    public String getName() {
        return this._name;
    }

    public Object clone() throws CloneNotSupportedException {
        ROIntegerAttr rOIntegerAttr = (ROIntegerAttr)super.clone();
        rOIntegerAttr._name = new String(this._name);
        rOIntegerAttr._value = new Integer(this._value);
        rOIntegerAttr.deleteObservers();
        return rOIntegerAttr;
    }
}

