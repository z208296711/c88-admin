package com.c88.admin.pojo.vo.menu;

import com.c88.member.vo.OptionVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "目錄菜單")
public class DirectoryMenuVO {

    @Schema(title = "ID")
    private Long value;

    @Schema(title = "顯示的標籤")
    private String label;

    @Schema(title = "菜單")
    private List<OptionVO<Long>> option;

}
