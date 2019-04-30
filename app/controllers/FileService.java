package controllers;

import java.io.File;

import play.mvc.Controller;
import play.mvc.*;

public class FileService extends Controller {
   public static Result getFile(String file){
          File myfile = new File(System.getProperty("user.dir")+"/canchas/"+file);
          myfile.getParentFile().mkdirs();
          return ok(myfile);
   }
}