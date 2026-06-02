@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 多对一：一个订单属于一个用户
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}