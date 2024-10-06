/*
 * Decompiled with CFR 0.152.
 */
package blockworld.lib;

import blockworld.lib.ROIntegerAttr;
import blockworld.lib.ROIntegerView;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class AttrGroup
extends JPanel {
    private static final long serialVersionUID = -1420253091328660372L;
    protected GridBagLayout gridbag = new GridBagLayout();
    protected GridBagConstraints constraints = new GridBagConstraints();

    public AttrGroup() {
        this("Attributes");
    }

    public AttrGroup(String string) {
        Border border = BorderFactory.createEtchedBorder();
        this.setBorder(BorderFactory.createTitledBorder(border, string));
        this.setLayout(this.gridbag);
        this.constraints.anchor = 13;
        this.constraints.insets = new Insets(1, 5, 2, 5);
    }

    protected void addRow(String string, JComponent jComponent) {
        JLabel jLabel = new JLabel(string);
        jLabel.setLabelFor(jComponent);
        this.constraints.gridwidth = -1;
        this.constraints.fill = 0;
        this.constraints.weightx = 0.0;
        this.gridbag.setConstraints(jLabel, this.constraints);
        this.add(jLabel);
        this.constraints.gridwidth = 0;
        this.constraints.fill = 2;
        this.constraints.weightx = 1.0;
        this.gridbag.setConstraints(jComponent, this.constraints);
        this.add(jComponent);
    }

    public void add(ROIntegerAttr rOIntegerAttr) {
        this.addRow(rOIntegerAttr.getName(), new ROIntegerView(rOIntegerAttr));
    }
}

