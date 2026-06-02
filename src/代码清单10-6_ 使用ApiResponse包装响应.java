@GetMapping("/{id}")
public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable Long id) {
    BookResponse book = bookService.findById(id);
    return ResponseEntity.ok(ApiResponse.success(book));
}