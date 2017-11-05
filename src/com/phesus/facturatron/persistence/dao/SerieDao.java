/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.phesus.facturatron.persistence.dao;

import facturatron.config.*;
import facturatron.Dominio.Serie;
import facturatron.mvc.DAO;
import facturatron.mvc.JDBCDAOSupport;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Octavio
 */
public class SerieDao extends Serie implements DAO<Integer,Serie> {

    public SerieDao() {
        
    }

    /**
     * Carga a la primera persona, que representa siempre al contribuyente
     * Nota: Las personas del ID 2 en adelante son clientes
     * @return this
     */
    public Serie load() throws SQLException {
        loadSerie();
        return this;
    }

    @Override
    public void persist() throws SQLException {
        try {
            JDBCDAOSupport bd = getBD();
            bd.conectar();
            persist(bd.getCon());
            bd.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDao.class.getName()).log(Level.SEVERE, "Error al guardar cliente/persona", ex);
            throw ex;
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("La configuración no puede ser eliminada.");
    }

    @Override
    public Serie findBy(Integer id) throws SQLException {
        return load();
    }

    @Override
    public List<Serie> findAll() {
        throw new UnsupportedOperationException("La configuración sólo contiene una fila, usar método find en su lugar.");
    }

    @Override
    public JDBCDAOSupport getBD() {
        return new JDBCDAOSupport();
    }

    public void loadSerie() {
        JDBCDAOSupport bd = getBD();
         try{

            bd.conectar();
            loadSerie(bd.getCon());

        }catch(Exception ex){

            Logger.getLogger(SerieDao.class.getName()).log(Level.SEVERE, "Error al consultar serie de facturas", ex);

        } finally{
            bd.desconectar();
        }
    }

    public static BigInteger nextId(Connection c) throws SQLException, Exception {
        SerieDao serie = new SerieDao();
        serie.loadSerie(c);
        BigInteger folio = serie.getFolioActual();
        serie.setFolioActual(folio.add(BigInteger.valueOf(1)));
        serie.persist(c);
        return folio;
    }

    public void loadSerie(Connection c) throws SQLException, Exception {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM serie");
        ResultSet rs = ps.executeQuery();

        if(!rs.next()) {
            throw new Exception("No existe registro de configuración de serie de facturas");
        }

        this.setSerie(rs.getString("serie"));
        this.setFolioActual(BigInteger.valueOf(rs.getLong("folioActual")));
        //this.setTipo(rs.getString("tipo"));

        setChanged();
        notifyObservers();

    }

    public void persist(Connection con) throws SQLException {

            PreparedStatement ps;
            ps = con.prepareStatement("update serie set serie=?, folioActual=?");

            ps.setString(1, getSerie());
            ps.setLong  (2, getFolioActual().longValue());

            ps.execute();
    }


}
