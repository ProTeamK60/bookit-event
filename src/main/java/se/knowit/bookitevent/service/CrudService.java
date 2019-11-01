package se.knowit.bookitevent.service;

import java.util.Optional;
import java.util.Set;

public interface CrudService<T, ID> {
    
    Optional<T> findById(ID id);
    Set<T> findAll();
    T save(T object);
}
