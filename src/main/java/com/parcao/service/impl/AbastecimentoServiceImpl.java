package com.parcao.service.impl;

import com.parcao.exception.ResourceNotFoundException;
import com.parcao.dto.AbastecimentoDTO;
import com.parcao.dto.AbastecimentoItemDTO;
import com.parcao.model.Abastecimento;
import com.parcao.model.AbastecimentoItem;
import com.parcao.model.Produto;
import com.parcao.enums.EEmailDetails;
import com.parcao.enums.MensagemEnum;
import com.parcao.repository.AbastecimentoRepository;
import com.parcao.service.IAbastecimentoService;
import com.parcao.service.IEmailService;
import com.parcao.service.IProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AbastecimentoServiceImpl implements IAbastecimentoService {

    private static final Logger logger = LoggerFactory.getLogger(AbastecimentoServiceImpl.class);
    private static final String CORRELATION_ID = "correlationId";

    private final AbastecimentoRepository abastecimentoRepository;
    private final IProdutoService produtoService;
    private final IEmailService emailService;

    public AbastecimentoServiceImpl(AbastecimentoRepository abastecimentoRepository,
                                     IProdutoService produtoService,
                                     IEmailService emailService) {
        this.abastecimentoRepository = abastecimentoRepository;
        this.produtoService = produtoService;
        this.emailService = emailService;
    }

    @Override
    public Abastecimento save(Abastecimento abastecimento) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Salvando abastecimento", correlationId);
        Abastecimento saved = abastecimentoRepository.save(abastecimento);
        logger.info("[correlationId={}] Abastecimento salvo com id: {}", correlationId, saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Object createAbastecimento(AbastecimentoDTO abastecimentoDto) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Iniciando criação de abastecimento para filial: {}", correlationId, abastecimentoDto.getIdFilial());

        Set<AbastecimentoItemDTO> produtoItemDto = abastecimentoDto.getProdutos();
        Set<AbastecimentoItem> produtos = new HashSet<>();

        produtoItemDto.forEach(produto -> {
            if (produtoService.existsById(produto.getId())) {
                produtos.add(new AbastecimentoItem(produto));
            }
        });

        Abastecimento abastecimento = new Abastecimento();
        BeanUtils.copyProperties(abastecimentoDto, abastecimento);
        abastecimento.setProdutos(produtos);

        for (AbastecimentoItem produtosParaEstoque : abastecimento.getProdutos()) {
            Produto produtoOptional = produtoService.findById(produtosParaEstoque.getId());

            // Verifica se a quantidade solicitada pela filial consta em estoque geral
            if ((produtoOptional.getQuantidade() - produtosParaEstoque.getQuantidade() < 0)) {
                logger.warn("[correlationId={}] Estoque insuficiente para produto: {}", correlationId, produtosParaEstoque.getDescricaoProduto());
                throw new ResourceNotFoundException(MensagemEnum.PRODUTO_ESTOQUE_GERAL_INSUFICIENTE.getMensagem() + " " + produtosParaEstoque.getDescricaoProduto());
            }

            // Verifica se o produto a ser inserido em estoque na filial já existe
            if (!getRowCountAbastecimento(abastecimento.getIdFilial(), produtosParaEstoque.getId()).isEmpty()) {
                adicionaQuantidadeProdutoAbastecimento(produtosParaEstoque.getQuantidade(), abastecimento.getIdFilial(), produtosParaEstoque.getId());
                produtoService.updateProdutoEstoque(produtosParaEstoque.getId(), (produtoOptional.getQuantidade() - produtosParaEstoque.getQuantidade()));

                logger.info("[correlationId={}] Estoque do produto na filial atualizado", correlationId);
                return MensagemEnum.ESTOQUE_PRODUTO_FILIAL_ATUALIZADO.getMensagem();
            }
            produtoService.updateProdutoEstoque(produtosParaEstoque.getId(), (produtoOptional.getQuantidade() - produtosParaEstoque.getQuantidade()));
        }

        Abastecimento saved = save(abastecimento);
        logger.info("[correlationId={}] Abastecimento criado com sucesso", correlationId);
        return saved;
    }

    @Override
    @Transactional
    public String updateEstoqueFilial(AbastecimentoDTO abastecimentoDto) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Iniciando atualização de estoque da filial: {}", correlationId, abastecimentoDto.getIdFilial());

        Set<AbastecimentoItemDTO> abastecimentoItemDto = abastecimentoDto.getProducts();
        StringBuilder produtosEstoqueBaixo = new StringBuilder();

        for (AbastecimentoItemDTO produtosParaAtualizarEstoque : abastecimentoItemDto) {
            if (getRowCountQuantidadeAbastecimento(abastecimentoDto.getIdFilial(), produtosParaAtualizarEstoque.getId(), produtosParaAtualizarEstoque.getQuantidade()).isEmpty()) {
                produtosEstoqueBaixo.append(System.lineSeparator())
                        .append("Produto: ").append(produtosParaAtualizarEstoque.getDescricaoProduto())
                        .append(" - Quantidade: ").append(produtosParaAtualizarEstoque.getQuantidade())
                        .append("\n");
            }

            if (!produtosEstoqueBaixo.isEmpty()) {
                logger.warn("[correlationId={}] Estoque baixo detectado, enviando e-mail", correlationId);
                emailService.sendEmail("userWarn", produtosEstoqueBaixo.toString(), EEmailDetails.ESTOQUE_BAIXO.getEEmailDetails());
            }

            if (updateAbastecimento(produtosParaAtualizarEstoque.getQuantidade(), abastecimentoDto.getIdFilial(), produtosParaAtualizarEstoque.getId()) == 0) {
                logger.error("[correlationId={}] Erro ao atualizar estoque", correlationId);
                throw new RuntimeException(MensagemEnum.ERRO_AO_ATUALIZAR_ESTOQUE.getMensagem());
            }
        }

        logger.info("[correlationId={}] Estoque da filial atualizado com sucesso", correlationId);
        return MensagemEnum.ESTOQUE_FILIAL_ATUALIZADO.getMensagem();
    }

    @Override
    public int updateAbastecimento(int qtd, Long idFilial, Long idProduto) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Atualizando abastecimento - filial: {}, produto: {}, qtd: {}", correlationId, idFilial, idProduto, qtd);
        return abastecimentoRepository.reduzirQuantidadeProduto(qtd, idFilial, idProduto);
    }

    @Override
    public List<Abastecimento> getRowCountAbastecimento(Long idFilial, Long idProduto) {
        return abastecimentoRepository.buscarAbastecimentosPorProduto(idFilial, idProduto);
    }

    @Override
    public void adicionaQuantidadeProdutoAbastecimento(int qtd, Long idFilial, Long idProduto) {
        abastecimentoRepository.adicionaQuantidadeProdutoAbastecimento(qtd, idFilial, idProduto);
    }

    @Override
    public List<Abastecimento> getRowCountQuantidadeAbastecimento(Long idFilial, Long idProduto, int qtd) {
        return abastecimentoRepository.buscarAbastecimentosComQuantidadeMenor(idFilial, idProduto, qtd);
    }
}
