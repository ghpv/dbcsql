grammar Contract;
import CommonLex, RestrictionExpr;

// RULES
init: contract;

contract: headerDecl preconditionDecl postconditionDecl commentDecl? ';'?
;

headerDecl: 'operation' func_name=ID argList
;

preconditionDecl: 'preconditions' preconditionList
;

postconditionDecl: 'postconditions' postconditionList
;

commentDecl: 'comment' STRING
;

argList: '{' (arg ';')* '}'
;

arg: alias=ID // p_car_code
;

preconditionList: '{' (precondition ';')* '}' // Semicolon separated preconditions
;

precondition: existsPrecondition
	| connectionPrecondition
;

existsPrecondition: 'exists' variableDef restrictionExpr? // exists car a(car_code = p_car_code)
;

variableDef: table=ID alias=ID
;

connectionPrecondition: 'connection' 'between' var1=ID 'and' var2=ID ('through' name=idList)? // connection between a and b
;

idList: ID (',' ID)?
;

postconditionList: '{' (postcondition ';')* '}' // Semicolon separated postconditions
;

postcondition: deletedPostcondition
	| insertedPostcondition
	| updatedPostcondition
;

deletedPostcondition: 'deleted' alias=ID ret=returnClause?
;

updatedPostcondition: 'updated' alias=ID ('{' (postconditionClause ';')* '}')? ret=returnClause?
;

insertedPostcondition: 'inserted' 'into'? variableDef ('{' (postconditionClause ';')* '}')? ret=returnClause?
;

returnClause: 'return' literalOrVar
;

postconditionClause: postconditionValue
	| postconditionLink
	| postconditionUnlink
;
postconditionValue: dsl_col_name=ID '=' value=literalOrVar
;
postconditionLink: 'link' 'with'? var=ID ('through' name=idList)?
;
postconditionUnlink: 'unlink' 'from'? var=ID ('through' name=idList)?
;
