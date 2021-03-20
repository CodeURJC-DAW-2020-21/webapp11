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

/**
 * A simple implementation for the {@link PdfExporterService}.
 */
@Component
public class PdfExporterServiceImpl implements PdfExporterService {

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Total Cost", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase("First Name", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase("Surname", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase("Creation Date", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase("Expiration Date", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, Order order) {
        table.addCell("$" + order.getFinalCost());
        table.addCell(order.getUser().getFirstName());
        table.addCell(order.getUser().getSurname());
        table.addCell(order.getCreationDate().toString());
        table.addCell(order.getExpiryDate().toString());
    }

    public void exportPdf(HttpServletResponse response, Order order) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        String category = order.getProduct().getCategory();
        long orderId = order.getId();

        final String title = String.format("Thanks for purchasing a %s server! Your order number is #%d", category, orderId);
        Paragraph p = new Paragraph(title, createFontWithSize(12, Color.BLUE));
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.5f, 2.5f, 2.5f, 2.5f, 2.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table, order);
        document.add(table);

        String ram = order.getProduct().getRam();
        String cores = order.getProduct().getCores();
        String storage = order.getProduct().getStorage();
        String transfer = order.getProduct().getTransfer();

        final String title3 = "Your product details are:";
        Paragraph p3 = new Paragraph(title3, createFontWithSize(12, Color.BLUE));
        p3.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p3);

        final String title1 = String.format("%s RAM, %s CORES, %s STORAGE and %s TRANSFER", ram, cores, storage, transfer);
        Paragraph p1 = new Paragraph(title1, createFontWithSize(12, Color.BLUE));
        p1.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p1);

        final String title2 = "Any doubt feel free to reach us at: dawhostservices@gmail.com";
        Paragraph p2 = new Paragraph(title2, createFontWithSize(12, Color.BLUE));
        p2.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p2);

        document.close();
    }

    public Font createFontWithSize(int size, Color color) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(size);
        font.setColor(color);
        return font;
    }

}
