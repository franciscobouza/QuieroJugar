package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import daos.exceptions.ErrorDeConexionException;
import daos.exceptions.YaExisteException;

public class DaoUsuario {
	public int addUsuario(String user, String password, String nombre) throws ErrorDeConexionException, YaExisteException
	{
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps;
			
			ps = conn.prepareStatement("SELECT 1 FROM Usuario WHERE UPPER(user) = ?");
			ps.setString(1,user.toUpperCase());
			ResultSet rs = ps.executeQuery();
			boolean existe = rs.next();
			ps.close();			
			if(existe)
			{
				conn.close();
				throw new YaExisteException("Ya existe un usuario con el user = '"+user+"'");
			}
			
			ps = conn.prepareStatement("INSERT INTO Usuario (user, password, nombre, fechaCreacion) values (?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1,user);
			ps.setString(2,password);
			ps.setString(3,nombre);
			ps.setTimestamp(4, new Timestamp(new Date().getTime()));
			int res = ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			int idUsuario = rs.getInt(1);
			ps.close();
			conn.close();
			if(res == 0)
				throw new ErrorDeConexionException("Se produjo un error al tratar de agregar una Usuario. No se modificó a la BD.");
			else
				return idUsuario;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de registrar un usuario con user = '"+user+"' y password = '"+password+"'", e);
		}
	}
	
	public int attemptToLogin(String user, String password) throws ErrorDeConexionException{
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT id FROM Usuario WHERE UPPER(user) = ? and UPPER(password) = ?");
			ps.setString(1,user.toUpperCase());
			ps.setString(2,password.toUpperCase());
			ResultSet rs = ps.executeQuery();
			boolean existe = rs.next();
			int idUsuario = -1;
			if(existe)
				idUsuario = rs.getInt(1);
			conn.close();
			return idUsuario;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de realizar un login con user = '"+user+"' y password = '"+password+"'", e);
		}
	}
}
