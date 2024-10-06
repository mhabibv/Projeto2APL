/*
 * Decompiled with CFR 0.152.
 */
package apapl.data;

import apapl.data.ComposedQuery;
import apapl.data.Query;

public class OrQuery
extends ComposedQuery {
    public OrQuery(Query query, Query query2) {
        super(query, query2);
    }

    @Override
    public String toString(boolean bl) {
        return this.left.toString(bl) + " or " + this.right.toString(bl);
    }

    @Override
    public String toPrologString() {
        return "(" + this.left.toPrologString() + ");(" + this.right.toPrologString() + ")";
    }

    @Override
    public OrQuery clone() {
        return new OrQuery(this.left.clone(), this.right.clone());
    }

    @Override
    public String toRTF(boolean bl) {
        return this.left.toString(bl) + "\\cf1  or \\cf0" + this.right.toString(bl);
    }

    @Override
    public boolean containsDisjunct() {
        return true;
    }
}

