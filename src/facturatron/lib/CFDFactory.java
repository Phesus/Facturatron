/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facturatron.lib;

import facturatron.Dominio.Configuracion;
import facturatron.facturacion.PAC.IPACService;
import facturatron.facturacion.PAC.PACContext;
import facturatron.facturacion.PAC.PACException;
import facturatron.facturacion.PAC.finkok.FinkokPACServiceImpl;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import facturatron.lib.entities.CFDv3Tron;
import facturatron.lib.entities.ComprobanteTron;
import java.net.URI;
import mx.bigdata.sat.cfdi.CFDv33;
import mx.bigdata.sat.security.KeyLoader;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.PrivateKeyLoader;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;

/**
 *
 * @author Octavio
 */
public class CFDFactory {
    public CFDv3Tron toCFDI(ComprobanteTron comprobante) throws Exception {
        return digitalizar(comprobante);
    }


    private CFDv3Tron digitalizar(ComprobanteTron comprobante) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        Boolean sellar = PACContext.instancePACService().getRequiereSellado();
        
        
        
        if(sellar)
            sellar(comprobante, ps);
        else {
            comprobante.setNoCertificado("");
            comprobante.setSello("");
            comprobante.setCadenaOriginal("");
            comprobante.setCertificado("");
        }
        
        CFDv3Tron cfdtron =  new CFDv3Tron();
        cfdtron.setComprobante(comprobante);
                
        cfdtron.setXML(baos.toString());
        System.out.println("XML Sin codificar:" + cfdtron.getXML() );
        
        cfdtron = timbrar(cfdtron);        
        CFDv33 cfdi = new CFDv33(cfdtron.getComprobante());
        baos.reset();
        ps.flush();
        if(sellar) {
            cfdi.validar();
            cfdi.verificar();
        }
        cfdi.guardar(ps);
        cfdtron.setXML(baos.toString());
        
        return cfdtron;
    }

    private void sellar(ComprobanteTron comprobante, PrintStream ps) throws Exception {
                    
        Configuracion cfg = Configuracion.getConfig();
        comprobante.setPassKey(cfg.getpassCer());
        comprobante.setURIKey(new URI("file:///"+cfg.getpathKey().replace("\\", "/")));
        comprobante.setURICer(new URI("file:///"+cfg.getpathCer().replace("\\", "/")));
            
        
        PrivateKey key = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
                new FileInputStream(new File(comprobante.getURIKey())),
                comprobante.getPassKey()
        ).getKey();

        X509Certificate cert = cert = KeyLoaderFactory.createInstance(
                KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
                new FileInputStream(new File(comprobante.getURICer()))
        ).getKey();
        
        CFDv33 cfd = new CFDv33(comprobante);
        Comprobante sellado;
        comprobante.getCadenaOriginal();
        
        sellado = cfd.sellarComprobante(key, cert);
        cfd.validar();
        cfd.verificar();
        cfd.guardar(ps);
        
        comprobante.setNoCertificado(sellado.getNoCertificado());
        comprobante.setSello(sellado.getSello());
        comprobante.setCadenaOriginal(cfd.getCadenaOriginal());
        comprobante.setCertificado(sellado.getCertificado());
    }
    
    public CFDv3Tron timbrar(CFDv3Tron cfdi) throws PACException {
        IPACService instance  = PACContext.instancePACService();
        CFDv3Tron cfdiTimbrado = instance.timbrar(cfdi);
        
        return cfdiTimbrado;
    }
    
}
