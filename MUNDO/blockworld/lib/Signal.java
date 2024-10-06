/*
 * Decompiled with CFR 0.152.
 */
package blockworld.lib;

import java.util.Observable;

public class Signal
extends Observable {
    protected String _name;
    protected long _counter = 0L;

    public Signal(String string) {
        this._name = string;
    }

    public void emit() {
        this.emit(null);
    }

    public void emit(Object object) {
        this.setChanged();
        this.notifyObservers(object);
        ++this._counter;
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public void clearChanged() {
        super.clearChanged();
    }

    public String toString() {
        return this._name;
    }

    public String getName() {
        return this._name;
    }

    public long getEmitCount() {
        return this._counter;
    }
}

