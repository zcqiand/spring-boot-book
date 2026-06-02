@Service
public class LogStatisticsService {
    private final MongoTemplate mongoTemplate;

    public LogStatisticsService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Map<String, Long> countByLevel(String serviceName, LocalDateTime since) {
        MatchOperation match = Aggregation.match(
            Criteria.where("serviceName").is(serviceName)
                    .and("timestamp").gte(since)
        );
        GroupOperation group = Aggregation.group("level").count().as("count");

        Aggregation aggregation = Aggregation.newAggregation(match, group);
        AggregationResults<Document> results = mongoTemplate.aggregate(
            aggregation, "app_logs", Document.class
        );

        Map<String, Long> stats = new HashMap<>();
        for (Document doc : results.getMappedResults()) {
            stats.put(doc.getString("_id"), doc.getLong("count"));
        }
        return stats;
    }

    public long deleteOldLogs(int daysToKeep) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(daysToKeep);
        Query query = Query.query(Criteria.where("timestamp").lt(cutoff));
        DeleteResult result = mongoTemplate.remove(query, AppLog.class);
        return result.getDeletedCount();
    }
}