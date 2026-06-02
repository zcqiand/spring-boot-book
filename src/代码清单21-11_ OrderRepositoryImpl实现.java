@Repository
@Transactional(readOnly = true)
public class OrderRepositoryImpl implements OrderRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Order> searchOrders(String orderNo, Long customerId,
            List<String> statuses, BigDecimal minAmount, BigDecimal maxAmount,
            LocalDateTime startTime, LocalDateTime endTime, String phonePrefix) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(orderNo))
            predicates.add(cb.equal(root.get("orderNo"), orderNo));
        if (customerId != null)
            predicates.add(cb.equal(root.get("customerId"), customerId));
        if (statuses != null && !statuses.isEmpty())
            predicates.add(root.get("status").in(statuses));
        if (minAmount != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), minAmount));
        if (maxAmount != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("amount"), maxAmount));
        if (startTime != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startTime));
        if (endTime != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endTime));
        if (StringUtils.hasText(phonePrefix))
            predicates.add(cb.like(root.get("receiverPhone"), phonePrefix + "%"));

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("createdAt")));
        return em.createQuery(query).getResultList();
    }

    @Override
    public OrderStatistics calculateStatistics(
            List<String> statuses, LocalDateTime startTime, LocalDateTime endTime) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> aggQuery = cb.createQuery(Object[].class);
        Root<Order> root = aggQuery.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();
        if (statuses != null && !statuses.isEmpty())
            predicates.add(root.get("status").in(statuses));
        if (startTime != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startTime));
        if (endTime != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endTime));

        aggQuery.multiselect(
            cb.count(root), cb.sum(root.get("amount")),
            cb.avg(root.get("amount")), cb.max(root.get("amount")), cb.min(root.get("amount"))
        );
        aggQuery.where(predicates.toArray(new Predicate[0]));
        Object[] result = em.createQuery(aggQuery).getSingleResult();
        return new OrderStatistics(
            ((Number)result[0]).longValue(),
            (BigDecimal)result[1], (BigDecimal)result[2],
            (BigDecimal)result[3], (BigDecimal)result[4]);
    }
}