table x
{
	x_code  INTEGER;
	y_code  SMALLINT;
	z SMALLINT;
};
identifier for x is x_code;
table state=y
{
	y_code SMALLINT;
	state_name=y_name VARCHAR;
};
identifier for y is y_code;
connection between x and y
{
	y_code = y_code;
};
