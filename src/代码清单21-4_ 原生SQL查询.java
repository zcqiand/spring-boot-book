@Query(value = """
    SELECT SUBSTRING_INDEX(email, '@', -1) AS domain,
           COUNT(*) AS cnt
    FROM t_user
    WHERE active = 1 AND email IS NOT NULL
    GROUP BY domain
    ORDER BY cnt DESC
    """, nativeQuery = true)
List<Object[]> countByEmailDomain();