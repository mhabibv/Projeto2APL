/*
 * Decompiled with CFR 0.152.
 */
package blockworld;

import blockworld.Agent;
import blockworld.Env;
import blockworld.TypeObject;
import blockworld.lib.ObsVectListener;
import blockworld.lib.Signal;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageProducer;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

class EnvView
extends JPanel
implements ObsVectListener,
Observer {
    Map imgMap = new HashMap();
    Image imgStone = null;
    Image imgBomb = null;
    Image imgTrap = null;
    Image[] imgAgents = new Image[10];
    Image[] imgAgentsHolding = new Image[10];
    private static final long serialVersionUID = -6981513623481400558L;
    protected Env _env;
    double _cw = 10.0;
    double _ch = 10.0;
    public Signal signalSelectionChanged = new Signal("Selected agent changed");
    protected Agent _selectedAgent = null;
    public MouseTool tool;

    EnvView(Env env) {
        this._env = env;
        try {
            this.imgStone = this.createImage((ImageProducer)this.getClass().getResource("images/stone.gif").getContent());
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            this.imgAgents[0] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/agent_army.gif").getContent());
            this.imgAgents[1] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/agent_blue.gif").getContent());
            this.imgAgents[2] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/agent_gray.gif").getContent());
            this.imgAgents[3] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/agent_green.gif").getContent());
            this.imgAgents[4] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/agent_orange.gif").getContent());
            this.imgAgents[5] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/agent_pink.gif").getContent());
            this.imgAgents[6] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/agent_purple.gif").getContent());
            this.imgAgents[7] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/agent_red.gif").getContent());
            this.imgAgents[8] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/agent_teal.gif").getContent());
            this.imgAgents[9] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/agent_yellow.gif").getContent());
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            this.imgAgentsHolding[0] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/holding_army.gif").getContent());
            this.imgAgentsHolding[1] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/holding_blue.gif").getContent());
            this.imgAgentsHolding[2] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/holding_gray.gif").getContent());
            this.imgAgentsHolding[3] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/holding_green.gif").getContent());
            this.imgAgentsHolding[4] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/holding_orange.gif").getContent());
            this.imgAgentsHolding[5] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/holding_pink.gif").getContent());
            this.imgAgentsHolding[6] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/holding_purple.gif").getContent());
            this.imgAgentsHolding[7] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/holding_red.gif").getContent());
            this.imgAgentsHolding[8] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/holding_teal.gif").getContent());
            this.imgAgentsHolding[9] = this.createImage((ImageProducer)this.getClass().getResource("images/agents/holding_yellow.gif").getContent());
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            this.imgBomb = this.createImage((ImageProducer)this.getClass().getResource("images/bomb.gif").getContent());
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            this.imgTrap = this.createImage((ImageProducer)this.getClass().getResource("images/trap.gif").getContent());
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this._env.addAgentListener(new ObsVectListener(){

            @Override
            public void onAdd(int n, Object object) {
                Agent agent = (Agent)object;
                agent.signalDropBomb.addObserver(EnvView.this);
                agent.signalPickupBomb.addObserver(EnvView.this);
                agent.signalMove.addObserver(EnvView.this);
                EnvView.this.repaint();
            }

            @Override
            public void onRemove(int n, Object object) {
                Agent agent = (Agent)object;
                agent.signalDropBomb.deleteObserver(EnvView.this);
                agent.signalPickupBomb.deleteObserver(EnvView.this);
                agent.signalMove.deleteObserver(EnvView.this);
                EnvView.this.repaint();
            }
        });
        this._env.addStonesListener(this);
        this._env.addBombsListener(this);
        this._env.addTrapsListener(this);
        this._env.signalSenseRangeChanged.addObserver(this);
        this._env.signalSizeChanged.addObserver(this);
        this._env.signalTrapChanged.addObserver(this);
        this.tool = new MouseTool(this._env);
        this.addMouseListener(this.tool);
        this.addMouseMotionListener(this.tool);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this._env.getWidth() * (int)this._cw, this._env.getHeight() * (int)this._ch);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        double d = this.getWidth() / this._env.getWidth();
        double d2 = this.getHeight() / this._env.getHeight();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        for (double d3 = 0.0; d3 < (double)this._env.getWidth(); d3 += 1.0) {
            for (double d4 = 0.0; d4 < (double)this._env.getHeight(); d4 += 1.0) {
                Point point = new Point((int)d3, (int)d4);
                if (this._env.isStone(point) != null) {
                    if (this.imgStone == null) {
                        graphics.setColor(Color.blue);
                        graphics.fillRect((int)(d3 * d), (int)(d4 * d2), (int)d, (int)d2);
                    } else {
                        graphics.drawImage(this.imgStone, (int)(d3 * d + 1.0), (int)(d4 * d2 + 1.0), (int)d - 1, (int)d2 - 1, this);
                    }
                }
                TypeObject typeObject = null;
                typeObject = this._env.isBomb(point);
                if (typeObject != null) {
                    if (this.imgBomb != null) {
                        graphics.drawImage(this.imgBomb, (int)(d3 * d + 1.0), (int)(d4 * d2 + 1.0), (int)(d - 1.0), (int)(d2 - 1.0), this);
                    } else {
                        graphics.setColor(Color.red);
                        graphics.fillOval((int)(d3 * d), (int)(d4 * d2), (int)d, (int)d2);
                    }
                }
                TypeObject typeObject2 = null;
                typeObject2 = this._env.isTrap(point);
                if (typeObject2 != null) {
                    if (this.imgTrap != null) {
                        graphics.drawImage(this.imgTrap, (int)(d3 * d + 1.0), (int)(d4 * d2 + 1.0), (int)(d - 1.0), (int)(d2 - 1.0), this);
                    } else {
                        graphics.setColor(Color.orange);
                        graphics.fillRect((int)(d3 * d), (int)(d4 * d2), (int)d, (int)d2);
                    }
                }
                graphics.setColor(Color.gray);
                graphics.drawRect((int)(d3 * d), (int)(d4 * d2), (int)d, (int)d2);
            }
        }
        for (Agent agent : this._env.getBlockWorldAgents()) {
            if (!agent.isEntered()) continue;
            Point point = agent.getPosition();
            int n = (int)((double)point.x * d);
            int n2 = (int)((double)point.y * d2);
            if (agent.senseBomb() != null) {
                if (this.imgAgentsHolding[agent._colorID] != null) {
                    graphics.drawImage(this.imgAgentsHolding[agent._colorID], n + 1, n2 + 1, (int)(d - 1.0), (int)(d2 - 1.0), this);
                } else {
                    graphics.setColor(agent != this._selectedAgent ? Color.green : Color.green.darker());
                    graphics.fillRect(n + 1, n2 + 1, (int)(d - 1.0), (int)(d2 - 1.0));
                    graphics.setColor(Color.black);
                    graphics.drawRect(n + 2, n2 + 2, 3, 3);
                }
            } else if (this.imgAgents[agent._colorID] != null) {
                graphics.drawImage(this.imgAgents[agent._colorID], n + 1, n2 + 1, (int)(d - 1.0), (int)(d2 - 1.0), this);
            } else {
                graphics.setColor(agent != this._selectedAgent ? Color.green : Color.green.darker());
                graphics.fillRect(n + 1, n2 + 1, (int)(d - 1.0), (int)(d2 - 1.0));
            }
            int n3 = (int)((double)this._env.getSenseRange() * d);
            int n4 = (int)((double)this._env.getSenseRange() * d2);
            graphics.setColor(Color.blue);
            graphics.drawOval(n - n3, n2 - n4, n3 * 2, n4 * 2);
        }
    }

    @Override
    public void onRemove(int n, Object object) {
        this.repaint();
    }

    @Override
    public void onAdd(int n, Object object) {
        this.repaint();
    }

    @Override
    public void update(Observable observable, Object object) {
        this.repaint();
    }

    public Agent getSelectedAgent() {
        return this._selectedAgent;
    }

    public Env getEnv() {
        return this._env;
    }

    class MouseTool
    implements MouseListener,
    MouseMotionListener {
        Env _env;
        final int STATE_SELECT = 0;
        final int STATE_REMOVE = 1;
        final int STATE_ADDBOMB = 2;
        final int STATE_ADDWALL = 3;
        final int STATE_ADDTRAP = 4;
        int _state = 0;

        public MouseTool(Env env) {
            this._env = env;
        }

        protected Point toEnv(MouseEvent mouseEvent) {
            double d = EnvView.this.getWidth() / this._env.getWidth();
            double d2 = EnvView.this.getHeight() / this._env.getHeight();
            int n = (int)((double)mouseEvent.getX() / d);
            int n2 = (int)((double)mouseEvent.getY() / d2);
            return new Point(n, n2);
        }

        public void drag(MouseEvent mouseEvent) {
            Point point = this.toEnv(mouseEvent);
            try {
                if (this._env.isAgent(point) != null) {
                    return;
                }
                switch (this._state) {
                    case 2: {
                        if (this._env.isStone(point) != null) {
                            this._env.removeStone(point);
                        }
                        if (this._env.isTrap(point) != null) {
                            this._env.removeTrap(point);
                        }
                        this._env.addBomb(point);
                        break;
                    }
                    case 3: {
                        if (this._env.isBomb(point) != null) {
                            this._env.removeBomb(point);
                        }
                        if (this._env.isTrap(point) != null) {
                            this._env.removeTrap(point);
                        }
                        this._env.addStone(point);
                        break;
                    }
                    case 4: {
                        if (this._env.isBomb(point) != null) {
                            this._env.removeBomb(point);
                        }
                        if (this._env.isStone(point) != null) {
                            this._env.removeStone(point);
                        }
                        this._env.addTrap(point);
                        break;
                    }
                    case 1: {
                        if (this._env.isStone(point) != null) {
                            this._env.removeStone(point);
                        }
                        if (this._env.isBomb(point) != null) {
                            this._env.removeBomb(point);
                        }
                        if (this._env.isTrap(point) == null) break;
                        this._env.removeTrap(point);
                    }
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }

        public void start(MouseEvent mouseEvent) {
            Point point = this.toEnv(mouseEvent);
            try {
                if (this._state == 0) {
                    Agent agent = this._env.isAgent(point);
                    if (agent != null) {
                        EnvView.this._selectedAgent = agent != EnvView.this._selectedAgent ? agent : null;
                        EnvView.this.signalSelectionChanged.emit();
                        EnvView.this.repaint();
                    }
                } else {
                    this.drag(mouseEvent);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            this.start(mouseEvent);
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            this.drag(mouseEvent);
        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
        }
    }
}

