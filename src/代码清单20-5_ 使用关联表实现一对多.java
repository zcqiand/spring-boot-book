@OneToMany
@JoinTable(
    name = "order_item_ref",
    joinColumns = @JoinColumn(name = "order_id"),
    inverseJoinColumns = @JoinColumn(name = "item_id")
)
private List<OrderItem> items;