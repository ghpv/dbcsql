CREATE TABLE test_status
(
	test_status_id SMALLSERIAL,
	name TEXT,
	PRIMARY KEY (test_status_id)
)
;

INSERT INTO
	test_status
VALUES
	(1, 'waiting'),
	(2, 'active'),
	(3, 'inactive'),
	(4, 'finished')
;

CREATE TABLE test
(
	id SMALLSERIAL,
	data CHARACTER VARYING(50),
	test_status_id SMALLINT DEFAULT 1,
	PRIMARY KEY (id),
	CONSTRAINT fk_test_status_id FOREIGN KEY(test_status_id) REFERENCES test_status(test_status_id) ON DELETE CASCADE
)
;

-- Don't do select * from this table, it is reserved as changeable
CREATE TABLE vardata
(
	col_text1 CHARACTER VARYING(50),
	col_int1 INTEGER,
	col_int2 INTEGER,
	col_timestamp1 TIMESTAMP WITHOUT TIME ZONE,
	col_date1 DATE,
	col_array1 INTEGER[][],
	col_short1 SMALLINT,
	col_float1 DECIMAL(5, 3)
);

CREATE TABLE test_multi
(
	id SMALLSERIAL,
	test_id1 SMALLINT,
	test_id2 SMALLINT,
	PRIMARY KEY (id),
	CONSTRAINT fk_test1 FOREIGN KEY(test_id1) REFERENCES test(id) ON DELETE CASCADE,
	CONSTRAINT fk_test2 FOREIGN KEY(test_id2) REFERENCES test(id) ON DELETE CASCADE
);

CREATE TABLE test_multi2
(
	id SMALLSERIAL,
	test_multi_id1 SMALLINT,
	test_multi_id2 SMALLINT,
	PRIMARY KEY (id),
	CONSTRAINT fk_test1 FOREIGN KEY(test_multi_id1) REFERENCES test_multi(id) ON DELETE CASCADE,
	CONSTRAINT fk_test2 FOREIGN KEY(test_multi_id2) REFERENCES test_multi(id) ON DELETE CASCADE
);

CREATE TABLE chain_a
(
	chain_a_id SERIAL,
	data TEXT,
	data2 TEXT,
	PRIMARY KEY (chain_a_id)
)
;

CREATE TABLE chain_b
(
	chain_b_id SERIAL,
	data TEXT,
	data2 TEXT,
	chain_a_id INT,
	PRIMARY KEY (chain_b_id),
	CONSTRAINT fk_chain_b FOREIGN KEY(chain_a_id) REFERENCES chain_a(chain_a_id) ON DELETE CASCADE
)
;

CREATE TABLE chain_c
(
	chain_c_id SERIAL,
	data TEXT,
	data2 TEXT,
	chain_b_id INT,
	PRIMARY KEY (chain_c_id),
	CONSTRAINT fk_chain_c FOREIGN KEY(chain_b_id) REFERENCES chain_b(chain_b_id) ON DELETE CASCADE
)
;

CREATE TABLE chain_d
(
	chain_d_id SERIAL,
	data TEXT,
	data2 TEXT,
	chain_c_id INT,
	PRIMARY KEY (chain_d_id),
	CONSTRAINT fk_chain_d FOREIGN KEY(chain_c_id) REFERENCES chain_c(chain_c_id) ON DELETE CASCADE
)
;

CREATE TABLE nc_chain_a
(
	nc_chain_a_id SERIAL,
	data TEXT,
	data2 TEXT,
	PRIMARY KEY (nc_chain_a_id)
)
;

CREATE TABLE nc_chain_b
(
	nc_chain_b_id SERIAL,
	data TEXT,
	data2 TEXT,
	nc_chain_a_id INT,
	PRIMARY KEY (nc_chain_b_id),
	CONSTRAINT fk_nc_chain_b FOREIGN KEY(nc_chain_a_id) REFERENCES nc_chain_a(nc_chain_a_id)
)
;

CREATE TABLE nc_chain_c
(
	nc_chain_c_id SERIAL,
	data TEXT,
	data2 TEXT,
	nc_chain_b_id INT,
	PRIMARY KEY (nc_chain_c_id),
	CONSTRAINT fk_nc_chain_c FOREIGN KEY(nc_chain_b_id) REFERENCES nc_chain_b(nc_chain_b_id)
)
;

CREATE TABLE nc_chain_d
(
	nc_chain_d_id SERIAL,
	data TEXT,
	data2 TEXT,
	nc_chain_c_id INT,
	PRIMARY KEY (nc_chain_d_id),
	CONSTRAINT fk_nc_chain_d FOREIGN KEY(nc_chain_c_id) REFERENCES nc_chain_c(nc_chain_c_id)
)
;

CREATE TABLE myschema.test_status
(
	test_status_id SMALLSERIAL,
	name TEXT,
	PRIMARY KEY (test_status_id)
)
;

INSERT INTO
	myschema.test_status
VALUES
	(1, 'waiting'),
	(2, 'active'),
	(3, 'inactive'),
	(4, 'finished')
;

CREATE TABLE myschema.test
(
	id SMALLSERIAL,
	data CHARACTER VARYING(50),
	test_status_id SMALLINT DEFAULT 1,
	PRIMARY KEY (id),
	CONSTRAINT fk_test_status_id FOREIGN KEY(test_status_id) REFERENCES myschema.test_status(test_status_id) ON DELETE CASCADE
)
;

CREATE TABLE "Annoying name"
(
	id SMALLSERIAL
)
;
