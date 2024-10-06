/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.SubstList
 *  apapl.UnboundedVarException
 */
package apapl.data;

import apapl.SubstList;
import apapl.UnboundedVarException;
import apapl.data.APLFunction;
import apapl.data.Query;
import apapl.data.Term;
import java.util.ArrayList;
import java.util.LinkedList;

public class Literal
extends Query {
    private boolean sign;
    private Term body;

    public Literal(Term term, boolean bl) {
        this.sign = bl;
        this.body = term;
    }

    @Override
    public String toPrologString() {
        if (this.body instanceof APLFunction) {
            ((APLFunction)this.body).evaluate();
            return (this.sign ? "" : "not ") + ((APLFunction)this.body).toString();
        }
        return (this.sign ? "" : "not ") + this.body.toString();
    }

    public boolean getSign() {
        return this.sign;
    }

    public Term getBody() {
        return this.body;
    }

    @Override
    public void applySubstitution(SubstList<Term> substList) {
        this.body.applySubstitution(substList);
    }

    @Override
    public Literal clone() {
        return new Literal(this.body.clone(), this.sign);
    }

    @Override
    public ArrayList<String> getVariables() {
        return this.body.getVariables();
    }

    @Override
    public void evaluate() {
        if (this.body instanceof APLFunction) {
            ((APLFunction)this.body).evaluateArguments();
        }
    }

    @Override
    public String toRTF(boolean bl) {
        return (this.sign ? "" : "\\cf1 not \\cf0 ") + this.body.toRTF(bl);
    }

    @Override
    public String toString(boolean bl) {
        return (this.sign ? "" : "not ") + this.body.toString(bl);
    }

    @Override
    public void freshVars(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<ArrayList<String>> arrayList3) {
        this.body.freshVars(arrayList, arrayList2, arrayList3);
    }

    @Override
    public LinkedList<Literal> toLiterals() {
        LinkedList<Literal> linkedList = new LinkedList<Literal>();
        linkedList.add(this);
        return linkedList;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean containsNots() {
        return !this.sign;
    }

    public boolean equals(Literal literal) {
        if (this.sign == literal.getSign()) {
            return this.body.toString().equals(literal.getBody().toString());
        }
        return false;
    }

    @Override
    public boolean containsDisjunct() {
        return false;
    }

    public void unvar() throws UnboundedVarException {
        this.body = Term.unvar(this.body);
    }

    public void setSign(boolean bl) {
        this.sign = bl;
    }
}

