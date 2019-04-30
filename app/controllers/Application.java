package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import daos.DaoCancha;
import daos.DaoUsuario;
import daos.exceptions.ErrorDeConexionException;
import daos.exceptions.NoExisteException;
import daos.exceptions.YaExisteException;
import entities.Cancha;
import entities.Partido;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render());
    }
    
    private static double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
	  double R = 6371; // Radius of the earth in km
	  double dLat = deg2rad(lat2-lat1);  // deg2rad below
	  double dLon = deg2rad(lon2-lon1); 
	  double a = 
	    Math.sin(dLat/2) * Math.sin(dLat/2) +
	    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
	    Math.sin(dLon/2) * Math.sin(dLon/2)
	    ; 
	  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
	  double d = R * c; // Distance in km
	  return d;
	}

	private static double deg2rad(double deg) {
	  return deg * (Math.PI/180);
	}

    private static JSONObject armarCancha(Cancha c, boolean conFotos) throws JSONException
    {
		JSONObject uno = new JSONObject();
		uno.put("nombre", c.getNombre());
		uno.put("direccion", c.getDireccion());
		uno.put("telefono", c.getTelefono());
		JSONObject dos = new JSONObject();
		dos.put("latitud", c.getLatitud());
		dos.put("longitud", c.getLongitud());
		uno.put("ubicacion", dos);
		uno.put("foto_inicial", c.getFotoInicial());
		if(conFotos)
		{
			JSONArray tres = new JSONArray(c.getFotos());
			uno.put("fotos", tres);
		}
		return uno;
    }
    

    private static JSONObject armarPartido(Partido par, boolean conJugadores) throws JSONException
    {
		JSONObject uno = new JSONObject();
		uno.put("id", par.getId());
		uno.put("nombre", par.getNombre());
		uno.put("cancha", par.getCancha().getNombre());
		uno.put("cantidad_jugadores", par.getCantJugadores());
		if(conJugadores)
		{
			JSONArray dos = new JSONArray();
			for(String jug: par.getJugadores())
				dos.put(jug);
			uno.put("jugadores", dos);
		}
		
		return uno;
    }
    
    public static Result inscribirseAPartido() {
    	try {
			JSONObject uno = new JSONObject();
    		DynamicForm form = Form.form().bindFromRequest();
    		int idUsuario = Integer.parseInt(form.get("idUsuario")), idPartido = Integer.parseInt(form.get("idPartido"));
			try {
				new DaoCancha().inscribirseAPartido(idUsuario, idPartido);
				uno.put("retorno", "OK");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "El idUsuario y el idPartido enviados deben ser numéricos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("El idUsuario y el idPartido enviados deben ser numéricos.");
			}
		}
    }
    
    public static Result crearPartido() {
    	try {
			JSONObject uno = new JSONObject();
    		DynamicForm form = Form.form().bindFromRequest();
    		String nombreCancha = form.get("nombreCancha"), nombrePartido = form.get("nombrePartido");
    		int idUsuario = Integer.parseInt(form.get("idUsuario"));
			try {
				int idPartido = new DaoCancha().crearPartido(idUsuario, nombreCancha, nombrePartido);
				uno.put("retorno", "OK");
				uno.put("idPartido", idPartido);
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "El idUsuario enviado debe ser numérico.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("El idUsuario enviado debe ser numérico.");
			}
		}
    }

    public static Result getPartido(int idPartido) {
    	try {
			JSONObject uno = new JSONObject();
			try {
				Partido par  = new DaoCancha().getPartido(idPartido);
				uno.put("retorno", "OK");
				uno.put("partido", armarPartido(par, true));
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException | NoExisteException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
    public static Result getPartidos(String nombreCancha) {
    	try {
			JSONObject uno = new JSONObject();
			try {
				List<Partido> partidos  = new DaoCancha().getPartidos(nombreCancha);
				uno.put("retorno", "OK");
				JSONArray arr = new JSONArray();
				for(Partido par : partidos)
				{
					arr.put(armarPartido(par, false));
				}
				uno.put("partidos", arr);
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException | NoExisteException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
    public static Result getCancha(String nombre) {
    	try {
			JSONObject uno = new JSONObject();
			try {
				Cancha can  = new DaoCancha().getCancha(nombre);
				uno.put("retorno", "OK");
				uno.put("cancha", armarCancha(can,true));
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException | NoExisteException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
    public static Result getCanchas() throws JSONException {
    	try {
			JSONObject uno = new JSONObject();
			try {
				List<Cancha> canchas  = new DaoCancha().getCanchas();
				uno.put("retorno", "OK");
				JSONArray arr = new JSONArray();
				for(Cancha can : canchas)
				{
					arr.put(armarCancha(can,false));
				}
				uno.put("canchas", arr);
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
  
    public static Result login(String user, String password) {
    	try {
			JSONObject uno = new JSONObject();
			try {
				int idUsu = new DaoUsuario().attemptToLogin(user, password);
				uno.put("resultado_login", idUsu!=-1);
				uno.put("id_usuario", idUsu);
				uno.put("retorno", "OK");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
  
    public static Result registrar() {
    	try {
    		JSONObject uno = new JSONObject();

    		DynamicForm form = Form.form().bindFromRequest();
    		String user = form.get("user"), password = form.get("password"), nombre = form.get("nombre");
    		if(user==null || password==null)
    		{
				uno.put("mensaje", "Los parámetros 'user', 'password' y 'nombre' deben ser enviados.");
				uno.put("retorno", "ERROR");
    		}
    		
			try {
				int idUsuario = new DaoUsuario().addUsuario(user, password, nombre);
				uno.put("retorno", "OK");
				uno.put("idUsuario", idUsuario);
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());

			} catch (ErrorDeConexionException | YaExisteException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
    		try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
    public static Result noSeSiSeraDemasiado(double latO, double longO) { 
	    try {
			JSONObject uno = new JSONObject();
			try {
				List<Cancha> canchas  = new DaoCancha().getCanchas();
				uno.put("retorno", "OK");
				JSONArray arr = new JSONArray();
				List<JSONObject> preList = new ArrayList<JSONObject>();
				for(Cancha can : canchas)
				{
					JSONObject cancha = armarCancha(can,false);
					cancha.put("distancia", getDistanceFromLatLonInKm(latO, longO, can.getLatitud(), can.getLongitud()));
					preList.add(cancha);
				}
				Collections.sort(preList, new Comparator<JSONObject>() {
			        @Override
			        public int compare(JSONObject a, JSONObject b) {
			            try {
							return (int) Math.signum(a.getDouble("distancia") - b.getDouble("distancia"));
						} catch (JSONException e) {
							return 0;
						}
			        }
			    });
				for(JSONObject jOb : preList)
					arr.put(jOb);
				uno.put("canchas", arr);
				response().setHeader("Access-Control-Allow-Origin", "*");
				return ok(uno.toString());
			} catch (ErrorDeConexionException e) {
				e.printStackTrace();
				uno.put("mensaje", e.getMessage());
				uno.put("retorno", "ERROR");
				response().setHeader("Access-Control-Allow-Origin", "*");
				return internalServerError(uno.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response().setHeader("Access-Control-Allow-Origin", "*");
			try {
				JSONObject uno = new JSONObject();
				uno.put("mensaje", "Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
				uno.put("retorno", "ERROR");
				return internalServerError(uno.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
				return internalServerError("Ocurrió un error al intentar conectarse con el WebService. Intente nuevamente en unos segundos.");
			}
		}
    }
    
}
