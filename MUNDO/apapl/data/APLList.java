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
import apapl.data.APLListVar;
import apapl.data.APLVar;
import apapl.data.Term;
import java.util.ArrayList;
import java.util.LinkedList;

public class APLList
extends APLListVar {
    private Term head;
    private APLListVar tail;

    public APLList() {
        this.head = null;
        this.tail = null;
    }

    public APLList(LinkedList<Term> linkedList) {
        this.head = linkedList.poll();
        this.tail = this.head != null ? new APLList(linkedList) : null;
    }

    public APLList(Term ... termArray) {
        if (termArray.length == 0) {
            this.head = null;
            this.tail = null;
        } else {
            this.head = termArray[0];
            this.tail = APLList.makeList(1, termArray);
        }
    }

    public boolean isEmpty() {
        return this.head == null && this.tail == null;
    }

    public APLList(boolean bl, Term term, APLListVar aPLListVar) {
        this.head = term;
        this.tail = aPLListVar;
    }

    private static APLListVar makeList(int n, Term ... termArray) {
        if (termArray.length == n) {
            return new APLList();
        }
        return new APLList(true, termArray[n], APLList.makeList(n + 1, termArray));
    }

    public static APLListVar constructList(LinkedList<Term> linkedList, APLListVar aPLListVar) {
        if (linkedList.isEmpty()) {
            return aPLListVar != null ? aPLListVar : new APLList();
        }
        return new APLList(true, linkedList.remove(), APLList.constructList(linkedList, aPLListVar));
    }

    @Override
    public String toString(boolean bl) {
        if (this.isEmpty()) {
            return "[]";
        }
        if (this.oneElement()) {
            return "[" + this.head.toString(bl) + "]";
        }
        if (this.tail instanceof APLList) {
            return "[" + this.head.toString(bl) + "," + this.tail.toString(bl).substring(1);
        }
        if (this.tail instanceof APLVar) {
            APLVar aPLVar = (APLVar)this.tail;
            if (aPLVar.isBounded()) {
                return "[" + this.head.toString(bl) + "," + aPLVar.toString(bl).substring(1);
            }
            return "[" + this.head.toString(bl) + "|" + aPLVar + "]";
        }
        return "[]";
    }

    @Override
    public String toString() {
        return this.toString(false);
    }

    public boolean oneElement() {
        if (this.tail instanceof APLList) {
            return ((APLList)this.tail).isEmpty();
        }
        if (this.tail instanceof APLVar) {
            if (((APLVar)this.tail).isBounded()) {
                Term term = ((APLVar)this.tail).getSubst();
                if (term instanceof APLList) {
                    return ((APLList)term).isEmpty();
                }
                return false;
            }
            return false;
        }
        return false;
    }

    @Override
    public String toRTF(boolean bl) {
        if (this.isEmpty()) {
            return "[]";
        }
        if (this.oneElement()) {
            return "[" + this.head.toRTF(bl) + "]";
        }
        if (this.tail instanceof APLList) {
            return "[" + this.head.toRTF(bl) + "," + this.tail.toRTF().substring(1);
        }
        if (this.tail instanceof APLVar) {
            APLVar aPLVar = (APLVar)this.tail;
            if (aPLVar.isBounded()) {
                return "[" + this.head + "," + aPLVar.toString().substring(1);
            }
            return "[" + this.head.toRTF(bl) + "\\cf1 |\\cf0 " + aPLVar.toRTF() + "]";
        }
        return "[]";
    }

    public Term getHead() {
        return this.head;
    }

    public APLListVar getTail() {
        return this.tail;
    }

    @Override
    public void applySubstitution(SubstList<Term> substList) {
        if (!this.isEmpty()) {
            this.head.applySubstitution(substList);
            this.tail.applySubstitution(substList);
        }
    }

    @Override
    public APLList clone() {
        if (this.isEmpty()) {
            return new APLList();
        }
        return new APLList(true, this.head.clone(), this.tail.clone());
    }

    @Override
    public void freshVars(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<ArrayList<String>> arrayList3) {
        if (!this.isEmpty()) {
            this.head.freshVars(arrayList, arrayList2, arrayList3);
            this.tail.freshVars(arrayList, arrayList2, arrayList3);
        }
    }

    @Override
    public ArrayList<String> getVariables() {
        if (this.isEmpty()) {
            return new ArrayList<String>();
        }
        ArrayList<String> arrayList = this.head.getVariables();
        arrayList.addAll(this.tail.getVariables());
        return arrayList;
    }

    public LinkedList<Term> toLinkedList() {
        if (this.head == null) {
            return new LinkedList<Term>();
        }
        try {
            Term term = Term.unvar(this.tail);
            if (term instanceof APLList) {
                LinkedList<Term> linkedList = ((APLList)term).toLinkedList();
                linkedList.addFirst(this.head);
                return linkedList;
            }
            return new LinkedList<Term>();
        }
        catch (UnboundedVarException unboundedVarException) {
            return new LinkedList<Term>();
        }
    }

    @Override
    public boolean equals(Term term) {
        if (term instanceof APLList) {
            APLList aPLList = (APLList)term;
            if (aPLList.isEmpty() && this.isEmpty()) {
                return true;
            }
            if (aPLList.isEmpty() || this.isEmpty()) {
                return false;
            }
            if (!this.head.equals(aPLList.getHead())) {
                return false;
            }
            return this.tail.equals(aPLList.getTail());
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

