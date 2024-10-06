/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.SubstList
 */
package apapl.data;

import apapl.SubstList;
import apapl.data.APLVar;
import apapl.data.Fact;
import apapl.data.Term;
import java.util.ArrayList;

public class APLIdent
extends Fact {
    private String name;
    private boolean quoted = false;

    public APLIdent(String string) {
        this.name = string;
        this.quoted = false;
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_') continue;
            this.quoted = true;
        }
    }

    public APLIdent(String string, boolean bl) {
        this.name = string;
        this.quoted = bl;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.quoted ? "'" + this.name + "'" : this.name;
    }

    @Override
    public String toString(boolean bl) {
        return this.toString();
    }

    @Override
    public String toRTF(boolean bl) {
        return this.quoted ? "\\cf6 '" + this.name + "'\\cf0 " : "\\cf6 " + this.name + "\\cf0 ";
    }

    public boolean groundedUnify(Term term, SubstList<Term> substList) {
        if (term instanceof APLVar) {
            return this.groundedUnify((APLVar)term, substList);
        }
        if (term instanceof APLIdent) {
            return this.groundedUnify((APLIdent)term, substList);
        }
        return false;
    }

    public boolean quoted() {
        return this.quoted;
    }

    @Override
    public void applySubstitution(SubstList<Term> substList) {
    }

    @Override
    public APLIdent clone() {
        return new APLIdent(new String(this.name), this.quoted);
    }

    @Override
    public void freshVars(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<ArrayList<String>> arrayList3) {
    }

    @Override
    public ArrayList<String> getVariables() {
        return new ArrayList<String>();
    }

    @Override
    public boolean equals(Term term) {
        if (term instanceof APLIdent) {
            APLIdent aPLIdent = (APLIdent)term;
            return this.name.equals(aPLIdent.getName());
        }
        if (term instanceof APLVar) {
            APLVar aPLVar = (APLVar)term;
            if (!aPLVar.isBounded()) {
                return false;
            }
            return this.equals(aPLVar.getSubst());
        }
        return false;
    }
}

