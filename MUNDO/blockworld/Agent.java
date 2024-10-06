/*
 * Decompiled with CFR 0.152.
 */
package blockworld;

import blockworld.TypeObject;
import blockworld.lib.Signal;
import java.awt.Point;

public class Agent {
    protected String _name;
    protected Point _position = null;
    protected TypeObject _bomb = null;
    int _colorID = 0;
    public transient Signal signalMove = new Signal("agent attempts move");
    public transient Signal signalPickupBomb = new Signal("agent attempts pickup");
    public transient Signal signalDropBomb = new Signal("agent attempts drop");
    public transient Signal signalMoveSucces = new Signal("agent succesful move");
    public transient Signal signalPickupBombSucces = new Signal("agent succesful pickup");
    public transient Signal signalDropBombSucces = new Signal("agent sucessful drop");

    public Agent(String string) {
        this._name = string;
    }

    public String getName() {
        return this._name;
    }

    public Point getPosition() {
        return this._position;
    }

    public TypeObject senseBomb() {
        return this._bomb;
    }

    public TypeObject senseBomb(String string) {
        return this._bomb != null && this._bomb.getType().equals(string) ? this._bomb : null;
    }

    public boolean atCapacity() {
        return this._bomb != null;
    }

    public void pickBomb(TypeObject typeObject) {
        this._bomb = typeObject;
    }

    public void dropBomb() {
        this._bomb = null;
    }

    public boolean isEntered() {
        return this._position != null;
    }

    public void reset() {
        this._position = null;
        this._bomb = null;
        this.signalMove.emit();
    }

    public String toString() {
        return this.getName();
    }

    public void deleteObservers() {
        this.signalMove.deleteObservers();
        this.signalPickupBomb.deleteObservers();
        this.signalDropBomb.deleteObservers();
        this.signalMoveSucces.deleteObservers();
        this.signalPickupBombSucces.deleteObservers();
        this.signalDropBombSucces.deleteObservers();
    }
}

