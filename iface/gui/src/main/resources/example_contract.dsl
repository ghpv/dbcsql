operation "delete an x"
{
	p_x_code;
}
preconditions
{
	exists x child (x_code=p_x_code and z>100);
	exists state parent (state_name = 'Inactive');
	connection between child and parent;
}
postconditions
{
	deleted child;
}
comment 'Delete an x instance if it is in the state
inactive and its z value is bigger than 100.

Contracts refer to the elements in the conceptual data model.
Conceptual data model specifies entity type state with an attribute state_name.
In the database corresponding table and column names are y and y_name, respectively.
The mapping is specified in the context description.';
