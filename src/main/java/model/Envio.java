package model;

public class Envio {


        private Integer id;                // PK
        private Pedido pedido;             // Pedido con sus detalles
        private Comercial comercial;       // Quién lo gestiona
        private Repartidor repartidor;     // Quién lo entrega


        public Envio() {}

        public Envio(Integer id, Pedido pedido, Comercial comercial, Repartidor repartidor) {
            this.id = id;
            this.pedido = pedido;
            this.comercial = comercial;
            this.repartidor = repartidor;
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public Pedido getPedido() { return pedido; }
        public void setPedido(Pedido pedido) { this.pedido = pedido; }

        public Comercial getComercial() { return comercial; }
        public void setComercial(Comercial comercial) { this.comercial = comercial; }

        public Repartidor getRepartidor() { return repartidor; }
        public void setRepartidor(Repartidor repartidor) { this.repartidor = repartidor; }


        @Override
        public String toString() {
            return "Envio{id=%d, pedido=%d, cliente=%d, total=%.2f, comercial=%s, repartidor=%s}"
                    .formatted(
                            id,
                            pedido.getId(),
                            pedido.getClienteId(),
                            pedido.getTotal(),
                            comercial.getNombre(),
                            repartidor.getNombre()
                    );
        }

    }

