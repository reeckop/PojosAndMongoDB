package dao;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import config.MongoClientProvider;
import exception.DaoException;
import exception.EntityNotFoundException;
import model.Usuario;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ricardo
 */
public class UsuarioDAO implements IUsuarioDAO {

    private final MongoCollection<Usuario> collection;

    public UsuarioDAO() {
        this.collection = MongoClientProvider.getInstance().getCollection("usuarios", Usuario.class);
    }

    @Override
    public void create(Usuario usuario) {
        try {
            if (usuario.getId() == null) {
                usuario.setId(new ObjectId());
            }
            collection.insertOne(usuario);
        } catch (MongoException e) {
            throw new DaoException("Error al crear el usuario en MongoDB", e);
        }
    }

    @Override
    public Usuario findById(ObjectId id) {
        try {
            Usuario usuario = collection.find(Filters.eq("_id", id)).first();
            if (usuario == null) {
                throw new EntityNotFoundException("No se encontró el usuario con id: " + id);
            }
            return usuario;
        } catch (MongoException e) {
            throw new DaoException("Error al buscar el usuario por ID", e);
        }
    }

    @Override
    public List<Usuario> findAll() {
        try {
            return collection.find().into(new ArrayList<>());
        } catch (MongoException e) {
            throw new DaoException("Error al obtener la lista de usuarios", e);
        }
    }

    @Override
    public void update(Usuario usuario) {
        try {
            if (usuario.getId() == null) {
                throw new IllegalArgumentException("El ID del usuario no puede ser nulo para actualizar");
            }
            
            var result = collection.replaceOne(Filters.eq("_id", usuario.getId()), usuario);
            
            if (result.getMatchedCount() == 0) {
                throw new EntityNotFoundException("No se pudo actualizar. Usuario no encontrado con id: " + usuario.getId());
            }
        } catch (MongoException e) {
            throw new DaoException("Error al actualizar el usuario", e);
        }
    }

    @Override
    public void deleteById(ObjectId id) {
        try {
            var result = collection.deleteOne(Filters.eq("_id", id));
            if (result.getDeletedCount() == 0) {
                throw new EntityNotFoundException("No se pudo eliminar. Usuario no encontrado con id: " + id);
            }
        } catch (MongoException e) {
            throw new DaoException("Error al eliminar el usuario", e);
        }
    }

    @Override
    public Usuario findByEmail(String email) {
        try {
            return collection.find(Filters.eq("email", email)).first();
        } catch (MongoException e) {
            throw new DaoException("Error al buscar el usuario por email", e);
        }
    }
}