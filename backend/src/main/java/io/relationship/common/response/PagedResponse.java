package io.relationship.common.response;
import lombok.*;
import org.springframework.data.domain.Page;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private int page, size, totalPages;
    private long totalElements;
    private boolean last;
    public static <T> PagedResponse<T> of(Page<T> p) {
        return PagedResponse.<T>builder().content(p.getContent()).page(p.getNumber())
            .size(p.getSize()).totalElements(p.getTotalElements()).totalPages(p.getTotalPages()).last(p.isLast()).build();
    }
    public static <T> PagedResponse<T> of(List<T> content, int page, int size, long total) {
        int tp = size == 0 ? 1 : (int) Math.ceil((double) total / size);
        return PagedResponse.<T>builder().content(content).page(page).size(size).totalElements(total).totalPages(tp).last(page >= tp-1).build();
    }
}
