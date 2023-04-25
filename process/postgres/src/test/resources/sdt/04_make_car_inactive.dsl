operation make_car_inactive
{
	p_car_id;
}
preconditions
{
	exists car_status_enum new_status (name in ('Mitteaktiivne'));
	exists car_status_enum old_status (name in ('Aktiivne'));
	exists car a (car_id=p_car_id);
	connection between a and old_status;
}
postconditions
{
	updated a
	{
		link with new_status;
	};
}
