package com.incture.cpm.Controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.incture.cpm.Entity.AssessmentLevelFinal;
import com.incture.cpm.Repo.AssessmentLevelFinalRepository;
import com.incture.cpm.Service.LoiService;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/loi")
@CrossOrigin("*")
public class LoiController {

    @Autowired
    private LoiService loiService;

    @Autowired
    private AssessmentLevelFinalRepository assessmentLevelFinalRepository;

    @PostMapping("/generateLOI")
    public ResponseEntity<String> generateLOI(@RequestParam List<Long> candidateIds,
            @RequestParam String fte,
            @RequestParam String ctc,
            @RequestParam String performancePay,
            @RequestParam String eklavyaBonus)
            throws SerialException, SQLException, MessagingException {
        try {
            loiService.generateLetterOfIntent(candidateIds, fte, ctc, performancePay, eklavyaBonus);
            return ResponseEntity.ok("Letter of Intent Generated Successfully!!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating documents: " + e.getMessage());
        }
    }

    // @PostMapping("/sendLOI")
    // public ResponseEntity<String> sendLOI(@RequestBody List<Long> candidateIds) {
    // String result = loiService.sendLetterOfIntent(candidateIds);
    // return new ResponseEntity<>(result, HttpStatus.OK);
    // }

    // @PostMapping("/sendLOI")
    // public CompletableFuture<ResponseEntity<String>>
    // sendLetterOfIntent(@RequestBody List<Long> candidateIds) {
    // return loiService.sendLetterOfIntent(candidateIds)
    // .thenApply(ResponseEntity::ok);
    // }
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostMapping("/sendLOI")
    public ResponseEntity<String> sendLetterOfIntent(@RequestBody List<Long> candidateIds) {
        CompletableFuture.runAsync(() -> {
            String result = loiService.sendLetterOfIntent(candidateIds).join();
            emitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("completion").data(result));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            });
        });

        return ResponseEntity.accepted().body("Letter of Intent sending process has started.");
    }

    @GetMapping("/status")
    public SseEmitter streamStatus() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    @GetMapping("/viewloi/{candidateId}")
    public ResponseEntity<byte[]> getMarksheet(@PathVariable Long candidateId) throws IOException {
        // Retrieve Talent object from the service layer
        Optional<AssessmentLevelFinal> candidate = assessmentLevelFinalRepository.findById(candidateId);

        if (candidate != null && candidate.get().getLoi() != null) {
            try {
                // Retrieve the PDF data from the Talent object
                Blob loiBlob = candidate.get().getLoi();
                byte[] pdfData = loiBlob.getBytes(1, (int) loiBlob.length());

                // Set appropriate response headers for PDF content
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(
                        ContentDisposition.builder("inline").filename("Letter Of Intent.pdf").build());

                return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
            } catch (SQLException e) {
                // Handle exceptions appropriately
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
