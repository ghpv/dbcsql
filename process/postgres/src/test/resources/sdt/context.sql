CREATE DOMAIN public.d_nimi AS character varying(50)
CONSTRAINT chk_d_nimi CHECK (((VALUE)::text !~ '^[[:space:]]*$'::text));
CREATE TABLE public.auto (
auto_kood integer NOT NULL,
nimetus character varying(50) NOT NULL,
mudel character varying(255) NOT NULL,
valjalaske_aasta smallint NOT NULL,
reg_number character varying(9) NOT NULL,
istekohtade_arv smallint NOT NULL,
mootori_maht numeric(5,1) NOT NULL,
vin_kood character varying(17) NOT NULL,
reg_aeg timestamp without time zone DEFAULT LOCALTIMESTAMP(0) NOT NULL,
registreerija_id bigint NOT NULL,
auto_kytuse_liik_kood smallint NOT NULL,
auto_seisundi_liik_kood smallint DEFAULT 1 NOT NULL,
auto_mark_kood smallint NOT NULL,
CONSTRAINT pk_auto PRIMARY KEY (auto_kood),
CONSTRAINT ak_auto_nimetus UNIQUE (nimetus),
CONSTRAINT ak_auto_vin_kood UNIQUE (vin_kood),
CONSTRAINT chk_auto_istekohtade_arv_kogus CHECK (((istekohtade_arv >= 2) AND
(istekohtade_arv <= 11))),
CONSTRAINT chk_auto_mootori_maht CHECK ((mootori_maht > (0)::numeric)),
CONSTRAINT chk_auto_mudel_pole_tyhi CHECK (((mudel)::text !~
'^[[:space:]]*$'::text)),
CONSTRAINT chk_auto_nimetus_pole_tyhi CHECK (((nimetus)::text !~
'^[[:space:]]*$'::text)),
CONSTRAINT chk_auto_reg_aeg_ajavahemik CHECK (((reg_aeg >= '2010-01-01
00:00:00'::timestamp without time zone) AND (reg_aeg < '2101-01-01
00:00:00'::timestamp without time zone))),
CONSTRAINT chk_auto_reg_number_markide_hulk CHECK
(((length((reg_number)::text) > 2) AND (length((reg_number)::text) < 9))),
CONSTRAINT chk_auto_reg_number_numbrid_tahed CHECK (((reg_number)::text ~
'^[A-Z0-9]*$'::text)),
CONSTRAINT chk_auto_valjalaske_aasta_tootmisaasta CHECK (((valjalaske_aasta
>= 2000) AND (valjalaske_aasta <= 2100))),
CONSTRAINT chk_auto_vin_kood CHECK ((length((vin_kood)::text) >= 11)),
CONSTRAINT chk_auto_vin_kood_numbrid_tahed CHECK (((vin_kood)::text ~ '^[A-Z0-9]*$'::text))
)
WITH (fillfactor='90')
;
CREATE TABLE public.auto_kategooria_omamine (
auto_kood integer NOT NULL,
auto_kategooria_kood smallint NOT NULL,
CONSTRAINT pk_auto_kategooria_omamine PRIMARY KEY (auto_kood)
)
;
CREATE TABLE public.auto_mark (
auto_mark_kood smallint NOT NULL,
nimetus character varying(50) NOT NULL,
CONSTRAINT pk_auto_mark PRIMARY KEY (auto_mark_kood),
CONSTRAINT ak_auto_mark_nimetus UNIQUE (nimetus),
CONSTRAINT chk_auto_mark_nimetus_pole_tyhi CHECK (((nimetus)::text !~
'^[[:space:]]*$'::text))
)
;
CREATE TABLE public.auto_seisundi_liik (
auto_seisundi_liik_kood smallint NOT NULL,
nimetus character varying(50) NOT NULL,
CONSTRAINT pk_auto_seisundi_liik PRIMARY KEY (auto_seisundi_liik_kood),
CONSTRAINT ak_auto_seisundi_liik_nimetus UNIQUE (nimetus),
CONSTRAINT chk_auto_seisundi_liik_nimetus_pole_tyhi CHECK
(((nimetus)::text !~ '^[[:space:]]*$'::text))
)
;
CREATE TABLE public.auto_kytuse_liik (
auto_kytuse_liik_kood smallint NOT NULL,
nimetus character varying(50) NOT NULL,
CONSTRAINT pk_auto_kytuse_liik PRIMARY KEY (auto_kytuse_liik_kood),
CONSTRAINT ak_auto_kytuse_liik_nimetus UNIQUE (nimetus),
CONSTRAINT chk_auto_kytuse_liik_nimetus_pole_tyhi CHECK (((nimetus)::text !~
'^[[:space:]]*$'::text))
)
;
CREATE TABLE public.isik (
isik_id bigint DEFAULT nextval(('isik_isik_id_seq'::text)::regclass) NOT NULL,
e_meil character varying(254) NOT NULL,
isikukood character varying(50) NOT NULL,
riik_kood character varying(3) NOT NULL,
isiku_seisundi_liik_kood smallint DEFAULT 1 NOT NULL,
reg_aeg timestamp without time zone DEFAULT LOCALTIMESTAMP(0) NOT NULL,
synni_kp date NOT NULL,
eesnimi public.d_nimi,
perenimi public.d_nimi,
elukoht character varying(500),
CONSTRAINT pk_isik PRIMARY KEY (isik_id),
CONSTRAINT ak_isik_isikukood_riik UNIQUE (isikukood, riik_kood),
CONSTRAINT chk_isik_e_meil CHECK (((e_meil)::text ~ '^.*[\@].*$'::text)),
CONSTRAINT chk_isik_eesnimi_perenimi_pole_tyhi CHECK (((eesnimi IS NOT NULL)
OR (perenimi IS NOT NULL))),
CONSTRAINT chk_isik_elukoht CHECK (((elukoht)::text ~ '(?!^\d+$)^.+$'::text)),
CONSTRAINT chk_isik_isikukood CHECK (((isikukood)::text ~* '^[\u00A0-\uD7FF\
uF900-\uFDCF\uFDF0-\uFFEFa-z0-9 \-\+\=\\\/]+$'::text)),
CONSTRAINT chk_isik_reg_aeg_ajavahemik CHECK (((reg_aeg >= '2010-01-01
00:00:00'::timestamp without time zone) AND (reg_aeg < '2101-01-01
00:00:00'::timestamp without time zone))),
CONSTRAINT chk_isik_reg_aeg_suurem CHECK ((reg_aeg >= synni_kp)),
CONSTRAINT chk_isik_synni_kp_date CHECK (((synni_kp >= '1900-01-01'::date)
AND (synni_kp <= '2100-12-31'::date)))
)
WITH (fillfactor='90')
;
CREATE TABLE public.auto_kategooria (
auto_kategooria_kood smallint NOT NULL,
auto_kategooria_tyyp_kood smallint NOT NULL,
nimetus character varying(50) NOT NULL,
CONSTRAINT pk_auto_kategooria PRIMARY KEY (auto_kategooria_kood),
CONSTRAINT ak_auto_kategooria_auto_kategooria_tyyp_kood_nimetus UNIQUE
(auto_kategooria_tyyp_kood, nimetus),
CONSTRAINT chk_auto_kategooria_nimetus_pole_tyhi CHECK (((nimetus)::text !~
'^[[:space:]]*$'::text))
)
;
CREATE TABLE public.auto_kategooria_tyyp (
auto_kategooria_tyyp_kood smallint NOT NULL,
nimetus character varying(50) NOT NULL,
CONSTRAINT pk_auto_kategooria_tyyp PRIMARY KEY (auto_kategooria_tyyp_kood),
CONSTRAINT ak_auto_kategooria_tyyp_nimetus UNIQUE (nimetus),
CONSTRAINT chk_auto_kategooria_tyyp_nimetus_pole_tyhi CHECK
(((nimetus)::text !~ '^[[:space:]]*$'::text))
)
;
CREATE TABLE public.isiku_seisundi_liik (
isiku_seisundi_liik_kood smallint NOT NULL,
nimetus character varying(50) NOT NULL,
CONSTRAINT pk_isiku_seisundi_liik PRIMARY KEY (isiku_seisundi_liik_kood),
CONSTRAINT ak_isiku_seisundi_liik_nimetus UNIQUE (nimetus),
CONSTRAINT chk_isiku_seisundi_liik_nimetus_pole_tyhi CHECK (((nimetus)::text
!~ '^[[:space:]]*$'::text))
)
;
CREATE TABLE public.kasutajakonto (
isik_id bigint NOT NULL,
parool character varying(60) NOT NULL,
on_aktiivne boolean DEFAULT true NOT NULL,
CONSTRAINT pk_kasutajakonto PRIMARY KEY (isik_id),
CONSTRAINT chk_kasutajakonto_parool_pole_tyhi CHECK (((parool)::text !~
'^[[:space:]]*$'::text))
)
WITH (fillfactor='90')
;
CREATE TABLE public.kliendi_seisundi_liik (
kliendi_seisundi_liik_kood smallint NOT NULL,
nimetus character varying(50) NOT NULL,
CONSTRAINT pk_kliendi_seisundi_liik PRIMARY KEY
(kliendi_seisundi_liik_kood),
CONSTRAINT ak_kliendi_seisundi_liik_nimetus UNIQUE (nimetus),
CONSTRAINT chk_kliendi_seisundi_liik_nimetus_pole_tyhi CHECK
(((nimetus)::text !~ '^[[:space:]]*$'::text))
)
;
CREATE TABLE public.klient (
isik_id bigint NOT NULL,
kliendi_seisundi_liik_kood smallint DEFAULT 1 NOT NULL,
on_nous_tylitamisega boolean DEFAULT false NOT NULL,
CONSTRAINT pk_klient PRIMARY KEY (isik_id)
)
WITH (fillfactor='90')
;
CREATE TABLE public.riik (
riik_kood character varying(3) NOT NULL,
nimetus character varying(50) NOT NULL,
CONSTRAINT pk_riik_ PRIMARY KEY (riik_kood),
CONSTRAINT ak_riik_nimetus UNIQUE (nimetus),
CONSTRAINT chk_riik_nimetus_pole_tyhi CHECK (((nimetus)::text !~
'^[[:space:]]*$'::text)),
CONSTRAINT chk_riik_riik_kood CHECK (((riik_kood)::text ~ '^[A-Z]{3}$'::text))
)
;
CREATE TABLE public.tootaja (
isik_id bigint NOT NULL,
tootaja_seisundi_liik_kood smallint DEFAULT 1 NOT NULL,
mentor bigint,
CONSTRAINT pk_tootaja PRIMARY KEY (isik_id),
CONSTRAINT chk_tootaja_mentor_ei_tohi_olla_iseenda_mentor CHECK ((isik_id <>
mentor))
)
WITH (fillfactor='90')
;
CREATE TABLE public.tootaja_roll (
tootaja_roll_kood smallint NOT NULL,
nimetus character varying(50) NOT NULL,
kirjeldus text,
CONSTRAINT pk_tootaja_roll PRIMARY KEY (tootaja_roll_kood),
CONSTRAINT ak_tootaja_roll_nimetus UNIQUE (nimetus),
CONSTRAINT chk_tootaja_roll_kirjeldus CHECK ((length(kirjeldus) <= 4000)),
CONSTRAINT chk_tootaja_roll_kirjeldus_pole_tyhi CHECK ((kirjeldus !~
'^[[:space:]]*$'::text)),
CONSTRAINT chk_tootaja_roll_nimetus_pole_tyhi CHECK (((nimetus)::text !~
'^[[:space:]]*$'::text))
)
WITH (fillfactor='90')
;
CREATE TABLE public.tootaja_rolli_omamine (
tootaja_rolli_omamine_id bigint DEFAULT
nextval(('tootaja_rolli_omamine_tootaja_rolli_omamine_id_seq'::text)::regclass)
NOT NULL,
isik_id bigint NOT NULL,
tootaja_roll_kood smallint NOT NULL,
alguse_aeg timestamp without time zone NOT NULL,
lopu_aeg timestamp without time zone DEFAULT 'infinity'::timestamp without
time zone NOT NULL,
CONSTRAINT pk_tootaja_rolli_omamine PRIMARY KEY (tootaja_rolli_omamine_id),
CONSTRAINT ak_tootaja_rolli_omamine UNIQUE (isik_id, tootaja_roll_kood,
alguse_aeg),
CONSTRAINT chk_tootaja_rolli_omamine_alguse_aeg CHECK (((alguse_aeg >=
'1900-01-01 00:00:00'::timestamp without time zone) AND (alguse_aeg <= '2101-01-01 00:00:00'::timestamp without time zone))),
CONSTRAINT chk_tootaja_rolli_omamine_lopu_aeg CHECK ((((lopu_aeg >= '1900-01-01 00:00:00'::timestamp without time zone) AND (lopu_aeg <= '2101-01-01 00:00:00'::timestamp without time zone)) OR (lopu_aeg = 'infinity'::timestamp without time zone))),
CONSTRAINT chk_tootaja_rolli_omamine_lopu_aeg_suurem CHECK ((lopu_aeg >
alguse_aeg))
)
;
CREATE TABLE public.tootaja_seisundi_liik (
tootaja_seisundi_liik_kood smallint NOT NULL,
nimetus character varying(50) NOT NULL,
CONSTRAINT pk_tootaja_seisundi_liik PRIMARY KEY
(tootaja_seisundi_liik_kood),
CONSTRAINT ak_tootaja_seisundi_liik_nimetus UNIQUE (nimetus),
CONSTRAINT chk_tootaja_seisundi_liik_nimetus_pole_tyhi CHECK
(((nimetus)::text !~ '^[[:space:]]*$'::text))
)
;
CREATE SEQUENCE tootaja_rolli_omamine_tootaja_rolli_omamine_id_seq INCREMENT 1 START 1
;
CREATE SEQUENCE isik_isik_id_seq INCREMENT 1 START 1
;

ALTER TABLE Tootaja_rolli_omamine ADD CONSTRAINT
FK_Tootaja_rolli_omamine_Tootaja
FOREIGN KEY (isik_id) REFERENCES Tootaja (isik_id) ON DELETE Cascade ON
UPDATE No Action
;
ALTER TABLE Tootaja_rolli_omamine ADD CONSTRAINT
FK_Tootaja_rolli_omamine_Tootaja_roll
FOREIGN KEY (tootaja_roll_kood) REFERENCES Tootaja_roll (tootaja_roll_kood)
ON DELETE No Action ON UPDATE Cascade
;
ALTER TABLE Isik ADD CONSTRAINT FK_Isik_Isiku_seisundi_liik
FOREIGN KEY (isiku_seisundi_liik_kood) REFERENCES Isiku_seisundi_liik
(isiku_seisundi_liik_kood) ON DELETE No Action ON UPDATE Cascade
;
ALTER TABLE Isik ADD CONSTRAINT FK_isikukoodi_riik
FOREIGN KEY (riik_kood) REFERENCES Riik (riik_kood) ON DELETE No Action ON
UPDATE Cascade
;
ALTER TABLE ONLY Auto ADD CONSTRAINT ak_auto_seisundi_liik_kood
EXCLUDE USING btree (reg_number WITH =) WHERE ((auto_seisundi_liik_kood =
2))
;
ALTER TABLE Auto ADD CONSTRAINT FK_Auto_Auto_mark
FOREIGN KEY (auto_mark_kood) REFERENCES Auto_mark (auto_mark_kood) ON DELETE
No Action ON UPDATE Cascade
;
ALTER TABLE Auto ADD CONSTRAINT FK_Auto_Auto_kytuse_liik
FOREIGN KEY (auto_kytuse_liik_kood) REFERENCES Auto_kytuse_liik
(auto_kytuse_liik_kood) ON DELETE No Action ON UPDATE Cascade
;
ALTER TABLE Auto ADD CONSTRAINT FK_Auto_Auto_seisundi_liik
FOREIGN KEY (auto_seisundi_liik_kood) REFERENCES Auto_seisundi_liik
(auto_seisundi_liik_kood) ON DELETE No Action ON UPDATE Cascade
;
ALTER TABLE Auto ADD CONSTRAINT FK_Auto_Tootaja
FOREIGN KEY (registreerija_id) REFERENCES Tootaja (isik_id) ON DELETE No
Action ON UPDATE No Action
;
ALTER TABLE Tootaja ADD CONSTRAINT FK_Tootaja_Tootaja
FOREIGN KEY (mentor) REFERENCES Tootaja (isik_id) ON DELETE Set Null ON
UPDATE No Action
;
ALTER TABLE Tootaja ADD CONSTRAINT FK_Tootaja_Tootaja_seisundi_liik
FOREIGN KEY (tootaja_seisundi_liik_kood) REFERENCES Tootaja_seisundi_liik
(tootaja_seisundi_liik_kood) ON DELETE No Action ON UPDATE Cascade
;
ALTER TABLE Tootaja ADD CONSTRAINT FK_Tootaja_Isik
FOREIGN KEY (isik_id) REFERENCES Isik (isik_id) ON DELETE Cascade ON UPDATE
No Action
;
ALTER TABLE Auto_kategooria ADD CONSTRAINT
FK_Auto_kategooria_auto_kategooria_tyyp
FOREIGN KEY (auto_kategooria_tyyp_kood) REFERENCES Auto_kategooria_tyyp
(auto_kategooria_tyyp_kood) ON DELETE No Action ON UPDATE Cascade
;
ALTER TABLE Klient ADD CONSTRAINT FK_Klient_Kliendi_seisundi_liik
FOREIGN KEY (kliendi_seisundi_liik_kood) REFERENCES Kliendi_seisundi_liik
(kliendi_seisundi_liik_kood) ON DELETE No Action ON UPDATE Cascade
;
ALTER TABLE Klient ADD CONSTRAINT FK_Klient_Isik
FOREIGN KEY (isik_id) REFERENCES Isik (isik_id) ON DELETE Cascade ON UPDATE
No Action
;
ALTER TABLE Auto_kategooria_omamine ADD CONSTRAINT
FK_Auto_kategooria_omamine_Auto
FOREIGN KEY (auto_kood) REFERENCES Auto (auto_kood) ON DELETE Cascade ON
UPDATE Cascade
;
ALTER TABLE Auto_kategooria_omamine ADD CONSTRAINT
FK_Auto_kategooria_omamine_Auto_kategooria
FOREIGN KEY (auto_kategooria_kood) REFERENCES Auto_kategooria
(auto_kategooria_kood) ON DELETE No Action ON UPDATE Cascade
;
ALTER TABLE Kasutajakonto ADD CONSTRAINT FK_Kasutajakonto_Isik
FOREIGN KEY (isik_id) REFERENCES Isik (isik_id) ON DELETE Cascade ON UPDATE
No Action
;
CREATE VIEW public.auto_aktiivne_mitte_aktiivne WITH (security_barrier='true')
AS
SELECT auto.auto_kood,
auto.nimetus AS auto_nimetus,
auto_seisundi_liik.nimetus AS hetke_seisund,
auto_mark.nimetus AS mark,
auto.mudel,
auto.valjalaske_aasta,
auto.reg_number,
auto.vin_kood
FROM (public.auto_seisundi_liik
JOIN (public.auto_mark
JOIN public.auto ON ((auto_mark.auto_mark_kood = auto.auto_mark_kood))) ON
((auto_seisundi_liik.auto_seisundi_liik_kood = auto.auto_seisundi_liik_kood)))
WHERE (auto_seisundi_liik.auto_seisundi_liik_kood = ANY (ARRAY[2, 3]))
;
COMMENT ON VIEW public.auto_aktiivne_mitte_aktiivne IS 'Süsteem kuvab ootel või
mitteaktiivses seisundis autode nimekirja, kus on kood, nimetus, hetkeseisundi
nimetus, mark, mudel, valjalaske_aasta, reg_number, vin_kood (OP7.1)'
;

CREATE VIEW public.auto_detailid WITH (security_barrier='true') AS
SELECT auto.auto_kood,
auto.nimetus AS auto_nimetus,
auto_mark.nimetus AS mark,
auto.mudel,
auto.valjalaske_aasta,
auto.mootori_maht,
auto_kytuse_liik.nimetus AS kytuse_liik,
auto.istekohtade_arv,
auto.reg_number,
auto.vin_kood,
auto.reg_aeg,
((COALESCE(((isik.eesnimi)::text || ' '::text), ''::text) ||
COALESCE(((isik.perenimi)::text || ' '::text), ''::text)) ||
(isik.e_meil)::text) AS registreerija,
auto_seisundi_liik.nimetus AS hetke_seisund
FROM (public.auto_mark
JOIN (public.auto_seisundi_liik
JOIN (public.auto_kytuse_liik
JOIN (public.auto
JOIN public.isik ON ((auto.registreerija_id = isik.isik_id))) ON
((auto_kytuse_liik.auto_kytuse_liik_kood = auto.auto_kytuse_liik_kood))) ON
((auto_seisundi_liik.auto_seisundi_liik_kood = auto.auto_seisundi_liik_kood)))
ON ((auto_mark.auto_mark_kood = auto.auto_mark_kood)))
;
COMMENT ON VIEW public.auto_detailid IS 'Süsteem kuvab ühe teatud auto kohta
kõik temaga seonduvad omadused.'
;
CREATE VIEW public.auto_kategooriad WITH (security_barrier='true') AS
SELECT auto_kategooria_omamine.auto_kood,
auto_kategooria.nimetus AS kategooria,
auto_kategooria_tyyp.nimetus AS kategooria_tyyp
FROM ((public.auto_kategooria_tyyp
JOIN public.auto_kategooria ON
((auto_kategooria_tyyp.auto_kategooria_tyyp_kood =
auto_kategooria.auto_kategooria_tyyp_kood)))
JOIN public.auto_kategooria_omamine ON
((auto_kategooria.auto_kategooria_kood =
auto_kategooria_omamine.auto_kategooria_kood)))
;
COMMENT ON VIEW public.auto_kategooriad IS 'Süsteem kuvab iga auto elutsükli
seisundi kohta selle seisundi koodi, nimetuse (suurtähtedega) ja hetkel selles
seisundis olevate autode arvu. Kui selles seisundis pole hetkel ühtegi autot,
siis on arv 0. Seisundid on sorteeritud autode arvu järgi kahanevalt. Kui mitmel
seisundil on samasugune autode arv, siis need on sorteeritud suurtähtedega nime
järgi tähestiku järjekorras. (OP10.1)'
;

CREATE VIEW public.autode_koondaruanne WITH (security_barrier='true') AS
SELECT auto_seisundi_liik.auto_seisundi_liik_kood,
upper((auto_seisundi_liik.nimetus)::text) AS seisundi_nimetus,
count(auto.*) AS seisundis_autode_arv
FROM (public.auto_seisundi_liik

LEFT JOIN public.auto ON ((auto_seisundi_liik.auto_seisundi_liik_kood =
auto.auto_seisundi_liik_kood)))
GROUP BY auto_seisundi_liik.auto_seisundi_liik_kood,
(upper((auto_seisundi_liik.nimetus)::text))
ORDER BY (count(auto.auto_kood)) DESC,
(upper((auto_seisundi_liik.nimetus)::text))
;
COMMENT ON VIEW public.autode_koondaruanne IS 'Süsteem kuvab iga auto elutsükli
seisundi kohta selle seisundi koodi, nimetuse (suurtähtedega) ja hetkel selles
seisundis olevate autode arvu. Kui selles seisundis pole hetkel ühtegi autot,
siis on arv 0. Seisundid on sorteeritud autode arvu järgi kahanevalt. Kui mitmel
seisundil on samasugune autode arv, siis need on sorteeritud suurtähtedega nime
järgi tähestiku järjekorras. (OP10.1)'
;

CREATE VIEW public.koik_autod WITH (security_barrier='true') AS
SELECT auto.auto_kood,
auto.nimetus AS auto_nimetus,
auto_seisundi_liik.nimetus AS hetke_seisund,
auto_mark.nimetus AS mark,
auto.mudel,
auto.valjalaske_aasta,
auto.reg_number,
auto.vin_kood
FROM (public.auto_mark
JOIN (public.auto_seisundi_liik
JOIN public.auto ON ((auto_seisundi_liik.auto_seisundi_liik_kood =
auto.auto_seisundi_liik_kood))) ON ((auto_mark.auto_mark_kood =
auto.auto_mark_kood)))
;
COMMENT ON VIEW public.koik_autod IS 'Süsteem kuvab kõigi autode nimekirja, kus
on kood, nimetus, hetkeseisundi nimetus, mark, mudel, valjalaske_aasta,
reg_number, vin_kood'
;

CREATE OR REPLACE FUNCTION f_unusta_viga() RETURNS trigger
LANGUAGE plpgsql SECURITY DEFINER
SET search_path TO 'public', 'pg_temp'
AS $$
BEGIN
RAISE EXCEPTION 'Autot kustutatakse ebakorrektses seisundis.' USING ERRCODE
= 20001;
END;
$$
;
COMMENT ON FUNCTION f_unusta_viga() IS 'Kutsub välja erandi, kui üritati
kustutada auto, mis pole seisundis "Ootel"'
;

CREATE OR REPLACE TRIGGER auto_d_unusta
BEFORE DELETE ON auto
FOR EACH ROW
WHEN (OLD.auto_seisundi_liik_kood <> 1) -- pole seisundis 'Ootel'
EXECUTE FUNCTION f_unusta_viga()
;

COMMENT ON TRIGGER auto_d_unusta ON auto IS 'Kontrollib kustutava auto
seisundit. Kui ta pole seisundis "Ootel", siis kustutamine ebaõnnestub.'
;
CREATE OR REPLACE FUNCTION f_aktiveeri_viga() RETURNS trigger
LANGUAGE plpgsql SECURITY DEFINER
SET search_path TO 'public', 'pg_temp'
AS $$
BEGIN
RAISE EXCEPTION 'Autot aktiveeriti ebakorrektses seisundis (pole Ootel ega
Mitteaktiivne).' USING ERRCODE = 20002;
RETURN NULL;
END;
$$
;
COMMENT ON FUNCTION f_aktiveeri_viga() IS 'Kutsub välja erandi, kui üritati
aktiveerida auto, mis pole seisundis "Ootel" või "Mitteaktiivne"'
;
CREATE OR REPLACE TRIGGER auto_u_aktiveeri
BEFORE UPDATE OF auto_seisundi_liik_kood ON auto
FOR EACH ROW
WHEN (
NEW.auto_seisundi_liik_kood = 2 -- kui tehakse aktiivseks
AND OLD.auto_seisundi_liik_kood NOT IN (1, 3) -- ja pole ootel või mitteaktiivne
AND OLD.auto_seisundi_liik_kood <> NEW.auto_seisundi_liik_kood -- Peab saama määrata samasugune kood
)
EXECUTE FUNCTION f_aktiveeri_viga()
;
COMMENT ON TRIGGER auto_u_aktiveeri ON auto IS 'Kontrollib aktiveeritava auto
seisundit. Kui ta pole seisundis "Ootel" või "Mitteaktiivne", siis aktiveerimine
ebaõnnestub.'
;
CREATE OR REPLACE FUNCTION f_lopeta_viga() RETURNS trigger
LANGUAGE plpgsql SECURITY DEFINER
SET search_path TO 'public', 'pg_temp'
AS $$
BEGIN
RAISE EXCEPTION 'Autot lõpetatakse ebakorrektses seisundis (pole Aktiivne
ega Mitteaktiivne).' USING ERRCODE = 20003;
RETURN NULL;
END;
$$
;
COMMENT ON FUNCTION f_lopeta_viga() IS 'Kutsub välja erandi, kui üritati
lõpetada auto, mis pole seisundis "Aktiivne" või "Mitteaktiivne"'
;
CREATE OR REPLACE TRIGGER auto_u_lopeta
BEFORE UPDATE OF auto_seisundi_liik_kood ON auto
FOR EACH ROW
WHEN (
NEW.auto_seisundi_liik_kood = 4 -- kui viiakse seisundisse "Lõpetatud"
AND OLD.auto_seisundi_liik_kood NOT IN (2, 3) -- ja pole "Aktiivne" või "Mitteaktiivne"
AND OLD.auto_seisundi_liik_kood <> NEW.auto_seisundi_liik_kood -- Peab saama määrata samasugune kood
)
EXECUTE FUNCTION f_lopeta_viga()
;
COMMENT ON TRIGGER auto_u_lopeta ON auto IS 'Kontrollib lõpetatava auto
seisundit. Kui ta pole seisundis "Aktiivne" või "Mitteaktiivne", siis lõpetamine
ebaõnnestub.'
;
INSERT INTO public.Tootaja_roll (tootaja_roll_kood, nimetus, kirjeldus) VALUES (1, 'Juhataja', 'Tööülesandeks on tagada ettevõtte töö ning strateegiliste ja taktikaliste plaanide väljatöötamine ning elluviimine.')
;
INSERT INTO public.Tootaja_roll (tootaja_roll_kood, nimetus, kirjeldus) VALUES (2, 'Autode haldur', 'Tegeleb on autode õige aegse hooldamisega ja ülalpidamisega')
;
INSERT INTO public.Tootaja_roll (tootaja_roll_kood, nimetus, kirjeldus) VALUES (3, 'Klienditeenindaja', 'Tegeleb klientidega ja väljastab sõidukeid klientidele')
;
INSERT INTO public.Tootaja_roll (tootaja_roll_kood, nimetus, kirjeldus) VALUES (4, 'Klassifikaatorite haldur', 'Haldab süsteemi klassifikaatoreid')
;
INSERT INTO public.Tootaja_roll (tootaja_roll_kood, nimetus, kirjeldus) VALUES (5, 'Autojuht', 'Tööülesandeks on transportida autosi')
;
INSERT INTO public.tootaja_seisundi_liik (tootaja_seisundi_liik_kood, nimetus) VALUES (1,'Tööl')
;
INSERT INTO public.tootaja_seisundi_liik (tootaja_seisundi_liik_kood, nimetus) VALUES (2, 'Puhkusel')
;
INSERT INTO public.tootaja_seisundi_liik (tootaja_seisundi_liik_kood, nimetus) VALUES (3, 'Haiguslehel')
;
INSERT INTO public.tootaja_seisundi_liik (tootaja_seisundi_liik_kood, nimetus) VALUES (4, 'Töösuhe peatatud')
;
INSERT INTO public.tootaja_seisundi_liik (tootaja_seisundi_liik_kood, nimetus) VALUES (5, 'Vallandatud')
;
INSERT INTO public.tootaja_seisundi_liik (tootaja_seisundi_liik_kood, nimetus) VALUES (6, 'Katseajal')
;
INSERT INTO public.isiku_seisundi_liik (isiku_seisundi_liik_kood, nimetus) VALUES (1, 'Elus')
;
INSERT INTO public.isiku_seisundi_liik (isiku_seisundi_liik_kood, nimetus) VALUES (2, 'Surnud')
;
INSERT INTO public.auto_kategooria_tyyp (auto_kategooria_tyyp_kood, nimetus) VALUES (1, 'Ruumikus')
;
INSERT INTO public.auto_kategooria_tyyp (auto_kategooria_tyyp_kood, nimetus) VALUES (2, 'Sihtgrupp')
;
INSERT INTO public.auto_kategooria (auto_kategooria_kood, auto_kategooria_tyyp_kood, nimetus) VALUES (1, 1, 'Pereauto')
;
INSERT INTO public.auto_kategooria (auto_kategooria_kood, auto_kategooria_tyyp_kood, nimetus) VALUES (2, 1, 'Väikeauto')
;
INSERT INTO public.auto_kategooria (auto_kategooria_kood, auto_kategooria_tyyp_kood, nimetus) VALUES (3, 1, 'Minibuss')
;
INSERT INTO public.auto_kategooria (auto_kategooria_kood, auto_kategooria_tyyp_kood, nimetus) VALUES (4, 2, 'Luksusauto')
;
INSERT INTO public.auto_kategooria (auto_kategooria_kood, auto_kategooria_tyyp_kood, nimetus) VALUES (5, 2, 'Linnas liiklemiseks loodud')
;
INSERT INTO public.auto_kategooria (auto_kategooria_kood, auto_kategooria_tyyp_kood, nimetus) VALUES (6, 2, 'Odav')
;
INSERT INTO public.auto_kategooria (auto_kategooria_kood, auto_kategooria_tyyp_kood, nimetus) VALUES (7, 1, 'Kaubik')
;
INSERT INTO public.auto_kytuse_liik (auto_kytuse_liik_kood, nimetus) VALUES (1, 'Bensiin')
;
INSERT INTO public.auto_kytuse_liik (auto_kytuse_liik_kood, nimetus) VALUES (2, 'Diisel')
;
INSERT INTO public.auto_kytuse_liik (auto_kytuse_liik_kood, nimetus) VALUES (3, 'Gaas')
;
INSERT INTO public.auto_kytuse_liik (auto_kytuse_liik_kood, nimetus) VALUES (4, 'Pistikhübriid')
;
INSERT INTO public.auto_kytuse_liik (auto_kytuse_liik_kood, nimetus) VALUES (5, 'Elektriauto')
;
INSERT INTO public.auto_mark (auto_mark_kood, nimetus) VALUES (1, 'Volkswagen')
;
INSERT INTO public.auto_mark (auto_mark_kood, nimetus) VALUES (2, 'Opel')
;
INSERT INTO public.auto_mark (auto_mark_kood, nimetus) VALUES (3, 'Nissan')
;
INSERT INTO public.auto_mark (auto_mark_kood, nimetus) VALUES (4, 'Tesla')
;
INSERT INTO public.auto_seisundi_liik (auto_seisundi_liik_kood, nimetus) VALUES (1, 'Ootel')
;
INSERT INTO public.auto_seisundi_liik (auto_seisundi_liik_kood, nimetus) VALUES (2, 'Aktiivne')
;
INSERT INTO public.auto_seisundi_liik (auto_seisundi_liik_kood, nimetus) VALUES (3, 'Mitteaktiivne')
;
INSERT INTO public.auto_seisundi_liik (auto_seisundi_liik_kood, nimetus) VALUES (4, 'Lõpetatud')
;
INSERT INTO public.kliendi_seisundi_liik (kliendi_seisundi_liik_kood, nimetus) VALUES (1, 'Aktiivne')
;
INSERT INTO public.kliendi_seisundi_liik (kliendi_seisundi_liik_kood, nimetus) VALUES (2, 'Mitteaktiivne')
;
INSERT INTO public.kliendi_seisundi_liik (kliendi_seisundi_liik_kood, nimetus) VALUES (3, 'Mustas nimekirjas')
;
