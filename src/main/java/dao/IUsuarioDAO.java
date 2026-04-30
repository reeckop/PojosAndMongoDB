package dao;

import exception.DaoException;
import exception.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import model.Usuario;
import org.bson.types.ObjectId;

/**
 *
 * @author Ricardo
 */
public interface IUsuarioDAO {
    ObjectId create(Usuario entity) throws DaoException;

    Optional<Usuario> findById(ObjectId _id) throws DaoException;

    List<Usuario> findAll() throws DaoException;

    boolean update(Usuario entity) throws DaoException, EntityNotFoundException;

    boolean deleteById(ObjectId _id) throws DaoException, EntityNotFoundException;

    long deleteAll() throws DaoException;

    Optional<Usuario> findByNombre(String nombre) throws DaoException;
}