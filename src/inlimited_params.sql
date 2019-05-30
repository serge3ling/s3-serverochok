/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES  */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

DROP TABLE IF EXISTS "inlimited_params";
CREATE TABLE IF NOT EXISTS "inlimited_params" (
	"id" CHARACTER VARYING(255) NOT NULL primary key,
	"title" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"reference" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"comment_type" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"sentiment" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_industryown" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_industrystate" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_broadcastname" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_broadcastgenre" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_broadcastmarker" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_broadcasttext" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_alcohol" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_baby" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_sign" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_morality" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_pluralism" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_election" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_state" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_propaganda" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_lang_radio" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_europe" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_lang_tv" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_mourning" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_ukr_film" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_national_product" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_war" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_invect" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_animal" CHARACTER VARYING(255) NULL DEFAULT NULL,
	"param_enmity" CHARACTER VARYING(255) NULL DEFAULT NULL
);

/*!40000 ALTER TABLE "inlimited_params" DISABLE KEYS */;
INSERT INTO "inlimited_params" ("id", "title", "reference", "comment_type", "sentiment", "param_industryown", "param_industrystate", "param_broadcastname", "param_broadcastgenre", "param_broadcastmarker", "param_broadcasttext", "param_alcohol", "param_baby", "param_sign", "param_morality", "param_pluralism", "param_election", "param_state", "param_propaganda", "param_lang_radio", "param_europe", "param_lang_tv", "param_mourning", "param_ukr_film", "param_national_product", "param_war", "param_invect", "param_animal", "param_enmity") VALUES
	(E'543416ee-609c-460e-854e-23338f5e5324', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, E'25', NULL, NULL, NULL, NULL, NULL, NULL, NULL, E'31', E'60', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(E'f1d1bfa8-d8e2-42cd-945f-c7a1f9901188', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, E'25', NULL, NULL, NULL, NULL, NULL, NULL, NULL, E'25', E'60', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(E'15cf4da2-5292-4e8e-b9d4-bf9293dbd1b5', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, E'16', NULL, NULL, NULL, NULL, NULL, NULL, NULL, E'46', E'111', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(E'a6cf857c-4600-4c04-ba35-e4c17e02e01d', E'Poltorak', E'file.mp3', NULL, E'discussion', E'1', E'2', E'3', E'4', E'5', E'6', E'7', E'25', E'9', E'10', E'11', E'12', E'13', E'14', E'15', E'16', E'17', E'18', E'19', E'20', E'21', E'22', E'23', E'2'),
	(E'd99488fa-0726-4c0a-a5b8-47d29c8b9a39', E'Title', E'null', NULL, E'null', E'null', E'null', E'null', E'null', E'null', E'null', E'99', E'25', E'null', E'null', E'null', E'null', E'null', E'null', E'null', E'123', E'85', E'null', E'null', E'null', E'null', E'null', E'null', E'null'),
	(E'79046111-938c-475f-815b-6db33aeb38d5', E'Р—Р°РіРѕР»РѕРІРѕРє 10-55-19', E'file105519.mp3', NULL, E'joy2', E'1', E'ua', E'prg', E'genre0', E'marker0', E'none', E'99', E'25', E'null', E'null', E'null', E'null', E'null', E'null', E'null', E'30', E'61', E'null', E'null', E'null', E'null', E'null', E'null', E'null'),
	(E'1a57d188-c2b3-4dcc-9693-831937266e03', E'Р‘РµСЂР±Р°РЅРє', E'file.mp3', NULL, E'joy', E'25', E'25', E'25', E'25', E'25', E'25', E'5', E'25', E'60', E'60', E'60', E'60', E'60', E'60', E'60', E'30', E'60', E'60', E'60', E'60', E'60', E'60', E'60', E'2');
/*!40000 ALTER TABLE "inlimited_params" ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
