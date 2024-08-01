package com.incture.cpm.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.History;
import com.incture.cpm.Repo.HistoryRepo;

@Service
public class HistoryService {
    @Autowired
    HistoryRepo historyRepo;

    public History logHistory(String entityId, String entityType, String logEntry, String userName) {
        History historyEntry = new History();
        historyEntry.setEntityId(entityId);
        historyEntry.setEntityType(entityType);
        historyEntry.setLogEntry(logEntry);
        historyEntry.setUserName(userName);

        return historyRepo.save(historyEntry);
    }

    public List<History> getAllHistoryByEntityIdAndEntityType(String entityId, String entityType) {
        return historyRepo.findByEntityIdAndEntityType(entityId, entityType);
    }

    public List<History> getAllHistoryByEntityType(String entityType) {
        return historyRepo.findAllByEntityType(entityType); 
    }

    public List<History> getAllHistory() {
        return historyRepo.findAll();
    }

    public void deleteAllByEntityType(String entityType) {
        historyRepo.deleteAllByEntityType(entityType);
    }
}
