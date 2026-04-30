package dao;

import model.Usuario;
import org.bson.types.ObjectId;
import java.util.List;

/**
 *
 * @author Ricardo
 */
public interface IUsuarioDAO {
    void create(Usuario usuario);
    Usuario findById(ObjectId id);
    List<Usuario> findAll();
    void update(Usuario usuario);
    void deleteById(ObjectId id);
    Usuario findByEmail(String email);
}