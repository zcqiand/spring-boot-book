// 解决方案1: @Primary指定默认实现
@Primary
@Service
public class MySqlOrderRepository implements OrderRepository {
    // ...
}

// 解决方案2: @Qualifier显式指定
@Autowired
public OrderService(@Qualifier("oracleOrderRepository") OrderRepository repository) {
    this.repository = repository;
}