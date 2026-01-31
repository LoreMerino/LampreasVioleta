package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Contenedor para exportación/importación:
 * una "instantánea" del estado de la BD.
 *
 * IMPORTANTE:
 * - Debe tener constructor vacío + getters/setters
 *   para que Jackson pueda deserializar.
 */
public class AppData {

    private List<Cliente> clientes = new ArrayList<>();
    private List<DetalleCliente> detallesCliente = new ArrayList<>();
    private List<Producto> productos = new ArrayList<>();
    private List<Pedido> pedidos = new ArrayList<>();
    private List<DetallePedido> detallesPedido = new ArrayList<>();
    private List<Repartidor> repartidores = new ArrayList<>();
    private List<Comercial> comerciales = new ArrayList<>();
    private List<Envio> envios = new ArrayList<>();

    public AppData() { }

    public List<Cliente> getClientes() { return clientes; }
    public void setClientes(List<Cliente> clientes) { this.clientes = clientes; }

    public List<DetalleCliente> getDetallesCliente() { return detallesCliente; }
    public void setDetallesCliente(List<DetalleCliente> detallesCliente) { this.detallesCliente = detallesCliente; }

    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }

    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }

    public List<DetallePedido> getDetallesPedido() { return detallesPedido; }
    public void setDetallesPedido(List<DetallePedido> detallesPedido) { this.detallesPedido = detallesPedido; }

    public List<Repartidor> getRepartidores() { return repartidores; }
    public void setRepartidores(List<Repartidor> repartidores) { this.repartidores = repartidores; }

    public List<Comercial> getComerciales() { return comerciales; }
    public void setComerciales(List<Comercial> comerciales) { this.comerciales = comerciales; }

    public List<Envio> getEnvios(){return envios;}
    public void setEnvio(List<Envio> envios){this.envios = envios;}
}