package T2F2.SPOT.util.AWS;

import T2F2.SPOT.domain.user.service.AuthService;
import T2F2.SPOT.util.exception.CustomException;
import T2F2.SPOT.util.exception.error_code.UserErrorCode;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AWSService {

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    private final S3Client s3Client;
    private final S3Presigner presigner;
    private final AuthService authService;

    private String generateUniqueFileName(Long userId, String filename) {
        String uuid = UUID.randomUUID().toString();
        return userId + "/" + uuid + "_" + filename;
    }
    public String createPresignedGetUrl(String fileName) {

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5)) // URL 만료시간
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
        log.info("Presigned URL : [{}]", presignedRequest.url().toString());
        log.info("HTTP method : [{}]", presignedRequest.httpRequest().method());

        return presignedRequest.url().toExternalForm();
    }

    public String createPresignedUrl(String filename) {

        Long userId = authService.getAuthenticatedUserId();
        if(userId == null) {
            throw new CustomException(UserErrorCode.NOT_FOUND);
        }

        String uniqueFileName = generateUniqueFileName(userId, filename);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueFileName)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))  // The URL expires in 10 minutes.
                .putObjectRequest(objectRequest)
                .build();


        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
        String myURL = presignedRequest.url().toString();
        log.info("Presigned URL to upload a file to: [{}]", myURL);
        log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

        return presignedRequest.url().toExternalForm();
    }

//    public String getPresignUrl(String filename){
//        if(filename == null || filename.equals("")) {
//            return null;
//        }
//
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(filename)
//                .build();
//        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(filename)
//                .build();
//
//        log.info(getObjectRequest.toString());
//        log.info(putObjectRequest.toString());
//
//        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(5)) // presignedURL 5분간 접근 허용
//                .getObjectRequest(getObjectRequest)
//                .build();
//
//        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(5))
//                .putObjectRequest(putObjectRequest)
//                .build();
//
//        log.info(getObjectPresignRequest.toString());
//        log.info(putObjectPresignRequest.toString());
//
//        PresignedGetObjectRequest presignedGetObjectRequest = presigner
//                .presignGetObject(getObjectPresignRequest);
//
//        PresignedPutObjectRequest presignedPutObjectRequest = presigner
//                .presignPutObject(putObjectPresignRequest);
//
//        String url = presignedPutObjectRequest.url().toString();
////        String url = presignedGetObjectRequest.url().toString();
//        log.info(url);
////        presigner.close(); // presigner를 닫고 획득한 모든 리소스를 해제
//        return url;
//    }

}
