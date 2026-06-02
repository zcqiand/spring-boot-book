package com.xrtech.api.controller;

/**
 * 用户管理控制器
 * 资源：users
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        // POST /api/v1/users - 创建用户
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        // GET /api/v1/users/{id} - 获取用户信息
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/{id}/borrows")
    public ResponseEntity<List<BorrowResponse>> getUserBorrows(@PathVariable Long id) {
        // GET /api/v1/users/{id}/borrows - 获取用户的借阅记录
        return ResponseEntity.ok(borrowService.findByUserId(id));
    }
}

/**
 * 借阅管理控制器
 * 资源：borrows
 */
@RestController
@RequestMapping("/api/v1/borrows")
public class BorrowController {

    @PostMapping
    public ResponseEntity<BorrowResponse> createBorrow(@Valid @RequestBody CreateBorrowRequest request) {
        // POST /api/v1/borrows - 创建借阅记录
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowResponse> getBorrowById(@PathVariable Long id) {
        // GET /api/v1/borrows/{id} - 获取借阅记录
        return ResponseEntity.ok(borrowService.findById(id));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowResponse> returnBook(@PathVariable Long id) {
        // PUT /api/v1/borrows/{id}/return - 归还图书（子资源操作）
        return ResponseEntity.ok(borrowService.returnBook(id));
    }

    @GetMapping
    public ResponseEntity<List<BorrowResponse>> getBorrows(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) String status) {
        // GET /api/v1/borrows?userId=1&status=ACTIVE - 筛选借阅记录
        return ResponseEntity.ok(borrowService.findByFilters(userId, bookId, status));
    }
}