package com.picus.core.old.domain.post.infra.batch;

import com.picus.core.old.domain.post.domain.entity.view.ViewCount;
import com.picus.core.old.domain.post.domain.entity.view.BackupViewCount;
import org.springframework.batch.item.ItemProcessor;

public class ViewCountProcessor implements ItemProcessor<ViewCount, BackupViewCount> {

    @Override
    public BackupViewCount process(ViewCount primary) throws Exception {
        return new BackupViewCount(
                ViewCount.extractPostId(primary.getKey()),
                primary.getCount()
        );
    }
}
