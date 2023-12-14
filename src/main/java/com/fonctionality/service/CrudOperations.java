package com.fonctionality.service;

import java.util.List;

public interface CrudOperations<R, C, I>{
    List<R> findAll();



    List<R> saveAll(List<C> toSaves);


    List<R> updateAll(List<R> toSaves);

    R save(C toSave);

    R update(C toUpdate, I id);

    R findById(I id);

    R deleteById(I id);
}
