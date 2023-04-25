operation change_car
{
	p_car_id_old;
	p_car_id_new;
	p_name;
	p_model;
	p_release_year;
	p_reg_number;
	p_seat_count;
	p_engine_volume;
	p_vin_code;
	p_fuel_type_id;
	p_brand_id;
}
preconditions
{
	exists car_status_enum status (name in ('Ootel', 'Mitteaktiivne'));
	exists car a (car_id=p_car_id_old);
	connection between a and status;
	exists car_fuel_type akl (car_fuel_type_id=p_fuel_type_id);
	exists car_brand am (car_brand_id=p_brand_id);
}
postconditions
{
	updated a
	{
		car_id = p_car_id_new;
		name = p_name;
		model = p_model;
		release_year = p_release_year;
		reg_number = p_reg_number;
		seat_count = p_seat_count;
		engine_volume = p_engine_volume;
		vin_code = p_vin_code;
		link with akl;
		link with am;
	};
}
