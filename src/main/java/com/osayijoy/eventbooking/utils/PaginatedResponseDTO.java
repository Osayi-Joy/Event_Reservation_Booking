package com.osayijoy.eventbooking.utils;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PaginatedResponseDTO<T> {
    private List<T> content = new ArrayList<>();
    private int currentPage;
    private long totalPages;
    private long totalItems;
    private Boolean isFirstPage;
    private Boolean isLastPage;
}
