/*
 * Decompiled with CFR 0.152.
 */
package blockworld.lib;

import blockworld.lib.IntegerAttrListener;
import blockworld.lib.ROIntegerAttr;
import javax.swing.JLabel;

public class ROIntegerView
extends JLabel
implements IntegerAttrListener {
    private static final long serialVersionUID = -4767694535801030958L;
    protected ROIntegerAttr _attr;

    public ROIntegerView(ROIntegerAttr rOIntegerAttr) {
        this._attr = rOIntegerAttr;
        this._attr.addAttributeListener(this);
        this.onValueChange(new Integer(rOIntegerAttr.getValue()));
    }

    @Override
    public void onValueChange(Integer n) {
        this.setText(n.toString());
    }
}

