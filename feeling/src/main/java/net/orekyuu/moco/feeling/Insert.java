package net.orekyuu.moco.feeling;

import net.orekyuu.moco.feeling.attributes.Attribute;
import net.orekyuu.moco.feeling.node.SqlNodeArray;
import net.orekyuu.moco.feeling.visitor.MySqlVisitor;
import net.orekyuu.moco.feeling.visitor.SqlVisitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Insert {
    private Table table;
    private List<Attribute> attributes = new ArrayList<>();
    private SqlNodeArray values;

    public Insert(Table table) {
        this.table = table;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void setValues(SqlNodeArray values) {
        this.values = values;
    }

    public Table getTable() {
        return table;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public SqlNodeArray getValues() {
        return values;
    }

    public SqlContext prepareQuery() {
        SqlContext context = new SqlContext();
        SqlVisitor visitor = new MySqlVisitor();
        accept(visitor, context);
        return context;
    }

    public int executeQuery(Connection connection) throws SQLException {
        SqlContext context = prepareQuery();
        try (PreparedStatement statement = context.createStatement(connection)) {
            return statement.executeUpdate();
        }
    }

    private void accept(SqlVisitor visitor, SqlContext context) {
        visitor.visit(this, context);
    }
}
