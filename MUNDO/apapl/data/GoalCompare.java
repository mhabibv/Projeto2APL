/*
 * Decompiled with CFR 0.152.
 */
package apapl.data;

import apapl.data.Literal;
import java.util.Comparator;

public class GoalCompare
implements Comparator<Literal> {
    public static final GoalCompare INSTANCE = new GoalCompare();

    private GoalCompare() {
    }

    @Override
    public int compare(Literal literal, Literal literal2) {
        return literal.toString().compareTo(literal2.toString());
    }

    public boolean equals(Literal literal, Literal literal2) {
        return literal.toString().equals(literal2.toString());
    }
}

