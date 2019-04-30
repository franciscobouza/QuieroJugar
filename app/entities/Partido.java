package entities;

import java.util.List;

public class Partido {
	private int id;
	private String nombre;
	private Cancha cancha;
	private int cantJugadores;
	private List<String> jugadores;
	public Partido(int id, String nombre, Cancha cancha, int cantJugadores, List<String> jugadores) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.cancha = cancha;
		this.cantJugadores = cantJugadores;
		this.jugadores = jugadores;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Cancha getCancha() {
		return cancha;
	}
	public void setCancha(Cancha cancha) {
		this.cancha = cancha;
	}
	public int getCantJugadores() {
		return cantJugadores;
	}
	public void setCantJugadores(int cantJugadores) {
		this.cantJugadores = cantJugadores;
	}
	public List<String> getJugadores() {
		return jugadores;
	}
	public void setJugadores(List<String> jugadores) {
		this.jugadores = jugadores;
	}
	@Override
	public String toString() {
		return "Partido [id=" + id + ", nombre=" + nombre + ", cancha=" + cancha + ", cantJugadores=" + cantJugadores
				+ ", jugadores=" + jugadores + "]";
	}

}
