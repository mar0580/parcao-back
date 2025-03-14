package com.parcao.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime datePedido;

    @UpdateTimestamp
    private LocalDateTime dateAtualizacao;

    @Column(name = "filial_id")
    private Long idFilial;

    @Column(name = "cliente_id")
    private Long idCliente;

    private BigDecimal valorTotal;

    private BigDecimal custoTotal;

    @Column(name = "taxaVenda_id")
    private Long idTaxaVenda;

    @Column(name = "user_id")
    private Long idUser;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "pedidoItens", joinColumns = @JoinColumn(name = "pedido_id"))
    @AttributeOverrides({ @AttributeOverride(name = "descricaoProduto", column = @Column(name = "nome_produto"))
    })
    private Set<PedidoItem> produtos = new HashSet<>();
    public Pedido(long idFilial, long idCliente, long idUser, long idTaxaVenda, BigDecimal valorTotal, Set<PedidoItem> produtos) {
        this.idFilial = idFilial;
        this.idCliente = idCliente;
        this.idUser = idUser;
        this.idTaxaVenda = idTaxaVenda;
        this.valorTotal = valorTotal;
        this.produtos = produtos;
    }
}
