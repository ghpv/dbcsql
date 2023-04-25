operation activate_car
{
	p_car_id;
}
preconditions
{
	exists car a (car_id=p_car_id);
	exists car_status_enum old_status (name in ('Ootel', 'Mitteaktiivne'));
	exists car_status_enum new_status (name in ('Aktiivne'));
	exists car_category_ownership cco (car_id=p_car_id);
	connection between a and cco;
	connection between a and old_status;
}
postconditions
{
	updated a
	{
		link with new_status;
	};
};
