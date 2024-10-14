package com.shopme.common.entity.admin.user.user.export;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryCsvExporter extends AbstractExporter {
    public void export(List<Category> list, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "categories_","text/csv", ".csv");
        ICsvBeanWriter csvWritter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Category ID", "Category Name"};
        String[] fieldMapping = {"id", "name"};
        csvWritter.writeHeader(csvHeader);

        for (Category category : list) {
            category.setName(category.getName().replace("--"," "));
            csvWritter.write(category, fieldMapping);
        }
        csvWritter.close();
    }
}

