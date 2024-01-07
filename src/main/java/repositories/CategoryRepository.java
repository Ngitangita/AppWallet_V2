package repositories;

import entitries.Category;

import java.util.List;

public class CategoryRepository implements CrudOperations<Category, Long> {
    @Override
    public List<Category> findAll(){
        return null;
    }

    @Override
    public List<Category> saveAll(List<Category> toSaves){
        return null;
    }

    @Override
    public List<Category> updateAll(List<Category> toUpdates){
        return null;
    }

    @Override
    public Category save(Category toSave){
        return null;
    }

    @Override
    public Category update(Category toUpdate){
        return null;
    }

    @Override
    public Category findById(Long id){
        return null;
    }

    @Override
    public Category deleteById(Long id){
        return null;
    }
}
