package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import daos.exceptions.ErrorDeConexionException;
import daos.exceptions.NoExisteException;
import daos.exceptions.YaExisteException;
import entities.Cancha;
import entities.Partido;

public class DaoCancha {

	public Partido getPartido(int idPartido) throws ErrorDeConexionException, NoExisteException{
		Partido par = null;
		Connection conn = new ConnectionsPool().getConnection();
		try {
//			PreparedStatement ps = conn.prepareStatement("SELECT p.nombre, p.cancha as p_cancha, ip.*, count(1) as cantidadInscriptos FROM Partido p, InscripcionPartido ip, Usuario u WHERE p.id = ip.idPartido AND ip.idPartido = ? AND u.id = ip.idUsuario");
			PreparedStatement ps = conn.prepareStatement("SELECT p.nombre, p.idCancha FROM Partido p WHERE p.id = ?");
			ps.setInt(1, idPartido);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				par = new Partido(idPartido, rs.getString("nombre"), getCancha(rs.getInt("idCancha")),0, new ArrayList<String>());
				ps.close();
				ps = conn.prepareStatement("SELECT ip.*, u.nombre FROM InscripcionPartido ip, Usuario u WHERE ip.idPartido = ? AND u.id = ip.idUsuario");
				ps.setInt(1, idPartido);
				rs = ps.executeQuery();
				while(rs.next())
				{
					par.setCantJugadores(par.getCantJugadores()+1);
					par.getJugadores().add(rs.getString("u.nombre"));
				}
			}
			else {
				ps.close();	
				throw new NoExisteException("No existe un partido de id: "+ idPartido);
			}
			conn.close();
			return par;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener el partido de id: "+idPartido, e);
		}
	}
	
	public List<Partido> getPartidos(String nombreCancha) throws ErrorDeConexionException, NoExisteException{
		List<Partido> pars = new ArrayList<Partido>();
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT p.*, (SELECT count(1) FROM InscripcionPartido ip where ip.idPartido = p.id) as cantidadInscriptos FROM Partido p, cancha c WHERE p.idCancha = c.id and UPPER(c.nombre) = ?");
			ps.setString(1, nombreCancha.toUpperCase());
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				pars.add(new Partido(rs.getInt("id"), rs.getString("nombre"), getCancha(rs.getInt("idCancha")),rs.getInt("cantidadInscriptos"), new ArrayList<String>()));
			}
			conn.close();
			return pars;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener los partidos de la cancha de nombre: "+nombreCancha, e);
		}
	}
	
	public Cancha getCancha(int idCancha) throws ErrorDeConexionException, NoExisteException{
		Cancha cancha = null;
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Cancha WHERE id = ?");
			ps.setInt(1, idCancha);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				cancha = new Cancha(rs.getInt("id"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"),rs.getDouble("latitud"),rs.getDouble("longitud"), rs.getString("foto_inicial"), rs.getString("fotos"));
			}
			else {
				ps.close();	
				throw new NoExisteException("No existe una cancha de id: "+ idCancha);
			}
			conn.close();
			return cancha;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener la cancha de id: "+idCancha, e);
		}
	}
	
	public Cancha getCancha(String nombre) throws ErrorDeConexionException, NoExisteException{
		Cancha cancha = null;
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Cancha WHERE UPPER(nombre) = ?");
			ps.setString(1, nombre.toUpperCase());
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				cancha = new Cancha(rs.getInt("id"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"),rs.getDouble("latitud"),rs.getDouble("longitud"), rs.getString("foto_inicial"), rs.getString("fotos"));
			}
			else {
				ps.close();	
				throw new NoExisteException("No existe una cancha de nombre: "+ nombre);
			}
			conn.close();
			return cancha;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener la cancha de nombre: "+nombre, e);
		}
	}
	
	public List<Cancha> getCanchas() throws ErrorDeConexionException{
		List<Cancha> canchas = new ArrayList<Cancha>();
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Cancha");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				canchas.add(new Cancha(rs.getInt("id"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"),rs.getDouble("latitud"),rs.getDouble("longitud"), rs.getString("foto_inicial"), null));
			}
			conn.close();
			return canchas;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener las canchas", e);
		}
	}

	public int crearPartido(int idUsuario, String nombreCancha, String nombrePartido) throws ErrorDeConexionException {
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps;
			ResultSet rs;
			
			ps = conn.prepareStatement("SELECT 1 from Usuario WHERE id = ?");
			ps.setInt(1, idUsuario);
			rs = ps.executeQuery();
			if(!rs.next()) {
				ps.close();				
				throw new ErrorDeConexionException("No existe un usuario con id: "+idUsuario);
			}
			else
				ps.close();
			
			ps = conn.prepareStatement("SELECT 1 from Cancha WHERE UPPER(nombre) = ?");
			ps.setString(1, nombreCancha.toUpperCase());
			rs = ps.executeQuery();
			if(!rs.next()) {
				ps.close();				
				throw new ErrorDeConexionException("No existe una cancha de nombre: "+nombreCancha);
			}
			else
				ps.close();
			
			ps = conn.prepareStatement("INSERT INTO Partido (idCancha, idUsuario, nombre, fechaCreacion) values (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1,getCancha(nombreCancha).getId());
			ps.setInt(2,idUsuario);
			ps.setString(3,nombrePartido);
			ps.setTimestamp(4, new Timestamp(new Date().getTime()));
			int res = ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			int idPartido = rs.getInt(1);
			ps.close();
			conn.close();
			if(res == 0)
				throw new ErrorDeConexionException("Se produjo un error al tratar de agregar un partido.");
			else
				return idPartido;
		} catch (SQLException | NoExisteException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de agregar un partido", e);
		}
	}

	public void inscribirseAPartido(int idUsuario, int idPartido) throws ErrorDeConexionException {
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps;
			ResultSet rs;
			
			ps = conn.prepareStatement("SELECT 1 from Usuario WHERE id = ?");
			ps.setInt(1, idUsuario);
			rs = ps.executeQuery();
			if(!rs.next()) {
				ps.close();				
				throw new ErrorDeConexionException("No existe un usuario con id: "+idUsuario);
			}
			else
				ps.close();

			ps = conn.prepareStatement("SELECT 1 from Partido WHERE id = ?");
			ps.setInt(1, idPartido);
			rs = ps.executeQuery();
			if(!rs.next()) {
				ps.close();				
				throw new ErrorDeConexionException("No existe un partido con id: "+idPartido);
			}
			else
				ps.close();

			ps = conn.prepareStatement("SELECT count(1) from InscripcionPartido WHERE idPartido = ? AND idUsuario = ?");
			ps.setInt(1, idPartido);
			ps.setInt(2, idUsuario);
			rs = ps.executeQuery();
			rs.next();
			int cant = rs.getInt(1);
			ps.close();				
			if(cant > 0)
				throw new ErrorDeConexionException("Usted ya está inscripto a dicho partido.");

			ps = conn.prepareStatement("SELECT count(1) from InscripcionPartido WHERE idPartido = ?");
			ps.setInt(1, idPartido);
			rs = ps.executeQuery();
			rs.next();
			cant = rs.getInt(1);
			ps.close();				
			if(cant >= 10)
				throw new ErrorDeConexionException("El partido ya está lleno. Tiene " + cant + " jugadores inscriptos.");
			
			ps = conn.prepareStatement("INSERT INTO InscripcionPartido (idUsuario, idPartido, fechaInscripcion) values (?,?,?)");
			ps.setInt(1,idUsuario);
			ps.setInt(2,idPartido);
			ps.setTimestamp(3, new Timestamp(new Date().getTime()));
			int res = ps.executeUpdate();
			ps.close();
			conn.close();
			if(res == 0)
				throw new ErrorDeConexionException("Se produjo un error al tratar de inscribirse a un partido.");
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de inscribirse a un partido", e);
		}
	}
}
