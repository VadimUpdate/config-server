
package com.study.config

import org.springframework.cloud.config.environment.Environment
import org.springframework.cloud.config.environment.PropertySource
import org.springframework.cloud.config.server.environment.EnvironmentRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class DatabaseEnvironmentRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : EnvironmentRepository {

    override fun findOne(application: String, profile: String?, label: String?): Environment {
        val env = Environment(application, profile ?: "default")

        val sql = """
            SELECT setting_id, value
            FROM user_setting_value
            WHERE user_id = :userId
        """

        val params = mapOf("userId" to 24)

        val settings = jdbcTemplate.query(sql, params) { rs, _ ->
            rs.getString("setting_id") to rs.getString("value")
        }.toMap()

        env.add(PropertySource("database", settings))
        return env
    }
}
