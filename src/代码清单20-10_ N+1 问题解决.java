// 方案一：@BatchSize
@OneToMany(mappedBy = "user")
@BatchSize(size = 10)
private List<Order> orders;

// 方案二：EntityGraph
@EntityGraph(attributePaths = {"orders"})
List<User> findAll();

// 方案三：JOIN FETCH
@Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
List<User> findAllWithOrders();