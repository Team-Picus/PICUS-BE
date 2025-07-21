package com.picus.core.price.adapter.in.web.data.request;

import java.util.List;

public record ApplyPriceChangesWebRequest(
    List<PriceWebRequest> prices
) {}
