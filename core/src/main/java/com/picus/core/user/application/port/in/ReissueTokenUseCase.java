package com.picus.core.user.application.port.in;

import com.picus.core.user.application.port.in.result.ReissueTokenResult;

public interface ReissueTokenUseCase {

    ReissueTokenResult reissue(String refreshToken, String userNo);

}
