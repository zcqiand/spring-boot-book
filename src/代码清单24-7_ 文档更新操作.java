@Service
public class BookUpdateService {
    private final MongoTemplate mongoTemplate;

    public BookUpdateService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public boolean updateBookPrice(String id, Double newPrice) {
        Query query = Query.query(Criteria.where("id").is(id));
        Update update = Update.update("price", newPrice);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Book.class);
        return result.getModifiedCount() > 0;
    }

    public long decreaseStock(String id, int amount) {
        Query query = Query.query(Criteria.where("id").is(id)
                .and("stock").gte(amount));
        Update update = Update.update("stock", -amount);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Book.class);
        return result.getModifiedCount();
    }

    public long addTag(String id, String tag) {
        Query query = Query.query(Criteria.where("id").is(id));
        Update update = Update.update("tags", tag);
        return mongoTemplate.updateFirst(query, update, Book.class).getModifiedCount();
    }
}