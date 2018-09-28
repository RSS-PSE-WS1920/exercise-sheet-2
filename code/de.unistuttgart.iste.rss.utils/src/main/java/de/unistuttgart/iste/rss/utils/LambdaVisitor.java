package de.unistuttgart.iste.rss.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LambdaVisitor<B, A> implements Function<B, A> {
    private final Map<Class<?>, Function<Object, A>> fMap = new HashMap<>();

    public <C> Acceptor<A, B, C> on(final Class<C> clazz) {
        return new Acceptor<>(this, clazz);
    }

    @Override
    public A apply(final Object o) {
        final Function<Object, A> f = fMap.get(o.getClass());
        if (f == null) {
            return null;
        }
        return f.apply( o );
    }

    public static class Acceptor<A, B, C> {
        @SuppressWarnings("rawtypes")
        private final LambdaVisitor visitor;
        private final Class<C> clazz;

        Acceptor( final LambdaVisitor<B, A> visitor, final Class<C> clazz ) {
            this.visitor = visitor;
            this.clazz = clazz;
        }

        @SuppressWarnings("unchecked")
        public LambdaVisitor<B, A> then(final Function<C, A> f) {
            visitor.fMap.put( clazz, f );
            return visitor;
        }
    }
}