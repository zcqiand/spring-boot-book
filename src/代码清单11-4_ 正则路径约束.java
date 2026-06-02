// 只匹配数字ID：/users/123 ✓  /users/abc ✗
@GetMapping("/users/{id:[0-9]+}")
public User getUserByNumericId(@PathVariable Long id) {
    return userService.findById(id);
}

// 只匹配字母代码：/products/ABC ✓  /products/123 ✗
@GetMapping("/products/{code:[a-zA-Z]+}")
public Product getProductByCode(@PathVariable String code) {
    return productService.findByCode(code);
}

// 匹配特定格式：ISBN
@GetMapping("/books/isbn/{isbn:\\d{3}-\\d{10}}")
public Book getBookByIsbn(@PathVariable String isbn) {
    return bookService.findByIsbn(isbn);
}