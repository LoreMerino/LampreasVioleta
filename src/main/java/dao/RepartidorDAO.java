package dao;
import db.Db;

import model.Repartidor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {
    private static final String INSERT_SQL =
            "INSERT INTO repartidor (id, nombre, vehiculo) VALUES (?, ?, ?)";
    // Consulta SQL para insertar un Repartidor.
    // Usamos ? para parámetros > evita SQL injection y mejora rendimiento con sentencias preparadas.

    private static final String SELECT_BY_ID_SQL =
            "SELECT id, nombre, vehiculo FROM repartidor WHERE id = ?";
    // Consulta SQL para buscar un repartidor por su ID.

    private static final String SELECT_ALL_SQL =
            "SELECT id, nombre, vehiculo FROM repartidor ORDER BY id";

    private static final String SEARCH_SQL = """
                    SELECT id, nombre, vehiculo
                    FROM repartidor
                    WHERE CAST(id AS TEXT) ILIKE ? 
                        OR nombre ILIKE ?  
                        OR vehiculo ILIKE ?
                    ORDER BY id                    
                    """;
    // ----------------------------------------------------------
    // MÉTODO: INSERTAR UN REPARTIDOR
    // ----------------------------------------------------------

    public void insert(Repartidor r) throws SQLException {
        // Método público que inserta un repartidor en la base de datos.
        // Recibe un objeto Repartidor y lanza SQLException si algo sale mal.

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            // try-with-resources: la conexión y el PreparedStatement se cerrarán automáticamente
            // al final del bloque, aunque haya errores.

            ps.setInt(1, r.getId());        // Parámetro 1 → columna id
            ps.setString(2, r.getNombre());  // Parámetro 2 → columna nombre
            ps.setString(3, r.getVehiculo());   // Parámetro 3 → columna vehiculo

            ps.executeUpdate();
            // Ejecuta la sentencia. Como es un INSERT, no devuelve ResultSet.

            // Recuperar el ID generado por PostgreSQL
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    r.setId(idGenerado);  // lo guardamos en el objeto
                }
            }

        }
    }

    // Versión transaccional: usa una conexión que le pasa el servicio
    public void insert(Repartidor r, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {
            ps.setInt(1, r.getId());
            ps.setString(2, r.getNombre());
            ps.setString(3, r.getVehiculo());
            ps.executeUpdate();
        }
    }

// ----------------------------------------------------------
    // MÉTODO: BUSCAR REPARTIDOR POR ID
    // ----------------------------------------------------------

    public Repartidor findById(int id) throws SQLException {
        // Devuelve el REPARTIDOR cuyo id coincida con el parámetro.
        // Si no existe, devuelve null.

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);  // Asignamos el id al parámetro ?

            try (ResultSet rs = ps.executeQuery()) {
                // executeQuery() devuelve un ResultSet ↔ una tabla virtual con las filas devueltas.

                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // ----------------------------------------------------------
    // MÉTODO: LISTAR TODOS LOS REPARTIDORES
    // ----------------------------------------------------------

    public List<Repartidor> findAll() throws SQLException {
        List<Repartidor> out = new ArrayList<>();
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(mapRow(rs));
            }
        }
        return out;
    }


    public List<Repartidor> search(String filtro) throws SQLException {

        String patron = "%" + filtro + "%";

        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(SEARCH_SQL)) {
            pst.setString(1, patron);
            pst.setString(2, patron);
            pst.setString(3, patron);

            List<Repartidor> out = new ArrayList<>();

            try(ResultSet rs = pst.executeQuery()){

                while (rs.next()){
                    out.add(mapRow(rs));
                }
            }
            return out;
        }
    }

    private Repartidor mapRow(ResultSet rs) throws SQLException {
        return new Repartidor(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("vehiculo")
        );
    }

}
