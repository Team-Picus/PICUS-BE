package com.picus.core.domain.post.infra.batch;

import com.picus.core.domain.post.domain.entity.view.BackupViewCount;
import com.picus.core.domain.post.domain.repository.BackupViewCountRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class BackupViewCountItemWriter implements ItemWriter<BackupViewCount> {

    private final BackupViewCountRepository backupViewCountRepository;

    public BackupViewCountItemWriter(BackupViewCountRepository backupViewCountRepository) {
        this.backupViewCountRepository = backupViewCountRepository;
    }

    @Override
    public void write(Chunk<? extends BackupViewCount> chunk) throws Exception {
        backupViewCountRepository.saveAll(chunk.getItems());
    }
}
