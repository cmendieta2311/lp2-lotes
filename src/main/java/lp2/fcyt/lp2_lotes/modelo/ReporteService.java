/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp2.fcyt.lp2_lotes.modelo;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author cmendieta
 */
public class ReporteService {
    
      public JasperPrint generar(String rutaJrxml, Map<String, Object> parametros) throws Exception {
        try (InputStream jrxml = getClass().getResourceAsStream(rutaJrxml);
             Connection con = Conexion.conectar()) {

            if (jrxml == null) {
                throw new IllegalArgumentException("No se encontró el reporte: " + rutaJrxml);
            }
            JasperReport reporte = JasperCompileManager.compileReport(jrxml);
            return JasperFillManager.fillReport(reporte,
                    parametros != null ? parametros : new HashMap<>(), con);
        }
    } 
      
     public void exportarPdf(Component parent, String rutaJrxml,
                            Map<String, Object> parametros, String nombreSugerido) throws Exception {
        JasperPrint print = generar(rutaJrxml, parametros);
        if (print.getPages().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay datos para el reporte");
            return;
        }

         JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar reporte");
        chooser.setSelectedFile(new File(nombreSugerido));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"));
        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return; // canceló
        }

        File archivo = chooser.getSelectedFile();
        if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
            archivo = new File(archivo.getAbsolutePath() + ".pdf");
        }

        JasperExportManager.exportReportToPdfFile(print, archivo.getAbsolutePath());

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(archivo);
        }
    }
}
