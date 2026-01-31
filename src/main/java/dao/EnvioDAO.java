package dao;

import db.Db;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnvioDAO {
    //EnvioDAO: Gestiona la entidad Envio, que es una "tabla de unión" compleja.
    // Su función principal es el mapeo Objeto-Relacional (ORM) manual:
    // convierte filas de SQL con JOINS en objetos Java anidados.

    // ===============================
    // SQL
    // ===============================

    // El INSERT debe seguir el orden exacto de las columnas en la BD
    private static final String INSERT_SQL = """
        INSERT INTO envio (id, pedido_id, comercial_id, repartidor_id)
        VALUES (?, ?, ?, ?)
        """;
    /**
     * Usamos JOINs:
     * Como un Envio tiene un Pedido, un Comercial y un Repartidor,
     * usamos SQL para traer toda la información relacionada de un solo golpe.
     * Usamos AS (Alias) para evitar conflictos si varias tablas tienen columnas llamadas 'id' o 'nombre'.
     */
    private static final String SELECT_ALL_SQL = """
        SELECT 
          e.id AS envio_id,
          p.id AS pedido_id, p.cliente_id, p.fecha,
          c.id AS comercial_id, c.nombre AS comercial_nombre, c.zonaventas,
          r.id AS repartidor_id, r.nombre AS repartidor_nombre, r.vehiculo
        FROM envio e
        JOIN pedido p ON e.pedido_id = p.id
        JOIN comercial c ON e.comercial_id = c.id
        JOIN repartidor r ON e.repartidor_id = r.id
        """;

//Reutiliza el anterior y le añade el filtro por ID
    private static final String SELECT_BY_ID_SQL = SELECT_ALL_SQL + " WHERE e.id = ?";


    /**
     * Método estático para insertar.
     * extraemos las IDs de los objetos anidados (e.getPedido().getId())
     * para guardarlas como Claves Foráneas (FK) en la tabla 'envio'.
     */
    public void insert(Envio e) throws SQLException {
        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(INSERT_SQL)) {

            pst.setInt(1, e.getId());
            pst.setInt(2, e.getPedido().getId());
            pst.setInt(3, e.getComercial().getId());
            pst.setInt(4, e.getRepartidor().getId());

            pst.executeUpdate();
        }
    }
    /**
     * Recupera todos los envíos.
     * Utiliza el método auxiliar 'mapRow' para no repetir código de conversión.
     */
    public List<Envio> findAll() throws SQLException {
        List<Envio> out = new ArrayList<>();
        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                out.add(mapRow(rs));
            }
        }
        return out;
    }

    public Envio findById(int id) throws SQLException {
        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(SELECT_BY_ID_SQL)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    /**
     * mapRow: Este método es el encargado de tomar una fila del ResultSet (que es plana) y crea la jerarquía de objetos Java:
     * Envio -> contiene Pedido
     * -> contiene Comercial
     * -> contiene Repartidor
     */
    private Envio mapRow(ResultSet rs) throws SQLException {
        // 1. Creamos el Pedido (solo con los datos que tenemos en el JOIN)
        Pedido pedido = new Pedido(
                rs.getInt("pedido_id"),
                rs.getInt("cliente_id")
        );


        // 2. Creamos el Comercial
        Comercial comercial = new Comercial(
                rs.getInt("comercial_id"),
                rs.getString("comercial_nombre"),
                rs.getString("zonaventas")
        );

        // 3. Creamos el Repartidor
        Repartidor repartidor = new Repartidor(
                rs.getInt("repartidor_id"),
                rs.getString("repartidor_nombre"),
                rs.getString("vehiculo")
        );

        // 4. Retornamos el Envio completo
        return new Envio(
                rs.getInt("envio_id"),
                pedido,
                comercial,
                repartidor
        );
    }
}

