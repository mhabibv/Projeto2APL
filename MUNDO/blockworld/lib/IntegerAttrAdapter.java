/*
 * Decompiled with CFR 0.152.
 */
package blockworld.lib;

import blockworld.lib.IntegerAttrListener;
import java.util.Observable;
import java.util.Observer;

public class IntegerAttrAdapter
implements Observer {
    protected IntegerAttrListener _listener;

    public IntegerAttrAdapter(IntegerAttrListener integerAttrListener) {
        this._listener = integerAttrListener;
    }

    @Override
    public void update(Observable observable, Object object) {
        this._listener.onValueChange((Integer)object);
    }

    public boolean equals(Object object) {
        return object instanceof IntegerAttrAdapter && ((IntegerAttrAdapter)object)._listener.equals(this._listener);
    }
}

