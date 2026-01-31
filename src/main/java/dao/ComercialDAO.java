package dao;
import db.Db;


import model.Comercial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComercialDAO {

    private static final String INSERT_SQL =
            "INSERT INTO comercial ( id, nombre, zonaventas) VALUES ( ?, ?, ?)";
    // Consulta SQL para insertar un comercial.
    // Usamos ? para parámetros > evita SQL injection y mejora rendimiento con sentencias preparadas.

    private static final String SELECT_BY_ID_SQL =
            "SELECT id, nombre, zonaventas FROM comercial WHERE id = ?";
    // Consulta SQL para buscar un comercial por su ID.

    private static final String SELECT_ALL_SQL =
            "SELECT id, nombre, zonaventas FROM comercial ORDER BY id";

    private static final String SEARCH_SQL = """
                    SELECT id, nombre, zonaventas
                    FROM comercial
                    WHERE CAST(id AS TEXT) ILIKE ? 
                        OR nombre ILIKE ?  
                        OR zonaventas ILIKE ?
                    ORDER BY id                    
                    """;
    // ----------------------------------------------------------
    // MÉTODO: INSERTAR UN Comercial
    // ----------------------------------------------------------

    public void insert(Comercial co) throws SQLException {
        // Método público que inserta un comercial en la base de datos.
        // Recibe un objeto Comercial y lanza SQLException si algo sale mal.

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            // try-with-resources: la conexión y el PreparedStatement se cerrarán automáticamente
            // al final del bloque, aunque haya errores.

            ps.setInt(1, co.getId());         // Parámetro 1 → columna id
            ps.setString(2, co.getNombre());  // Parámetro 2 → columna nombre
            ps.setString(3, co.getZonaventas());   // Parámetro 3 → columna zonaventas

            ps.executeUpdate();
            // Ejecuta la sentencia. Como es un INSERT, no devuelve ResultSet.

            // Recuperar el ID generado por PostgreSQL
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    co.setId(idGenerado);  // lo guardamos en el objeto
                }
            }

        }
    }


    // Versión transaccional: usa una conexión que le pasa el servicio
    public void insert(Comercial co, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {
            ps.setInt(1, co.getId());
            ps.setString(2, co.getNombre());
            ps.setString(3, co.getZonaventas());
            ps.executeUpdate();
        }
    }

    // ----------------------------------------------------------
    // MÉTODO: BUSCAR Comercial POR ID
    // ----------------------------------------------------------

    public Comercial findById(int id) throws SQLException {
        // Devuelve el Comercial cuyo id coincida con el parámetro.
        // Si no existe, devuelve null.

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);  // Asignamos el id al parámetro ?

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }


    // ----------------------------------------------------------
    // MÉTODO: LISTAR TODOS LOS COMERCIALES
    // ----------------------------------------------------------

    public List<Comercial> findAll() throws SQLException {
        List<Comercial> out = new ArrayList<>();
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(mapRow(rs));
            }
        }
        return out;
    }


    public List<Comercial> search(String filtro) throws SQLException {

        String patron = "%" + filtro + "%";

        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(SEARCH_SQL)) {
            pst.setString(1, patron);
            pst.setString(2, patron);
            pst.setString(3, patron);

            List<Comercial> out = new ArrayList<>();

            try(ResultSet rs = pst.executeQuery()){

                while (rs.next()){
                    out.add(mapRow(rs));
                }
            }
            return out;
        }
    }

    private Comercial mapRow(ResultSet rs) throws SQLException {
        return new Comercial(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("zonaventas")
        );
    }

}
