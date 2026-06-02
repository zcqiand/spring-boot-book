// 业务层定义接口（属于业务层）
public interface EquipmentRepository {
    Equipment findById(Long id);
    List<Equipment> findByLabId(Long labId);
    void save(Equipment equipment);
}

// 数据访问层实现接口（属于数据访问层）
@Repository
public class MyBatisEquipmentRepository implements EquipmentRepository {
    @Override
    public Equipment findById(Long id) { /* ... */ }
    @Override
    public List<Equipment> findByLabId(Long labId) { /* ... */ }
    @Override
    public void save(Equipment equipment) { /* ... */ }
}

// 业务层使用接口（不依赖具体实现）
@Service
public class EquipmentService {
    private final EquipmentRepository repository;
    
    public EquipmentService(EquipmentRepository repository) {
        this.repository = repository;
    }
    
    public Equipment getEquipment(Long id) {
        return repository.findById(id);
    }
}