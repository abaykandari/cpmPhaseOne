package com.incture.cpm.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.incture.cpm.Entity.AssessmentLevelFinal;
import com.incture.cpm.Entity.File;
import com.incture.cpm.Repo.AssessmentLevelFinalRepository;
import com.incture.cpm.Repo.FileRepository;
import com.incture.cpm.helper.DateFormatter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import java.sql.Blob;
import java.sql.SQLException;

@Service
public class LoiService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AssessmentLevelFinalRepository assessmentLevelFinalRepository;

    @Autowired
    private FileRepository fileRepository;

    public String generateLetterOfIntent(List<Long> candidateIds, String fte,
            String ctc,
            String performancePay,
            String eklavyaBonus) {
        try {
            Optional<File> pdfDataOptional = fileRepository.findByFileName("Letter of Intent");
            if (pdfDataOptional.isPresent()) {
                File pdfData = pdfDataOptional.get();
                Blob blob = new SerialBlob(pdfData.getPdf());

                List<AssessmentLevelFinal> candidateList = assessmentLevelFinalRepository.findAllById(candidateIds);
                for (AssessmentLevelFinal candidate : candidateList) {
                    byte[] docFile = blob.getBytes(1l, (int) blob.length());
                    if (docFile != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(docFile))) {
                            PDPage page = document.getPage(0);
                            PDPageContentStream contentStream = new PDPageContentStream(document, page,
                                    PDPageContentStream.AppendMode.APPEND, true, true);
                            contentStream.beginText();
                            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                            contentStream.newLineAtOffset(80, 745); // Adjust position
                            contentStream.showText(" " + DateFormatter.formatDateWithSuffix(LocalDate.now()));
                            contentStream.newLineAtOffset(0, -35);
                            contentStream.showText(candidate.getCandidateName() + ",");
                            // for fte
                            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                            contentStream.newLineAtOffset(80, -326);
                            contentStream.showText("INR " + fte);

                            // for ctc
                            contentStream.setFont(PDType1Font.HELVETICA, 10);
                            contentStream.newLineAtOffset(-57, -13);
                            contentStream.showText("INR " + ctc + "/- per annum");

                            // performance pay
                            contentStream.setFont(PDType1Font.HELVETICA, 10);
                            contentStream.newLineAtOffset(32, -14);
                            contentStream.showText("INR " + performancePay
                                    + " payable end of two years from date of joining.");

                            // ek-lavya bonus
                            contentStream.setFont(PDType1Font.HELVETICA, 10);
                            contentStream.newLineAtOffset(-13, -14);
                            contentStream.showText("INR " + eklavyaBonus
                                    + " payable end of three years from date of joining.");

                            contentStream.endText();
                            contentStream.close();
                            document.save(baos);
                        }
                        byte[] updatedPdfBytes = baos.toByteArray();
                        candidate.setLoi(new SerialBlob(updatedPdfBytes));
                        candidate.setLoiGenerated(true);
                        assessmentLevelFinalRepository.save(candidate);
                        return "Letter Of Intent Generated Successfully ";
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error Occurred while Generating Letter Of Intent";
    }

    private boolean sendEmailWithAttachment(AssessmentLevelFinal to) throws MessagingException, SQLException {
        try {
            byte[] pdfBytes = to.getLoi().getBytes(1l, (int) to.getLoi().length());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to.getEmail());
            helper.setSubject("Letter Of Intent");
            helper.setText("Dear " + to.getCandidateName()
                    + ",\n\nCongratulations.\n\nPlease find the attached Letter of Intent of Incture." +
                    "\n\nYou are required to take the printout of the same Letter of Intent and do as per the following.  \r\n"
                    +
                    "\r\n" +
                    "1. You are required to write Offer Accepted along with signature & date on the Page of LOI (Below right-hand side).  \r\n"
                    +
                    "2. Scan the document (Pdf file only) and send it back on test.hr@incture.com latest by 11:00AM 3rd December 2024.  \r\n"
                    +
                    "3. You are required to share your CV [PDF Format] \n\n\nFor any clarification, please feel free to contact me.\n\n\nAll the best.\n\nTest HR,\n"
                    +
                    "Hiring and Talent Acquisition Department" +
                    "\n" +
                    "Incture Technologies, Bengaluru" +
                    "\n" +
                    "9876543210");
            helper.addAttachment("LetterOfIntent.pdf", new ByteArrayResource(pdfBytes));
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    public String sendLetterOfIntent(List<Long> candidateIds) {
        long finalCount = 0;
        try {
            List<AssessmentLevelFinal> candidateList = assessmentLevelFinalRepository.findAllById(candidateIds);
            long initialCount = candidateList.size();
            for (AssessmentLevelFinal candidate : candidateList) {

                boolean ans = sendEmailWithAttachment(candidate);
                if (ans == true) {
                    finalCount++;
                    candidate.setLoiSent(ans);
                    assessmentLevelFinalRepository.save(candidate);
                }

            }
            if (initialCount == finalCount) {
                return "Letter of Intent Rolled Out Successfully";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ("Error Occurred while Sending Mail\n" + "Mail sent to only " +
                finalCount + " Candidates");
    }
}
