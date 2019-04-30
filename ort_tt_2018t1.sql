-- phpMyAdmin SQL Dump
-- version 4.7.3
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3306
-- Tiempo de generación: 30-04-2019 a las 19:54:09
-- Versión del servidor: 5.5.57-cll
-- Versión de PHP: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `ort_tt_2018t1`
--
CREATE DATABASE IF NOT EXISTS `ort_tt_2018t1` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `ort_tt_2018t1`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cancha`
--

DROP TABLE IF EXISTS `cancha`;
CREATE TABLE `cancha` (
  `id` int(11) NOT NULL,
  `nombre` varchar(64) NOT NULL,
  `direccion` varchar(64) NOT NULL,
  `telefono` varchar(32) NOT NULL,
  `latitud` double NOT NULL,
  `longitud` double NOT NULL,
  `foto_inicial` varchar(64) DEFAULT NULL,
  `fotos` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `cancha`
--

INSERT INTO `cancha` (`id`, `nombre`, `direccion`, `telefono`, `latitud`, `longitud`, `foto_inicial`, `fotos`) VALUES
(1, 'La Bombonera', 'Av. Mcal. Fco. Solano López 1646', '26131874', -34.891415, -56.125527, 'la_bombonera_1.jpg', '[\'la_bombonera_1.jpg\',\'la_bombonera_2.jpg\',\'la_bombonera_3.jpg\']'),
(2, '2 a 1', 'Comercio 2159', '25082115', -34.8823756, -56.1357099, '2_1_1.jpeg', '[\'2_1_1.jpeg\',\'2_1_2.jpeg\',\'2_1_1_3.jpeg\']'),
(3, 'Clubur Fútbol 5', 'Jose Ordeig esq. Arrospide', '26048283', -34.88095, -56.087394, 'clubur_1.jpg', '[\'clubur_1.jpg\',\'clubur_2.jpg\',\'clubur_3.jpg\']'),
(4, 'El Galpón', 'Paraguay 2211', '29248880', -34.888818, -56.194454, 'el_galpon_1.jpeg', '[\'el_galpon_1.jpeg\',\'el_galpon_2.jpeg\',\'el_galpon_3.jpeg\']'),
(5, 'Indoor 3 cruces', 'Cufré 1918', '24090740', -34.892593, -56.168783, 'indoor_1.jpeg', '[\'indoor_1.jpeg\',\'indoor_2.jpeg\',\'indoor_3.jpeg\']'),
(6, 'La Experimental', 'Estrázulas 1578', '091929235', -34.892716, -56.09867, 'la_experimental_1.jpeg', '[\'la_experimental_1.jpeg\',\'la_experimental_2.jpeg\',\'la_experimental_3.jpeg\']'),
(7, 'La Masía', 'Servando Gómez 2970', '095971877', -34.865348, -56.063211, 'la_masia_1.jpeg', '[\'la_masia_1.jpeg\',\'la_masia_2.jpeg\',\'la_masia_3.jpeg\']'),
(8, 'Urreta', 'Canstatt 2966', '24813078', -34.875862, -56.162646, 'urreta_1.jpeg', '[\'urreta_1.jpeg\',\'urreta_2.jpeg\',\'urreta_3.jpeg\']'),
(9, 'Zimon Zinko', 'Gral. Urquiza 2883', '24875917', -34.886054, -56.158794, 'zimon_1.jpeg', '[\'zimon_1.jpeg\',\'zimon_2.jpeg\',\'zimon_3.jpeg\']'),
(10, 'Yacht Club Uruguayo', 'Av. Prof. E Peluffo s/n', '26280987', -34.910851, -56.131833, 'yatch_1.jpeg', '[\'yatch_1.jpeg\',\'yatch_2.jpeg\',\'yatch_3.jpeg\']'),
(11, 'Wimbledon', 'Saldanha Da Gama 3727', '26224875', -34.901931, -56.132728, 'wimbledon_1.jpeg', '[\'wimbledon_1.jpeg\',\'wimbledon_2.jpeg\',\'wimbledon_3.jpeg\']');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inscripcionpartido`
--

DROP TABLE IF EXISTS `inscripcionpartido`;
CREATE TABLE `inscripcionpartido` (
  `id` int(11) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `idPartido` int(11) NOT NULL,
  `fechaInscripcion` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partido`
--

DROP TABLE IF EXISTS `partido`;
CREATE TABLE `partido` (
  `id` int(11) NOT NULL,
  `idCancha` int(11) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `nombre` varchar(64) NOT NULL,
  `fechaCreacion` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `user` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `nombre` varchar(64) NOT NULL,
  `fechaCreacion` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cancha`
--
ALTER TABLE `cancha`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `inscripcionpartido`
--
ALTER TABLE `inscripcionpartido`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `partido`
--
ALTER TABLE `partido`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `cancha`
--
ALTER TABLE `cancha`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT de la tabla `inscripcionpartido`
--
ALTER TABLE `inscripcionpartido`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1322;
--
-- AUTO_INCREMENT de la tabla `partido`
--
ALTER TABLE `partido`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=809;
--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=589;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
