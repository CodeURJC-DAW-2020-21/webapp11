package es.urjc.code.daw.marketplace.service;

import es.urjc.code.daw.marketplace.domain.Order;
import javax.servlet.http.HttpServletResponse;

public interface PdfExporterService {

    /**
     * Exports the order and attaches the PDF to the current controller's response.
     *
     * @param response the current controller's response
     * @param order the order to be exported to PDF
     * @throws Exception when the exportation has failed for any reason
     */
    void exportPdf(HttpServletResponse response, Order order) throws Exception;

}
