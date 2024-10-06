/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.SubstList
 */
package apapl.data;

import apapl.SubstList;
import apapl.Substitutable;
import apapl.data.APLVar;
import apapl.data.Term;
import java.math.BigDecimal;
import java.util.ArrayList;

public class APLNum
extends Term {
    private BigDecimal val;

    public APLNum(Number number) {
        this.val = new BigDecimal(number.toString());
    }

    public APLNum(double d) {
        this.val = new BigDecimal(d);
    }

    public APLNum(int n) {
        this.val = new BigDecimal(n);
    }

    public BigDecimal getVal() {
        return this.val;
    }

    @Override
    public String toString(boolean bl) {
        return this.val.toString();
    }

    @Override
    public String toRTF(boolean bl) {
        return this.toString();
    }

    public boolean groundedUnify(Term term, SubstList<Term> substList) {
        if (term instanceof APLVar) {
            return this.groundedUnify((APLVar)term, substList);
        }
        if (term instanceof APLNum) {
            return ((APLNum)term).getVal().equals(this.val);
        }
        return false;
    }

    private boolean groundedUnify(APLVar aPLVar, SubstList<Term> substList) {
        Term term = (Term)substList.get(aPLVar.getName());
        if (term != null) {
            return this.groundedUnify(term, substList);
        }
        substList.put(aPLVar.getName(), (Substitutable)this);
        return true;
    }

    @Override
    public void applySubstitution(SubstList<Term> substList) {
    }

    @Override
    public APLNum clone() {
        return new APLNum(this.val);
    }

    @Override
    public void freshVars(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<ArrayList<String>> arrayList3) {
    }

    @Override
    public ArrayList<String> getVariables() {
        return new ArrayList<String>();
    }

    public double toDouble() {
        return this.val.doubleValue();
    }

    public int toInt() {
        return this.val.intValue();
    }

    @Override
    public String toString() {
        return this.val.toString();
    }

    @Override
    public boolean equals(Term term) {
        if (term instanceof APLNum) {
            APLNum aPLNum = (APLNum)term;
            return this.val.equals(aPLNum.getVal());
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

