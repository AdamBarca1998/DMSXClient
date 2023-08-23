package inet.dmsx.DMSXClient.dto;

import java.util.List;

public record HealthDto(
        String state,
        double cpuUsage,
        List<MemoryDto> memories
) {
}
