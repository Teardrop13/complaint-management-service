package pl.teardrop.complaintmanagementservice.dto;

import java.util.List;

public record ComplaintListDto(
        List<ComplaintDto> complaints,
        Integer page,
        Integer pageSize,
        Integer count
) {

}
