package app;

import config.MongoClientProvider;
import dao.IUsuarioDAO;
import dao.UsuarioDAO;
import exception.DaoException;
import exception.EntityNotFoundException;
import java.util.List;
import model.Direccion;
import model.Usuario;
import org.bson.types.ObjectId;

/**
 *
 * @author Ricardo
 */
public class App {
      public static void main(String[] args) {
        // Inicializa Solo Una Vez
        MongoClientProvider.INSTANCE.init();

        // Usa el DAO
        IUsuarioDAO dao = new UsuarioDAO();

        try {
            // CREATE
            Usuario nuevo = new Usuario(
                    new ObjectId(), 
                    "Martin", 
                    25,
                    new Direccion("Prueba", "Obregón", "MEX"),
                    List.of("beta", "premium"),
                    null
            );
            
            ObjectId id = dao.create(nuevo);
            System.out.println("Creado con ID: " + id);

            // READ
            dao.findById(id).ifPresent(u
                    -> System.out.println("Encontrado: " + u.getNombre() + " (" + u.getEdad() + ")"));

            // UPDATE
            nuevo.setEdad(26);
            dao.update(nuevo);
            System.out.println("Actualizado a edad 26.");

            // LIST
            var all = dao.findAll();
            System.out.println("Total usuarios: " + all.size());

            // DELETE
//            dao.deleteById(id);
//            System.out.println("Eliminado con ID: " + id);

        } catch (EntityNotFoundException e) {
            System.err.println("No encontrado: " + e.getMessage());
        } catch (DaoException e) {
            System.err.println("Error DAO: " + e.getMessage());
        }
    }
}
