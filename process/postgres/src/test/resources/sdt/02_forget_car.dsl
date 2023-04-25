operation forget_car
{
	p_car_id;
}
preconditions
{
	exists car c (car_id=p_car_id);
	exists car_status_enum status (name = 'Ootel');
	connection between c and status;
}
postconditions
{
	deleted c;
}
