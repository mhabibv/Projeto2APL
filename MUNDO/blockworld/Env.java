/*
 * Decompiled with CFR 0.152.
 */
package blockworld;

import apapl.Environment;
import apapl.ExternalActionFailedException;
import apapl.data.APLFunction;
import apapl.data.APLIdent;
import apapl.data.APLList;
import apapl.data.APLListVar;
import apapl.data.APLNum;
import apapl.data.Term;
import blockworld.Agent;
import blockworld.TypeObject;
import blockworld.Window;
import blockworld.lib.ObsVect;
import blockworld.lib.ObsVectListener;
import blockworld.lib.Signal;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.SwingUtilities;

public class Env
extends Environment
implements ObsVectListener {
    protected final Window m_window;
    protected int _senserange = 5;
    protected Dimension m_size = new Dimension(16, 16);
    private HashMap<String, Agent> agentmap = new HashMap();
    protected ObsVect _agents = new ObsVect(this);
    protected ObsVect _traps = new ObsVect();
    protected ObsVect _stones = new ObsVect();
    protected ObsVect _bombs = new ObsVect();
    protected String _objType = "default";
    public transient Signal signalBombTrapped = new Signal("env bomb trapped");
    public transient Signal signalSenseRangeChanged = new Signal("env sense range changed");
    public transient Signal signalSizeChanged = new Signal("env size changed");
    public transient Signal signalTrapChanged = new Signal("env trap position changed");

    public Env() {
        this.m_window = new Window(this);
    }

    public Term enter(String string, APLNum aPLNum, APLNum aPLNum2, APLIdent aPLIdent) throws ExternalActionFailedException {
        int n;
        int n2 = aPLNum.toInt();
        int n3 = aPLNum2.toInt();
        String string2 = aPLIdent.toString();
        Agent agent = this.getAgent(string);
        agent.signalMove.emit();
        Env.writeToLog("Agent entered: " + agent.getName());
        Point point = new Point(n2, n3);
        String string3 = "(" + n2 + "," + n3 + ")";
        if (agent.isEntered()) {
            Env.writeToLog("agent already entered");
            throw new ExternalActionFailedException("Agent \"" + agent.getName() + "\" has already entered.");
        }
        if (this.isOutOfBounds(point)) {
            throw new ExternalActionFailedException("Position " + string3 + " out of bounds.");
        }
        if (!this.isFree(point)) {
            throw new ExternalActionFailedException("Position " + string3 + " is not free.");
        }
        agent._position = point;
        agent._colorID = n = this.getColorID(string2);
        this.validatewindow();
        this.m_window.repaint();
        agent.signalMoveSucces.emit();
        return Env.wrapBoolean(true);
    }

    public Term north(String string) throws ExternalActionFailedException {
        Agent agent = this.getAgent(string);
        Point point = (Point)agent.getPosition().clone();
        --point.y;
        boolean bl = this.setAgentPosition(agent, point);
        if (!bl) {
            throw new ExternalActionFailedException("Moving north failed.");
        }
        this.validatewindow();
        this.m_window.repaint();
        return Env.wrapBoolean(bl);
    }

    public Term east(String string) throws ExternalActionFailedException {
        Agent agent = this.getAgent(string);
        Point point = (Point)agent.getPosition().clone();
        ++point.x;
        boolean bl = this.setAgentPosition(agent, point);
        if (!bl) {
            throw new ExternalActionFailedException("Moving north failed.");
        }
        this.validatewindow();
        this.m_window.repaint();
        return Env.wrapBoolean(bl);
    }

    public Term south(String string) throws ExternalActionFailedException {
        Agent agent = this.getAgent(string);
        Point point = (Point)agent.getPosition().clone();
        ++point.y;
        boolean bl = this.setAgentPosition(agent, point);
        if (!bl) {
            throw new ExternalActionFailedException("Moving north failed.");
        }
        this.validatewindow();
        this.m_window.repaint();
        return Env.wrapBoolean(bl);
    }

    public Term west(String string) throws ExternalActionFailedException {
        Agent agent = this.getAgent(string);
        Point point = (Point)agent.getPosition().clone();
        --point.x;
        boolean bl = this.setAgentPosition(agent, point);
        if (!bl) {
            throw new ExternalActionFailedException("Moving north failed.");
        }
        this.validatewindow();
        this.m_window.repaint();
        return Env.wrapBoolean(bl);
    }

    public Term pickup(String string) throws ExternalActionFailedException {
        Agent agent = this.getAgent(string);
        agent.signalPickupBomb.emit();
        if (agent.atCapacity()) {
            Env.writeToLog("Pickup bomb failed");
            throw new ExternalActionFailedException("Pickup bomb failed");
        }
        TypeObject typeObject = this.removeBomb(agent.getPosition());
        if (typeObject == null) {
            Env.writeToLog("Pickup bomb failed");
            throw new ExternalActionFailedException("Pickup bomb failed");
        }
        agent.signalPickupBombSucces.emit();
        agent.pickBomb(typeObject);
        this.validatewindow();
        this.m_window.repaint();
        return Env.wrapBoolean(true);
    }

    public Term drop(String string) throws ExternalActionFailedException {
        Agent agent = this.getAgent(string);
        agent.signalDropBomb.emit();
        TypeObject typeObject = agent.senseBomb();
        if (typeObject == null) {
            Env.writeToLog("Drop bomb failed");
            throw new ExternalActionFailedException("Drop bomb failed");
        }
        Point point = agent.getPosition();
        if (!(this.addBomb(point) || this.isTrap(point) != null && agent.senseBomb(this.isTrap(point).getType()) != null)) {
            Env.writeToLog("Drop bomb failed");
            throw new ExternalActionFailedException("Drop bomb failed");
        }
        if (this.isTrap(point) != null && agent.senseBomb(this.isTrap(point).getType()) != null) {
            this.signalBombTrapped.emit();
        }
        agent.dropBomb();
        agent.signalDropBombSucces.emit();
        this.validatewindow();
        this.m_window.repaint();
        return Env.wrapBoolean(true);
    }

    public Term getSenseRange(String string) {
        int n = this.getSenseRange();
        return new APLList(new APLNum(n));
    }

    public synchronized Term senseAllAgent(String string) throws ExternalActionFailedException {
        Object object;
        Object object22;
        Point point = this.getAgent(string).getPosition();
        Vector<Object> vector = new Vector<Object>();
        for (Object object22 : this._agents) {
            object = ((Agent)object22).getPosition();
            if (object == null || ((Point)object).equals(point)) continue;
            vector.add(object22);
        }
        object22 = new LinkedList();
        object = vector.iterator();
        while (object.hasNext()) {
            Agent agent;
            Agent agent2 = agent = (Agent)object.next();
            APLList aPLList = new APLList(new APLIdent(agent2.getName()), new APLNum(agent2.getPosition().x), new APLNum(agent2.getPosition().y));
            ((LinkedList)object22).add(aPLList);
        }
        return new APLList((LinkedList<Term>)object22);
    }

    public synchronized Term sensePosition(String string) throws ExternalActionFailedException {
        Point point = this.getAgent(string).getPosition();
        return new APLList(new APLNum(point.x), new APLNum(point.y));
    }

    public synchronized Term senseTraps(String string) throws ExternalActionFailedException {
        Point point = this.getAgent(string).getPosition();
        Vector<TypeObject> vector = new Vector<TypeObject>();
        for (TypeObject typeObject : this._traps) {
            if (!(point.distance(typeObject.getPosition()) <= (double)this._senserange)) continue;
            vector.add(typeObject);
        }
        return Env.convertCollectionToTerm(vector);
    }

    public synchronized Term senseAllTraps(String string) {
        Vector<TypeObject> vector = new Vector<TypeObject>();
        Iterator iterator = this._traps.iterator();
        while (iterator.hasNext()) {
            vector.add((TypeObject)iterator.next());
        }
        return Env.convertCollectionToTerm(vector);
    }

    public synchronized Term senseBombs(String string) throws ExternalActionFailedException {
        Point point = this.getAgent(string).getPosition();
        Vector<TypeObject> vector = new Vector<TypeObject>();
        for (TypeObject typeObject : this._bombs) {
            if (!(point.distance(typeObject.getPosition()) <= (double)this._senserange)) continue;
            vector.add(typeObject);
        }
        return Env.convertCollectionToTerm(vector);
    }

    public synchronized Term senseAllBombs(String string) {
        Vector<TypeObject> vector = new Vector<TypeObject>();
        Iterator iterator = this._bombs.iterator();
        while (iterator.hasNext()) {
            vector.add((TypeObject)iterator.next());
        }
        return Env.convertCollectionToTerm(vector);
    }

    public synchronized Term senseStones(String string) throws ExternalActionFailedException {
        Point point = this.getAgent(string).getPosition();
        Vector<TypeObject> vector = new Vector<TypeObject>();
        for (TypeObject typeObject : this._stones) {
            if (!(point.distance(typeObject.getPosition()) <= (double)this._senserange)) continue;
            vector.add(typeObject);
        }
        return Env.convertCollectionToTerm(vector);
    }

    public synchronized Term senseAllStones(String string) {
        Vector<TypeObject> vector = new Vector<TypeObject>();
        Iterator iterator = this._stones.iterator();
        while (iterator.hasNext()) {
            vector.add((TypeObject)iterator.next());
        }
        return Env.convertCollectionToTerm(vector);
    }

    public synchronized Term senseAgent(String string) throws ExternalActionFailedException {
        Object object22;
        Point point = this.getAgent(string).getPosition();
        Vector<Object> vector = new Vector<Object>();
        for (Object object22 : this._agents) {
            Point point2 = ((Agent)object22).getPosition();
            if (point2 == null || point2.equals(point) || !(point.distance(point2) <= (double)this._senserange)) continue;
            vector.add(object22);
        }
        object22 = new LinkedList();
        for (Object e : vector) {
            Agent agent = (Agent)e;
            APLList aPLList = new APLList(new APLIdent(agent.getName()), new APLNum(agent.getPosition().x), new APLNum(agent.getPosition().y));
            ((LinkedList)object22).add(aPLList);
        }
        return new APLList((LinkedList<Term>)object22);
    }

    private void notifyEvent(String string, Point point) {
        APLNum aPLNum = new APLNum(point.getX());
        APLNum aPLNum2 = new APLNum(point.getY());
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Agent agent : this.agentmap.values()) {
            if (agent.getPosition() == null || !(point.distance(agent.getPosition()) <= (double)this.getSenseRange())) continue;
            arrayList.add(agent.getName());
        }
        Env.writeToLog("EVENT: " + string + "(" + aPLNum + "," + aPLNum2 + ")" + " to " + arrayList);
        if (!arrayList.isEmpty()) {
            this.throwEvent(new APLFunction(string, aPLNum, aPLNum2), arrayList.toArray(new String[0]));
        }
    }

    @Override
    public synchronized void addAgent(String string) {
        String string2 = Env.getMainModule(string);
        if (this.agentmap.keySet().contains(string2)) {
            this.agentmap.put(string, this.agentmap.get(string2));
            Env.writeToLog("linking " + string + "");
        } else {
            Agent agent = new Agent(string2);
            this._agents.add(agent);
            this.agentmap.put(string, agent);
            Env.writeToLog("agent " + agent + " added");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public synchronized void removeAgent(String string) {
        try {
            Agent agent = this.getAgent(string);
            this.agentmap.remove(string);
            if (!this.agentmap.containsValue(agent)) {
                this._agents.remove(agent);
                agent.reset();
            }
            Env.writeToLog("Agent removed: " + string);
            Env env = this;
            synchronized (env) {
                this.notifyAll();
            }
        }
        catch (ExternalActionFailedException externalActionFailedException) {
            // empty catch block
        }
    }

    public synchronized Term getWorldSize(String string) {
        int n = this.getWidth();
        int n2 = this.getHeight();
        return new APLList(new APLNum(n), new APLNum(n2));
    }

    private synchronized Agent getAgent(String string) throws ExternalActionFailedException {
        Agent agent = this.agentmap.get(string);
        if (agent == null) {
            throw new ExternalActionFailedException("No such agent: " + string);
        }
        return agent;
    }

    private static String getMainModule(String string) {
        int n = string.indexOf(46);
        if (n == -1) {
            return string;
        }
        return string.substring(0, n);
    }

    public synchronized int getWidth() {
        return this.m_size.width;
    }

    public synchronized int getHeight() {
        return this.m_size.height;
    }

    public synchronized Collection getBlockWorldAgents() {
        return new Vector(this._agents);
    }

    private static Term convertCollectionToTerm(Collection collection) {
        LinkedList<Term> linkedList = new LinkedList<Term>();
        for (Object e : collection) {
            TypeObject typeObject = (TypeObject)e;
            APLList aPLList = new APLList(new APLIdent(typeObject.getType()), new APLNum(typeObject.getPosition().x), new APLNum(typeObject.getPosition().y));
            linkedList.add(aPLList);
        }
        return new APLList(linkedList);
    }

    public int getSenseRange() {
        return this._senserange;
    }

    private void validatewindow() {
        Runnable runnable = new Runnable(){

            @Override
            public void run() {
                Env.this.m_window.doLayout();
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private synchronized boolean setAgentPosition(Agent agent, Point point) {
        agent.signalMove.emit();
        if (this.isOutOfBounds(point)) {
            return false;
        }
        if (!this.isFree(point)) {
            return false;
        }
        agent.signalMoveSucces.emit();
        Env env = this;
        synchronized (env) {
            this.notifyAll();
        }
        agent._position = point;
        return true;
    }

    protected boolean isOutOfBounds(Point point) {
        return (double)point.x >= this.m_size.getWidth() || point.x < 0 || (double)point.y >= this.m_size.getHeight() || point.y < 0;
    }

    public synchronized boolean isFree(Point point) {
        return this.isStone(point) == null && this.isAgent(point) == null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized Agent isAgent(Point point) {
        ObsVect obsVect = this._agents;
        synchronized (obsVect) {
            for (Agent agent : this._agents) {
                if (!point.equals(agent.getPosition())) continue;
                return agent;
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized TypeObject isStone(Point point) {
        ObsVect obsVect = this._agents;
        synchronized (obsVect) {
            for (TypeObject typeObject : this._stones) {
                if (!point.equals(typeObject.getPosition())) continue;
                return typeObject;
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized TypeObject isTrap(Point point) {
        ObsVect obsVect = this._traps;
        synchronized (obsVect) {
            for (TypeObject typeObject : this._traps) {
                if (!point.equals(typeObject.getPosition())) continue;
                return typeObject;
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized TypeObject isBomb(Point point) {
        ObsVect obsVect = this._bombs;
        synchronized (obsVect) {
            for (TypeObject typeObject : this._bombs) {
                if (!point.equals(typeObject.getPosition())) continue;
                return typeObject;
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized TypeObject removeBomb(Point point) {
        Env env = this;
        synchronized (env) {
            Iterator iterator = this._bombs.iterator();
            while (iterator.hasNext()) {
                TypeObject typeObject = (TypeObject)iterator.next();
                if (!point.equals(typeObject.getPosition())) continue;
                iterator.remove();
                return typeObject;
            }
        }
        this.notifyEvent("bombRemovedAt", point);
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized boolean removeStone(Point point) {
        ObsVect obsVect = this._stones;
        synchronized (obsVect) {
            Iterator iterator = this._stones.iterator();
            while (iterator.hasNext()) {
                if (!point.equals(((TypeObject)iterator.next()).getPosition())) continue;
                iterator.remove();
                Env env = this;
                synchronized (env) {
                    this.notifyAll();
                }
                return true;
            }
        }
        this.notifyEvent("wallRemovedAt", point);
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized boolean removeTrap(Point point) {
        ObsVect obsVect = this._traps;
        synchronized (obsVect) {
            Iterator iterator = this._traps.iterator();
            while (iterator.hasNext()) {
                if (!point.equals(((TypeObject)iterator.next()).getPosition())) continue;
                iterator.remove();
                return true;
            }
        }
        this.notifyEvent("trapRemovedAt", point);
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized boolean addStone(Point point) throws IndexOutOfBoundsException {
        if (this.isOutOfBounds(point)) {
            throw new IndexOutOfBoundsException("setStone out of range: " + point + ", " + this.m_size);
        }
        if (this.isBomb(point) != null || this.isStone(point) != null || this.isTrap(point) != null) {
            return false;
        }
        ObsVect obsVect = this._stones;
        synchronized (obsVect) {
            this._stones.add(new TypeObject(this._objType, point));
        }
        this.notifyEvent("wallAt", point);
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized boolean addBomb(Point point) throws IndexOutOfBoundsException {
        if (this.isOutOfBounds(point)) {
            throw new IndexOutOfBoundsException("addBomb outOfBounds: " + point + ", " + this.m_size);
        }
        if (this.isBomb(point) != null || this.isStone(point) != null || this.isTrap(point) != null) {
            return false;
        }
        ObsVect obsVect = this._bombs;
        synchronized (obsVect) {
            this._bombs.add(new TypeObject(this._objType, point));
        }
        this.notifyEvent("bombAt", point);
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized boolean addTrap(Point point) throws IndexOutOfBoundsException {
        if (this.isOutOfBounds(point)) {
            throw new IndexOutOfBoundsException("setTrap out of range: " + point + ", " + this.m_size);
        }
        if (this.isBomb(point) != null || this.isStone(point) != null || this.isTrap(point) != null) {
            return false;
        }
        ObsVect obsVect = this._traps;
        synchronized (obsVect) {
            this._traps.add(new TypeObject(this._objType, point));
        }
        this.notifyEvent("trapAt", point);
        return true;
    }

    public static void writeToLog(String string) {
    }

    public static APLListVar wrapBoolean(boolean bl) {
        return new APLList(new APLIdent(bl ? "true" : "false"));
    }

    private int getColorID(String string) {
        if (string.equals("army")) {
            return 0;
        }
        if (string.equals("blue")) {
            return 1;
        }
        if (string.equals("gray")) {
            return 2;
        }
        if (string.equals("green")) {
            return 3;
        }
        if (string.equals("orange")) {
            return 4;
        }
        if (string.equals("pink")) {
            return 5;
        }
        if (string.equals("purple")) {
            return 6;
        }
        if (string.equals("red")) {
            return 7;
        }
        if (string.equals("teal")) {
            return 8;
        }
        if (string.equals("yellow")) {
            return 9;
        }
        return 7;
    }

    public void setSenseRange(int n) {
        this._senserange = n;
        this.signalSenseRangeChanged.emit();
    }

    public void setSize(int n, int n2) {
        this.setSize(new Dimension(n, n2));
    }

    public void setSize(Dimension dimension) {
        this.m_size = dimension;
        this.signalSizeChanged.emit();
        Iterator iterator = this._bombs.iterator();
        while (iterator.hasNext()) {
            if (!this.isOutOfBounds(((TypeObject)iterator.next()).getPosition())) continue;
            iterator.remove();
        }
        iterator = this._stones.iterator();
        while (iterator.hasNext()) {
            if (!this.isOutOfBounds((Point)iterator.next())) continue;
            iterator.remove();
        }
        iterator = this._traps.iterator();
        while (iterator.hasNext()) {
            if (!this.isOutOfBounds(((TypeObject)iterator.next()).getPosition())) continue;
            iterator.remove();
        }
    }

    public String getObjType() {
        return this._objType;
    }

    public void setObjType(String string) {
        this._objType = string;
    }

    public void clear() {
        this._stones.removeAllElements();
        this._bombs.removeAllElements();
        this._traps.removeAllElements();
    }

    public void save(OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this.m_size);
        objectOutputStream.writeInt(this._senserange);
        objectOutputStream.writeObject(this._stones);
        objectOutputStream.writeObject(this._bombs);
        objectOutputStream.writeObject(this._traps);
        objectOutputStream.flush();
    }

    public void load(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Dimension dimension = (Dimension)objectInputStream.readObject();
        int n = objectInputStream.readInt();
        Vector vector = (Vector)objectInputStream.readObject();
        Vector vector2 = (Vector)objectInputStream.readObject();
        Vector vector3 = (Vector)objectInputStream.readObject();
        this.m_size = dimension;
        this._senserange = n;
        this.signalSizeChanged.emit();
        this.signalTrapChanged.emit();
        this.signalSenseRangeChanged.emit();
        this.clear();
        this._stones.addAll((Collection)vector);
        this._bombs.addAll((Collection)vector2);
        this._traps.addAll((Collection)vector3);
    }

    public void addAgentListener(ObsVectListener obsVectListener) {
        this._agents.addListener(obsVectListener);
    }

    public void addStonesListener(ObsVectListener obsVectListener) {
        this._stones.addListener(obsVectListener);
    }

    public void addBombsListener(ObsVectListener obsVectListener) {
        this._bombs.addListener(obsVectListener);
    }

    public void addTrapsListener(ObsVectListener obsVectListener) {
        this._traps.addListener(obsVectListener);
    }

    @Override
    public void onAdd(int n, Object object) {
    }

    @Override
    public void onRemove(int n, Object object) {
        ((Agent)object).deleteObservers();
    }
}

