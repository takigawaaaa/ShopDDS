package com.shopdds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 下拉框项：{id, name}
 */
@Data
@AllArgsConstructor
public class DropdownItem {

    private String id;
    private String name;
}