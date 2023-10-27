package com.exam.test;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;


//메일 테스트
public class AmazonSESTest {
    //SES Key, 이메일
    final String accessKey ="ACCESSKEY";
    final String secretKey = "SECRETKEY";
    final String fromEmailAddress = "from@email.com";

    //aws credentials
    final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
    final AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(basicAWSCredentials);

    @Test
    @DisplayName("SES MAIL 전송 성공")
    void mailSend() {
        AmazonSimpleEmailService amazonSimpleEmailService = AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(awsStaticCredentialsProvider)
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();

        List<String> receivers = new ArrayList<>();

        receivers.add("hongjae30@gmail.com");

        SendEmailRequest sendEmailRequest = toSendRequestDto("ses test", "ses test본문내용", receivers);

        SendEmailResult sendEmailResult = amazonSimpleEmailService.sendEmail(sendEmailRequest);
        System.out.println(sendEmailResult.getSdkHttpMetadata().toString());
        System.out.println(sendEmailResult.getSdkHttpMetadata().getAllHttpHeaders().toString());
        System.out.println(sendEmailResult.getSdkHttpMetadata().getHttpStatusCode());
        System.out.println("######################## HTTP METADATA #######################");
        System.out.println(sendEmailResult.getSdkResponseMetadata().toString());
        System.out.println("######################## RESPONSE METADATA #######################");
    }

    //SendEmailRequest 객체형태로 맞춰주기
    public SendEmailRequest toSendRequestDto(String subject, String content, List<String> receivers) {
        //목적지 설정
        Destination destination = new Destination()
                .withToAddresses(receivers);

        //제목, 본문 설정
        Message message = new Message().withSubject(createContent(subject))
                .withBody(new Body().withHtml(createContent(content)));

        return new SendEmailRequest().withSource(fromEmailAddress).withDestination(destination)
                .withMessage(message);
    }

    //본문 형식 설정
    private Content createContent(String text) {
        return new Content().withCharset("UTF-8").withData(text);
    }
}
