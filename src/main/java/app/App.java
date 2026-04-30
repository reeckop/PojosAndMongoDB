package app;

import dao.IProductoDAO;
import dao.mongo.ProductoDAO;
import model.Producto;
import model.Proveedor;
import exception.EntityNotFoundException;
import exception.DaoException;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Ricardo
 */
public class App {
    public static void main(String[] args) {
        
        IProductoDAO productoDAO = new ProductoDAO();

        try {
            productoDAO.deleteAll();

            // Crear
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombre("Laptop Gamer");
            nuevoProducto.setPrecio(1500.50);
            nuevoProducto.setStock(10);
            nuevoProducto.setProveedor(new Proveedor("TechGlobal", "contacto@techglobal.com", "USA"));
            nuevoProducto.setCategorias(Arrays.asList("Electrónica", "Computación", "Gaming"));
            
            productoDAO.create(nuevoProducto);
            ObjectId generadoId = nuevoProducto.getId();
            System.out.println("Producto creado con ID: " + generadoId);

            // Leer id
            Producto productoLeido = productoDAO.findById(generadoId);
            System.out.println("Producto recuperado: " + productoLeido);

            // Actualizar
            productoLeido.setPrecio(1300.00);
            productoLeido.setStock(8);
            productoLeido.getCategorias().add("Oferta");
            productoDAO.update(productoLeido);
            System.out.println("Producto tras actualización: " + productoDAO.findById(generadoId));

            Producto producto2 = new Producto();
            producto2.setNombre("Mouse Inalámbrico");
            producto2.setPrecio(25.99);
            producto2.setStock(50);
            productoDAO.create(producto2);

            // Listar
            List<Producto> todos = productoDAO.findAll();
            for (Producto p : todos) {
                System.out.println(" - " + p);
            }

            // Eliminar por id
            System.out.println("Eliminando producto con ID: " + generadoId);
            productoDAO.deleteById(generadoId);
            System.out.println("Productos después de eliminar: " + productoDAO.findAll().size());

            // Probar exceepciones
            productoDAO.findById(generadoId);

        } catch (EntityNotFoundException | DaoException e) {
            System.err.println("Excepcion capturada: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }        
    }
}