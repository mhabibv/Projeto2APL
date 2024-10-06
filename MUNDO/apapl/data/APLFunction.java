/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.SubstList
 *  apapl.program.Base
 */
package apapl.data;

import apapl.SubstList;
import apapl.data.APLNum;
import apapl.data.APLVar;
import apapl.data.Fact;
import apapl.data.Term;
import apapl.program.Base;
import java.math.BigDecimal;
import java.util.ArrayList;

public class APLFunction
extends Fact {
    private boolean infix = false;
    private String name;
    ArrayList<Term> params;

    public APLFunction(String string, ArrayList<Term> arrayList) {
        this.name = string;
        this.params = arrayList;
    }

    public APLFunction(String string, Term ... termArray) {
        this.name = string;
        this.params = new ArrayList();
        for (Term term : termArray) {
            this.params.add(term);
        }
    }

    public APLFunction(Term term, String string, Term term2) {
        this.infix = true;
        this.name = string;
        this.params = new ArrayList();
        this.params.add(term);
        this.params.add(term2);
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Term> getParams() {
        return this.params;
    }

    public void evaluateArguments() {
        for (int i = 0; i < this.params.size(); ++i) {
            Term term = this.params.get(i);
            if (term instanceof APLVar && ((APLVar)term).isBounded()) {
                this.params.set(i, ((APLVar)term).getSubst());
            }
            if (!(term instanceof APLFunction)) continue;
            this.params.set(i, ((APLFunction)term).evaluate());
        }
    }

    public Term evaluate() {
        if (this.isInfix()) {
            Term term;
            Term term2 = this.params.get(0);
            Term term3 = this.params.get(1);
            if (term2 instanceof APLVar && ((APLVar)term2).isBounded()) {
                term2 = ((APLVar)term2).getSubst();
            }
            if (term3 instanceof APLVar && ((APLVar)term3).isBounded()) {
                term3 = ((APLVar)term3).getSubst();
            }
            if (term2 instanceof APLFunction) {
                term2 = ((APLFunction)term2).evaluate();
            }
            if (term3 instanceof APLFunction) {
                term3 = ((APLFunction)term3).evaluate();
            }
            if ((term = APLFunction.eval(term2, this.name, term3)) != null) {
                return term;
            }
        }
        return this;
    }

    private static Term eval(Term term, String string, Term term2) {
        if (term instanceof APLNum && term2 instanceof APLNum) {
            BigDecimal bigDecimal = ((APLNum)term).getVal();
            BigDecimal bigDecimal2 = ((APLNum)term2).getVal();
            if (string.equals("+")) {
                return new APLNum(bigDecimal.add(bigDecimal2));
            }
            if (string.equals("-")) {
                return new APLNum(bigDecimal.subtract(bigDecimal2));
            }
            if (string.equals("*")) {
                return new APLNum(bigDecimal.multiply(bigDecimal2));
            }
            if (string.equals("/")) {
                return new APLNum(bigDecimal.divide(bigDecimal2));
            }
            if (string.equals("%")) {
                return new APLNum(bigDecimal.divideAndRemainder(bigDecimal2)[1]);
            }
        }
        return null;
    }

    public boolean isInfix() {
        return this.infix;
    }

    @Override
    public void applySubstitution(SubstList<Term> substList) {
        for (Term term : this.params) {
            term.applySubstitution(substList);
        }
        this.evaluateArguments();
    }

    @Override
    public ArrayList<String> getVariables() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Term term : this.params) {
            arrayList.addAll(term.getVariables());
        }
        return arrayList;
    }

    @Override
    public APLFunction clone() {
        if (this.infix) {
            return new APLFunction(this.params.get(0).clone(), this.name, this.params.get(1).clone());
        }
        ArrayList<Term> arrayList = new ArrayList<Term>();
        for (Term term : this.params) {
            arrayList.add(term.clone());
        }
        return new APLFunction(new String(this.name), arrayList);
    }

    public int infixLevel() {
        if (!this.infix) {
            return 0;
        }
        if (this.name.equals("+")) {
            return 3;
        }
        if (this.name.equals("-")) {
            return 3;
        }
        if (this.name.equals("*")) {
            return 2;
        }
        if (this.name.equals("/")) {
            return 2;
        }
        if (this.name.equals("<")) {
            return 1;
        }
        if (this.name.equals(">")) {
            return 1;
        }
        if (this.name.equals("=")) {
            return 1;
        }
        if (this.name.equals("=<")) {
            return 1;
        }
        if (this.name.equals(">=")) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString(boolean bl) {
        if (this.infix) {
            Term term = this.params.get(0);
            Term term2 = this.params.get(1);
            String string = term.toString(bl);
            String string2 = term2.toString(bl);
            int n = 0;
            int n2 = 0;
            int n3 = this.infixLevel();
            if (term instanceof APLFunction) {
                n = ((APLFunction)term).infixLevel();
            }
            if (term2 instanceof APLFunction) {
                n2 = ((APLFunction)term2).infixLevel();
            }
            if (n > n3) {
                string = "(" + string + ")";
            }
            if (n2 > n3) {
                string2 = "(" + string2 + ")";
            }
            return string + this.name + string2;
        }
        if (this.params.size() > 0) {
            String string = "";
            for (Term term : this.params) {
                string = string + term.toString(bl) + ", ";
            }
            if (string.length() >= 1) {
                string = string.substring(0, string.length() - 2);
            }
            return this.name + "(" + string + ")";
        }
        return this.name;
    }

    @Override
    public String toRTF(boolean bl) {
        if (this.params.size() == 1 && this.params.get(0).toString().equals("arityequaltozero")) {
            return "\\b " + this.name + "\\b0 ()";
        }
        if (this.infix) {
            return "(" + this.params.get(0).toRTF(bl) + this.name + this.params.get(1).toRTF(bl) + ")";
        }
        return "\\b " + this.name + "\\b0 (" + Base.rtfconcatWith(this.params, (String)",", (boolean)bl) + ")";
    }

    public String toQueryString() {
        int n;
        if (this.infix) {
            return this.toString();
        }
        String string = this.name + "(";
        for (n = 1; n <= this.params.size(); ++n) {
            string = string + "QAZXSW" + n + ",";
        }
        string = string.substring(0, string.length() - 1) + ")";
        n = 1;
        for (Term term : this.params) {
            string = string + ",QAZXSW" + n + " = " + term;
            ++n;
        }
        return string;
    }

    @Override
    public void freshVars(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<ArrayList<String>> arrayList3) {
        for (Term term : this.params) {
            term.freshVars(arrayList, arrayList2, arrayList3);
        }
    }

    @Override
    public boolean equals(Term term) {
        if (term instanceof APLFunction) {
            APLFunction aPLFunction = (APLFunction)term;
            if (!this.name.equals(aPLFunction.getName())) {
                return false;
            }
            if (this.params.size() != aPLFunction.getParams().size()) {
                return false;
            }
            for (int i = 0; i < this.params.size(); ++i) {
                if (this.params.get(i).equals(aPLFunction.getParams().get(i))) continue;
                return false;
            }
            return true;
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

