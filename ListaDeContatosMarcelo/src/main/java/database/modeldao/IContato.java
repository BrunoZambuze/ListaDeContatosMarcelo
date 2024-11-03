package database.modeldao;

import domain.model.Contato;
import exception.queryFailedException;

import java.util.List;

public interface IContato {

    void insert(Contato contato) throws queryFailedException;
    void update(Contato contato) throws queryFailedException;
    void delete(Contato contato);
    Contato findById(Integer contatoId) throws queryFailedException;
    List<Contato> findAll();

}