/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.APLModule
 *  apapl.ModuleAccessException
 *  apapl.SolutionIterator
 *  apapl.SubstList
 *  apapl.benchmarking.APLBenchmarkParam
 *  apapl.benchmarking.APLBenchmarker
 *  apapl.program.Goalbase
 */
package apapl.data;

import apapl.APLModule;
import apapl.ModuleAccessException;
import apapl.SolutionIterator;
import apapl.SubstList;
import apapl.benchmarking.APLBenchmarkParam;
import apapl.benchmarking.APLBenchmarker;
import apapl.data.APLIdent;
import apapl.data.Query;
import apapl.data.Term;
import apapl.data.Test;
import apapl.program.Goalbase;

public class GoalTest
extends Test {
    public GoalTest(Query query, Test test) {
        this.query = query;
        this.next = test;
    }

    public GoalTest(APLIdent aPLIdent, Query query) {
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
        APLBenchmarker.startTiming((APLModule)aPLModule2, (String)APLBenchmarkParam.GOAL_QUERY);
        SolutionIterator solutionIterator = goalbase.doTest(this.query);
        if (this.next == null) {
            SubstList substList = solutionIterator.next();
            APLBenchmarker.stopTiming((APLModule)aPLModule2, (String)APLBenchmarkParam.GOAL_QUERY);
            return substList;
        }
        for (SubstList substList : solutionIterator) {
            if (substList == null) {
                APLBenchmarker.stopTiming((APLModule)aPLModule2, (String)APLBenchmarkParam.GOAL_QUERY);
                return null;
            }
            Test test = this.next.clone();
            test.applySubstitution((SubstList<Term>)substList);
            SubstList<Term> substList2 = test.test(aPLModule);
            if (substList2 == null) continue;
            substList2.putAll(substList);
            APLBenchmarker.stopTiming((APLModule)aPLModule2, (String)APLBenchmarkParam.GOAL_QUERY);
            return substList2;
        }
        APLBenchmarker.stopTiming((APLModule)aPLModule2, (String)APLBenchmarkParam.GOAL_QUERY);
        return null;
    }

    @Override
    public GoalTest clone() {
        if (this.next == null) {
            return new GoalTest(this.moduleId, this.query.clone());
        }
        return new GoalTest(this.query.clone(), this.next.clone());
    }

    @Override
    public String toString() {
        String string = "";
        if (this.moduleId != null) {
            string = this.moduleId + ".";
        }
        return string + "G(" + super.toString() + ")";
    }

    @Override
    public String toRTF(boolean bl) {
        return this.toString();
    }
}

