package com.picus.core.shared;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class IntegrationTestSupport {
    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected TokenProvider tokenProvider;

    protected <T> HttpEntity<T> settingWebRequest(UserEntity userEntity, T webRequest) {
        String accessToken = tokenProvider.createAccessToken(userEntity.getUserNo(), userEntity.getRole().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(webRequest, headers);
    }

    protected void commitTestTransaction() {
        TestTransaction.flagForCommit();  // 지금까지 열린 테스트 트랜잭션을 커밋
        TestTransaction.end(); // 실제 커밋 수행
    }
}
