package com.high.highblog.helper;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.dto.response.BasePaginationRes;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PaginationHelper {

    public static BasePaginationRes buildBasePaginationRes(final Page<?> page) {
        return BasePaginationRes.builder()
                                .items(page.toList())
                                .totalItems(page.getTotalElements())
                                .lastPage(page.getTotalPages())
                                .pageSize(page.getNumberOfElements())
                                .page(page.getNumber())
                                .build();

    }

    public static PageRequest generatePageRequest(final Integer page,
                                                  final Integer pageSize,
                                                  final String[] sorts) {
        if (ObjectUtils.isEmpty(sorts)) {
            return PageRequest.of(page, pageSize);
        } else {
            return PageRequest.of(page, pageSize, generateSort(sorts));
        }
    }

    public static PageRequest generatePageRequest(final BasePaginationReq req) {
        return generatePageRequest(req.getPage(), req.getPageSize(), req.getSorts());
    }

    private static Sort generateSort(final String[] sorts) {
        Sort sortResult = Sort.unsorted();
        for (String sortExpression : sorts) {

            char prefix = sortExpression.charAt(0);
            String columnName = sortExpression.substring(1);

            if (prefix == '+')
                sortResult.and(Sort.Order.asc(columnName));
            else if (prefix == '-')
                sortResult.and(Sort.Order.desc(columnName));
            else
                throw new ValidatorException("Invalid sort rerquest", "sorts");

        }

        return sortResult;
    }
}
