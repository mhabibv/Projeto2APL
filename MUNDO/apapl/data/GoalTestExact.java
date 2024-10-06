/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.APLModule
 *  apapl.ModuleAccessException
 *  apapl.SubstList
 *  apapl.Unifier
 *  apapl.program.Goalbase
 */
package apapl.data;

import apapl.APLModule;
import apapl.ModuleAccessException;
import apapl.SubstList;
import apapl.Unifier;
import apapl.data.APLIdent;
import apapl.data.Goal;
import apapl.data.Query;
import apapl.data.Term;
import apapl.data.Test;
import apapl.program.Goalbase;

public class GoalTestExact
extends Test {
    public GoalTestExact(Query query, Test test) {
        this.query = query;
        this.next = test;
    }

    public GoalTestExact(APLIdent aPLIdent, Query query) {
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
        Goalbase goalbase = aPLModule2.getGoalbase();
        Goal goal = new Goal(this.query.toLiterals());
        if (this.query.containsDisjunct()) {
            return null;
        }
        for (Goal goal2 : goalbase) {
            SubstList substList;
            if (!Unifier.unify((Goal)goal2, (Goal)goal, (SubstList)(substList = new SubstList()))) continue;
            if (this.next == null) {
                return substList;
            }
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
    public GoalTestExact clone() {
        if (this.next == null) {
            return new GoalTestExact(this.moduleId, this.query.clone());
        }
        return new GoalTestExact(this.query.clone(), this.next.clone());
    }

    @Override
    public String toString() {
        String string = "";
        if (this.moduleId != null) {
            string = this.moduleId + ".";
        }
        return string + "!G(" + super.toString() + ")";
    }
}

