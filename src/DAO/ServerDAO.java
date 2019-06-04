/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;

/**
 *
 * @author lvazquezdorna
 */
public class ServerDAO {

    //ATRIBUTO DE CONEXION CONTRA LA BASE DE DATOS
    private AccesoDB acceso;

    //CONTRUCTORES
    public ServerDAO() throws SQLException, ClassNotFoundException {
        acceso = new AccesoDB();
    }

    /**
     * Metodo que da de alta un usuario
     *
     * @param u
     * @return >0 si inserta en la tabla y 0 si no pudo insertar
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean altaUsuario(Usuario u) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm;
        Connection con;
        String sql;
        //campos
        sql = "INSERT INTO usuarios (nick,password) VALUES (?,?)";

        //conexion 
        if (acceso == null) {
            return false;
        } else {
            con = acceso.getConexion();
        }
        pstm = con.prepareStatement(sql);
        pstm.setString(1, u.getNick());
        pstm.setString(2, u.getPassword());

        int res = pstm.executeUpdate();// indica el numero de filas insertadas
        pstm.close();
        con.close();

        return res > 0;
    }

    /**
     * Metodo que borra un usuario
     *
     * @param u
     * @return >0 si inserta en la tabla y 0 si no pudo borrar
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean bajaUsuario(Usuario u) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm;
        Connection con;
        String sql;
        //campos
        sql = "DELETE FROM usuarios  WHERE id=?";

        //conexion 
        if (acceso == null) {
            return false;
        } else {
            con = acceso.getConexion();
        }
        pstm = con.prepareStatement(sql);
        pstm.setInt(1, u.getId());

        int res = pstm.executeUpdate();// indica el numero de filas insertadas
        pstm.close();
        con.close();

        return res > 0;

    }

    public boolean modificarUsuario(Usuario u) throws SQLException, ClassNotFoundException {
        PreparedStatement pstm;
        Connection con;
        String sql;
        //campos
        sql = "UPDATE usuarios SET nick=?, password=? WHERE id=?";

        //conexion 
        if (acceso == null) {
            return false;
        } else {
            con = acceso.getConexion();
        }
        pstm = con.prepareStatement(sql);
        pstm.setString(1, u.getNick());
        pstm.setString(2, u.getPassword());
        pstm.setInt(3, u.getId());

        int res = pstm.executeUpdate();// indica el numero de filas insertadas
        pstm.close();
        con.close();

        return res > 0;
    }

    /**
     * Metodo que lista los usuarios guardados en la bd
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws Exception
     */
    public List<Usuario> getUsuarios() throws SQLException, ClassNotFoundException, Exception {
        List<Usuario> listado = new ArrayList<>();
        Connection con;
        Statement stm;
        ResultSet rs;
        String sql;

        sql = "SELECT * FROM usuarios";

        if (acceso == null) {
            return null;
        } else {
            con = acceso.getConexion();
        }

        stm = con.createStatement();
        rs = stm.executeQuery(sql);
        while (rs.next()) {
            Usuario u = new Usuario();

            u.setId(rs.getInt("id"));
            u.setNick(rs.getString(("nick")));
            u.setPassword(rs.getString("password"));
            listado.add(u);
        }
        rs.close();
        stm.close();
        con.close();
        return listado;
    }

    /**
     * metodo que devuelve usuario con nick y contraseña, para comprobar loging
     *
     * @param u
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Usuario getUsuario(Usuario u) throws SQLException, ClassNotFoundException {
        List<Usuario> listado = new ArrayList<>();
        Connection con;
        PreparedStatement pstm;
        ResultSet rs;
        String sql;

        sql = "SELECT * FROM usuarios where nick=? and password=?";

        if (acceso == null) {
            return null;
        } else {
            con = acceso.getConexion();
        }

        pstm = con.prepareStatement(sql);
        pstm.setString(1, u.getNick());
        pstm.setString(2, u.getPassword());
        rs = pstm.executeQuery();

        while (rs.next()) {
            Usuario u2 = new Usuario();

            u2.setId(rs.getInt("id"));
            u2.setNick(rs.getString(("nick")));
            u2.setPassword(rs.getString("password"));
            listado.add(u2);
        }

        rs.close();
        pstm.close();
        con.close();
        if (listado.size() > 0) {
            return listado.get(0);
        } else {
            return null;
        }

    }

}
