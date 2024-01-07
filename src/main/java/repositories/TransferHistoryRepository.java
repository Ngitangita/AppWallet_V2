package repositories;

import entitries.TransferHistory;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TransferHistoryRepository implements CrudOperations<TransferHistory, Long>{

    private final TransactionRepository transactionRepository;

    @Override
    public List<TransferHistory> findAll(){
        return null;
    }

    @Override
    public List<TransferHistory> saveAll(List<TransferHistory> toSaves){
        return null;
    }

    @Override
    public List<TransferHistory> updateAll(List<TransferHistory> toUpdates){
        return null;
    }

    @Override
    public TransferHistory save(TransferHistory toSave){
        return null;
    }

    @Override
    public TransferHistory update(TransferHistory toUpdate){
        return null;
    }

    @Override
    public TransferHistory findById(Long id){
        return null;
    }

    @Override
    public TransferHistory deleteById(Long id){
        return null;
    }
}
