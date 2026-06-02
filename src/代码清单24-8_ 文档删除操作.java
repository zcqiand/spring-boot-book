@Service
public class BookDeleteService {
    private final MongoTemplate mongoTemplate;

    public BookDeleteService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public boolean deleteById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        DeleteResult result = mongoTemplate.remove(query, Book.class);
        return result.getDeletedCount() > 0;
    }

    public long deleteByAuthor(String author) {
        Query query = Query.query(Criteria.where("author").is(author));
        DeleteResult result = mongoTemplate.remove(query, Book.class);
        return result.getDeletedCount();
    }
}