package com.shopme.common.entity.admin.user.paging;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


public class PagingAndSortingArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer model, NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
        String sortDir = request.getParameter("sortDir");
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        String keyword = request.getParameter("keyword");
        String sortField = request.getParameter("sortField");
        PagingAndSortingParam annotation = parameter.getParameterAnnotation(PagingAndSortingParam.class);





        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL",  annotation.moduleURL());

        // Kann so auf moduleURL und listName zugreifen
        return new PagingAndSortingHelper(model,annotation.listName(),sortField,sortDir,keyword);
    }
}
