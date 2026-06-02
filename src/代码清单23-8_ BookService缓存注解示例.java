@Service
public class BookService {
    private final Map<Long, Book> database = new HashMap<>();

    @Cacheable(value = "books", key = "#id")
    public Optional<Book> getBookById(Long id) {
        System.out.println(">>> 执行数据库查询，id=" + id);
        return Optional.ofNullable(database.get(id));
    }

    @CachePut(value = "books", key = "#result.id")
    public Book updateBook(Long id, String title, String author) {
        Book book = database.get(id);
        if (book != null) {
            if (title != null) book.setTitle(title);
            if (author != null) book.setAuthor(author);
        }
        return book;
    }

    @CacheEvict(value = "books", key = "#id")
    public void deleteBook(Long id) {
        database.remove(id);
    }

    @Cacheable(value = "books", key = "#isbn", unless = "#result == null")
    public Book findByIsbn(String isbn) {
        return database.values().stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }
}