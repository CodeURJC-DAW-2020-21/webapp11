package es.urjc.code.daw.marketplace.service;


import es.urjc.code.daw.marketplace.domain.Order;


import javax.servlet.http.HttpServletResponse;


public interface PdfExporterService {
    void exportPdf(HttpServletResponse response, Order order) throws Exception;
}
