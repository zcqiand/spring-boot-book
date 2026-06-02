@Service
public class UserQueryService {
    private final UserRepository userRepository;

    public List<User> flexibleSearch(UserFlexibleCriteria criteria) {
        List<Specification<User>> conditions = new ArrayList<>();

        if (criteria.getActive() != null) {
            conditions.add((root, query, cb) ->
                cb.equal(root.get("active"), criteria.getActive()));
        }
        if (StringUtils.hasText(criteria.getUsernamePrefix())) {
            conditions.add((root, query, cb) ->
                cb.like(root.get("username"), criteria.getUsernamePrefix() + "%"));
        }
        if (StringUtils.hasText(criteria.getEmailContains())) {
            conditions.add((root, query, cb) ->
                cb.like(root.get("email"), "%" + criteria.getEmailContains() + "%"));
        }

        Specification<User> combined = conditions.stream()
            .reduce(Specification::and)
            .orElse((root, query, cb) -> cb.conjunction());

        return userRepository.findAll(combined);
    }

    public Page<User> dynamicSearchPaged(UserSearchCriteria criteria,
            int page, int size, String sortField, String sortDirection) {
        Specification<User> spec = buildSpec(criteria);
        Pageable pageable = PageRequest.of(page, size,
            Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return userRepository.findAll(spec, pageable);
    }

    private Specification<User> buildSpec(UserSearchCriteria criteria) {
        return Specification.where(activeSpec(criteria.getActive()))
            .and(createdAtBetweenSpec(criteria.getStartTime(), criteria.getEndTime()))
            .and(emailContainsSpec(criteria.getEmail()));
    }
    // ... helper methods
}