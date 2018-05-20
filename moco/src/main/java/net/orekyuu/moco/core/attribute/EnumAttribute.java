package net.orekyuu.moco.core.attribute;

import net.orekyuu.moco.core.UpdateValuePair;
import net.orekyuu.moco.feeling.node.SqlBindParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumAttribute<OWNER, E extends Enum<E>> extends Attribute<OWNER> {
    public EnumAttribute(net.orekyuu.moco.feeling.attributes.StringAttribute attribute, AttributeValueAccessor<OWNER> accessor) {
        super(attribute, accessor);
    }

    @Override
    public Class<?> bindType() {
        return String.class;
    }

    public Predicate eq(E value) {
        return new Predicate(attribute.eq(new SqlBindParam<>(value.name(), String.class)));
    }

    public Predicate in(E value) {
        return eq(value);
    }

    public Predicate in(E ... value) {
        List<SqlBindParam> paramList = Stream.of(value)
                .distinct()
                .map(i -> new SqlBindParam<>(i.name(), String.class))
                .collect(Collectors.toList());
        return new Predicate(attribute.in(paramList));
    }

    public Predicate not(E value) {
        return new Predicate(attribute.noteq(new SqlBindParam<>(value.name(), String.class)));
    }

    @SuppressWarnings("unchecked")
    public UpdateValuePair<OWNER> set(Object value) {
        E v = (E) value;
        return UpdateValuePair.of(this, new SqlBindParam<>(v == null ? null : v.name(), String.class));
    }
}
