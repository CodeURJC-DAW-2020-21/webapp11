package es.urjc.code.daw.marketplace.service;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import es.urjc.code.daw.marketplace.domain.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;

@Component
public class PdfExporterServiceImpl implements PdfExporterService {

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Accumulative Cost", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("First Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Surname", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Creation Date", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Expiration Date", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, Order order) {
        table.addCell(String.valueOf(order.getFinalCost()));
        table.addCell(order.getUser().getFirstName());
        table.addCell(order.getUser().getSurname());
        table.addCell(order.getCreationDate().toString());
        table.addCell(String.valueOf(order.getExpiryDate().toString()));
    }

    public void exportPdf(HttpServletResponse response, Order order) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Service " + order.getProduct().getCategory() + " " + order.getId() + " receipt", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table, order);

        document.add(table);
        document.close();
    }

}
