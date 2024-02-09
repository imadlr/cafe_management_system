package cf.serviceImpl;

import cf.JWT.JwtFilter;
import cf.POJO.Bill;
import cf.constents.CafeConstants;
import cf.dao.BillDao;
import cf.services.BillService;
import cf.utils.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Transactional
@AllArgsConstructor
public class BillServiceImpl implements BillService {

    private BillDao billDao;
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try {
            String fileName;
            if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
                fileName = (String) requestMap.get("uuid");
            } else {
                fileName = CafeUtils.getUUID();
                requestMap.put("uuid", fileName);
                insertBill(requestMap);
            }
            String data = "Name : " + requestMap.get("name") + "\n" +
                    "Phone : " + requestMap.get("phone") + "\n" +
                    "Email : " + requestMap.get("email") + "\n" +
                    "Payment Method : " + requestMap.get("paymentMethod") + "\n";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));

            document.open();
            setRectangleInPdf(document);

            Paragraph title = new Paragraph("Cafe Management", getFont("Header"));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph paragraph = new Paragraph(data + "\n \n", getFont("Data"));
            document.add(paragraph);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            addTableHeader(table);

            JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
            for (int i = 0; i < jsonArray.length(); i++) {
                addRow(table, CafeUtils.getMapFromJson(jsonArray.getString(i)));
            }
            document.add(table);

            Paragraph footer = new Paragraph("Total : " + requestMap.get("total") + "\n" +
                    "Thank You For Your Visit !!");
            document.add(footer);

            document.close();
            return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> bills = new ArrayList<>();
        if (jwtFilter.isAdmin()) {
            bills = billDao.getAllBills();
        } else {
            bills = billDao.getBillByUsername(jwtFilter.getCurrentUserEmail());
        }
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try {
            byte[] byteArray = new byte[0];
            if (!requestMap.containsKey("uuid")) {
                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
            } else {
                String filePath = CafeConstants.STORE_LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";
                if (CafeUtils.isFileExist(filePath)) {
                    byteArray = getByteArray(filePath);
                    return new ResponseEntity<>(byteArray, HttpStatus.OK);
                } else {
                    requestMap.put("isGenerate", false);
                    generateReport(requestMap);
                    byteArray = getByteArray(filePath);
                    return new ResponseEntity<>(byteArray, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deletBill(Integer billId) {
        try {
            if (jwtFilter.isAdmin()) {
                if (billDao.findById(billId).isPresent()) {
                    billDao.deleteById(billId);
                    return CafeUtils.getResponseEntity("Bill deleted successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Bill doesn't exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity("You are not authorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private byte[] getByteArray(String filePath) throws IOException {
        File initFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    private void addRow(PdfPTable table, Map<String, Object> data) {
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Name", "Category", "Quantity", "Price", "Total").forEach(col -> {
            PdfPCell head = new PdfPCell();
            head.setBackgroundColor(BaseColor.LIGHT_GRAY);
            head.setBorderWidth(2);
            head.setPhrase(new Phrase(col));
            head.setBackgroundColor(BaseColor.GREEN);
            head.setHorizontalAlignment(Element.ALIGN_CENTER);
            head.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(head);
        });
    }

    private Font getFont(String type) {
        switch (type) {
            case "Header" -> {
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            }
            case "Data" -> {
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            }
            default -> {
                return new Font();
            }
        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        document.add(rect);
    }

    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setPhone((String) requestMap.get("phone"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Integer.parseInt((String) requestMap.get("total")));
            bill.setProductDetails((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUserEmail());
            billDao.save(bill);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
