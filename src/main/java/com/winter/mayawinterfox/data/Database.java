package com.winter.mayawinterfox.data;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.winter.mayawinterfox.Main;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.*;

public class Database {

	private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

	/**
	 * The connection pool
	 */
	private static MysqlConnectionPoolDataSource poolingDataSource;

	/**
	 * Executes an update to the database
	 *
	 * @param sql String containing an SQL statement to be executed.
	 * @return true on success, false on failure
	 */
	@NotNull
	public static Mono<Integer> set(@Language("MariaDB") String sql, Object... params) {
		return Mono.fromCallable(() -> {
			try (Connection con = poolingDataSource.getConnection();
			     PreparedStatement statement = setStatementParams(con.prepareStatement(sql), params)) {
				return statement.executeUpdate();
			}
		});
	}

	@NotNull
	public static Flux<Row> get(@Language("MariaDB") String sql, Object... params) {
		return Flux.create(sink -> {
			try (Connection con = poolingDataSource.getConnection();
			     PreparedStatement statement = setStatementParams(con.prepareStatement(sql), params);
			     ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					Row row = new Row();
					ResultSetMetaData md = set.getMetaData();
					int columns = md.getColumnCount();
					for (int i = 1; i <= columns; i++) {
						row.addColumn(md.getColumnName(i), set.getObject(i));
					}
					sink.next(row);
				}
				sink.complete();
			} catch (SQLException e) {
				sink.error(e);
			}
		});
	}

	/**
	 * This method should not be used during normal operation
	 *
	 * @param sql The sql to execute
	 * @return True on success false on failure
	 */
	private static boolean executeUnsafe(@Language("MariaDB") String sql) {
		try (
				Connection con = poolingDataSource.getConnection();
				Statement statement = con.createStatement()
		) {
			statement.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			LOGGER.error("Caught an SQL exception!", e);
		}
		return false;
	}

	/**
	 * Sets the statement parameters for a prepared statement
	 *
	 * @param statement PreparedStatement with parameters that need to be set
	 * @param params    Array of parameters that need to be set
	 * @throws SQLException Upon failing to set a parameter
	 */
	@Contract("_, _ -> param1")
	private static PreparedStatement setStatementParams(PreparedStatement statement, @NotNull Object[] params) throws SQLException {
		for (int i = 0; i < params.length; i++) {
			if (params[i] instanceof String)
				statement.setString(i + 1, (String) params[i]);
			else if (params[i] instanceof Integer)
				statement.setInt(i + 1, (Integer) params[i]);
			else if (params[i] instanceof Boolean)
				statement.setBoolean(i + 1, (Boolean) params[i]);
			else if (params[i] instanceof Long)
				statement.setLong(i + 1, (Long) params[i]);
			else if (params[i] instanceof Double)
				statement.setDouble(i + 1, (Double) params[i]);
			else if (params[i] instanceof Date)
				statement.setDate(i + 1, (Date) params[i]);
			else if (params[i] instanceof Snowflake)
				statement.setLong(i + 1, ((Snowflake) params[i]).asLong());
			else if (params[i] instanceof Guild)
				statement.setLong(i + 1, ((Guild) params[i]).getId().asLong());
			else if (params[i] instanceof User)
				statement.setLong(i + 1, ((User) params[i]).getId().asLong());
			else
				statement.setObject(i + 1, params[i]);
		}
		return statement;
	}

	/**
	 * Fills up the connection pool with valid connections to the database
	 */
	public static void connect() {
		poolingDataSource = new MysqlConnectionPoolDataSource();
		poolingDataSource.setServerName("localhost");
		poolingDataSource.setPort(3306);
		poolingDataSource.setUser("maya");
		poolingDataSource.setPassword(Main.config.get(Main.ConfigValue.DB_PASS));
		poolingDataSource.setDatabaseName("maya");
	}

	/**
	 * Sets up the database for bot use, creating all schemas and tables required
	 *
	 * @return true upon successful setup and false on failure
	 */
	public static boolean setup() {
		return (
				executeUnsafe("CREATE TABLE IF NOT EXISTS guild(" +
						"id BIGINT PRIMARY KEY NOT NULL," +
						"newguild BOOLEAN NOT NULL DEFAULT TRUE," +
						"language VARCHAR(2) NOT NULL DEFAULT 'en'," +
						"welcomeEnabled BOOLEAN NOT NULL DEFAULT FALSE," +
						"welcomeEmbed BOOLEAN NOT NULL DEFAULT FALSE," +
						"welcomeChannel BIGINT NOT NULL DEFAULT 0," +
						"welcome TEXT NOT NULL," +
						"pm TEXT NOT NULL," +
						"lvlup BOOLEAN NOT NULL DEFAULT TRUE," +
						"premium BOOLEAN NOT NULL DEFAULT FALSE," +
						"permission BOOLEAN NOT NULL DEFAULT TRUE);")) &&
				(executeUnsafe("CREATE TABLE IF NOT EXISTS prefixes(" +
						"id BIGINT NOT NULL," +
						"prefix VARCHAR(128) NOT NULL," +
						"PRIMARY KEY(id, prefix));")) &&
				(executeUnsafe("CREATE TABLE IF NOT EXISTS autoroles(" +
						"id BIGINT NOT NULL," +
						"role BIGINT NOT NULL," +
						"PRIMARY KEY(id, role));")) &&
				(executeUnsafe("CREATE TABLE IF NOT EXISTS user(" +
						"id BIGINT PRIMARY KEY NOT NULL," +
						"description TEXT NOT NULL," +
						"level INTEGER NOT NULL DEFAULT 1," +
						"xp INTEGER NOT NULL DEFAULT 0," +
						"maxxp INTEGER NOT NULL DEFAULT 420," +
						"totalxp BIGINT NOT NULL DEFAULT 0," +
						"coins INTEGER NOT NULL DEFAULT 0," +
						"gems INTEGER NOT NULL DEFAULT 0," +
						"background INTEGER NOT NULL DEFAULT 1690," +
						"notifications BOOLEAN NOT NULL DEFAULT true," +
						"premium BOOLEAN NOT NULL DEFAULT false," +
						"premiumexpiry DATE DEFAULT null);")) &&
				(executeUnsafe("CREATE TABLE IF NOT EXISTS usergroup(" +
						"guild BIGINT NOT NULL," +
						"userid BIGINT NOT NULL," +
						"groupname VARCHAR(128) NOT NULL," +
						"PRIMARY KEY(guild, userid, groupname));")) &&
				(executeUnsafe("CREATE TABLE IF NOT EXISTS permission(" +
						"guild BIGINT NOT NULL," +
						"userid BIGINT NOT NULL," +
						"permission VARCHAR(128) NOT NULL," +
						"PRIMARY KEY(guild, userid, permission));")) &&
				(executeUnsafe("CREATE TABLE IF NOT EXISTS groups(" +
						"guild BIGINT NOT NULL," +
						"groupname VARCHAR(128) NOT NULL," +
						"permission VARCHAR(128) NOT NULL," +
						"role BIGINT DEFAULT NULL," +
						"PRIMARY KEY(guild, groupname, permission));")) &&
				(executeUnsafe("CREATE TABLE IF NOT EXISTS item(" +
						"id BIGINT NOT NULL," +
						"item INTEGER NOT NULL," +
						"PRIMARY KEY(id, item));") &&
						(executeUnsafe("CREATE TABLE IF NOT EXISTS wolf(" +
								"id BIGINT PRIMARY KEY NOT NULL," +
								"name VARCHAR(128) NOT NULL DEFAULT 'Wolf'," +
								"state INTEGER NOT NULL DEFAULT 0," +
								"happiness INTEGER NOT NULL DEFAULT 100," +
								"energy INTEGER NOT NULL DEFAULT 100," +
								"xp INTEGER NOT NULL DEFAULT 0," +
								"maxxp INTEGER NOT NULL DEFAULT 200," +
								"level INTEGER NOT NULL DEFAULT 1," +
								"hunger INTEGER NOT NULL DEFAULT 0," +
								"fedtimes INTEGER NOT NULL DEFAULT 0," +
								"lastfedtime BIGINT NOT NULL DEFAULT 0," +
								"background INTEGER NOT NULL DEFAULT 1690," +
								"hat INTEGER NOT NULL DEFAULT 0," +
								"body INTEGER NOT NULL DEFAULT 0," +
								"paws INTEGER NOT NULL DEFAULT 0," +
								"tail INTEGER NOT NULL DEFAULT 0," +
								"shirt INTEGER NOT NULL DEFAULT 0," +
								"nose INTEGER NOT NULL DEFAULT 0," +
								"eye INTEGER NOT NULL DEFAULT 0," +
								"neck INTEGER NOT NULL DEFAULT 0);")) &&
						executeUnsafe("CREATE TABLE IF NOT EXISTS permission(" +
								"id BIGINT NOT NULL," +
								"permission VARCHAR(128)," +
								"PRIMARY KEY(id, permission));")) &&
				executeUnsafe("CREATE TABLE IF NOT EXISTS rss(" +
						"feed VARCHAR(128) PRIMARY KEY NOT NULL," +
						"guild BIGINT NOT NULL," +
						"channel BIGINT NOT NULL);");
	}
}