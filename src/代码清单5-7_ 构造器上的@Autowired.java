@Service
public class ProductService {
    private final ProductRepository repository;

    @Autowired // 标注在构造器上
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }
}