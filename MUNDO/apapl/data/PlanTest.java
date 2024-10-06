/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.APLModule
 *  apapl.ModuleAccessException
 *  apapl.SolutionIterator
 *  apapl.SubstList
 *  apapl.program.Planbase
 */
package apapl.data;

import apapl.APLModule;
import apapl.ModuleAccessException;
import apapl.SolutionIterator;
import apapl.SubstList;
import apapl.data.APLIdent;
import apapl.data.Query;
import apapl.data.Term;
import apapl.data.Test;
import apapl.program.Planbase;

public class PlanTest
extends Test {
    public PlanTest(Query query, Test test) {
        this.query = query;
        this.next = test;
    }

    public PlanTest(APLIdent aPLIdent, Query query) {
        this.moduleId = aPLIdent;
        this.query = query;
        this.next = null;
    }

    @Override
    public SubstList<Term> test(APLModule aPLModule) {
        APLModule aPLModule2 = null;
        try {
            aPLModule2 = this.moduleId == null ? aPLModule : aPLModule.getMas().getModule(aPLModule, this.moduleId.getName());
        }
        catch (ModuleAccessException moduleAccessException) {
            return null;
        }
        Planbase planbase = aPLModule2.getPlanbase();
        SolutionIterator solutionIterator = planbase.doTest(this.query);
        if (this.next == null) {
            return solutionIterator.next();
        }
        for (SubstList substList : solutionIterator) {
            if (substList == null) {
                return null;
            }
            Test test = this.next.clone();
            test.applySubstitution((SubstList<Term>)substList);
            SubstList<Term> substList2 = test.test(aPLModule);
            if (substList2 == null) continue;
            substList2.putAll(substList);
            return substList2;
        }
        return null;
    }

    @Override
    public PlanTest clone() {
        if (this.next == null) {
            return new PlanTest(this.moduleId, this.query.clone());
        }
        return new PlanTest(this.query.clone(), this.next.clone());
    }

    @Override
    public String toString() {
        String string = "";
        if (this.moduleId != null) {
            string = this.moduleId + ".";
        }
        return string + "P(" + super.toString() + ")";
    }

    @Override
    public String toRTF(boolean bl) {
        return this.toString();
    }
}

