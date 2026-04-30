package dao.mongo;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import config.MongoClientProvider;
import dao.IProductoDAO;
import exception.DaoException;
import exception.EntityNotFoundException;
import model.Producto;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductoDAO implements IProductoDAO {

    private final MongoCollection<Producto> collection;

    public ProductoDAO() {
        // Usa MongoClientProvider para obtener la colección tipada
        this.collection = MongoClientProvider.getInstance().getCollection("productos", Producto.class);
    }

    @Override
    public void create(Producto producto) {
        try {
            if (producto.getId() == null) {
                producto.setId(new ObjectId()); // genera _id
            }
            Date now = new Date();
            producto.setCreatedAt(now); // inicializa fechas
            producto.setUpdatedAt(now);
            
            collection.insertOne(producto);
        } catch (MongoException e) {
            throw new DaoException("Error al crear el producto en MongoDB", e);
        }
    }

    @Override
    public Producto findById(ObjectId id) {
        try {
            Producto producto = collection.find(Filters.eq("_id", id)).first();
            if (producto == null) {
                throw new EntityNotFoundException("No se encontró el producto con id: " + id);
            }
            return producto;
        } catch (MongoException e) {
            throw new DaoException("Error al buscar el producto por ID", e);
        }
    }

    @Override
    public List<Producto> findAll() {
        try {
            return collection.find().into(new ArrayList<>());
        } catch (MongoException e) {
            throw new DaoException("Error al obtener todos los productos", e);
        }
    }

    @Override
    public void update(Producto producto) {
        try {
            if (producto.getId() == null) {
                throw new IllegalArgumentException("El ID del producto no puede ser nulo para actualizar");
            }
            producto.setUpdatedAt(new Date()); // actualiza updatedAt
            
            // Reemplaza el documento completo
            var result = collection.replaceOne(Filters.eq("_id", producto.getId()), producto);
            
            if (result.getMatchedCount() == 0) {
                throw new EntityNotFoundException("No se pudo actualizar. Producto no encontrado con id: " + producto.getId());
            }
        } catch (MongoException e) {
            throw new DaoException("Error al actualizar el producto", e);
        }
    }

    @Override
    public void deleteById(ObjectId id) {
        try {
            var result = collection.deleteOne(Filters.eq("_id", id));
            if (result.getDeletedCount() == 0) {
                throw new EntityNotFoundException("No se pudo eliminar. Producto no encontrado con id: " + id);
            }
        } catch (MongoException e) {
            throw new DaoException("Error al eliminar el producto", e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            collection.deleteMany(new org.bson.Document());
        } catch (MongoException e) {
            throw new DaoException("Error al vaciar la colección de productos", e);
        }
    }

    @Override
    public List<Producto> findByNombre(String nombre) {
        try {
            return collection.find(Filters.eq("nombre", nombre)).into(new ArrayList<>());
        } catch (MongoException e) {
            throw new DaoException("Error al buscar productos por nombre", e);
        }
    }

    @Override
    public List<Producto> findByCategoria(String categoria) {
        try {
            return collection.find(Filters.in("categorias", categoria)).into(new ArrayList<>());
        } catch (MongoException e) {
            throw new DaoException("Error al buscar productos por categoría", e);
        }
    }
}