/*
 * Decompiled with CFR 0.152.
 */
package blockworld;

import blockworld.Env;
import blockworld.EnvView;
import blockworld.Statistics;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class Window
extends JFrame {
    private static final long serialVersionUID = -462965463955994316L;
    protected boolean _done = false;
    protected boolean _init = false;
    protected File _lastFile = null;
    private JToolBar m_tbToolbar = null;
    private ArrayList<JToggleButton> m_aEditOptions = new ArrayList();

    public Window(final Env env) {
        super("Blockworld");
        JMenuItem jMenuItem = new JMenuItem("Clear environment");
        jMenuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int n = JOptionPane.showConfirmDialog(Window.this, "Are you sure you want to clear the environment", "Confirm clear", 0);
                if (n != 0) {
                    return;
                }
                env.clear();
            }
        });
        JMenuItem jMenuItem2 = new JMenuItem("Revert to saved");
        jMenuItem2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (Window.this._lastFile == null) {
                    JOptionPane.showMessageDialog(Window.this, "You did not load or save this environment yet.\nPlease load or save first", "Nothing to revert to", 0);
                    return;
                }
                int n = JOptionPane.showConfirmDialog(Window.this, "Are you sure you want revert to " + Window.this._lastFile.getPath(), "Confirm revert to saved", 0);
                if (n != 0) {
                    return;
                }
                try {
                    env.clear();
                    FileInputStream fileInputStream = new FileInputStream(Window.this._lastFile);
                    env.load(fileInputStream);
                }
                catch (Exception exception) {
                    System.out.println("Loading failed! " + exception);
                }
            }
        });
        JMenuItem jMenuItem3 = new JMenuItem("Load from File");
        jMenuItem3.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    File file = new File(".");
                    JFileChooser jFileChooser = new JFileChooser(file);
                    int n = jFileChooser.showOpenDialog(Window.this);
                    if (n != 0) {
                        return;
                    }
                    File file2 = jFileChooser.getSelectedFile();
                    FileInputStream fileInputStream = new FileInputStream(file2);
                    env.load(fileInputStream);
                    Window.this._lastFile = file2;
                }
                catch (Exception exception) {
                    System.out.println("Loading failed! " + exception);
                }
            }
        });
        JMenuItem jMenuItem4 = new JMenuItem("Save to File");
        jMenuItem4.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    File file = new File(".");
                    JFileChooser jFileChooser = new JFileChooser(file);
                    int n = jFileChooser.showSaveDialog(Window.this);
                    if (n != 0) {
                        return;
                    }
                    File file2 = jFileChooser.getSelectedFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    env.save(fileOutputStream);
                    Window.this._lastFile = file2;
                }
                catch (Exception exception) {
                    System.out.println("Saving failed! " + exception);
                }
            }
        });
        JMenuItem jMenuItem5 = new JMenuItem("Environment Size");
        jMenuItem5.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String string = (String)JOptionPane.showInputDialog(Window.this, "Resize environment (X * Y) (X, Y) (X x Y) (X Y)", "Resize environment", -1, null, null, env.getWidth() + " * " + env.getHeight());
                if (string != null && string.length() > 0) {
                    StringTokenizer stringTokenizer = new StringTokenizer(string, "*x, ");
                    if (!stringTokenizer.hasMoreTokens()) {
                        return;
                    }
                    String string2 = stringTokenizer.nextToken();
                    if (!stringTokenizer.hasMoreTokens()) {
                        return;
                    }
                    String string3 = stringTokenizer.nextToken();
                    try {
                        Dimension dimension = new Dimension(Integer.parseInt(string2), Integer.parseInt(string3));
                        env.setSize(dimension);
                    }
                    catch (NumberFormatException numberFormatException) {
                        JOptionPane.showMessageDialog(Window.this, "Invalid number format, it should be one of (X * Y) (X, Y) (X x Y) (X Y)", "Error parsing size", 0);
                    }
                }
            }
        });
        JMenuItem jMenuItem6 = new JMenuItem("Default APLIdentifier");
        jMenuItem6.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String string = (String)JOptionPane.showInputDialog(Window.this, "Enter default identifier for new bombs/traps (all char, first char in lowercase)", "Set Default Object APLIdentifier", -1, null, null, env.getObjType());
                if (string == null) {
                    return;
                }
                boolean bl = false;
                for (int i = 0; i < string.length(); ++i) {
                    char c = string.charAt(i);
                    if (Character.isLetter(c) && (i != 0 || Character.isLowerCase(c))) continue;
                    bl = true;
                    break;
                }
                if (string.length() == 0 || bl) {
                    JOptionPane.showMessageDialog(Window.this, "The object type identifier must be an all-character string with the first character in lowercase", "Invalid object type", 0);
                    return;
                }
                env.setObjType(string);
            }
        });
        JMenuItem jMenuItem7 = new JMenuItem("Sensor Range");
        jMenuItem7.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String string = (String)JOptionPane.showInputDialog(Window.this, "Set agent sensor range in cells", "Set Sensor Range in Cells", -1, null, null, Integer.toString(env.getSenseRange()));
                if (string != null && string.length() > 0) {
                    env.setSenseRange(Integer.parseInt(string));
                }
            }
        });
        JMenuItem jMenuItem8 = new JMenuItem("About BlockWorld");
        jMenuItem8.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(Window.this, "BlockWorld for 2APL\n\nhttp://www.cs.uu.nl/2apl/\n\nDeveloped by\n  The 2APL development group\n  Utrecht University, the Netherlands", "About BlockWorld", 1);
            }
        });
        JMenu jMenu = new JMenu("World");
        jMenu.add(jMenuItem3);
        jMenu.add(jMenuItem4);
        jMenu.add(jMenuItem2);
        jMenu.add(jMenuItem);
        JMenu jMenu2 = new JMenu("Properties");
        jMenu2.add(jMenuItem5);
        jMenu2.add(jMenuItem7);
        jMenu2.add(jMenuItem6);
        JMenu jMenu3 = new JMenu("Help");
        jMenu3.add(jMenuItem8);
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(jMenu);
        jMenuBar.add(jMenu2);
        jMenuBar.add(jMenu3);
        this.getContentPane().setLayout(new BorderLayout());
        EnvView envView = new EnvView(env);
        Statistics statistics = new Statistics(envView);
        JScrollPane jScrollPane = new JScrollPane(new JTable(statistics));
        JSplitPane jSplitPane = new JSplitPane(1, envView, jScrollPane);
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerLocation(230);
        jSplitPane.setResizeWeight(1.0);
        this.getContentPane().add((Component)jSplitPane, "Center");
        this.setJMenuBar(jMenuBar);
        this.m_tbToolbar = new JToolBar();
        this.m_tbToolbar.setFloatable(false);
        this.addButton("info.gif", "Select agent", envView.tool.STATE_SELECT, envView.tool).setSelected(true);
        this.addButton("bomb.gif", "Place bombs", envView.tool.STATE_ADDBOMB, envView.tool);
        this.addButton("stone.gif", "Place walls", envView.tool.STATE_ADDWALL, envView.tool);
        this.addButton("trap.gif", "Place traps", envView.tool.STATE_ADDTRAP, envView.tool);
        this.addButton("eraser.gif", "Erase objects", envView.tool.STATE_REMOVE, envView.tool);
        this.getContentPane().add((Component)this.m_tbToolbar, "North");
        this.setSize(400, 250);
        this.setVisible(true);
    }

    public synchronized void init() {
        if (this._init) {
            return;
        }
        while (!this._done) {
            try {
                this.wait();
            }
            catch (InterruptedException interruptedException) {}
        }
        this._init = true;
    }

    protected synchronized void done() {
        this._done = true;
        this.notifyAll();
    }

    public JToggleButton addButton(String string, String string2, final int n, final EnvView.MouseTool mouseTool) {
        final JToggleButton jToggleButton = new JToggleButton(this.makeIcon(string));
        jToggleButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mouseTool._state = n;
                for (JToggleButton jToggleButton2 : Window.this.m_aEditOptions) {
                    if (jToggleButton == jToggleButton2) continue;
                    jToggleButton2.setSelected(false);
                }
            }
        });
        jToggleButton.setToolTipText(string2);
        this.m_tbToolbar.add(jToggleButton);
        this.m_aEditOptions.add(jToggleButton);
        return jToggleButton;
    }

    private ImageIcon makeIcon(String string) {
        string = "images/toolbar/" + string;
        return new ImageIcon(this.getClass().getResource(string));
    }
}

