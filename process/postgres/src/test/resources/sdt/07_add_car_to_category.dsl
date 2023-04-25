operation add_car_to_category
{
	p_car_id;
	p_car_category_id;
}
preconditions
{
	exists car a (car_id=p_car_id);
	exists car_category ci (car_category_id = p_car_category_id);
	exists car_status_enum status (name in ('Aktiivne', 'Mitteaktiivne'));
	connection between a and status;
}
postconditions
{
	inserted into car_category_ownership cco
	{
		link with a;
		link with ci;
	};
}
