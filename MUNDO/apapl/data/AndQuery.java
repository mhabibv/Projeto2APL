/*
 * Decompiled with CFR 0.152.
 */
package apapl.data;

import apapl.data.ComposedQuery;
import apapl.data.OrQuery;
import apapl.data.Query;

public class AndQuery
extends ComposedQuery {
    public AndQuery(Query query, Query query2) {
        super(query, query2);
    }

    @Override
    public String toPrologString() {
        return "(" + this.left.toPrologString() + "),(" + this.right.toPrologString() + ")";
    }

    @Override
    public AndQuery clone() {
        return new AndQuery(this.left.clone(), this.right.clone());
    }

    @Override
    public String toString(boolean bl) {
        String string = this.left.toString(bl);
        String string2 = this.right.toString(bl);
        if (this.left instanceof OrQuery) {
            string = "(" + string + ")";
        }
        if (this.right instanceof OrQuery) {
            string2 = "(" + string2 + ")";
        }
        return string + " and " + string2;
    }

    @Override
    public String toRTF(boolean bl) {
        String string = this.left.toString(bl);
        String string2 = this.right.toString(bl);
        if (this.left instanceof OrQuery) {
            string = "(" + string + ")";
        }
        if (this.right instanceof OrQuery) {
            string2 = "(" + string2 + ")";
        }
        return string + " \\cf1 and \\cf0" + string2;
    }

    @Override
    public boolean containsDisjunct() {
        return this.left.containsDisjunct() || this.right.containsDisjunct();
    }
}

