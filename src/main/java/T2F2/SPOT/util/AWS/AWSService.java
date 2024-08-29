package T2F2.SPOT.util.AWS;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class AWSService {

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    private final S3Client s3Client;
    private final S3Presigner presigner;

    public String getPresignUrl(String filename){
        if(filename == null || filename.equals("")) {
            return null;
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        log.info(getObjectRequest.toString());
        log.info(putObjectRequest.toString());

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5)) // presignedURL 5분간 접근 허용
                .getObjectRequest(getObjectRequest)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();

        log.info(getObjectPresignRequest.toString());
        log.info(putObjectPresignRequest.toString());

        PresignedGetObjectRequest presignedGetObjectRequest = presigner
                .presignGetObject(getObjectPresignRequest);

        PresignedPutObjectRequest presignedPutObjectRequest = presigner
                .presignPutObject(putObjectPresignRequest);

        String url = presignedPutObjectRequest.url().toString();
//        String url = presignedGetObjectRequest.url().toString();
        log.info(url);
//        presigner.close(); // presigner를 닫고 획득한 모든 리소스를 해제
        return url;
    }

}
