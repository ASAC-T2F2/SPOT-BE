package T2F2.SPOT.util.AWS;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/aws")
@RequiredArgsConstructor
public class AWSController {

    private final AWSService awsService;

    @GetMapping("/geturl")
    public ResponseEntity<String> getS3Url(@RequestParam String filename) {
        String url = awsService.createPresignedGetUrl(filename);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/posturl")
    public ResponseEntity<String> getPostUrl(@RequestParam String filename) {
        String url = awsService.createPresignedUrl(filename);
        return ResponseEntity.ok(url);
    }
//    @GetMapping("file/{filename}")
//    public ResponseEntity<String> getFile(@PathVariable(value = "filename") String filename) throws IOException {
//
//        String url = awsService.(filename);
//
//        return new ResponseEntity<>(url, HttpStatus.OK);
//    }
}
