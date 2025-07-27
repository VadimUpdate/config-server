package com.study.configserver

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
            SELECT name, default_value
            FROM setting, setting_sbp
        """

        val settings = jdbcTemplate.query(sql) { rs, _ ->
            rs.getString("name") to rs.getString("default_value")
        }.toMap()

        env.add(PropertySource("database", settings))
        return env
    }
}
