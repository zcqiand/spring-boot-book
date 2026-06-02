@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
private List<OrderItem> items;