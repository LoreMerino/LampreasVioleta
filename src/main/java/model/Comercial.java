package model;
import java.util.ArrayList;
import java.util.List;

public class Comercial {
        private Integer id;            // PK
        private String nombre;
        private String zonaventas;

        // 1:1
        private DetallePedido detalle; // puede ser null si aún no hay detalle

        // 1:N
        private List<Pedido> pedidos = new ArrayList<>();

        public Comercial() {}
        public Comercial(Integer id, String nombre, String zonaventas) {
            this.id = id; this.nombre = nombre; this.zonaventas = zonaventas;
        }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getZonaventas() { return zonaventas; }
    public void setZonaventas(String zonaventas) { this.zonaventas = zonaventas; }

    public DetallePedido getDetalle() { return detalle; }
    public void setDetalle(DetallePedido detalle) { this.detalle = detalle; }

    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }

    @Override public String toString() {
        return "Comercial{id=%d, nombre='%s', zona de ventas='%s'}".formatted(id, nombre, zonaventas);
    }
}
