package com.wallet.demo.shared.infrastructure.type;

import com.wallet.demo.shared.domain.BaseId;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractJavaType;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.util.UUID;
import java.util.function.Function;

public class BaseIdJavaType<T extends BaseId> extends AbstractJavaType<T> {

    private final Function<String, T> factory;

    @SuppressWarnings("unchecked")
    public BaseIdJavaType(Class<T> idClass, Function<String, T> factory) {
        super(idClass, (ImmutableMutabilityPlan<T>) (ImmutableMutabilityPlan<?>) ImmutableMutabilityPlan.INSTANCE);
        this.factory = factory;
    }

    @Override
    public JdbcType getRecommendedJdbcType(JdbcTypeIndicators indicators) {
        return UUIDJdbcType.INSTANCE;
    }

    @Override
    public String toString(T value) {
        return value == null ? null : value.toString();
    }

    @Override
    public T fromString(CharSequence string) {
        return string == null ? null : factory.apply(string.toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> X unwrap(T value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (type.isAssignableFrom(UUID.class)) {
            return (X) value.value();
        }
        if (type.isAssignableFrom(String.class)) {
            return (X) value.toString();
        }
        throw unknownUnwrap(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> T wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (getJavaTypeClass().isInstance(value)) {
            return (T) value;
        }
        if (value instanceof UUID uuid) {
            return factory.apply(uuid.toString());
        }
        if (value instanceof String s) {
            return factory.apply(s);
        }
        throw unknownWrap(value.getClass());
    }
}
