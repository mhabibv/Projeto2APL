/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.SubstList
 */
package apapl.data;

import apapl.SubstList;
import apapl.data.APLListVar;
import apapl.data.Term;
import java.util.ArrayList;

public class APLVar
extends APLListVar {
    private String name;
    private Term subst = null;
    private boolean anonymous = false;
    private int freshCounter = 0;

    public APLVar() {
        this.name = "_";
        this.anonymous = true;
    }

    public APLVar(String string) {
        this.name = string;
    }

    public APLVar(String string, Term term, int n) {
        this.name = string;
        this.subst = term;
        this.freshCounter = n;
    }

    public String getName() {
        return this.name + (this.freshCounter == 0 ? "" : "" + this.freshCounter);
    }

    public boolean isBounded() {
        return this.subst != null;
    }

    public boolean isBounded(SubstList<Term> substList) {
        if (this.subst != null) {
            return true;
        }
        return substList.get(this.getName()) != null;
    }

    public Term getSubst(SubstList<Term> substList) {
        if (this.anonymous) {
            return null;
        }
        if (this.subst != null) {
            return this.subst;
        }
        return (Term)substList.get(this.getName());
    }

    public Term getSubst() {
        return this.subst;
    }

    @Override
    public String toString(boolean bl) {
        if (this.isBounded()) {
            return bl ? "[" + this.getName() + "/" + this.subst.toString(bl) + "]" : this.subst.toString(bl);
        }
        return this.getName();
    }

    @Override
    public String toRTF(boolean bl) {
        return this.toString(bl);
    }

    @Override
    public void applySubstitution(SubstList<Term> substList) {
        if (this.anonymous) {
            return;
        }
        Term term = (Term)substList.get(this.getName());
        if (term != null) {
            this.subst = term;
        }
        while (this.subst instanceof APLVar) {
            APLVar aPLVar = (APLVar)this.subst;
            if (aPLVar.isBounded()) {
                this.subst = aPLVar.getSubst();
                continue;
            }
            this.name = aPLVar.getName();
            this.freshCounter = aPLVar.getFreshCounter();
            this.subst = (Term)substList.get(aPLVar.getName());
        }
    }

    public int getFreshCounter() {
        return this.freshCounter;
    }

    public void boundTo(Term term) {
        this.subst = term;
    }

    @Override
    public APLVar clone() {
        Term term = null;
        if (this.isBounded()) {
            term = this.subst.clone();
        }
        return new APLVar(new String(this.name), term, this.freshCounter);
    }

    @Override
    public void freshVars(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<ArrayList<String>> arrayList3) {
        String string = this.getName();
        if (arrayList.contains(this.getName())) {
            while (arrayList.contains(this.getName()) || arrayList2.contains(this.getName())) {
                ++this.freshCounter;
            }
        }
        if (!string.equals(this.getName())) {
            ArrayList<String> arrayList4 = new ArrayList<String>();
            arrayList4.add(string);
            arrayList4.add(this.getName());
            arrayList3.add(arrayList4);
        }
    }

    @Override
    public ArrayList<String> getVariables() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(this.name);
        return arrayList;
    }

    public Term getTerm() {
        if (this.isBounded()) {
            return this.subst;
        }
        return this;
    }

    @Override
    public boolean equals(Term term) {
        if (this.isBounded()) {
            return this.subst.equals(term);
        }
        if (term instanceof APLVar) {
            APLVar aPLVar = (APLVar)term;
            if (!aPLVar.isBounded()) {
                return this.name.equals(aPLVar.getName());
            }
            return false;
        }
        return false;
    }
}

