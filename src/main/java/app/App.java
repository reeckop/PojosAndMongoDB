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
        System.out.println("--- Iniciando pruebas de ProductoDAO ---");

        // Crea instancia del DAO (Esto inicializa automáticamente la conexión Singleton)
        IProductoDAO productoDAO = new ProductoDAO();

        try {
            // Limpiamos la colección antes de iniciar la prueba
            productoDAO.deleteAll();

            // 1. Crear
            System.out.println("\n[1] Creando producto...");
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombre("Laptop Gamer");
            nuevoProducto.setPrecio(1500.50);
            nuevoProducto.setStock(10);
            nuevoProducto.setProveedor(new Proveedor("TechGlobal", "contacto@techglobal.com", "USA"));
            nuevoProducto.setCategorias(Arrays.asList("Electrónica", "Computación", "Gaming"));
            
            productoDAO.create(nuevoProducto);
            ObjectId generadoId = nuevoProducto.getId();
            System.out.println("Producto creado con ID: " + generadoId);

            // 2. Leer por _id
            System.out.println("\n[2] Leyendo producto por ID...");
            Producto productoLeido = productoDAO.findById(generadoId);
            System.out.println("Producto recuperado: " + productoLeido);

            // 3. Actualizar
            System.out.println("\n[3] Actualizando producto...");
            productoLeido.setPrecio(1300.00);
            productoLeido.setStock(8);
            productoLeido.getCategorias().add("Oferta");
            productoDAO.update(productoLeido);
            System.out.println("Producto tras actualización: " + productoDAO.findById(generadoId));

            // Añadimos otro producto para la lista
            Producto producto2 = new Producto();
            producto2.setNombre("Mouse Inalámbrico");
            producto2.setPrecio(25.99);
            producto2.setStock(50);
            productoDAO.create(producto2);

            // 4. Listar todos
            System.out.println("\n[4] Listando todos los productos...");
            List<Producto> todos = productoDAO.findAll();
            for (Producto p : todos) {
                System.out.println(" - " + p);
            }

            // 5. Eliminar por _id
            System.out.println("\n[5] Eliminando producto con ID: " + generadoId);
            productoDAO.deleteById(generadoId);
            System.out.println("Total de productos después de eliminar: " + productoDAO.findAll().size());

            // Probar captura de excepciones (buscar un producto eliminado)
            System.out.println("\n[6] Intentando buscar el producto eliminado para forzar excepción...");
            productoDAO.findById(generadoId);

        } catch (EntityNotFoundException | DaoException e) {
            System.err.println("Excepción capturada correctamente: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n--- Fin de las pruebas ---");
    }
}