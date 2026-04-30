package exception;

/**
 *
 * @author Ricardo
 */
public class EntityNotFoundException extends DaoException {
    public EntityNotFoundException(String message) { super(message); }
}
