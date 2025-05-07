package dev.vinpol.nebula.dragonship.web.utils;

import dev.vinpol.spacetraders.sdk.models.Meta;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PagingUtils {
    private PagingUtils() {

    }

    public static List<Integer> calculatePages(int $currentPage, int startOffset, int endOffset, int $totalPage) {
        int $startPage = $currentPage - startOffset;
        int $endPage = $currentPage + endOffset;

        if ($startPage <= 0) {
            $endPage -= ($startPage - 1);
            $startPage = 1;
        }

        if ($endPage > $totalPage) {
            $endPage = $totalPage;
        }

        List<Integer> pagesToShow = new ArrayList<>();

        for (int $i = $startPage; $i <= $endPage; $i++) {
            pagesToShow.add($i);
        }

        return pagesToShow;
    }

    public static void setMetaOnModel(Model model, Meta meta) {
        model.addAttribute("meta", meta);
        model.addAttribute("currentPage", meta.page());
        model.addAttribute("total", calculateTotalPageCount(meta));
        model.addAttribute("count", meta.total());
        model.addAttribute("limit", meta.limit());
    }

    private static int calculateTotalPageCount(Meta meta) {
        return calculateTotalPageCount(meta.total(), meta.limit());
    }

    private static int calculateTotalPageCount(int total, int limit) {
        int roundedResult = (int) ((float) total / limit);

        // Check if there is a remainder, this to show an extra page if there is left over
        if (total % limit != 0) {
            roundedResult++;
        }

        return roundedResult;
    }

    public static <T> void paginateOnModel(Model model, List<T> data, int page, int total) {
        Meta meta = new Meta(
            total,
            page,
            data.size()
        );

        setMetaOnModel(model, meta);
    }

    public static <T> List<T> paginate(List<T> data, int page, int total) {
        return paginate(data.stream(), page, total);
    }

    public static <T> List<T> paginate(Stream<T> data, int page, int total) {
        return data
            .skip((long) (page - 1) * total)
            .limit(total)
            .toList();
    }
}
