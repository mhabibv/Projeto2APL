/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.SubstList
 */
package apapl.data;

import apapl.SubstList;
import apapl.data.Query;
import apapl.data.Term;
import java.util.ArrayList;

public class True
extends Query {
    @Override
    public String toString(boolean bl) {
        return "true";
    }

    @Override
    public String toPrologString() {
        return this.toString();
    }

    public boolean equals(Query query) {
        return query instanceof True;
    }

    @Override
    public void applySubstitution(SubstList<Term> substList) {
    }

    @Override
    public True clone() {
        return new True();
    }

    @Override
    public ArrayList<String> getVariables() {
        return new ArrayList<String>();
    }

    @Override
    public void evaluate() {
    }

    @Override
    public boolean containsNots() {
        return false;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public String toRTF(boolean bl) {
        return "\\cf5 true \\cf0";
    }

    @Override
    public void freshVars(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<ArrayList<String>> arrayList3) {
    }

    @Override
    public boolean containsDisjunct() {
        return false;
    }
}

