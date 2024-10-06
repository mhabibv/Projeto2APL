/*
 * Decompiled with CFR 0.152.
 */
package blockworld;

import java.awt.Point;
import java.io.Serializable;

public class TypeObject
implements Serializable {
    private String _type;
    private Point _position;

    public TypeObject(String string, Point point) {
        this._type = string;
        this._position = point;
    }

    public String toString() {
        return this._type + "(" + this._position.x + "," + this._position.y + ")";
    }

    public Point getPosition() {
        return this._position;
    }

    public void setPosition(Point point) {
        this._position = point;
    }

    public String getType() {
        return this._type;
    }

    public void setType(String string) {
        this._type = string;
    }
}

