package model;
import java.util.ArrayList;
import java.util.List;

public class Repartidor {
    private Integer id;
    private String nombre;
    private String vehiculo;

    // 1:1
    private DetalleCliente detalle; // puede ser null si aún no hay detalle

    // 1:N
    private List<Pedido> pedidos = new ArrayList<>();

    public Repartidor() {}
    public Repartidor(Integer id, String nombre, String vehiculo) {
        this.id = id; this.nombre = nombre; this.vehiculo = vehiculo;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getVehiculo() { return vehiculo; }
    public void setVehiculo(String vehiculo) { this.vehiculo = vehiculo; }

    public DetalleCliente getDetalle() { return detalle; }
    public void setDetalle(DetalleCliente detalle) { this.detalle = detalle; }

    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }

    @Override public String toString() {
        return "Repartidor{id=%d, nombre='%s', en vehiculo='%s'}".formatted(id, nombre, vehiculo);
    }
}
