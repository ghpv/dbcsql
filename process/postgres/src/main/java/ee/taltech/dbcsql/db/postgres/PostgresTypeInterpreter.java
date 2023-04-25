package ee.taltech.dbcsql.db.postgres;

import java.util.Map;

import ee.taltech.dbcsql.core.db.TypeInterpreter;

public class PostgresTypeInterpreter extends TypeInterpreter
{
	private static final Map<String, String> MAPPING = Map.ofEntries(
		Map.entry("array", "ARRAY"),
		Map.entry("bigint", "BIGINT"),
		Map.entry("bigserial", "BIGSERIAL"),
		Map.entry("bit varying", "VARBIT"),
		Map.entry("bit", "BIT"),
		Map.entry("bool", "BOOLEAN"),
		Map.entry("boolean", "BOOLEAN"),
		Map.entry("box", "BOX"),
		Map.entry("bytea", "BYTE_ARRAY"),
		Map.entry("character varying", "VARCHAR"),
		Map.entry("varchar", "VARCHAR"),
		Map.entry("character", "CHAR"),
		Map.entry("cidr", "CIDR"),
		Map.entry("circle", "CIRCLE"),
		Map.entry("date", "DATE"),
		Map.entry("double precision", "DOUBLE"),
		Map.entry("double", "DOUBLE"),
		Map.entry("float", "FLOAT"),
		Map.entry("float4", "FLOAT"),
		Map.entry("float8", "DOUBLE"),
		Map.entry("inet", "INET"),
		Map.entry("int", "INTEGER"),
		Map.entry("int2", "SHORT"),
		Map.entry("int4", "INTEGER"),
		Map.entry("int8", "BIGINT"),
		Map.entry("integer", "INTEGER"),
		Map.entry("interval", "INTERVAL"),
		Map.entry("json", "JSON"),
		Map.entry("jsonb", "JSON_BINARY"),
		Map.entry("line", "LINE"),
		Map.entry("long", "BIGINT"),
		Map.entry("lseg", "LINE_SEGMENT"),
		Map.entry("macaddr", "MAC_ADDR"),
		Map.entry("macaddr8", "MAC_ADDR_64"),
		Map.entry("money", "MONEY"),
		Map.entry("numeric", "NUMERIC"),
		Map.entry("path", "PATH"),
		Map.entry("point", "POINT"),
		Map.entry("polygon", "POLYGON"),
		Map.entry("real", "FLOAT"),
		Map.entry("serial", "SERIAL"),
		Map.entry("serial2", "SMALLSERIAL"),
		Map.entry("serial8", "BIGSERIAL"),
		Map.entry("short", "SMALLINT"),
		Map.entry("smallint", "SMALLINT"),
		Map.entry("smallserial", "SMALLSERIAL"),
		Map.entry("text", "TEXT"),
		Map.entry("time without time zone", "TIME"),
		Map.entry("time", "TIME"),
		Map.entry("timestamp with time zone", "TIMESTAMP"),
		Map.entry("timestamp without time zone", "TIMESTAMP"),
		Map.entry("timestamp", "TIMESTAMP"),
		Map.entry("uuid", "UUID"),
		Map.entry("varbit", "VARBIT"),
		Map.entry("xml", "XML")
	);

	public PostgresTypeInterpreter()
	{
		super(MAPPING);
	}
}
