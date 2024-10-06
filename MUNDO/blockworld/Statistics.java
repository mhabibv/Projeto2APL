/*
 * Decompiled with CFR 0.152.
 */
package blockworld;

import blockworld.Agent;
import blockworld.Env;
import blockworld.EnvView;
import blockworld.lib.Signal;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

class Statistics
extends AbstractTableModel
implements Observer {
    private static final long serialVersionUID = 5372407086612717903L;
    protected EnvView _view = null;
    protected Vector _signals = new Vector();

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public int getRowCount() {
        return this._signals.size();
    }

    @Override
    public Object getValueAt(int n, int n2) {
        Signal signal = (Signal)this._signals.get(n);
        if (n2 == 0) {
            return signal.getName();
        }
        return new Integer((int)signal.getEmitCount());
    }

    @Override
    public String getColumnName(int n) {
        if (n == 0) {
            return "Signal Description";
        }
        return "Total Emitted Events";
    }

    public Statistics(EnvView envView) {
        this.setEnvView(envView);
    }

    public void setEnvView(EnvView envView) {
        if (this._view != null) {
            this._view.signalSelectionChanged.deleteObserver(this);
        }
        this._view = envView;
        this._view.signalSelectionChanged.addObserver(this);
        this.update();
    }

    @Override
    public void update(Observable observable, Object object) {
        this.update();
    }

    public void update() {
        this._signals.clear();
        Env env = this._view.getEnv();
        Agent agent = this._view.getSelectedAgent();
        this._signals.add(env.signalBombTrapped);
        if (agent != null) {
            this._signals.add(agent.signalDropBomb);
            this._signals.add(agent.signalDropBombSucces);
            this._signals.add(agent.signalMove);
            this._signals.add(agent.signalMoveSucces);
            this._signals.add(agent.signalPickupBomb);
            this._signals.add(agent.signalPickupBombSucces);
        }
        this.fireTableDataChanged();
    }
}

