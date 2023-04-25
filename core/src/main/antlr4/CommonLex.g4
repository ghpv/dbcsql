lexer grammar CommonLex;

fragment CHAR: [a-zA-Z];
fragment EST_ONLY_CHAR: [õäöüÕÄÖÜ];
fragment DIGIT: [0-9];
fragment INT: DIGIT+;
fragment EST_CHAR: (CHAR | EST_ONLY_CHAR);

NUMBER: ('-'|'+') NUMBER
	| INT
	| INT '.' INT?
	| '.' INT
;
SINGLE_QUOTE: '\'';
SINGLE_DQUOTE: '"';
STRING: SINGLE_QUOTE (~['])* SINGLE_QUOTE;

fragment ID_START: (EST_CHAR | '_');
fragment ID_END: (EST_CHAR | DIGIT | '_');
ID: ID_START ID_END*
	| SINGLE_DQUOTE ID_START (ID_END | ' ')* SINGLE_DQUOTE
;

// Ignored
WS: [ \t\r\n]+ -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;
LINE_COMMENT: '//' ~ [\r\n]* -> skip;
