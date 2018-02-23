package org.fs.sync.transfer.handler;

import java.util.Map;

public interface FileHandler {
    void handle(Map<String, Object> infoMap);
}
