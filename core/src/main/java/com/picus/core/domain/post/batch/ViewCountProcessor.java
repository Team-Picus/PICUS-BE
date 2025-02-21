package com.picus.core.domain.post.batch;

import com.picus.core.domain.post.entity.view.ViewCount;
import com.picus.core.domain.post.entity.view.BackupViewCount;
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
