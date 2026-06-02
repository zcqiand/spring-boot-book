// Order（一端）使用 @OneToMany(mappedBy = "order")
// OrderItem（多端）使用 @ManyToOne
// 外键在 order_items.order_id