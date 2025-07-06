package com.picus.core.expert.domain.model;

import com.picus.core.expert.domain.model.vo.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Expert {
    private String expertNo;


    private String backgroundImageKey;
    private String intro;
    private String career;
    private List<ActivityArea> activityAreas = new ArrayList<>();
    private String activityDuration;
    private int activityCount;
    private LocalDateTime recentlyActivityAt;
    private List<String> portfolioLinks = new ArrayList<>();
    private ApprovalStatus approvalStatus;
    // 스튜디오 정보
    private Studio studio;

    private List<Skill> skills = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();


}
