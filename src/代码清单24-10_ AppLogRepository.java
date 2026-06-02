@Repository
public class AppLogRepository {
    private final MongoTemplate mongoTemplate;

    public AppLogRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public AppLog save(AppLog log) {
        if (log.getTimestamp() == null) {
            log.setTimestamp(LocalDateTime.now());
        }
        return mongoTemplate.insert(log);
    }

    public List<AppLog> findByServiceAndLevel(String serviceName, String level) {
        Query query = Query.query(
            Criteria.where("serviceName").is(serviceName)
                    .and("level").is(level)
        );
        return mongoTemplate.find(query, AppLog.class);
    }

    public List<AppLog> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        Query query = Query.query(
            Criteria.where("timestamp").gte(start).lte(end)
        );
        return mongoTemplate.find(query, AppLog.class);
    }

    public Page<AppLog> findByLevelPaged(String level, int page, int size) {
        Query query = Query.query(Criteria.where("level").is(level));
        long total = mongoTemplate.count(query, AppLog.class);
        query.skip(page * size).limit(size);
        List<AppLog> logs = mongoTemplate.find(query, AppLog.class);
        return new PageImpl<>(logs, PageRequest.of(page, size), total);
    }
}