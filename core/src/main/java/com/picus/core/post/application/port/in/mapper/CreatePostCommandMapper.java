package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.command.CreatePostCommand;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CreatePostCommandMapper {

    public Post toDomain(CreatePostCommand appReq, String expertNo) {
        
        List<String> packageNos = new ArrayList<>();
        Set<PostThemeType> postThemeTypes = new HashSet<>(); // 중복된 테마가 넘어올 수 있으므로 Set
        Set<SnapSubTheme> snapSubThemes = new HashSet<>();
        setPackageInfo(appReq.packages(), packageNos, postThemeTypes, snapSubThemes);

        return Post.builder()
                .packageNos(packageNos)
                .authorNo(expertNo)
                .title(appReq.title())
                .postThemeTypes(postThemeTypes.stream().toList())
                .snapSubThemes(snapSubThemes.stream().toList())
                .oneLineDescription(appReq.oneLineDescription())
                .detailedDescription(appReq.detailedDescription())
                .postMoodTypes(appReq.postMoodTypes())
                .spaceType(appReq.spaceType())
                .spaceAddress(appReq.spaceAddress())
                .isPinned(false) // 고정여부 초기값은 false
                .postImages(appReq.postImages())
                .build();
    }


    private void setPackageInfo(List<CreatePostCommand.PackageCommand> packages, List<String> packageNos,
                                Set<PostThemeType> postThemeTypes, Set<SnapSubTheme> snapSubThemes) {
        for (CreatePostCommand.PackageCommand packageCommand : packages) {
            packageNos.add(packageCommand.packageNo());
            try {
                postThemeTypes.add(PostThemeType.valueOf(packageCommand.packageThemeType()));
                snapSubThemes.add(SnapSubTheme.valueOf(packageCommand.snapSubTheme()));
            } catch (IllegalArgumentException e) {
                throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
            }
        }
    }
}
