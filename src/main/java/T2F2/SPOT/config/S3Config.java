package T2F2.SPOT.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Value("${AWS_S3_ACCESSKEY}")
    private String accessKey;
    @Value("${AWS_S3_SECRETKEY}")
    private String secretKey;
    @Value("${AWS_REGION}")
    private String region;

    @Bean
    public AwsCredentials basicAWSCredentials() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    //클라이언트가 S3와 직접 상호작용하도록 지원
    @Bean
    public S3Presigner s3Presigner(AwsCredentials awsCredentials) {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    //서버에서 S3에 직 작업이 필요할 때 사용
    @Bean
    public S3Client s3Client (AwsCredentials awsCredentials) {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

}
