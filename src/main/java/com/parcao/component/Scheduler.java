package com.parcao.component;

import com.parcao.models.EEmailDetails;
import com.parcao.models.Filial;
import com.parcao.models.Produto;
import com.parcao.services.EmailService;
import com.parcao.services.FilialService;
import com.parcao.services.SchedulerService;
import com.parcao.utils.Util;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//@Component
public class Scheduler {

    final EmailService emailService;
    final FilialService filialService;
    final SchedulerService schedulerService;

    String data = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    public Scheduler(EmailService emailService, FilialService filialService, SchedulerService schedulerService) {
        this.emailService = emailService;
        this.filialService = filialService;
        this.schedulerService = schedulerService;
    }

    //@Scheduled(cron = "0/15 * * * * *")//teste
    @Scheduled(cron = "0 0 12,20 * * *")
    public void countVendasByPagamentoPeriodo() throws ParseException {
        List<Object[]> objects = schedulerService.countVendasByPagamentoPeriodo(Util.dateToInicialTimestamp(data), Util.dateToFinalTimestamp(data));
        StringBuilder msgEmailBody = new StringBuilder();
        msgEmailBody.append("Data Referência " + Util.dateToPTBR() + "\n\n");
        if (objects.size() > 0) {
            for (Object[] o : objects) {
                for (int i = 0; i < o.length; i++) {
                    if (i == 1) {
                        msgEmailBody.append("Tipo Pagamento: " + o[i].toString() + "\n");
                    }

                    if (i == 2) {
                        msgEmailBody.append("Quantidade: " + o[i].toString() + "\n");
                    }

                    if (i == 3) {
                        msgEmailBody.append("Valor Total: " + NumberFormat.getCurrencyInstance().format(new BigDecimal(o[i].toString())).toString() + "\n-------------------\n");
                    }
                }
            }
            emailService.sendEmail("userInfo", msgEmailBody.toString(), EEmailDetails.RELATORIO_POR_TIPO_PAGAMENTO.getEEmailDetails());
        }
    }

    @Scheduled(cron = "0 05 12,16,20 * * *")
    public void vendasDetalhadasPorFiialandProduto() throws ParseException {
        List<Filial> filiais = filialService.findAll();
        StringBuilder msgEmailBody = new StringBuilder();
        msgEmailBody.append("Data Referência " + Util.dateToPTBR() + "\n\n");
        for (Filial f : filiais) {
            List<Object[]> objects = schedulerService.vendasDetalhadasPorFiialandProduto(f.getId(), Util.dateToInicialTimestamp(data), Util.dateToFinalTimestamp(data));
            if(objects.size() > 0){
                for (Object[] o : objects) {
                    msgEmailBody.append("Produto: " + o[0].toString() + "\n");
                    msgEmailBody.append("Quantidade vendida: " + o[1].toString() + "\n");
                    msgEmailBody.append("Valor bruto R$ " + o[2].toString() + "\n");
                    msgEmailBody.append("-------------"  + "\n");
                }
            }
        }
        emailService.sendEmail("userInfo", msgEmailBody.toString(), EEmailDetails.RELATORIO_VENDAS_PARCIAL.getEEmailDetails());
    }
}
