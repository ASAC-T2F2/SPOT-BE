package T2F2.SPOT.util.AWS;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/aws")
@RequiredArgsConstructor
public class AWSController {

    private final AWSService awsService;
//    @GetMapping("/geturl")
//    public ResponseEntity<Map<String, String>> getS3Urls(@RequestParam List<String> filenames) {
//
//        if (filenames == null || filenames.isEmpty()) {
//            throw new IllegalArgumentException("Filenames must not be null or empty");
//        }
//
//        Map<String, String> urls = filenames.stream()
//                .collect(Collectors.toMap(
//                        filename -> filename,
//                        filename -> awsService.createPresignedGetUrl(filename)
//                ));
//
//        return ResponseEntity.ok(urls);
//    }

    @GetMapping("/geturl")
    public ResponseEntity<Map<String, String>> getCloudFrontUrls(@RequestParam List<String> filenames) {
        if (filenames == null || filenames.isEmpty()) {
            throw new IllegalArgumentException("Filenames must not be null or empty");
        }

        Map<String, String> urls = filenames.stream()
                .collect(Collectors.toMap(
                        filename -> filename,
                        filename -> awsService.getCloudFrontUrl(filename)
                ));

        return ResponseEntity.ok(urls);
    }

    @GetMapping("/puturl")
    public ResponseEntity<Map<String, String>> getPostUrl(@RequestParam List<String> filenames) {

        if (filenames == null || filenames.isEmpty()) {
            throw new IllegalArgumentException("Filename must not be null or empty");
        }

        Map<String, String> urls = filenames.stream()
                .collect(Collectors.toMap(
                        filename -> filename,
                        filename -> awsService.createPresignedUrl(filename)
                ));

        return ResponseEntity.ok(urls);
    }
//    @GetMapping("file/{filename}")
//    public ResponseEntity<String> getFile(@PathVariable(value = "filename") String filename) throws IOException {
//
//        String url = awsService.(filename);
//
//        return new ResponseEntity<>(url, HttpStatus.OK);
//    }
}
