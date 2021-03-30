package com.high.highblog.helper;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.dto.response.BasePaginationRes;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PaginationHelper {

    public static BasePaginationRes buildBasePaginationRes(final Page<?> page) {
        return BasePaginationRes.builder()
                                .items(page.toList())
                                .totalItems(page.getTotalElements())
                                .totalPage(page.getTotalPages())
                                .pageSize(page.getNumberOfElements())
                                .page(page.getNumber() + 1)
                                .build();

    }

    public static PageRequest generatePageRequest(final BasePaginationReq req) {
        return generatePageRequest(req.getPage(), req.getPageSize(), req.getSorts());
    }

    public static PageRequest generatePageRequestWithDefaultSort(final BasePaginationReq req,
                                                                 final String defaultSort) {
        return generatePageRequest(req.getPage(),
                                   req.getPageSize(),
                                   ObjectUtils.isEmpty(req.getSorts())
                                           ? ArrayUtils.toArray(defaultSort)
                                           : req.getSorts());
    }

    public static PageRequest generatePageRequest(final Integer page,
                                                  final Integer pageSize,
                                                  final String[] sorts) {
        if (ObjectUtils.isEmpty(sorts)) {
            return PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));
        } else {
            return PageRequest.of(page - 1, pageSize, generateSort(sorts));
        }
    }

    private static Sort generateSort(final String[] sorts) {
        List<Sort.Order> orders = new ArrayList<>();

        boolean isContainId = false;

        for (String sortExpression : sorts) {

            char prefix = sortExpression.charAt(0);
            String columnName = sortExpression.substring(1);
            if(columnName.equals("id"))
                isContainId = true;

            if (prefix == '+')
                orders.add(Sort.Order.asc(columnName));
            else if (prefix == '-')
                orders.add(Sort.Order.desc(columnName));
            else
                throw new ValidatorException("Invalid sort request", "sorts");
        }

        if(!isContainId)
            orders.add(Sort.Order.desc("id"));

        return Sort.by(orders);
    }
}
