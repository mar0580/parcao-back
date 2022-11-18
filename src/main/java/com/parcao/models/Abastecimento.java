package com.parcao.models;

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
public class Abastecimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime dateAbastecimento;

    @UpdateTimestamp
    private LocalDateTime dateAtualizacao;

    @Column(name = "filial_id")
    private Long idFilial;

    @Column(name = "user_id")
    private Long idUser;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "abastecimentoItens", joinColumns = @JoinColumn(name = "abastecimento_id"))
    @AttributeOverrides({ @AttributeOverride(name = "descricaoProduto", column = @Column(name = "nome_produto"))
    })
    private Set<AbastecimentoItem> produtos = new HashSet<>();

    public Abastecimento(long idFilial, long idUser, Set<AbastecimentoItem> produtos){
        this.idFilial = idFilial;
        this.idUser = idUser;
        this.produtos = produtos;
    }
}
