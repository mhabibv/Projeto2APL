/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.SubstList
 */
package apapl.data;

import apapl.SubstList;
import apapl.data.Literal;
import apapl.data.Query;
import apapl.data.Term;
import java.util.ArrayList;
import java.util.LinkedList;

public abstract class ComposedQuery
extends Query {
    protected Query left;
    protected Query right;

    public ComposedQuery(Query query, Query query2) {
        this.left = query;
        this.right = query2;
    }

    @Override
    public String toPrologString() {
        return "(" + this.left.toPrologString() + ");(" + this.right.toPrologString() + ")";
    }

    @Override
    public void applySubstitution(SubstList<Term> substList) {
        this.left.applySubstitution(substList);
        this.right.applySubstitution(substList);
    }

    @Override
    public abstract ComposedQuery clone();

    @Override
    public ArrayList<String> getVariables() {
        ArrayList<String> arrayList = this.left.getVariables();
        arrayList.addAll(this.right.getVariables());
        return arrayList;
    }

    @Override
    public void evaluate() {
        this.left.evaluate();
        this.right.evaluate();
    }

    @Override
    public void freshVars(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<ArrayList<String>> arrayList3) {
        this.left.freshVars(arrayList, arrayList2, arrayList3);
        this.right.freshVars(arrayList, arrayList2, arrayList3);
    }

    @Override
    public LinkedList<Literal> toLiterals() {
        LinkedList<Literal> linkedList = this.left.toLiterals();
        linkedList.addAll(this.right.toLiterals());
        return linkedList;
    }

    @Override
    public int size() {
        return this.left.size() + this.right.size();
    }

    @Override
    public boolean containsNots() {
        return this.left.containsNots() || this.right.containsNots();
    }

    public Query getLeft() {
        return this.left;
    }

    public Query getRight() {
        return this.right;
    }
}

