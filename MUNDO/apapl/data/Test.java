/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.APLModule
 *  apapl.SubstList
 */
package apapl.data;

import apapl.APLModule;
import apapl.SubstList;
import apapl.data.APLIdent;
import apapl.data.Query;
import apapl.data.Term;
import java.util.ArrayList;

public abstract class Test {
    protected APLIdent moduleId;
    protected Query query;
    protected Test next = null;

    public Query getQuery() {
        return this.query;
    }

    public abstract SubstList<Term> test(APLModule var1);

    public void addLast(Test test) {
        if (this.next == null) {
            this.next = test;
        } else {
            this.next.addLast(test);
        }
    }

    public abstract Test clone();

    public void applySubstitution(SubstList<Term> substList) {
        this.query.applySubstitution(substList);
        if (this.next != null) {
            this.next.applySubstitution(substList);
        }
    }

    public String toString() {
        return this.query + (this.next != null ? " && " + this.next.toString() : "");
    }

    public String toRTF(boolean bl) {
        return this.query + (this.next != null ? " \\b&&\\b0 " + this.next.toString() : "");
    }

    public void freshVars(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<ArrayList<String>> arrayList3) {
        this.query.freshVars(arrayList, arrayList2, arrayList3);
        if (this.next != null) {
            this.next.freshVars(arrayList, arrayList2, arrayList3);
        }
    }

    public ArrayList<String> getVariables() {
        ArrayList<String> arrayList = this.query.getVariables();
        if (this.next != null) {
            arrayList.addAll(this.next.getVariables());
        }
        return arrayList;
    }

    public Test getNext() {
        return this.next;
    }

    public APLIdent getModuleId() {
        return this.moduleId;
    }
}

