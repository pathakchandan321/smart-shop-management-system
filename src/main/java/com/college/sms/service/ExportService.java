package com.college.sms.service;

import com.college.sms.entity.Student;
import com.college.sms.repository.StudentRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public byte[] exportStudentsPdf() throws Exception {
        List<Student> students = studentRepository.findAll();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter.getInstance(doc, baos);
        doc.open();
        doc.add(new Paragraph("Student Management System - Student Report"));
        doc.add(new Paragraph(" "));
        for (Student s : students) {
            doc.add(new Paragraph(String.format("%s %s | ID: %s | %s",
                    s.getFirstName(), s.getLastName(), s.getStudentId(),
                    s.getCourse() != null ? s.getCourse().getCourseName() : "N/A")));
        }
        doc.close();
        return baos.toByteArray();
    }

    @Transactional(readOnly = true)
    public byte[] exportStudentsExcel() throws Exception {
        List<Student> students = studentRepository.findAll();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Students");
            Row header = sheet.createRow(0);
            String[] cols = {"Student ID", "First Name", "Last Name", "Email", "Course", "Status"};
            for (int i = 0; i < cols.length; i++) header.createCell(i).setCellValue(cols[i]);
            int rowNum = 1;
            for (Student s : students) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(s.getStudentId());
                row.createCell(1).setCellValue(s.getFirstName());
                row.createCell(2).setCellValue(s.getLastName());
                row.createCell(3).setCellValue(s.getEmail());
                row.createCell(4).setCellValue(s.getCourse() != null ? s.getCourse().getCourseName() : "");
                row.createCell(5).setCellValue(s.getStatus().name());
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        }
    }
}
