package danal.batch.restaurant.dataloader.job.valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class RestaurantJobParameterValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String csvFilePath = parameters.getString("csvFilePath");
        if (StringUtils.isEmpty(csvFilePath)) {
            throw new JobParametersInvalidException("csvFilePath must be provided!");
        }
    }
}
