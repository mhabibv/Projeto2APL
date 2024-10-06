/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.SubstList
 */
package apapl.data;

import apapl.SubstList;
import apapl.data.Literal;
import apapl.data.Term;
import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Query {
    public abstract void applySubstitution(SubstList<Term> var1);

    public abstract Query clone();

    public abstract String toPrologString();

    public abstract ArrayList<String> getVariables();

    public abstract void evaluate();

    public abstract void freshVars(ArrayList<String> var1, ArrayList<String> var2, ArrayList<ArrayList<String>> var3);

    public abstract String toString(boolean var1);

    public String toString() {
        return this.toString(false);
    }

    public abstract String toRTF(boolean var1);

    public String toRTF() {
        return this.toRTF(false);
    }

    public abstract int size();

    public abstract boolean containsDisjunct();

    public abstract boolean containsNots();

    public LinkedList<Literal> toLiterals() {
        return new LinkedList<Literal>();
    }
}

