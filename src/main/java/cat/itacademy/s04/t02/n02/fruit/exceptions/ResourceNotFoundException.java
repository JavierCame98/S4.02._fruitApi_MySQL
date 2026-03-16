package cat.itacademy.s04.t02.n02.fruit.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Object id) {
        super(String.format("%s not found with id: %s ", resourceName, id));
    }
}
