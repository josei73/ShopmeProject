package com.shopme.common.entity.admin.user.user.export;

import com.shopme.common.entity.Brand;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BrandCsvExporter extends AbstractExporter {

    public void export(List<Brand> list, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "brands_","text/csv", ".csv");
        ICsvBeanWriter csvWritter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Brand ID", "Brand Name"};
        String[] fieldMapping = {"id", "name"};
        csvWritter.writeHeader(csvHeader);

        for (Brand brand : list) {
            csvWritter.write(brand, fieldMapping);
        }
        csvWritter.close();
    }
}
