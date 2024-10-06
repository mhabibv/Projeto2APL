/*
 * Decompiled with CFR 0.152.
 */
package blockworld.lib;

import blockworld.lib.ObsVectListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public class ObsVect
extends Vector {
    private static final long serialVersionUID = 1140345853942753525L;
    protected static final int ADD = 0;
    protected static final int REMOVE = 1;
    protected transient MyObservable _observable = new MyObservable();

    public ObsVect() {
    }

    public ObsVect(Collection collection) {
        super(collection);
    }

    public ObsVect(int n) {
        super(n);
    }

    public ObsVect(int n, int n2) {
        super(n, n2);
    }

    public ObsVect(ObsVectListener obsVectListener, int n) {
        super(n);
        this.addListener(obsVectListener);
    }

    public ObsVect(ObsVectListener obsVectListener, int n, int n2) {
        super(n, n2);
        this.addListener(obsVectListener);
    }

    public ObsVect(ObsVectListener obsVectListener) {
        this.addListener(obsVectListener);
    }

    public ObsVect(ObsVectListener obsVectListener, Collection collection) {
        super(collection);
        this.addListener(obsVectListener);
    }

    @Override
    public boolean add(Object object) {
        this.addElement(object);
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addElement(Object object) {
        Object[] objectArray = this;
        synchronized (this) {
            super.addElement(object);
            // ** MonitorExit[var2_2] (shouldn't be in output)
            objectArray = new Object[]{new Integer(0), new Integer(this.size() - 1), object};
            this._observable.setChanged();
            this._observable.notifyObservers(objectArray);
            return;
        }
    }

    @Override
    public boolean addAll(Collection collection) {
        return this.addAll(this.size(), collection);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean addAll(int n, Collection collection) {
        Object[] objectArray = this;
        synchronized (this) {
            boolean bl = super.addAll(n, collection);
            // ** MonitorExit[var4_3] (shouldn't be in output)
            if (bl) {
                objectArray = new Object[3];
                objectArray[0] = new Integer(0);
                Iterator iterator = collection.iterator();
                int n2 = 0;
                while (iterator.hasNext()) {
                    objectArray[1] = new Integer(++n2);
                    objectArray[2] = iterator.next();
                    this._observable.setChanged();
                    this._observable.notifyObservers(objectArray);
                }
                return true;
            }
            return false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void insertElementAt(Object object, int n) {
        Object[] objectArray = this;
        synchronized (this) {
            super.insertElementAt(object, n);
            // ** MonitorExit[var3_3] (shouldn't be in output)
            objectArray = new Object[]{new Integer(0), new Integer(n), object};
            this._observable.setChanged();
            this._observable.notifyObservers(objectArray);
            return;
        }
    }

    @Override
    public synchronized Object remove(int n) {
        Object e = this.get(n);
        this.removeElementAt(n);
        return e;
    }

    @Override
    public synchronized void removeAllElements() {
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void removeElementAt(int n) {
        Object[] objectArray = this;
        synchronized (this) {
            Object e = this.get(n);
            super.removeElementAt(n);
            // ** MonitorExit[var3_2] (shouldn't be in output)
            objectArray = new Object[]{new Integer(1), new Integer(n), e};
            this._observable.setChanged();
            this._observable.notifyObservers(objectArray);
            return;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Object set(int n, Object object) {
        Object[] objectArray = this;
        synchronized (this) {
            Object e = this.get(n);
            super.set(n, object);
            // ** MonitorExit[var4_3] (shouldn't be in output)
            objectArray = new Object[]{new Integer(1), new Integer(n), e};
            this._observable.setChanged();
            this._observable.notifyObservers(objectArray);
            objectArray[0] = new Integer(0);
            objectArray[1] = new Integer(n);
            objectArray[2] = object;
            this._observable.setChanged();
            this._observable.notifyObservers(objectArray);
            return e;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setElementAt(Object object, int n) {
        Object[] objectArray = this;
        synchronized (this) {
            Object e = this.get(n);
            super.setElementAt(object, n);
            // ** MonitorExit[var4_3] (shouldn't be in output)
            objectArray = new Object[]{new Integer(1), new Integer(n), e};
            this._observable.setChanged();
            this._observable.notifyObservers(objectArray);
            objectArray[0] = new Integer(0);
            objectArray[1] = new Integer(n);
            objectArray[2] = object;
            this._observable.setChanged();
            this._observable.notifyObservers(objectArray);
            return;
        }
    }

    @Override
    public synchronized void setSize(int n) {
        if (n < this.size()) {
            ListIterator listIterator = this.listIterator(n);
            while (listIterator.hasNext()) {
                listIterator.next();
                listIterator.remove();
            }
        } else {
            super.setSize(n);
        }
    }

    public void addListener(ObsVectListener obsVectListener) {
        this._observable.addObserver(new ObsVectAdapter(obsVectListener));
    }

    public void removeListener(ObsVectListener obsVectListener) {
        this._observable.deleteObserver(new ObsVectAdapter(obsVectListener));
    }

    protected class ObsVectAdapter
    implements Observer {
        protected ObsVectListener _listener;

        public ObsVectAdapter(ObsVectListener obsVectListener) {
            this._listener = obsVectListener;
        }

        @Override
        public void update(Observable observable, Object object) {
            Object[] objectArray = (Object[])object;
            switch ((Integer)objectArray[0]) {
                case 0: {
                    this._listener.onAdd((Integer)objectArray[1], objectArray[2]);
                    break;
                }
                case 1: {
                    this._listener.onRemove((Integer)objectArray[1], objectArray[2]);
                }
            }
        }

        public boolean equals(Object object) {
            return object instanceof ObsVectAdapter && this._listener.equals(((ObsVectAdapter)object)._listener);
        }
    }

    private class MyObservable
    extends Observable {
        private MyObservable() {
        }

        @Override
        public void setChanged() {
            super.setChanged();
        }
    }
}

