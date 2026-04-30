package dao;

import model.Producto;
import org.bson.types.ObjectId;
import java.util.List;

/**
 *
 * @author Ricardo
 */
public interface IProductoDAO {
    void create(Producto producto);
    Producto findById(ObjectId id);
    List<Producto> findAll();
    void update(Producto producto);
    void deleteById(ObjectId id);
    void deleteAll();
    
    List<Producto> findByNombre(String nombre);
    List<Producto> findByCategoria(String categoria);
}