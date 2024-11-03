package database.impl;

import database.dbConnection;
import database.modeldao.IContato;
import domain.model.Contato;
import exception.queryFailedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContatoImpl implements IContato {

    private static Connection connection;

    public ContatoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Contato contato) throws queryFailedException {

        PreparedStatement stmt = null;
        ResultSet result = null;
        String sqlStandard = "INSERT INTO contato (nome, email, telefone)"
                + " VALUES (?, ?, ?)";
        try {
            stmt = connection.prepareStatement(sqlStandard, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getEmail());
            stmt.setString(3, contato.getTelefone());
            if(stmt.executeUpdate() > 0) {
                result = stmt.getGeneratedKeys();
                if(result.next()) {
                    int id = result.getInt(1);
                    contato.setId(id);
                }
            } else {
                throw new queryFailedException("Erro ao inserir contato!");
            }
        } catch(SQLException e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
        } finally {
            dbConnection.closeResultSet(result);
            dbConnection.closeStatement(stmt);
        }

    }

    @Override
    public void update(Contato contato) throws queryFailedException {
        PreparedStatement stmt = null;
        ResultSet result = null;
        String sql = "UPDATE standard SET nome = ?, email = ?, email = ?, telefone = ? WHERE conta.id = ?";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getEmail());
            stmt.setString(3, contato.getTelefone());
            stmt.setInt(5, contato.getId());
            if(stmt.executeUpdate() < 0) {
                throw new queryFailedException("Erro ao atualizar os dados!");
            }
        } catch(SQLException e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
        } finally {
            dbConnection.closeStatement(stmt);
            dbConnection.closeResultSet(result);
        }

    }

    @Override
    public void delete(Contato contato){
        PreparedStatement stmt = null;
        String sql = "DELETE FROM contato WHERE id = ?";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, contato.getId());
            if(stmt.executeUpdate() < 0) {
                throw new queryFailedException("Erro ao remover contato!");
            }
        } catch (queryFailedException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            dbConnection.closeStatement(stmt);
        }
    }

    @Override
    public Contato findById(Integer id) throws queryFailedException{
        PreparedStatement stmt = null;
        ResultSet result = null;
        Contato contato = null;
        String sql = "SELECT * FROM contato WHERE id = ?";
        try {

            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            result = stmt.executeQuery();
            if(result.next()) {
                contato = new Contato();
                contato.setId(result.getInt("id"));
                contato.setNome(result.getString("nome"));
                contato.setEmail(result.getString(("email")));
                contato.setTelefone(result.getString("telefone"));
                return contato;
            }
            throw new queryFailedException("Erro ao selecionar a contato com id: " + id);
        } catch (SQLException e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
        } finally {
            dbConnection.closeResultSet(result);
            dbConnection.closeStatement(stmt);
        }
        return null;
    }

    @Override
    public List<Contato> findAll() {
        Statement stmt = null;
        ResultSet result = null;
        List<Contato> listaContatos = new ArrayList<>();
        String sql = "SELECT id, nome, email, telefone FROM contato";
        try {
            stmt = connection.createStatement();
            result = stmt.executeQuery(sql);
            if(result.next()) {
                do {
                    Contato contato = new Contato();
                    contato.setId(result.getInt("id"));
                    contato.setNome(result.getString("nome"));
                    contato.setEmail(result.getString(("email")));
                    contato.setTelefone(result.getString("telefone"));
                    listaContatos.add(contato);
                } while(result.next());
            }
            if(listaContatos.isEmpty()) {
                System.out.println("Lista vazia!");
                return Collections.emptyList();
            }
            return listaContatos;
        } catch(SQLException e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
        } finally {
            dbConnection.closeResultSet(result);
            dbConnection.closeStatement(stmt);
        }
        System.out.println("Lista vazia!");
        return Collections.emptyList();
    }

}
