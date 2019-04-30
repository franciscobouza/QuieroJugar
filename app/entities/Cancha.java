package entities; 

public class Cancha {
	private int id;
	private String nombre;
	private String direccion;
	private String telefono;
	private double latitud;
	private double longitud;
	private String fotoInicial;
	private String fotos;
	public Cancha(int id, String nombre, String direccion, String telefono, double latitud, double longitud, String fotoInicial, String fotos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.latitud = latitud;
		this.longitud = longitud;
		this.fotoInicial = fotoInicial;
		this.fotos = fotos;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFotoInicial() {
		return fotoInicial;
	}

	public void setFotoInicial(String fotoInicial) {
		this.fotoInicial = fotoInicial;
	}

	public String getFotos() {
		return fotos;
	}

	public void setFotos(String fotos) {
		this.fotos = fotos;
	}
	
	
}
