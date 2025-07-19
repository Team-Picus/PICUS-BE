package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.command.ExpertBasicInfoCommandRequest;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.user.application.port.out.UserCommandPort;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import com.picus.core.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class ExpertInfoCommandServiceTest {
    
    final UserQueryPort userQueryPort = Mockito.mock(UserQueryPort.class);
    final UserCommandPort userCommandPort = Mockito.mock(UserCommandPort.class);
    final LoadExpertPort loadExpertPort = Mockito.mock(LoadExpertPort.class);
    final UpdateExpertPort updateExpertPort = Mockito.mock(UpdateExpertPort.class);
    final ExpertInfoCommandService expertInfoCommandService = 
            new ExpertInfoCommandService(userQueryPort, userCommandPort, loadExpertPort, updateExpertPort);

    @Test
    @DisplayName("Expert와 User 기본 정보가 모두 수정되는 경우 update 호출이 수행된다.")
    void updateExpertBasicInfo_success() {
        // given
        String userNo = "USER001";
        String expertNo = "EXP001";

        ExpertBasicInfoCommandRequest request = ExpertBasicInfoCommandRequest.builder()
                .currentUserNo(userNo)
                .backgroundImageFileKey("new-background")
                .link("https://new.link")
                .intro("New intro")
                .nickname("NewNickname")
                .profileImageFileKey("new-profile-img")
                .build();

        Expert expert = Mockito.mock(Expert.class);

        UserWithProfileImageDto userWithProfile = UserWithProfileImageDto.builder()
                .expertNo(expertNo)
                .nickname("OldNickname")
                .profileImageFileKey("old-profile-img")
                .build();

        User user = Mockito.mock(User.class);
        Mockito.when(user.getExpertNo()).thenReturn(expertNo);

        given(userQueryPort.findById(userNo)).willReturn(user);
        given(loadExpertPort.findById(expertNo)).willReturn(Optional.of(expert));
        given(userQueryPort.findUserInfoByExpertNo(expertNo)).willReturn(Optional.of(userWithProfile));

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(userQueryPort).should().findById(userNo);
        then(loadExpertPort).should().findById(expertNo);
        then(expert).should().updateBasicInfo("new-background", "https://new.link", "New intro");
        then(updateExpertPort).should().updateExpert(expert);
        then(userQueryPort).should().findUserInfoByExpertNo(expertNo);
        then(userCommandPort).should().updateNicknameAndImageByExpertNo(Mockito.argThat(updatedDto ->
                updatedDto.nickname().equals("NewNickname") &&
                        updatedDto.profileImageFileKey().equals("new-profile-img") &&
                        updatedDto.expertNo().equals(expertNo)
        ));
    }

    @Test
    @DisplayName("Expert 정보만 수정되는 경우, Expert만 update 된다.")
    void updateExpertBasicInfo_onlyExpertChanged() {
        // given
        String userNo = "USER001";
        String expertNo = "EXP001";

        ExpertBasicInfoCommandRequest request = ExpertBasicInfoCommandRequest.builder()
                .currentUserNo(userNo)
                .backgroundImageFileKey("bg-key") // expert 관련 필드만 있음
                .link("https://new.link")
                .intro("new intro")
                .build();

        Expert expert = Mockito.mock(Expert.class);
        User user = Mockito.mock(User.class);

        given(userQueryPort.findById(userNo)).willReturn(user);
        given(user.getExpertNo()).willReturn(expertNo);
        given(loadExpertPort.findById(expertNo)).willReturn(Optional.of(expert));

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(userQueryPort).should().findById(userNo);
        then(loadExpertPort).should().findById(expertNo);
        then(expert).should().updateBasicInfo("bg-key", "https://new.link", "new intro");
        then(updateExpertPort).should().updateExpert(expert);
        then(userCommandPort).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("User 정보만 수정되는 경우, User만 update 된다.")
    void updateExpertBasicInfo_onlyUserChanged() {
        // given
        String userNo = "USER002";
        String expertNo = "EXP002";

        ExpertBasicInfoCommandRequest request = ExpertBasicInfoCommandRequest.builder()
                .currentUserNo(userNo)
                .nickname("UpdatedNickname")
                .profileImageFileKey("updated-profile-img")
                .build();

        User user = Mockito.mock(User.class);
        Mockito.when(user.getExpertNo()).thenReturn(expertNo);

        UserWithProfileImageDto userWithProfile = UserWithProfileImageDto.builder()
                .expertNo(expertNo)
                .nickname("OldNickname")
                .profileImageFileKey("old-img")
                .build();

        given(userQueryPort.findById(userNo)).willReturn(user);
        given(userQueryPort.findUserInfoByExpertNo(expertNo)).willReturn(Optional.of(userWithProfile));

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(userQueryPort).should().findById(userNo);
        then(userQueryPort).should().findUserInfoByExpertNo(expertNo);
        then(userCommandPort).should().updateNicknameAndImageByExpertNo(Mockito.argThat(dto ->
                dto.nickname().equals("UpdatedNickname") &&
                        dto.profileImageFileKey().equals("updated-profile-img") &&
                        dto.expertNo().equals(expertNo)
        ));
        then(loadExpertPort).shouldHaveNoInteractions();
        then(updateExpertPort).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("모든 값이 null일 경우 아무 업데이트도 수행되지 않는다.")
    void updateExpertBasicInfo_nothingChanged() {
        // given
        String userNo = "USER003";

        ExpertBasicInfoCommandRequest request = ExpertBasicInfoCommandRequest.builder()
                .currentUserNo(userNo)
                .build(); // 모든 필드가 null

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(userQueryPort).shouldHaveNoInteractions();
        then(loadExpertPort).shouldHaveNoInteractions();
        then(updateExpertPort).shouldHaveNoInteractions();
        then(userCommandPort).shouldHaveNoInteractions();
    }
}