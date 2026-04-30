package dao;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import config.MongoClientProvider;
import exception.DaoException;
import exception.EntityNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Usuario;
import org.bson.types.ObjectId;

/**
 *
 * @author Ricardo
 */
public class UsuarioDAO implements IUsuarioDAO {

    private final MongoCollection<Usuario> col;

    public UsuarioDAO() {
        this.col = MongoClientProvider.INSTANCE.getCollection("usuarios", Usuario.class);
    }

    @Override
    public ObjectId create(Usuario entity) throws DaoException {
        try {
            if (entity.getId()== null) entity.setId(new ObjectId());
            entity.setCreadoEn(Instant.now());
            col.insertOne(entity);
            return entity.getId();
        } catch (MongoException e) {
            throw new DaoException("Error insertando usuario", e);
        }
    }

    @Override
    public Optional<Usuario> findById(ObjectId _id) throws DaoException {
         try {
            return Optional.ofNullable(col.find(Filters.eq("_id", _id)).first());
        } catch (MongoException e) {
            throw new DaoException("Error consultando usuario por ID", e);
        }
    }

    @Override
    public List<Usuario> findAll() throws DaoException {
         try {
            return col.find().into(new ArrayList<>());
        } catch (MongoException e) {
            throw new DaoException("Error consultando todos los usuarios", e);
        }
    }

    @Override
    public boolean update(Usuario entity) throws DaoException, EntityNotFoundException {
        try {
            var result = col.updateOne(
                Filters.eq("_id", entity.getId()),
                Updates.combine(
                    Updates.set("nombre", entity.getNombre()),
                    Updates.set("edad", entity.getEdad()),
                    Updates.set("tags", entity.getTags())
                )
            );
            if (result.getMatchedCount() == 0)
                throw new EntityNotFoundException("Usuario no encontrado: " + entity.getId());
            return result.getModifiedCount() > 0;
        } catch (MongoException e) {
            throw new DaoException("Error actualizando usuario", e);
        }
    }

    @Override
    public boolean deleteById(ObjectId _id) throws DaoException, EntityNotFoundException {
        try {
            var result = col.deleteOne(Filters.eq("_id", _id));
            if (result.getDeletedCount() == 0)
                throw new EntityNotFoundException("Usuario no encontrado: " + _id);
            return true;
        } catch (MongoException e) {
            throw new DaoException("Error eliminando usuario", e);
        }
    }

    @Override
    public long deleteAll() throws DaoException {
         try {
            return col.deleteMany(Filters.exists("_id")).getDeletedCount();
        } catch (MongoException e) {
            throw new DaoException("Error eliminando todos los usuarios", e);
        }
    }

    @Override
    public Optional<Usuario> findByNombre(String nombre) throws DaoException {
        try {
            return Optional.ofNullable(col.find(Filters.eq("nombre", nombre)).first());
        } catch (MongoException e) {
            throw new DaoException("Error consultando usuario por nombre", e);
        }
    }

}