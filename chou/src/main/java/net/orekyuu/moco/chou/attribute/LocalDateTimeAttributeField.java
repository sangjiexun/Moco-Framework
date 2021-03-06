package net.orekyuu.moco.chou.attribute;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import net.orekyuu.moco.chou.RoundContext;
import net.orekyuu.moco.core.annotations.Column;
import net.orekyuu.moco.core.attribute.LocalDateTimeAttribute;
import net.orekyuu.moco.feeling.node.SqlBindParam;

import javax.lang.model.element.VariableElement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class LocalDateTimeAttributeField extends AttributeField {
    public LocalDateTimeAttributeField(RoundContext context, Column column, VariableElement variableElement) {
        super(context, column, variableElement);
    }

    @Override
    public ClassName getAttributeClass() {
        return ClassName.get(LocalDateTimeAttribute.class);
    }

    @Override
    public ColumnFindableMethod getFeelingTableMethod() {
        return ColumnFindableMethod.TIME;
    }

    @Override
    public CodeBlock createSetterBlock() {
        return CodeBlock.builder().addStatement("$L.set(record, $T.ofNullable(resultSet.getTimestamp($S)).map(t -> t.toLocalDateTime()).orElse(null))",
                variableElement.getSimpleName().toString(), Optional.class, column.name()).build();
    }

    @Override
    public CodeBlock createSqlBindParam() {
        return CodeBlock.builder().add("new $T($T.valueOf(($T)$L.getAccessor().get(entity)), $L.bindType())", SqlBindParam.class, Timestamp.class, LocalDateTime.class, tableClassColumnName(), tableClassColumnName()).build();
    }

    @Override
    public CodeBlock createColumnMethod() {
        return CodeBlock.builder().add("._datetime($S)", column.name()).build();
    }
}
