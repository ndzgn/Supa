package iut.tp.supply;

import iut.tp.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplyRepository implements Repository<Supply> {
    private Map<String, Supply> supplies = new HashMap<>();

    @Override
    public void save(Supply supply) {
        supplies.put(supply.getId(), supply);
    }

    @Override
    public Supply findById(String id) {
        return supplies.get(id);
    }

    @Override
    public List<Supply> findAll() {
        return new ArrayList<>(supplies.values());
    }

    @Override
    public void update(Supply supply) {
        supplies.put(supply.getId(), supply);
    }

    @Override
    public void delete(String id) {
        supplies.remove(id);
    }
}
