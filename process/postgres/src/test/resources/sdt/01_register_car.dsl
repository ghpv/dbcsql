operation register_car
{
	p_car_id;
	p_name;
	p_model;
	p_release_year;
	p_reg_number;
	p_seat_count;
	p_engine_volume;
	p_vin_code;
	p_registrator_id;
	p_car_fuel_type_id;
	p_car_brand_id;
}
preconditions
{
	exists car_status_enum status (name='Ootel');
	exists car_fuel_type cft (car_fuel_type_id=p_car_fuel_type_id);
	exists car_brand cb (car_brand_id=p_car_brand_id);
	exists worker w (person_id=p_registrator_id);
}
postconditions
{
	inserted into car a
	{
		car_id = p_car_id;
		name = p_name;
		model = p_model;
		release_year = p_release_year;
		reg_number = p_reg_number;
		seat_count = p_seat_count;
		engine_volume = p_engine_volume;
		vin_code = p_vin_code;
		link with status;
		link with cft;
		link with cb;
		link with w;
	};
}
