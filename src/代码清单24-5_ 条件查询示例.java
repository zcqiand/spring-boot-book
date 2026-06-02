@Service
public class BookQueryService {
    private final MongoTemplate mongoTemplate;

    public BookQueryService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<Book> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Book.class));
    }

    public List<Book> findByAuthor(String author) {
        Query query = Query.query(Criteria.where("author").is(author));
        return mongoTemplate.find(query, Book.class);
    }

    public List<Book> findByPriceRange(Double minPrice, Double maxPrice) {
        Query query = Query.query(
            Criteria.where("price").gte(minPrice).lte(maxPrice)
        );
        return mongoTemplate.find(query, Book.class);
    }

    public List<Book> findByTagsContaining(String tag) {
        Query query = Query.query(Criteria.where("tags").contains(tag));
        return mongoTemplate.find(query, Book.class);
    }

    public Book findByIsbn(String isbn) {
        Query query = Query.query(Criteria.where("isbn").is(isbn));
        return mongoTemplate.findOne(query, Book.class);
    }
}