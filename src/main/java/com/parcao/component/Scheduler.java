package com.parcao.component;

import com.parcao.models.EEmailDetails;
import com.parcao.services.EmailService;
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

//https://crontab.cronhub.io/
//@Component
public class Scheduler {

    final EmailService emailService;
    final SchedulerService schedulerService;

    public Scheduler(EmailService emailService, SchedulerService schedulerService) {
        this.emailService = emailService;
        this.schedulerService = schedulerService;
    }

    //@Scheduled(cron = "0/15 * * * * *")//teste
    @Scheduled(cron = "0 0 8,10,12,14,16,18,20 * * *")
    public void countVendasByPagamentoPeriodo() throws ParseException {
        String data = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<Object[]> objects = schedulerService.countVendasByPagamentoPeriodo(Util.dateToInicialTimestamp(data), Util.dateToFinalTimestamp(data));
        StringBuilder msgEmailBody = new StringBuilder();
        msgEmailBody.append("Data Referência " + Util.dateToPTBR() + "\n");
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
}
