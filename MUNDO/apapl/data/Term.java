/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.SubstList
 *  apapl.UnboundedVarException
 */
package apapl.data;

import apapl.SubstList;
import apapl.Substitutable;
import apapl.UnboundedVarException;
import apapl.data.APLFunction;
import apapl.data.APLList;
import apapl.data.APLListVar;
import apapl.data.APLVar;
import java.util.ArrayList;

public abstract class Term
implements Substitutable {
    public abstract void applySubstitution(SubstList<Term> var1);

    public abstract Term clone();

    public abstract String toRTF(boolean var1);

    @Override
    public String toRTF() {
        return this.toRTF(false);
    }

    public abstract void freshVars(ArrayList<String> var1, ArrayList<String> var2, ArrayList<ArrayList<String>> var3);

    public abstract ArrayList<String> getVariables();

    public abstract String toString(boolean var1);

    public String toString() {
        return this.toString(false);
    }

    public abstract boolean equals(Term var1);

    public static Term unvar(Term term) throws UnboundedVarException {
        if (term instanceof APLVar) {
            APLVar aPLVar = (APLVar)term;
            if (aPLVar.isBounded()) {
                return Term.unvar(aPLVar.getSubst());
            }
            throw new UnboundedVarException("" + term);
        }
        if (term instanceof APLList) {
            APLList aPLList = (APLList)term;
            if (aPLList.isEmpty()) {
                return aPLList;
            }
            Term term2 = Term.unvar(aPLList.getHead());
            Term term3 = Term.unvar(aPLList.getTail());
            if (!(term3 instanceof APLListVar)) {
                throw new UnboundedVarException("" + term);
            }
            return new APLList(true, term2, (APLListVar)term3);
        }
        if (term instanceof APLFunction) {
            APLFunction aPLFunction = (APLFunction)term;
            ArrayList<Term> arrayList = aPLFunction.getParams();
            for (int i = 0; i < arrayList.size(); ++i) {
                arrayList.set(i, Term.unvar(arrayList.get(i)));
            }
            return new APLFunction(aPLFunction.getName(), arrayList);
        }
        return term;
    }
}

