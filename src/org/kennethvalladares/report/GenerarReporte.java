
package org.kennethvalladares.report;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperFillManager;
import java.util.Map;
import java.io.InputStream;
import net.sf.jasperreports.view.JasperViewer;
import org.kennethvalladares.db.Conexion;


/**
 *
 * @author luis valladares
 */
public class GenerarReporte  {
    public static void mostarReporte(String nombreReporte , String titulo, Map parametros){
        InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);
            try{
                JasperReport reporteMaestro = (JasperReport)JRLoader.loadObject(reporte);
                JasperPrint reporteImpreso = JasperFillManager.fillReport(reporteMaestro,parametros,Conexion.getInstance().getConexion());
                JasperViewer visor = new JasperViewer(reporteImpreso,false);
                visor.setTitle(titulo);
                visor.setVisible(true);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
}
