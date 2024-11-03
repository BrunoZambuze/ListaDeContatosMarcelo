package database.modeldao;

import database.dbConnection;
import database.impl.ContatoImpl;

import java.sql.SQLException;

public abstract class FactoryInterfaceDao {

    public static IContato getInstanceContato() throws SQLException {
        return new ContatoImpl(dbConnection.getConnection());
    }

}