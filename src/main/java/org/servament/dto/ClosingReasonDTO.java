package org.servament.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClosingReasonDTO {

    @NotBlank
    @Size(max = 1000)
    public String note;
}
