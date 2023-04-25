grammar RestrictionExpr;
import CommonLex;

// RULES
restrictionAtom: column=ID comp=singleTargetOperators literalOrVar #restrictionAtomSingle
	| column=ID comp=multiTargetOperators literalOrVarList #restrictionAtomMulti
;

singleTargetOperators: '='
	|'>='
	|'<='
	|'is' 'not'?
	|'<>'
	|'!='
	|'>'
	|'<'
;

multiTargetOperators:'not'? 'in'
;

literalOrVarList: literalOrVar
	| '(' literalOrVar (',' literalOrVar)* ')'
;

literalOrVar: symbol=ID // p_car_code
	| NUMBER // 5
	| STRING // 'active'
	| functionLiteral
	| '(' literalOrVar ')'
	| literalOrVar operator=('*' | '/') literalOrVar
	| literalOrVar operator=('+' | '-') literalOrVar
	| literalOrVar operator='||' literalOrVar
;

functionLiteral: ID '(' ')'
	| ID '(' literalOrVar (',' literalOrVar)* ')'
;

restrictionExpr: restrictionAtom # restrictionExprAtom
	| ('not' | '!') restrictionExpr #restrictionExprNegate
	| restrictionExpr 'and' restrictionExpr #restrictionExprAnd
	| restrictionExpr 'or' restrictionExpr #restrictionExprOr
	| '(' restrictionExpr ')' #restrictionExprNest
;
