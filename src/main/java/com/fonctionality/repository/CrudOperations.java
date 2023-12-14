package com.fonctionality.repository;

import java.util.List;

public interface CrudOperations <T, I>{
    List<T> findAll();



    List<T> saveAll(List<T> toSaves);


    List<T> updateAll(List<T> toSaves);

    T save(T toSave);

    T update(T toUpdate);

    T findById(I id);

    T deleteById(I id);
}
