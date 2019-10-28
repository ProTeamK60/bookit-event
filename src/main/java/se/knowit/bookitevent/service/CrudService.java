package se.knowit.bookitevent.service;

public interface CrudService<T, ID> {
    
    T findById(ID id);
    
    T save(T object);
}
