package com.example.demo.controller;

@RestController
public class BookController {
    @GetMapping("/api/books/{id}")
    public String getBook(@PathVariable Long id) { return "Book ID: " + id; }

    @GetMapping("/api/books")
    public String listBooks() { return "Book List"; }
}