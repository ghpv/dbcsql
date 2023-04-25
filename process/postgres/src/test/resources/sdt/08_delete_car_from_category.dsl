operation delete_car_from_category
{
	p_car_id;
	p_car_category_id;
}
preconditions
{
	exists car_status_enum status (name in ('Aktiivne', 'Mitteaktiivne'));
	exists car c (car_id=p_car_id);
	connection between c and status;

	exists car_category_ownership cco;
	connection between cco and c;

	exists car_category category (car_category_id = p_car_category_id);
	connection between cco and category;
}
postconditions
{
	deleted cco;
}
;
