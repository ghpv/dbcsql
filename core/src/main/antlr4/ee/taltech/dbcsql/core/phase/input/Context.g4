grammar Context;
import CommonLex;

// RULES
init: context;

context: (decl ';')+
;

decl: tableDecl
	| connectionDecl
	| identifierDecl
;

dbName: (schema=ID '.')? db_name=ID
;

tableDecl: 'table' dsl_name=ID ('=' db_name=dbName)? ('extends' parent_table=dbName)? '{' (columnDecl ';')* '}' // table car=auto { car_code=auto_kood INTEGER };
;

columnDecl: dsl_name=ID ('=' db_name=ID)? type=ID // car_code = auto_kood INTEGER
;

connectionDecl: 'connection' 'between' left=dbName 'and' right=dbName ('called' name=ID)?  '{' (connectedColumnsDecl ';')+ '}' // connection between car and car_fuel_type (fuel_type_id = car_fuel_type_id)
;

connectedColumnsDecl: left=ID '=' right=ID // fuel_type_id = car_fuel_type_id
;

identifierDecl: 'identifier' 'for' table=dbName 'is' column=ID
;
