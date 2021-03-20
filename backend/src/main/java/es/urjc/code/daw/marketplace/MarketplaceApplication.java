package es.urjc.code.daw.marketplace;

import es.urjc.code.daw.marketplace.util.TimeUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

@Configuration
@EnableSpringDataWebSupport
@EnableJpaRepositories
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class MarketplaceApplication {

    public static void main(String[] args) {
        List<Integer> salesPerDayInWeek = new LinkedList<>();
        Date startDate = TimeUtils.firstDayOfCurrentWeek();
        for(int dayIncrement = 0; dayIncrement < 7; dayIncrement++) {
            Date endDate = TimeUtils.sumDaysToDate(startDate, 1);
            Date exclusiveEndDate = TimeUtils.removeSecondsFromDate(endDate, 1);
            System.out.println(startDate + " " + exclusiveEndDate);
            salesPerDayInWeek.add(0);
            startDate = endDate;
        }
        //SpringApplication.run(MarketplaceApplication.class, args);
    }

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"));
    }

}
