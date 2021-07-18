
package org.kennethvalladares.db;

import java.sql.Connection;
import com.mysql.jdbc.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author luis valladares
 */
public class Conexion {
        private Connection conexion;
        private static Conexion instancia;
        
        
        public Conexion() {
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conexion = DriverManager.getConnection("jdbc:mysql://localhost/DBTonysKinal2016277?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root" ,"admin");
                //conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBTonysKinal2016277?useSSL=false", "root" ,"admin");
                        
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }catch(InstantiationException e){
                e.printStackTrace();
            }catch(IllegalAccessException e){
                e.printStackTrace();
            }catch(SQLException e){
                e.printStackTrace();
        }
     }

        
        
        public static Conexion getInstance() {
            if(instancia == null){
                instancia = new Conexion();
            }
                return instancia;
        }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
        
}

