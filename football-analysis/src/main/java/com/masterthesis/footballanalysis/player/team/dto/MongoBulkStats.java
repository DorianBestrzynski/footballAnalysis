package com.masterthesis.footballanalysis.player.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MongoBulkStats {
    private Integer inserted;
    private Integer matched;
    private Integer deleted;
}
