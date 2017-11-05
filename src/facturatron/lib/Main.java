/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facturatron.lib;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import com.phesus.facturatron.presentation.mvc.model.RenglonModel;
import facturatron.lib.entities.CFDv3Tron;
import facturatron.lib.entities.ComprobanteTron;
import facturatron.lib.entities.ConceptoTron;
import facturatron.lib.entities.ConceptosTron;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Emisor;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos.Traslados;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos.Traslados.Traslado;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Receptor;
import mx.bigdata.sat.cfdi.v33.schema.ObjectFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import mx.bigdata.sat.cfdi.v33.schema.CMetodoPago;
import mx.bigdata.sat.cfdi.v33.schema.CPais;
import mx.bigdata.sat.cfdi.v33.schema.CTipoDeComprobante;
import mx.bigdata.sat.cfdi.v33.schema.CUsoCFDI;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import mx.bigdata.sat.cfdi.v33.schema.TimbreFiscalDigital;

/**
 *
 * @author Octavio
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        /*
    CFDv2 cfd = new CFDv2(createComprobante());
    PrivateKey key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream("C:/Users/Octavio/desarrollo/Java/Cer_Sellos/aaa010101aaa_csd_01.key"),
                           "a0123456789");
    X509Certificate cert = KeyLoader
      .loadX509Certificate(new FileInputStream("C:/Users/Octavio/desarrollo/Java/Cer_Sellos/aaa010101aaa_csd_01.cer"));
    cfd.sellar(key, cert);
    cfd.validar();
    cfd.verificar();
    cfd.guardar(System.err);*/
        /*
        ComprobanteTron comp = createComprobante();
        comp.setPassKey("a0123456789");
        comp.setURIKey(new URI("file:///C:/Users/Octavio/desarrollo/Java/Cer_Sellos/aaa010101aaa_csd_01.key"));
        comp.setURICer(new URI("file:///C:/Users/Octavio/desarrollo/Java/Cer_Sellos/aaa010101aaa_csd_01.cer"));

        CFDFactory cfdf = new CFDFactory();
        CFDv2Tron cfd = cfdf.toCFD(comp);

        System.out.println(cfd.getXML());
         *
         */
         Calendar periodo = Calendar.getInstance();
         periodo.set(2010, 12, 1);
         InformeMensual reporte = new InformeMensual(InformeMensual.Esquema.CFD, createEmisor(new ObjectFactory()), periodo);
         ArrayList<ComprobanteTron> comps = new ArrayList<ComprobanteTron>();

         ComprobanteTron[] compsArray = createComprobantesPrueba();

         comps.addAll(Arrays.asList(compsArray));
         reporte.cargarComprobantes(comps);
         reporte.toTXTFile("C:\\Users\\Octavio\\Documents\\CFD\\");

    }
    /**
     *
     * @return
     * @throws Exception
     */
    public static ComprobanteTron[] createComprobantesPrueba() throws Exception {
        ComprobanteTron[] comp = createComprobantes();
        comp[0].setPassKey("12345678a");
        comp[0].setURIKey(new URI("file:////Users/octavioruizcastillo/AppDev/Proyectos/CFD/CFDI/aad990814bp7_1210261233s.cer"));
        comp[0].setURICer(new URI("file:////Users/octavioruizcastillo/AppDev/Proyectos/CFD/CFDI/aad990814bp7_1210261233s.key"));
        comp[0].setPathLogo("/Users/octavioruizcastillo/Dropbox/FormatosFacturatron1_7_0/LOGOTIPOHA.png");
        
        CFDFactory cfdf = new CFDFactory();
        CFDv3Tron cfd = cfdf.toCFDI(comp[0]);
        
        cfd.toPDFFile("C:\\Users\\Octavio\\Documents\\CFD\\FacturaDigital.jasper", "C:\\Users\\Octavio\\Documents\\CFD\\prueba.pdf");
        cfd.toXMLFILE("C:\\Users\\Octavio\\Documents\\CFD\\factura.xml");
        
        return comp;
    }
    public static facturatron.lib.entities.ComprobanteTron[] createComprobantes() throws Exception {
        ComprobanteTron[] comps = new facturatron.lib.entities.ComprobanteTron[1];
        comps[0] = createComprobante();
        comps[0].setPassKey("12345678a");
        comps[0].setURIKey(new URI("file:////Users/octavioruizcastillo/AppDev/Proyectos/CFD/CFDI/aad990814bp7_1210261233s.key"));
        comps[0].setURICer(new URI("file:////Users/octavioruizcastillo/AppDev/Proyectos/CFD/CFDI/aad990814bp7_1210261233s.cer"));
        
        return comps;
    }
    public static facturatron.lib.entities.ComprobanteTron[] createComprobantesFirmados() throws Exception {
        ComprobanteTron[] comps = new facturatron.lib.entities.ComprobanteTron[1];
        comps[0] = createComprobante();
        comps[0].setPassKey("12345678a");
        comps[0].setURIKey(new URI("file:////Users/octavioruizcastillo/AppDev/Proyectos/CFD/CFDI/aad990814bp7_1210261233s.key"));
        comps[0].setURICer(new URI("file:////Users/octavioruizcastillo/AppDev/Proyectos/CFD/CFDI/aad990814bp7_1210261233s.cer"));
        comps[0].setPathLogo("/Users/octavioruizcastillo/Dropbox/FormatosFacturatron1_7_0/LOGOTIPOHA.png");
        
        ObjectFactory of = new ObjectFactory();
        TimbreFiscalDigital timbre = of.createTimbreFiscalDigital();
            
        timbre.setFechaTimbrado( DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(2012,1,2,20,20,0)));
        timbre.setNoCertificadoSAT( "30001000000100000801" );
        timbre.setVersion("1.0");
        timbre.setUUID( "ad662d33-6934-459c-a128-bdf0393ef44" );
        timbre.setSelloSAT( "j5bSpqM3w0+shGtlmqOwqqy6+d659O78ckfstu5vTSFa+2CVMj6Awfr18x4yMLGBwk6ruYbjBIVURodEII6nJIhTTUtYQV1cbRDG9kvvhaNAakx qaSOnOnOx79nHxqFPRVoqh10CsjocS9PZkSM2jz1uwLgaF0knf1g8pjDkLYwlk=" );
        timbre.setSelloCFD( "tOSe+Ex/wvn33YIGwtfmrJwQ31Crd7II9VcH63TGjHfxk5cfb3q9uSbDUGk9TXvo70ydOpikRVw+9B2Six0mbu3PjoPpO909oAYITrRyomdeUGJ 4vmA2/12L86EJLWpU7vlt4cL8HpkEw7TOFhSdpzb/890+jP+C1adBsHU1VHc=" );

        Comprobante.Complemento complemento = of.createComprobanteComplemento();
        complemento.getAny().add(timbre);
        comps[0].getComplemento().add(complemento);
        return comps;
    }
    private static facturatron.lib.entities.ComprobanteTron createComprobante() throws DatatypeConfigurationException {
        ObjectFactory of = new ObjectFactory();
        facturatron.lib.entities.ComprobanteTron comp = new facturatron.lib.entities.ComprobanteTron();
        comp.setVersion("3.3");
        comp.setFecha(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
        comp.setSerie("A");
        comp.setFolio("2");
        comp.setFormaPago("UNA SOLA EXHIBICION");
        comp.setSubTotal(new BigDecimal("2000.00"));
        comp.setTotal(new BigDecimal("2320.00"));
        comp.setDescuento(new BigDecimal("0.00"));
        comp.setTipoDeComprobante(CTipoDeComprobante.I);
        comp.setEmisor(createEmisor(of));
        comp.setReceptor(createReceptor(of));
        comp.setConceptos(createConceptos(of).toConceptos());        
        comp.setConceptosTron(createConceptos(of));
        
        comp.setImpuestos(createImpuestos(of));
        comp.setMetodoPago(CMetodoPago.PUE); //Pago en una sola exhibición
        comp.setLugarExpedicion("Tlatlauquitepec, Puebla");
        comp.setCertificado(null);

        return comp;
    }
    private static Emisor createEmisor(ObjectFactory of) {
        Emisor emisor = of.createComprobanteEmisor();
        emisor.setNombre("ASOCIACION DE AGRICULTORES DEL DISTRITO DE RIEGO 004 DON MARTIN COAHUILA Y NUEVO LEON AC");
        emisor.setRfc("AAD990814BP7");
        
        emisor.setRegimenFiscal("Persona física con actividad empresarial");
        return emisor;
    }

    private static Receptor createReceptor(ObjectFactory of) {
        Receptor receptor = of.createComprobanteReceptor();
        receptor.setNombre("JUAN PEREZ PEREZ");
        receptor.setRfc("PEPJ8001019Q8");
        
        receptor.setUsoCFDI(CUsoCFDI.G_01);
        receptor.setResidenciaFiscal(CPais.MEX);
        return receptor;
    }
    
    private static ConceptosTron createConceptos(ObjectFactory of) {
        ConceptosTron cps = new ConceptosTron();
        
        RenglonModel r1 = new RenglonModel();
        r1.setUnidad("CAPSULAS");
        r1.setCantidad(new BigDecimal("1.0"));
        r1.setDescripcion("VIBRAMICINA 100MG 10");
        r1.setValorUnitario(new BigDecimal("244.00"));
        r1.setTasaIEPS(new BigDecimal("8"));
        r1.setIEPS(new BigDecimal(19.52));
        cps.add(r1.toConceptoTron());
        
        RenglonModel r2 = new RenglonModel();
        r2.setUnidad("BOTELLA");
        r2.setCantidad(new BigDecimal("1.0"));
        r2.setDescripcion("CLORUTO 500M");
        r2.setValorUnitario(new BigDecimal("137.93"));
        r2.setTasaIEPS(new BigDecimal("25"));
        r2.setIEPS(new BigDecimal("34.48"));
        cps.add(r2.toConceptoTron());
        
        RenglonModel r3 = new RenglonModel();
        r3.setUnidad("TABLETAS");
        r3.setCantidad(new BigDecimal("1.0"));
        r3.setDescripcion("SEDEPRON 250MG 10");
        r3.setValorUnitario(new BigDecimal("84.50"));
        r3.setTasaIEPS(new BigDecimal("8"));
        r3.setIEPS(new BigDecimal("6.76"));
        cps.add(r3.toConceptoTron());
        return cps;
    }

    private static Impuestos createImpuestos(ObjectFactory of) {
        Impuestos imps = of.createComprobanteImpuestos();
        Traslados trs = of.createComprobanteImpuestosTraslados();
        List<Traslado> list = trs.getTraslado();
        Traslado t1 = of.createComprobanteImpuestosTrasladosTraslado();
        t1.setImporte(new BigDecimal("20.00"));
        t1.setImpuesto("002"); //002 = IVA
        t1.setTasaOCuota(new BigDecimal("16.00"));
        list.add(t1);
        Traslado t2 = of.createComprobanteImpuestosTrasladosTraslado();
        t2.setImporte(new BigDecimal("22.07"));
        t2.setImpuesto("003"); //003 = IEPS
        t2.setTasaOCuota(new BigDecimal("25.00"));
        list.add(t2);
        Traslado t3 = of.createComprobanteImpuestosTrasladosTraslado();
        t3.setImporte(new BigDecimal("4.00"));
        t3.setImpuesto("003"); //003 = IEPS
        t3.setTasaOCuota(new BigDecimal("8.00"));
        list.add(t3);
        imps.setTraslados(trs);
        imps.setTotalImpuestosTrasladados(new BigDecimal("46.07"));
        return imps;
    }

    }
