package inet.dmsx.DMSXClient.dto;

public record MemoryDto(

        String name,
        long total,
        long free,
        long used
) {
}
