@Service
public class BookPageService {
    private final MongoTemplate mongoTemplate;

    public BookPageService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Page<Book> findByAuthorPaged(String author, int page, int size) {
        Query baseQuery = Query.query(Criteria.where("author").is(author));

        long total = mongoTemplate.count(baseQuery, Book.class);

        baseQuery.skip(page * size).limit(size);
        List<Book> books = mongoTemplate.find(baseQuery, Book.class);

        return new PageImpl<>(books, PageRequest.of(page, size), total);
    }

    public Page<Book> findAllPaged(int page, int size) {
        Query query = new Query();
        long total = mongoTemplate.count(query, Book.class);

        query.skip(page * size).limit(size);
        List<Book> books = mongoTemplate.find(query, Book.class);

        return new PageImpl<>(books, PageRequest.of(page, size), total);
    }
}