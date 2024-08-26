package T2F2.SPOT.util.AWS;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/aws")
@RequiredArgsConstructor
public class AWSController {

    private final AWSService awsService;

    @GetMapping("file/{filename}")
    public ResponseEntity<String> getFile(@PathVariable(value = "filename") String filename) throws IOException {

        String url = awsService.getPresignUrl(filename);

        return new ResponseEntity<>(url, HttpStatus.OK);
    }
}
