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
public class FechamentoCaixa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime dateFechamentoCaixa;

    @UpdateTimestamp
    private LocalDateTime dateAtualizacaoFechamentoCaixa;

    @Column(name = "filial_id")
    private Long idFilial;

    @Column(name = "user_id")
    private Long idUser;

    @Column(name = "observacao")
    private String observacao;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "fechamentoCaixaItens", joinColumns = @JoinColumn(name = "fechamento_caixa_id"))
    private Set<FechamentoCaixaItem> produtos = new HashSet<>();
    public FechamentoCaixa(long idFilial, long idUser, Set<FechamentoCaixaItem> produtos) {
        this.idFilial = idFilial;
        this.idUser = idUser;
        this.produtos = produtos;
    }
}
