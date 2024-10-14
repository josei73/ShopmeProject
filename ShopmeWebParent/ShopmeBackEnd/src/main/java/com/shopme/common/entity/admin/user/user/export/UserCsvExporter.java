package com.shopme.common.entity.admin.user.user.export;

import com.shopme.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/*
    CSV wurde durch SuperCSV generiert
 */

public class UserCsvExporter extends AbstractExporter {

    public void export(List<User> list, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "users_","text/csv", ".csv");

        ICsvBeanWriter csvWritter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
        csvWritter.writeHeader(csvHeader);
        for (User user : list)
            csvWritter.write(user, fieldMapping);

        csvWritter.close();
    }
}
