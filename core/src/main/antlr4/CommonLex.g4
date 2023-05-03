lexer grammar CommonLex;

fragment DIGIT: [0-9];
fragment INT: DIGIT+;

NUMBER: INT
	| INT '.' INT?
	| '.' INT
;
SINGLE_QUOTE: '\'';
SINGLE_DQUOTE: '"';
STRING: SINGLE_QUOTE ('\\\'' | ~['])* SINGLE_QUOTE;


fragment UTF8: [\p{Alnum}\p{General_Category=Other_Letter}_];
ID: UTF8*
	| SINGLE_DQUOTE UTF8 (UTF8 | ' ')* SINGLE_DQUOTE
;

// Ignored
WS: [ \t\r\n]+ -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;
LINE_COMMENT: '//' ~ [\r\n]* -> skip;
