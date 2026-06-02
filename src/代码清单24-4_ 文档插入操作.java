@Service
public class BookInsertService {
    private final MongoTemplate mongoTemplate;

    public BookInsertService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Book insertBook(Book book) {
        book.setCreatedAt(LocalDateTime.now());
        return mongoTemplate.insert(book);
    }

    public List<Book> insertBooks(List<Book> books) {
        books.forEach(book -> book.setCreatedAt(LocalDateTime.now()));
        return mongoTemplate.insert(books, Book.class);
    }
}