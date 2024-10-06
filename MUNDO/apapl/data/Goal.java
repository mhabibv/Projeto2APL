/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.Prolog
 *  apapl.SolutionIterator
 *  apapl.SubstList
 *  apapl.UnboundedVarException
 *  apapl.program.Base
 */
package apapl.data;

import apapl.Prolog;
import apapl.SolutionIterator;
import apapl.SubstList;
import apapl.UnboundedVarException;
import apapl.data.AndQuery;
import apapl.data.GoalCompare;
import apapl.data.Literal;
import apapl.data.Query;
import apapl.data.Term;
import apapl.data.True;
import apapl.program.Base;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class Goal
implements Iterable<Literal> {
    private LinkedList<Literal> goal;

    public Goal() {
        this.goal = new LinkedList();
    }

    public Goal(LinkedList<Literal> linkedList) {
        this.goal = linkedList;
    }

    public void addLiteral(Literal literal) {
        this.goal.add(literal);
    }

    @Override
    public Iterator<Literal> iterator() {
        return this.goal.iterator();
    }

    public ArrayList<SubstList<Term>> possibleSubstitutions(Query query) {
        ArrayList<SubstList<Term>> arrayList = new ArrayList<SubstList<Term>>();
        Prolog prolog = new Prolog();
        for (Literal object : this.goal) {
            prolog.addPredicate(object.toPrologString());
        }
        SolutionIterator solutionIterator = prolog.doTest(query);
        for (SubstList substList : solutionIterator) {
            arrayList.add((SubstList<Term>)substList);
        }
        return arrayList;
    }

    public SolutionIterator doTest(Query query) {
        Prolog prolog = new Prolog();
        for (Literal literal : this.goal) {
            prolog.addPredicate(literal.toPrologString());
        }
        SolutionIterator solutionIterator = prolog.doTest(query);
        return solutionIterator;
    }

    public String toPrologString() {
        return Base.concatWith(this.goal, (String)" , ");
    }

    public boolean equals(Goal goal) {
        LinkedList<Literal> linkedList = new LinkedList<Literal>();
        LinkedList<Literal> linkedList2 = new LinkedList<Literal>();
        for (Literal literal : goal) {
            linkedList.add(literal.clone());
        }
        for (Literal literal : this) {
            linkedList2.add(literal.clone());
        }
        if (linkedList.size() != linkedList2.size()) {
            return false;
        }
        Collections.sort(linkedList, GoalCompare.INSTANCE);
        Collections.sort(linkedList2, GoalCompare.INSTANCE);
        for (int i = 0; i < linkedList.size(); ++i) {
            if (((Literal)linkedList.get(i)).toString().equals(((Literal)linkedList2.get(i)).toString())) continue;
            return false;
        }
        return true;
    }

    public void removeLiteral(Literal literal) {
        this.goal.remove(literal);
    }

    public void applySubstitution(SubstList<Term> substList) {
        for (Literal literal : this.goal) {
            literal.applySubstitution(substList);
        }
    }

    public void evaluate() {
        for (Literal literal : this.goal) {
            literal.evaluate();
        }
    }

    public Goal clone() {
        Goal goal = new Goal();
        for (Literal literal : this.goal) {
            goal.addLiteral(literal.clone());
        }
        return goal;
    }

    public String toString(boolean bl) {
        String string = "";
        for (Literal literal : this.goal) {
            string = string + literal.toString(bl) + " and ";
        }
        if (string.length() >= 5) {
            string = string.substring(0, string.length() - 5);
        }
        return string;
    }

    public String toString() {
        return this.toString(false);
    }

    public String toRTF(boolean bl) {
        if (this.goal.size() <= 0) {
            return "";
        }
        String string = "";
        String string2 = "\\cf1  and \\cf0 ";
        for (Literal literal : this.goal) {
            string = string + literal.toRTF(bl) + string2;
        }
        if (string.length() >= string2.length()) {
            string = string.substring(0, string.length() - string2.length());
        }
        return string;
    }

    public void freshVars(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<ArrayList<String>> arrayList3) {
        for (Literal literal : this.goal) {
            literal.freshVars(arrayList, arrayList2, arrayList3);
        }
    }

    public ArrayList<String> getVariables() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Literal literal : this.goal) {
            arrayList.addAll(literal.getVariables());
        }
        return arrayList;
    }

    public int size() {
        return this.goal.size();
    }

    public boolean isEmpty() {
        return this.goal.isEmpty();
    }

    public Query convertToQuery() {
        if (this.goal.size() == 0) {
            return new True();
        }
        Query query = null;
        for (Literal literal : this.goal) {
            if (query == null) {
                query = literal;
                continue;
            }
            query = new AndQuery(query, literal);
        }
        return query;
    }

    public LinkedList<Literal> getList() {
        return this.goal;
    }

    public int uniqueItems() {
        int n = 0;
        Literal[] literalArray = this.goal.toArray(new Literal[0]);
        for (int i = 0; i < literalArray.length; ++i) {
            boolean bl = true;
            for (int j = i + 1; j < literalArray.length; ++j) {
                if (!literalArray[i].equals(literalArray[j])) continue;
                bl = false;
            }
            if (!bl) continue;
            ++n;
        }
        return n;
    }

    public void unvar() throws UnboundedVarException {
        for (Literal literal : this.goal) {
            literal.unvar();
        }
    }
}

