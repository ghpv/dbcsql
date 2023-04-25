table car=auto
{
	car_id = auto_kood INTEGER;
	name = nimetus VARCHAR;
	model = mudel VARCHAR;
	release_year = valjalaske_aasta SMALLINT;
	reg_number = reg_number VARCHAR;
	seat_count = istekohtade_arv SMALLINT;
	engine_volume = mootori_maht NUMERIC;
	vin_code = vin_kood VARCHAR;
	reg_time = reg_aeg TIMESTAMP;
	registrator_id = registreerija_id BIGINT;
	car_fuel_type_id = auto_kytuse_liik_kood SMALLINT;
	car_status_enum_id = auto_seisundi_liik_kood SMALLINT;
	car_brand_id = auto_mark_kood SMALLINT;
};

identifier for auto is auto_kood;

table car_status_enum=auto_seisundi_liik
{
	car_status_id=auto_seisundi_liik_kood SMALLINT;
	name=nimetus VARCHAR;
};

connection between auto and auto_seisundi_liik
{
	auto_seisundi_liik_kood = auto_seisundi_liik_kood;
};

table car_category=auto_kategooria
{
	car_category_id=auto_kategooria_kood SMALLINT;
	car_category_type_id=auto_kategooria_tyyp_kood SMALLINT;
	name=nimetus VARCHAR;
};

table car_category_type=auto_kategooria_tyyp
{
	car_category_type_id = auto_kategooria_tyyp_kood SMALLINT;
	name = nimetus VARCHAR;
};

connection between auto_kategooria_tyyp and auto_kategooria
{
	auto_kategooria_tyyp_kood = auto_kategooria_tyyp_kood;
};

table car_category_ownership=auto_kategooria_omamine
{
	car_id = auto_kood INTEGER;
	car_category_id = auto_kategooria_kood SMALLINT;
};

identifier for auto_kategooria_omamine is auto_kood;

connection between auto and auto_kategooria_omamine
{
	auto_kood = auto_kood;
};

connection between auto_kategooria and auto_kategooria_omamine
{
	auto_kategooria_kood = auto_kategooria_kood;
};

table car_fuel_type = auto_kytuse_liik
{
	car_fuel_type_id = auto_kytuse_liik_kood SMALLINT;
	name = nimetus VARCHAR;
};

connection between auto and auto_kytuse_liik
{
	auto_kytuse_liik_kood = auto_kytuse_liik_kood;
};

table car_brand = auto_mark
{
	car_brand_id = auto_mark_kood SMALLINT;
	name = nimetus VARCHAR;
};

connection between auto and auto_mark
{
	auto_mark_kood = auto_mark_kood;
};

table worker = tootaja
{
	person_id = isik_id BIGINT;
};

connection between auto and tootaja
{
	registreerija_id = isik_id;
};
